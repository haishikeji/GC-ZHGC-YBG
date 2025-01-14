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
      <el-col>
        <el-form-item label="" prop="name">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-input
            v-model="queryParams.name"
            placeholder="物料编码/物料名称"
            clearable
            class="input_first"
            @keyup.enter.native="handleQuery"
          >
          </el-input>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item>
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
            v-hasPermi="['basic:materials:add']"
            >新增</el-button
          >
          <el-button
            type="info"
            plain
            icon="el-icon-upload2"
            size="mini"
            @click="handleImport"
            v-hasPermi="['basic:materials:export']"
            >导入</el-button
          >
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['basic:materials:export']"
            >导出</el-button
          >
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane :key="dict.value" :label="全部" :value="0" :name="全部">
          </el-tab-pane>
          <el-tab-pane label="全部" name="first"></el-tab-pane>
          <el-tab-pane
            v-for="dict in materialstypeList"
            :key="dict.materialsTypeValue"
            :label="dict.materialsTypeLabel"
            :value="parseInt(dict.materialsTypeValue)"
            :name="dict.materialsTypeLabel"
          >
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-form>

    <el-table
      v-loading="loading"
      :data="materialsList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column
        label="物料编码"
        align="center"
        prop="number"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="物料名称"
        align="center"
        prop="name"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column label="物料图片" align="center" width="150">
        <!-- <template slot-scope="scope" v-if="scope.row.materialsImg">
          <img
            :src="urlHeand + scope.row.materialsImg"
            alt="物料图片"
            @click="handleImageClick(scope.row.materialsImg)"
            style="width: 100px; cursor: pointer"
          />
        </template> -->
        <template slot-scope="scope">
          <image-preview
            :src="scope.row.materialsImg"
            :width="50"
            :height="50"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="物料类型"
        align="center"
        prop="categoryName"
        width="100"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="计量单位"
        align="center"
        prop="unitId"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_unit_type"
            :value="scope.row.unitId"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="是否启用"
        align="center"
        prop="status"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zicc_bas_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="单位重量(kg)" align="center" prop="unitWeight" width="140" show-overflow-tooltip/> -->
      <el-table-column
        label="物料规格"
        align="center"
        prop="materialsModel"
        width="150"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="规格尺寸(mm)"
        align="center"
        prop="model"
        width="160"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.lmodel > 0">
            {{ scope.row.lmodel }}
          </span>
          <span v-if="scope.row.wmodel > 0"> *{{ scope.row.wmodel }} </span>
          <span v-if="scope.row.hmodel > 0"> *{{ scope.row.hmodel }} </span>
          <span v-if="scope.row.tmodel > 0"> *{{ scope.row.tmodel }} </span>
        </template>
      </el-table-column>
      <el-table-column
        label="工艺路线"
        align="center"
        prop="routingName"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="200"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{
            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
          }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建人" align="center" prop="createBy" width="140" show-overflow-tooltip/>      -->
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
            @click="handleBomUpdate(scope.row)"
            v-hasPermi="['basic:materials:edit']"
            >配置BOM</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['basic:materials:edit']"
            >修改</el-button
          >
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['basic:materials:remove']"
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

    <!-- 物料导入对话框 -->
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
    <el-dialog
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <img
        :src="currentImageUrl"
        alt="放大的图片"
        style="width: 100%; height: auto"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listMaterials, delMaterials } from "@/api/basic/materials";
import {
  listMaterialstype,
  listTypeMaterialstype,
} from "@/api/basic/materialstype";
import { getToken, getTenant } from "@/utils/auth";
export default {
  name: "Materials",
  dicts: ["zcii_bas_unit_type", "zicc_bas_status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      activeName: "first",
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 基础数据-物料表格数据
      materialsList: [],
      // 物料类型数据表格数据
      materialstypeList: [],
      // 弹出层标题
      title: "",
      dialogVisible: false,
      urlHeand: process.env.VUE_APP_BASE_API,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null,
        categoryId: 1, //默认查询
        supplyMode: null,
        model: null,
        routingId: null,
        deptId: null,
        lModel: null,
        wModel: null,
        hModel: null,
        tModel: null,
        unitWeight: null,
        unitId: null,
        status: null,
        creator: null,
        tenantId: null,
        updateCreator: null,
        remork: null,
      },
      typeValues: null,
      // 表单参数
      form: {},
      currentImageUrl: "",
      // 表单校验
      rules: {},
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
          tenant: getTenant(),
        },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/basic/materials/importData",
      },
    };
  },
  created() {
    this.activeName = "first";
    this.queryParams.categoryId = null;
    this.getListMaterialstype();
    this.getList();
  },
  methods: {
    /** 查询物料类型数据列表 */
    getListMaterialstype() {
      this.loading = true;
      listTypeMaterialstype(this.queryParams).then((response) => {
        this.materialstypeList = response;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询基础数据-物料列表 */
    getList() {
      this.loading = true;
      listMaterials(this.queryParams).then((response) => {
        this.materialsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        number: null,
        categoryId: null,
        supplyMode: null,
        model: null,
        routingId: null,
        deptId: null,
        lModel: null,
        wModel: null,
        hModel: null,
        tModel: null,
        unitWeight: null,
        unitId: null,
        status: 0,
        creator: null,
        createTime: null,
        tenantId: null,
        updateCreator: null,
        updateTime: null,
        remork: null,
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        name: null,
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    handleAdd() {
      this.$tab.openPage("新增物料信息", "/basic/addMatter");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;

      let params = { id: id };

      this.$tab.openPage("修改物料信息", "/basic/addMatter", params);
    },
    handleImageClick(imageUrl) {
      this.currentImageUrl = imageUrl;
      this.dialogVisible = true;
    },
    handleClose(done) {
      this.dialogVisible = false;
      done();
    },
    /**
     * tab页签切换
     * @param {*} tab
     * @param {*} event
     */
    handleClick(tab, event) {
      console.log("tab...", tab.$attrs.value, tab);
      if (this.activeName == "first") {
        this.queryParams.categoryId = null;
      } else {
        this.queryParams.categoryId = tab.$attrs.value;
      }

      this.getList();
      this.reset();
    },

    /**
     * 配置bom
     */
    handleBomUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      const params = { matterId: id };
      this.$tab.openPage("配置BOM", "/basic/bom", params);
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm(
          '是否确认删除基础数据-物料编号为"' + row.number + '"的数据项？'
        )
        .then(function () {
          return delMaterials(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "basic/materials/export",
        {
          ...this.queryParams,
        },
        `materials_${new Date().getTime()}.xlsx`
      );
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "basic/materials/importTemplate",
        {},
        `materials_${new Date().getTime()}.xlsx`
      );
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "物料信息导入";
      this.upload.open = true;
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

    formatterDate(row, column) {
      let data = row[column.property];
      return this.formatDate(data);
    },

    formatDate(value) {
      var dt = new Date(value);
      let year = dt.getFullYear();
      let month = dt.getMonth() + 1;
      let date = dt.getDate();
      return `${year}-${month}-${date}`;
    },
  },
};
</script>

<style lang="scss" scoped>
.col_span {
  width: 200px;
}
.col_span_min {
  width: 100px;
  margin-right: 20px;
}
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
</style>
