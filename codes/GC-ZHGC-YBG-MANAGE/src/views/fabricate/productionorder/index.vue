<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      class="sasda"
      v-show="showSearch"
      label-width="68px"
    >
      <img
        src="../../../assets/images/sousuo.png"
        style="margin-bottom: -13px; width: 37px"
      />
      <el-input
        v-model="queryParams.number"
        placeholder="生产订单号"
        clearable
        @keyup.enter.native="handleQuery"
        class="input_first"
      >
      </el-input>
      <img
        src="../../../assets/images/sousuo.png"
        style="margin-bottom: -13px; width: 37px"
      />
      <el-input
        v-model="queryParams.salesOrderNumber"
        placeholder="销售订单号"
        clearable
        @keyup.enter.native="handleQuery"
        class="input_first"
      >
      </el-input>

      <img
        src="../../../assets/images/sousuo.png"
        style="margin-bottom: -13px; width: 37px"
      />
      <el-input
        v-model="queryParams.customerName"
        placeholder="客户名称"
        clearable
        @keyup.enter.native="handleQuery"
        class="input_first"
      >
      </el-input>

      <img
        src="../../../assets/images/sousuo.png"
        style="margin-bottom: -13px; width: 37px"
      />
      <el-input
        v-model="queryParams.materialsName"
        placeholder="产品/物料名称"
        clearable
        @keyup.enter.native="handleQuery"
        class="input_first"
      >
      </el-input>
      <el-row :gutter="10" class="mb8">
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="selectionUpdate()"
            v-hasPermi="['fabricate:productionorder:edit']"
            >制定计划
          </el-button>
        </el-form-item>
      </el-row>
    </el-form>

    <el-table
      v-loading="loading"
      :data="productionorderList"
      row-key="id"
      @selection-change="handleSelectionChange"
      :tree-props="{ children: 'children' }"
      border
    >
      <el-table-column
        :selectable="selectable"
        type="selection"
        width="55"
        align="center"
      />
      <el-table-column
        label="生产订单号"
        prop="number"
        width="170"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <router-link
            v-if="scope.row.merge == 0"
            :to="{
              path: '/production/formulatePlan',
              query: { id: scope.row.id },
            }"
          >
            <span style="color: rgb(7, 171, 247)">{{ scope.row.number }}</span>
          </router-link>
          <router-link
            v-else-if="scope.row.merge == 2"
            :to="{
              path: '/production/formulatePlans',
              query: { selid: scope.row.id },
            }"
          >
            <span style="color: rgb(7, 171, 247)">{{ scope.row.number }}</span>
          </router-link>
          <span v-else>{{ scope.row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="销售订单号"
        align="center"
        prop="salesOrderNumber"
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        label="订单完成进度"
        align="center"
        prop="schedule"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <!-- <el-progress
            :text-inside="true"
            :stroke-width="20"
            :percentage="
              scope.row.status == 2 ? 60 : scope.row.status == 3 ? 100 : 0
            "
            status="exception"
            :color="color"
          ></el-progress> -->
          <el-progress
            :text-inside="true"
            :stroke-width="20"
            :percentage="scope.row.schedule"
            status="exception"
            :define-back-color="backcolor"
            :color="color"
          ></el-progress>
        </template>
      </el-table-column>
      <el-table-column
        label="计划状态"
        align="center"
        prop="status"
        width="120"
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
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        label="客户名称"
        align="center"
        prop="customerName"
        width="160"
        show-overflow-tooltip
      />
      <el-table-column
        label="产品编号"
        align="center"
        prop="materialsNumber"
        width="160"
        show-overflow-tooltip
      />
      <el-table-column
        label="产品名称"
        align="center"
        prop="materialsName"
        width="160"
        show-overflow-tooltip
      />
      <el-table-column label="产品图片" align="center" width="130">
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
        width="120"
        show-overflow-tooltip
      />
      <!-- <el-table-column
        label="是否合并"
        align="center"
        prop="merge"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_fab_order_merge"
            :value="scope.row.merge"
          />
        </template>
      </el-table-column> -->
      <el-table-column
        label="要求交货日期"
        align="center"
        prop="date"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.status == '1'"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['fabricate:productionorder:edit']"
            >制定计划
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-else
            disabled
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['fabricate:productionorder:edit']"
            >制定计划
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.status == '2'"
            icon="el-icon-arrow-down"
            @click="handleSend(scope.row)"
            v-hasPermi="['fabricate:productionorder:remove']"
            >计划下达
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-else
            disabled
            icon="el-icon-arrow-down"
            @click="handleSend(scope.row)"
            v-hasPermi="['fabricate:productionorder:remove']"
            >计划下达
          </el-button>
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

    <!-- 添加或修改生产制造--生产订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="生产订单号" prop="number">
          <el-input v-model="form.number" placeholder="请输入生产订单号" />
        </el-form-item>
        <el-form-item label="销售订单id" prop="salesOrderId">
          <el-input
            v-model="form.salesOrderId"
            placeholder="请输入销售订单id"
          />
        </el-form-item>
        <el-form-item label="计划状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.zcii_fab_plan_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
              >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="客户编号" prop="customerNumber">
          <el-input
            v-model="form.customerNumber"
            placeholder="请输入客户编号"
          />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="产品编号" prop="materialsNumber">
          <el-input
            v-model="form.materialsNumber"
            placeholder="请输入产品编号"
          />
        </el-form-item>
        <el-form-item label="产品名称" prop="materialsName">
          <el-input v-model="form.materialsName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="需求数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入需求数量" />
        </el-form-item>
        <el-form-item label="要求交货日期" prop="date">
          <el-date-picker
            clearable
            v-model="form.date"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择要求交货日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="租户ID" prop="tenantId">
          <el-input v-model="form.tenantId" placeholder="请输入租户ID" />
        </el-form-item>
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
  listProductionorder,
  addProductionorder,
  updateProductionorder,
} from "@/api/fabricate/productionorder";

export default {
  name: "Productionorder",
  dicts: ["zcii_fab_plan_status", "zcii_fab_order_merge"],
  data() {
    return {
      //进度条颜色
      color: "#67C23A",
      //进度条底色
      backcolor: "#80f0a5",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中数组
      vas: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 生产制造--生产订单表格数据
      productionorderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: null,
        salesOrderNumber: null,
        status: null,
        customerNumber: null,
        customerName: null,
        materialsId: null,
        materialsNumber: null,
        materialsName: null,
        count: null,
        date: null,
        tenantId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询生产制造--生产订单列表 */
    getList() {
      this.loading = true;
      listProductionorder(this.queryParams).then((response) => {
        this.productionorderList = response.rows;
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
        number: null,
        salesOrderId: null,
        status: 0,
        customerNumber: null,
        customerName: null,
        materialsId: null,
        materialsNumber: null,
        materialsName: null,
        count: null,
        date: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        tenantId: null,
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
      this.queryParams.customerName = null;
      this.queryParams.materialsName = null;
      this.queryParams.salesOrderNumber = null;
      this.queryParams.number = null;
      this.handleQuery();
    },
    //复选框禁用
    selectable(row, rowIndex) {
      // return true; //不禁用
      if (row.status == "1") {
        return true; //不禁用
      } else {
        return false; //禁用
      }
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.vas = selection.map((item) => item.materialsNumber);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加生产制造--生产订单";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      let params = { id: id };
      this.$tab.openPage("制定计划", "/production/formulatePlan", params);
    },

    /** 多选制定计划 */
    selectionUpdate() {
      this.reset();
      const id = this.ids;
      let params = { id: id };

      if (id.length == 0) {
        this.$message({
          showClose: true,
          message: "请先选择信息！",
          type: "warning",
        });
        return;
      }
      if (this.ids.length > 1) {
        let isVas = this.vas.every((element) => element === this.vas[0]);
        if (isVas) {
          this.$tab.openPage("制定计划", "/production/formulatePlans", params);
        } else {
          this.$message({
            showClose: true,
            message: "不同的产品不可以合并！请检查您选中的产品信息",
            type: "warning",
          });
        }
      } else {
        this.$tab.openPage("制定计划", "/production/formulatePlan", params);
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateProductionorder(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProductionorder(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 下达按钮操作 */
    handleSend(row) {
      const id = row.id || this.ids;
      let params = { id: id };
      this.$tab.openPage("下达计划", "/production/formulatePlan", params);
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "fabricate/productionorder/export",
        {
          ...this.queryParams,
        },
        `productionorder_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
<style lang="scss" scoped>
.input_first {
  margin-left: 8px;
  width: 190px;
}

.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}

.mb8 {
  margin-top: 10px;
}
</style>
