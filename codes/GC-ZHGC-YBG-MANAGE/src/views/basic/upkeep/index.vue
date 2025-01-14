<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      class="sasda"
    >
      <img
        src="../../../assets/images/sousuo.png"
        style="margin-bottom: -13px; width: 37px"
      />
      <el-input
        v-model="queryParams.processName"
        placeholder="请输入工序名称"
        clearable
        @keyup.enter.native="handleQuery"
        class="input_first"
      />
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查找</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['basic:upkeep:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['basic:upkeep:export']"
          >导出</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['basic:upkeep:export']"
          >导入</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="upkeepList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column
        label="工序编号"
        align="center"
        prop="processId"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="工序名称"
        align="center"
        prop="processName"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="工序类型"
        align="center"
        prop="gxType"
        width="200"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_gx_type"
            :value="scope.row.gxType"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="工序时长（h）"
        align="center"
        prop="burningTime"
        width="200"
        show-overflow-tooltip
      >
        <!-- <template slot-scope="scope">
          <dict-tag :options="dict.type.zcii_bas_suit" :value="scope.row.suitObj"/>
        </template> -->
      </el-table-column>
      <!-- <el-table-column label="物料编码" align="center" prop="materialCode" width="180" show-overflow-tooltip/>
      <el-table-column label="物料名称" align="center" prop="materialName" width="180" show-overflow-tooltip/> -->
      <el-table-column
        label="是否启用"
        align="center"
        prop="myState"
        width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_upkeep_type"
            :value="scope.row.myState"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="200"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建人" align="center" prop="createBy" width="180" show-overflow-tooltip/> -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['basic:upkeep:edit']"
            >修改</el-button
          >
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['basic:upkeep:remove']"
            >删除</el-button
          > -->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改工序维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="460px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- <el-form-item label="名称分类" prop="processType">
          <el-radio-group v-model="form.processType">
            <el-radio label="0">工业</el-radio>
            <el-radio label="1">家用</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="工序编码" prop="processId">
          <el-input
            v-model="form.processId"
            placeholder="请输入工序编码"
            class="input_first"
          />
        </el-form-item>
        <el-form-item label="工序名称" prop="processName">
          <el-input
            v-model="form.processName"
            placeholder="请输入工序名称"
            class="input_first"
          />
        </el-form-item>
        <el-form-item label="工序类型" prop="gxType">
          <el-select
            v-model="form.gxType"
            placeholder="请选择工序类型"
            style="width: 200px; margin-left: 9px"
          >
            <el-option
              v-for="dict in dict.type.zcii_bas_gx_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工序时长（h）" prop="burningTime">
          <el-input
            v-model="form.burningTime"
            placeholder="请输入工序时长"
            class="input_first"
          ></el-input>
        </el-form-item>
        <el-form-item label="产品编码" prop="materialCode">
          <el-input
            v-model="form.materialCode"
            placeholder="请输入物料编码"
            class="input_first"
          >
            <i slot="suffix" @click="handleChange">
              <img
                src="../../../assets/images/sousuo.png"
                style="margin-bottom: 0px; width: 37px"
              />
            </i>
          </el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="materialName">
          <el-input
            v-model="form.materialName"
            placeholder="请输入物料名称"
            class="input_first"
          >
            <i slot="suffix" @click="handleChange">
              <img
                src="../../../assets/images/sousuo.png"
                style="margin-bottom: 0px; width: 37px"
              />
            </i>
          </el-input>
        </el-form-item>
        <el-form-item label="状态" prop="myState">
          <el-select
            v-model="form.myState"
            placeholder="请选择状态"
            style="width: 200px; margin-left: 9px"
          >
            <el-option
              v-for="dict in dict.type.zcii_bas_upkeep_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" class="el-button--medium">取 消</el-button>
        <el-button type="primary" class="el-button--medium" @click="submitForm"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <!-- 物料信息选择对话框 -->
    <el-dialog
      title="物料选择"
      :visible.sync="materialOpen"
      width="760px"
      append-to-body
    >
      <el-form
        :model="queryMaterials"
        :rules="rules"
        label-width="100px"
        class="sasda"
      >
        <div class="dialog-footer-table">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />

          <el-input
            v-model="queryMaterials.name"
            placeholder="物料编号/物料名称"
            clearable
            @keyup.enter.native="MaterialsQuery"
            class="input_first"
          />
          <el-button
            icon="el-icon-refresh"
            size="mini"
            @click="MaterialsQuery"
            class="button_first"
            >重置</el-button
          >
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="getMaterialsList"
            class="button_first"
            >查找</el-button
          >
        </div>
        <el-table
          v-loading="loading"
          :data="materialsList"
          @row-click="clickRow"
          border=""
          style="margin-top: 20px"
        >
          <el-table-column
            label="产品编码"
            prop="number"
            width="200"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-radio
                :label="scope.row.number"
                v-model="radio"
                align="center"
              ></el-radio>
            </template>
          </el-table-column>
          <el-table-column
            label="产品名称"
            align="center"
            prop="name"
            show-overflow-tooltip
          />
          <el-table-column
            label="物料类型"
            align="center"
            prop="categoryId"
            width="180"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.zcii_bas_matter_type"
                :value="scope.row.categoryId"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="Materialscancel" class="el-button--medium"
          >取 消</el-button
        >

        <el-button
          type="primary"
          class="el-button--medium"
          @click="Materialscancel"
          >确 定</el-button
        >
      </div>
      <pagination
        v-show="materialstotal > 0"
        :total="materialstotal"
        :page.sync="queryMaterials.pageNum"
        :limit.sync="queryMaterials.pageSize"
        @pagination="getMaterialsList"
      />
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          >
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listUpkeep,
  getUpkeep,
  delUpkeep,
  addUpkeep,
  updateUpkeep,
} from "@/api/basic/upkeep";
import { getToken } from "@/utils/auth";
import { productList } from "@/api/basic/materials";
import Cookies from "js-cookie";
export default {
  name: "Upkeep",
  dicts: [
    "zcii_bas_matter_type",
    "zcii_bas_gx_type",
    "zcii_bas_suit",
    "zcii_bas_upkeep_type",
  ],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      //物料总条数
      materialstotal: 0,
      //物料编号
      radio: 0,
      // 工序维护表格数据
      upkeepList: [],
      // 弹出层标题
      title: "",
      // 是否禁用上传
      isUploading: false,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken(),
          tenant: Cookies.get("tenantId"),
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/basic/upkeep/importData",
      },
      // 是否显示弹出层
      open: false,
      // 是否显示物料信息弹出层
      materialOpen: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processName: null,
        //仅展示1
        display: 1,
      },
      // 物料查询参数
      queryMaterials: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null,
      },
      // 基础数据-物料数据
      materialsList: [],
      row: {},
      // 表单参数
      form: {
        processType: "0",
      },
      // 表单校验
      rules: {
        processId: [
          { required: true, message: "工序编码不能为空", trigger: "blur" },
        ],
        processName: [
          { required: true, message: "工序名称不能为空", trigger: "blur" },
        ],
        gxType: [
          { required: true, message: "工序类型不能为空", trigger: "change" },
        ],
        burningTime: [
          { required: true, message: "工序时长不能为空", trigger: "blur" },
        ],
        myState: [
          { required: true, message: "状态不能为空", trigger: "change" },
        ],
        processType: [
          { required: true, message: "名称分类不能为空", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工序维护列表 */
    getList() {
      this.loading = true;
      listUpkeep(this.queryParams).then((response) => {
        this.upkeepList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询基础数据-物料列表 */
    getMaterialsList() {
      this.loading = true;
      productList(this.queryMaterials).then((response) => {
        this.materialsList = response.rows;
        this.materialstotal = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 物料弹框取消
    Materialscancel() {
      this.queryMaterials.name = null;
      this.materialOpen = false;
    },
    clickRow(row) {
      this.form.materialCode = row.number;
      this.form.materialName = row.name;
      this.form.materialId = row.id;
      this.radio = row.number;
    },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        processName: null,
        gxType: null,
        suitObj: null,
        materialId: null,
        materialCode: null,
        materialName: null,
        myState: 1,
        burningTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        display: null,
        tenantId: null,
        remark: null,
        processType: "0",
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      // this.resetForm("queryForm");
      (this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        processName: null,
        //仅展示1
        display: 1,
      }),
        this.handleQuery();
    },
    /** 物料查询重置按钮操作 */
    MaterialsQuery() {
      this.queryMaterials.name = null;
      this.queryMaterials.pageNum = 1;
      this.getMaterialsList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增工序维护";
    },
    // 打开物料查询
    handleChange() {
      this.getMaterialsList();
      this.radio = this.form.materialCode;
      this.materialOpen = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getUpkeep(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工序维护";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateUpkeep(this.form)
              .then((response) => {
                if (response.code == 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              })
              .catch((error) => {
                console.log(error);
              });
          } else {
            addUpkeep(this.form)
              .then((response) => {
                if (response.code == 200) {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                }
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除工序维护编号为"' + ids + '"的数据项？')
        .then(function () {
          type: "warning";
        })
        .then(() => {
          this.form.id = ids;
          this.form.display = 0;
          updateUpkeep(this.form).then((response) => {
            this.$modal.msgSuccess("删除成功");

            this.getList();
          });
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "basic/upkeep/export",
        {
          ...this.queryParams,
        },
        `upkeep_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "工序导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "basic/upkeep/importTemplate",
        {},
        `zcii_basic_upkeep.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
          response.msg +
          "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
      );
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>

<style lang="scss" scoped>
.input_first {
  margin-left: 8px;
  width: 200px;
}
.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}
.mb8 {
  margin-top: 8px;
}

.dialog-footer-table .button_first {
  float: right;
  margin-right: 20px;
}
.el-button--medium {
  margin-right: 20%;
}
</style>
