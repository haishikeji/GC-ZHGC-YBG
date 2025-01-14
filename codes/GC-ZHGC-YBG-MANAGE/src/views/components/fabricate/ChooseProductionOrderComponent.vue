<template>
  <div>
    <el-input
      @focus="showDialog"
      v-model="textValue"
      placeholder="请选择生产订单"
      class="col_span"
      size="mini"
    >
      <i slot="suffix" @click="showDialog">
        <img
          src="../../../assets/images/sousuo.png"
          style="margin-bottom: 0px; width: 37px"
        />
      </i>
    </el-input>
    <!-- 展示并选择客户列表 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1460px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="10px"
        class="sasda"
      >
        <el-row :gutter="24">
          <el-col :xs="24" :sm="18" :md="18" :lg="18" :xl="18" class="mb20">
            <el-form-item label="" prop="processName">
              <el-input
                v-model="queryParams.number"
                placeholder="生产订单号"
                class="col_span"
                @keyup.enter.native="handleQuery"
              >
                <i slot="suffix">
                  <img
                    src="../../../assets/images/sousuo.png"
                    style="margin-bottom: 0px; width: 37px"
                  />
                </i>
              </el-input>
              <el-input
                v-model="queryParams.customerName"
                placeholder="客户名称"
                class="col_span"
                @keyup.enter.native="handleQuery"
                style="margin-left: 30px"
              >
                <i slot="suffix">
                  <img
                    src="../../../assets/images/sousuo.png"
                    style="margin-bottom: 0px; width: 37px"
                  />
                </i>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="6">
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
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="37">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <el-table
              v-loading="loading"
              :data="productionorderList"
              row-key="id"
              @row-click="clickRow"
              ref="filterTable"
              border
              :tree-props="{ children: 'children' }"
            >
              <el-table-column
                label="生产订单号"
                align="center"
                prop="number"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <el-radio
                    v-if="scope.row.merge != 1"
                    :label="scope.row.number"
                    v-model="radio"
                  ></el-radio>
                  <span v-else>
                    {{ scope.row.number }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                label="计划状态"
                align="center"
                prop="status"
                width="100"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <dict-tag
                    :options="dict.type.zcii_fab_plan_status"
                    :value="scope.row.status"
                  />
                </template>
              </el-table-column>
              <el-table-column
                label="客户编号"
                align="center"
                prop="customerNumber"
                show-overflow-tooltip
              />
              <el-table-column
                label="客户名称"
                align="center"
                prop="customerName"
                show-overflow-tooltip
              />
              <el-table-column
                label="产品编号"
                align="center"
                prop="materialsNumber"
                show-overflow-tooltip
              />
              <el-table-column
                label="产品名称"
                align="center"
                prop="materialsName"
                show-overflow-tooltip
              />
              <el-table-column label="物料图片" align="center" width="150">
                <template slot-scope="scope">
                  <image-preview
                    :src="scope.row.materialsImg"
                    :width="50"
                    :height="50"
                  />
                </template>
              </el-table-column>
              <el-table-column
                label="需求数量"
                align="center"
                prop="count"
                width="100"
                show-overflow-tooltip
              />
              <el-table-column
                label="要求交货日期"
                align="center"
                prop="date"
                width="140"
              >
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.date, "{y}-{m}-{d}") }}</span>
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
import { listProductionorder } from "@/api/fabricate/productionorder";

export default {
  name: "ChooseProductionOrderComponent",
  dicts: ["zcii_fab_plan_status"],
  props: {
    value: {
      type: String,
      default: "",
    },
    text: {
      type: String,
      default: "",
    },
    notStatus: {
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
      //生产制造-客户表格数据
      productionorderList: [],
      filterTable: [],
      // 弹出层标题
      title: "订单选择",
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
        custName: undefined,
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
    this.getList();
    this.Reassign();
  },
  methods: {
    /** 查询基础数据-包装列表 */
    getList() {
      this.loading = true;
      if (this.notStatus == 1) {
        this.queryParams.notStatus = 1;
      }
      listProductionorder(this.queryParams).then((response) => {
        this.productionorderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    Reassign() {
      if (this.sourceurl != 0) {
        this.textValue = this.text;
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
      // console.log('选择一行...', this.row)
    },

    showDialog() {
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
      // 查询参数
      this.queryParams = {
        custName: undefined,
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
  width: 200px;
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
