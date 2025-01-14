<template>
  <div>
    <el-input
      prefix-icon="el-icon-search"
      @focus="showDialog"
      v-model="textValue"
      :disabled="disabled"
      class="col_span"
    />
    <!-- 展示并选择物料列表 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="10px">
        <el-row :gutter="24">
          <!--物料数据-->
          <el-col :span="24" :xs="24">
            <el-form
              :model="queryParams"
              ref="queryForm"
              size="small"
              :inline="true"
              v-show="showSearch"
              label-width="68px"
            >
              <img
                src="../../../assets/images/sousuo.png"
                style="margin-bottom: -13px; width: 37px"
              />
              <el-input
                v-model="queryParams.name"
                placeholder="产品名称"
                clearable
                @keyup.enter.native="handleQuery"
                style="width: 200px"
                class="sasda"
              >
              </el-input>

              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                  style="margin-top: 10px"
                  >搜索</el-button
                >
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                  style="margin-top: 10px"
                  >重置</el-button
                >
              </el-form-item>
            </el-form>

            <el-table
              v-loading="loading"
              :data="materialsList"
              @row-click="clickRow"
              ref="filterTable"
              border
            >
              <el-table-column
                label="产品ID"
                align="center"
                key="id"
                prop="id"
                :show-overflow-tooltip="true"
                width="100"
              >
                <template slot-scope="scope">
                  <el-radio :label="scope.row.id" v-model="radio"></el-radio>
                </template>
              </el-table-column>
              <el-table-column
                label="产品编号"
                align="center"
                key="number"
                prop="number"
                :show-overflow-tooltip="true"
                width="260"
              >
              </el-table-column>
              <el-table-column
                label="产品名称"
                align="center"
                key="name"
                prop="name"
                show-overflow-tooltip
              />
              <el-table-column
                label="产品类型"
                align="center"
                prop="categoryName"
                width="200"
                show-overflow-tooltip
              >
                <!-- <template slot-scope="scope">
                  <dict-tag
                    :options="dict.type.zcii_bas_matter_type"
                    :value="scope.row.categoryId"
                  />
                </template> -->
              </el-table-column>
              <el-table-column label="物料图片" align="center" width="150">
                <template slot-scope="scope">
                  <image-preview
                    :src="scope.row.materialsImg"
                    :width="50"
                    :height="50"
                  />
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
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterials } from "@/api/basic/materials";

export default {
  name: "ChooseMaterialsComponent",
  // dicts: ["zcii_bas_matter_type"],
  props: {
    value: {
      type: Number,
      default: 0,
    },
    text: {
      type: String,
      default: "",
    },
    sourceurl: {
      type: Number,
      default: 0,
    },
    index: {
      type: Number,
      default: 0,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
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
      //判断来源
      source: 0,
      // 总条数
      total: 0,
      //生产制造-产品表格数据
      materialsList: [],
      filterTable: [],
      // 弹出层标题
      title: "产品选择",
      // 是否显示弹出层
      open: false,
      //input
      row: {},
      radio: 0,
      //多选

      multipleSelection: [],
      textValue: "",
      disabled: this.disabled,
      // 查询参数
      queryParams: {
        name: undefined,
        id: null,
        status: 1,
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.Reassign();
    this.init();
  },
  methods: {
    /** 查询基础数据-产品列表 */
    getList() {
      this.loading = true;
      listMaterials(this.queryParams).then((response) => {
        this.materialsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    Reassign() {
      if (this.sourceurl != 0) {
        this.textValue = this.text;
        this.radio = this.text.replace(/[0-9]/g, "");
      }
    },
    init() {
      this.textValue = this.text;
      if (this.value != null) {
        this.radio = this.value;
      }
    },

    //延迟加载
    initText(text) {
      this.textValue = text;
      if (this.text != undefined && this.text != "") {
        this.radio = this.text;
      }
    },
    // 重置数据
    reset_data() {
      this.textValue = "";
      this.radio = 0;
    },
    //赋值选中table数据
    assign_check(val) {
      if (val == null) {
        this.textValue = null;
      }
      this.textValue = val;
      this.radio = val;
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.multipleSelection = [];
    },

    // 单击选中某一行选框
    clickRow(row) {
      // this.$refs.filterTable.toggleRowSelection(row);
      this.row = row;
      this.textValue = row["number"];
      this.radio = row.number;
      console.log("选择一行...", this.row);
    },

    /**
     * 打开弹窗
     */
    showDialog() {
      if (this.value != null) {
        this.queryParams.id = this.value;
        // console.log("id....", this.queryParams.id);
      }
      this.getList();
      this.open = true;
    },
    //确定按钮
    submitForm() {
      //  this.$emit('selectChange', this.multipleSelection);
      this.$emit("chooseRow", this.row, this.index);
      this.open = false;
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams = {
        name: undefined,
        id: null,
        pageNum: 1,
        pageSize: 10,
      };
      this.handleQuery();
    },
  },
};
</script>

<style lang="scss" scoped>
.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}

.col_span {
  width: 184px;
}

.col_span_min {
  width: 100px;
  margin-right: 20px;
}

.sel_input {
  width: 120px;
  margin-right: 20px;
}
</style>
