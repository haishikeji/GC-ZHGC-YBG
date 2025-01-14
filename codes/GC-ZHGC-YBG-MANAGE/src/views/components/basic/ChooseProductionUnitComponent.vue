<template>
  <div>
    <el-input
      @focus="showDialog"
      v-model="textValue"
      placeholder="请输入生产单元"
      class="col_span"
    >
      <i slot="suffix" @click="showDialog">
        <img
          src="../../../assets/images/sousuo.png"
          style="margin-bottom: 0px; width: 37px"
        />
      </i>
    </el-input>
    <!-- <el-input prefix-icon="el-icon-search" class="col_span" @focus="showDialog" v-model="textValue"/> -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form
        ref="queryParams"
        :model="queryParams"
        :rules="rules"
        label-width="18 px"
      >
        <el-row :gutter="35">
          <el-col :xs="24" :sm="18" :md="18" :lg="18" :xl="18" class="mb20">
            <el-form-item label="" prop="unitName">
              <el-input
                v-model="queryParams.name"
                placeholder="生产单元名称"
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
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <el-table
              v-loading="loading"
              :data="productList"
              style="margin-left: 16px; width: 96%"
              @row-click="clickRow"
              ref="filterTable"
              border=""
            >
              <!-- @selection-change="handleSelectionChange" -->
              <!-- <el-table-column type="selection" width="50" align="center"/> -->
              <el-table-column
                label="生产单元编号"
                align="center"
                prop="unitCode"
                width="160"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <el-radio
                    :label="scope.row.unitCode"
                    v-model="radio"
                  ></el-radio>
                </template>
              </el-table-column>
              <el-table-column
                label="父级生产单元"
                align="center"
                prop="superiorUnit"
                width="200"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  {{ scope.row.superiorUnit }}
                </template>
              </el-table-column>
              <el-table-column
                label="生产单元名称"
                align="center"
                prop="unitName"
                width="160"
                show-overflow-tooltip
              />
              <el-table-column
                label="生产单元类型"
                align="center"
                prop="unitType"
                width="120"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <dict-tag
                    :options="dict.type.zcii_bas_produce_type"
                    :value="scope.row.unitType"
                  />
                </template>
              </el-table-column>
              <el-table-column
                label="责任人"
                align="center"
                prop="curatorName"
              />
            </el-table>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listUnitVoByRouting } from "@/api/basic/unit";
export default {
  name: "ChooseProductionUnitComponent",
  dicts: ["zcii_bas_produce_type"],
  props: {
    value: {
      type: Number,
      default: 0,
    },
    text: {
      type: String,
      default: "",
    },
    index: {
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
      // 总条数
      total: 0,
      // 基础数据-包装表格数据
      productList: [],
      // 弹出层标题
      title: "生产单元选择",
      // 是否显示弹出层
      open: false,
      //input
      textValue: "",
      row: {},
      radio: 0,
      //多选
      multipleSelection: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        unitName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
    this.init();
  },
  methods: {
    /** 查询基础数据-包装列表 */
    getList() {
      this.loading = true;
      listUnitVoByRouting(this.queryParams).then((response) => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    init() {
      this.textValue = this.text;
      this.radio = this.text;
    },

    //延迟加载
    initText() {
      this.textValue = this.text;
      this.radio = this.text;
      // console.log("生成单元...initText...",this.text)
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
      this.textValue = this.text;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      this.multipleSelection = selection;
      console.log("选择多行...", this.multipleSelection);
    },

    // 单击选中某一行选框
    clickRow(row) {
      // this.$refs.filterTable.toggleRowSelection(row);
      this.textValue = row["unitName"];
      this.row = row;
      this.radio = row.unitCode;
      console.log("选择一行...", this.row);
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
  },
};
</script>

<style>
.col_span {
  width: 200px;
}
.col_span_min {
  width: 100px;
  margin-right: 20px;
}
</style>
