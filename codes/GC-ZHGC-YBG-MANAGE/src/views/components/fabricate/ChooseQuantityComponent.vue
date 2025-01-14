<template>
  <div>
    <el-input
      prefix-icon="el-icon-search"
      @focus="showDialog"
      v-model="textValue"
      class="col_span"
    />
    <!-- 展示并选择货位列表 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="10px">
        <el-row :gutter="24">
          <!--货位数据-->
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
                v-model="queryParams.stashName"
                placeholder="仓库名称"
                clearable
                @keyup.enter.native="handleQuery"
                style="width: 200px"
                class="sasda"
              >
              </el-input>

              <img
                src="../../../assets/images/sousuo.png"
                style="margin-bottom: -13px; width: 37px"
              />
              <el-input
                v-model="queryParams.name"
                placeholder="货位名称"
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
                  >搜索
                </el-button>
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                  style="margin-top: 10px"
                  >重置
                </el-button>
              </el-form-item>
            </el-form>

            <el-table
              v-loading="loading"
              :data="cargopositionList"
              @row-click="clickRow"
              ref="filterTable"
              border
            >
              <el-table-column
                label="货位号"
                align="center"
                key="number"
                prop="number"
                :show-overflow-tooltip="true"
              >
                <template slot-scope="scope">
                  <el-radio
                    :label="scope.row.number"
                    v-model="radio"
                  ></el-radio>
                </template>
              </el-table-column>
              <el-table-column label="货位名称" align="center" prop="name" />
              <el-table-column
                label="仓库名称"
                align="center"
                prop="stashName"
              />
              <el-table-column label="仓库类型" align="center" prop="type">
                <template slot-scope="scope">
                  <dict-tag
                    :options="dict.type.zcli_stor_stash_type"
                    :value="scope.row.stashType"
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
import {
  listCargoposition,
  getCargoposition,
} from "@/api/storage/cargoposition";

export default {
  name: "ChooseQuantityComponent",
  dicts: ["zcli_stor_stash_type"],
  props: {
    value: {
      type: Number,
      default: 0,
    },
    index: {
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
      //货位表格数据
      cargopositionList: [],
      filterTable: [],
      // 弹出层标题
      title: "货位选择",
      // 是否显示弹出层
      open: false,
      //input
      row: {},
      radio: 0,
      //多选

      multipleSelection: [],
      textValue: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stashId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
    this.Reassign();
  },
  methods: {
    /** 查询基础数据-包装列表 */
    getList() {
      this.loading = true;
      if (this.value != undefined) {
        this.queryParams.stashId = this.value;
      }
      listCargoposition(this.queryParams).then((response) => {
        this.cargopositionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    Reassign() {
      if (this.sourceurl != 0) {
        this.textValue = this.text;
        this.radio = this.text.replace(/[0-9]/g, "");
      }else{
        this.textValue = this.text;
      }
    },
    // 重置数据
    reset_data() {
      this.textValue = "";
      this.radio = 0;
    },
    //赋值选中table数据
    assign_check(val) {
      if (val != null) {
        getCargoposition(val).then((response) => {
          this.textValue = response.data.number;
          this.radio = response.data.number;
        });
      } else {
        this.textValue = null;
      }
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
      // console.log("货位。。。选择一行...", this.row,this.index);
    },

    showDialog() {
      this.open = true;
      this.getList();
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
