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
            placeholder="包装名称"
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
            @click="handleAdd"
            type="primary"
            size="mini"
            icon="el-icon-plus"
          >
            新增
          </el-button>
        </el-form-item>
      </el-col>
    </el-form>

    <el-table
      v-loading="loading"
      :data="packList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column
        label="包装编码"
        align="center"
        prop="number"
        show-overflow-tooltip
      />
      <el-table-column
        label="包装名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="包装类型"
        align="center"
        prop="packageTypeId"
        width="200"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_package_type"
            :value="scope.row.packageTypeId"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="单位重量"
        align="center"
        prop="unitWeight"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="规格尺寸(mm)"
        align="center"
        prop="model"
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
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建人" align="center" prop="createBy" width="140" show-overflow-tooltip/>  -->
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
            v-hasPermi="['basic:pack:edit']"
            >修改</el-button
          >
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['basic:pack:remove']"
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

    <!-- 添加或修改基础数据-包装对话框 -->
    <el-drawer
      :title="title"
      :visible.sync="drawer"
      :with-header="true"
      size="50%"
    >
      <div style="margin-left: 20px">
        <el-button type="primary" @click="submitForm" size="mini"
          >确 定</el-button
        >
        <el-button @click="cancel" size="mini">取 消</el-button>
      </div>

      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        style="margin-top: 20px; width: 800px"
      >
        <el-row>
          <el-col :xs="24" :sm="16" :md="16" :lg="16" :xl="16">
            <el-form-item label="包装名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入包装名称"
                class="col_span"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
            <el-form-item label="包装类型" prop="packageTypeId">
              <el-select
                v-model="form.packageTypeId"
                placeholder="包装类型"
                clearable
                class="col_span"
              >
                <el-option
                  v-for="dict in dict.type.zcii_bas_package_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="16" :md="16" :lg="16" :xl="16">
            <el-form-item label="规格尺寸(mm)" prop="model">
              <el-input
                class="col_span_min"
                v-model="form.lModel"
                placeholder="长"
                maxlength="17"
                oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
              />
              <el-input
                class="col_span_min"
                v-model="form.wModel"
                placeholder="宽"
                maxlength="17"
                oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
              />
              <el-input
                class="col_span_min"
                v-model="form.hModel"
                placeholder="高"
                maxlength="17"
                oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
            <el-form-item label="单位重量(kg)" prop="unitWeight">
              <el-input
                v-model="form.unitWeight"
                placeholder="请输入单位重量"
                class="col_span"
                maxlength="17"
                oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
import {
  listPack,
  getPack,
  delPack,
  addPack,
  updatePack,
  existPack,
} from "@/api/basic/pack";

export default {
  name: "Pack",
  dicts: ["zcii_bas_package_type"],
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 基础数据-包装表格数据
      packList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      drawer: false,
      isExist: false,
      packs: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null,
        packageTypeId: null,
        model: null,
        lModel: null,
        wModel: null,
        hModel: null,
        packageNumber: null,
        unitWeight: null,
        status: null,
        tenantId: null,
        remork: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "包装名称不能为空", trigger: "blur" },
        ],
        packageTypeId: [
          { required: true, message: "包装类型不能为空", trigger: "blur" },
        ],
        // model: [
        //   { required: true, message: "规格尺寸不能为空", trigger: "blur" },
        // ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询基础数据-包装列表 */
    getList() {
      this.loading = true;
      listPack(this.queryParams).then((response) => {
        this.packList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.drawer = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        number: null,
        packageTypeId: null,
        model: null,
        lModel: null,
        wModel: null,
        hModel: null,
        packageNumber: null,
        unitWeight: null,
        status: 0,
        createBy: null,
        createTime: null,
        tenantId: null,
        updateBy: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.drawer = true;
      this.title = "新增物料包装";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPack(id).then((response) => {
        this.form = response.data;
        if (this.form.lModel == 0) {
          this.form.lModel = null;
        }
        if (this.form.wModel == 0) {
          this.form.wModel = null;
        }
        if (this.form.hModel == 0) {
          this.form.hModel = null;
        }
        this.drawer = true;
        this.title = "修改物料包装";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // if(this.isExist){
          //   this.$modal.msgError("该包装名称已存在！");
          //   return;
          // }

          this.input_Model();

          if (this.form.id != null) {
            updatePack(this.form).then((response) => {
              if (response.code == 200) {
                this.$modal.msgSuccess("修改成功");
                this.drawer = false;
                this.getList();
              }
            });
          } else {
            addPack(this.form)
              .then((response) => {
                if (response.code == 200) {
                  this.$modal.msgSuccess("新增成功");
                  this.drawer = false;
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
        .confirm(
          '是否确认删除基础数据-包装编号为"' + row.number + '"的数据项？'
        )
        .then(function () {
          return delPack(ids);
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
        "basic/pack/export",
        {
          ...this.queryParams,
        },
        `pack_${new Date().getTime()}.xlsx`
      );
    },
    input_Model() {
      if (this.form.lModel == null || this.form.lModel == "") {
        this.form.lModel = null;
      }

      if (this.form.wModel == null || this.form.wModel == "") {
        this.form.wModel = null;
      }

      if (this.form.hModel == null || this.form.hModel == "") {
        this.form.hModel = null;
      }
    },

    /**
     * 名称是否重复
     * @param {} value
     */
    existName(e) {
      var name = e.target.value.trim();
      if (name != undefined && name != null && name != "") {
        existPack(name).then((response) => {
          this.packs = response.data;
          if (this.packs.length > 0) {
            if (this.form.id != undefined) {
              let exist = true;
              this.packs.forEach((element) => {
                if (element.id == this.form.id) {
                  exist = false;
                  return;
                }
              });
              if (exist) {
                this.isExist = true;
                this.$modal.msgError("包装名称已存在！");
              }
            }
          } else {
            this.isExist = false;
          }
        });
      }
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
