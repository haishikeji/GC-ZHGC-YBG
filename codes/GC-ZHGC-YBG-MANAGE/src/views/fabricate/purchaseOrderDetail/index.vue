<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="采购订单id" prop="purchaseOrderId">
        <el-input
          v-model="queryParams.purchaseOrderId"
          placeholder="请输入采购订单id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品编号" prop="materialsNumber">
        <el-input
          v-model="queryParams.materialsNumber"
          placeholder="请输入产品编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="materialsName">
        <el-input
          v-model="queryParams.materialsName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品id" prop="materialsId">
        <el-input
          v-model="queryParams.materialsId"
          placeholder="请输入产品id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位" prop="unitId">
        <el-input
          v-model="queryParams.unitId"
          placeholder="请输入单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采购价" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入采购价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采购数量" prop="count">
        <el-input
          v-model="queryParams.count"
          placeholder="请输入采购数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="含税金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入含税金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户ID" prop="tenantId">
        <el-input
          v-model="queryParams.tenantId"
          placeholder="请输入租户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['reportwork:purchaseOrderDetail:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['reportwork:purchaseOrderDetail:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['reportwork:purchaseOrderDetail:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['reportwork:purchaseOrderDetail:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="purchaseOrderDetailList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column
        label="采购订单id"
        align="center"
        prop="purchaseOrderId"
      />
      <el-table-column label="产品编号" align="center" prop="materialsNumber" />
      <el-table-column label="产品名称" align="center" prop="materialsName" />
      <el-table-column label="产品id" align="center" prop="materialsId" />
      <el-table-column label="单位" align="center" prop="unitId" />
      <el-table-column label="采购价" align="center" prop="price" />
      <el-table-column label="采购数量" align="center" prop="count" />
      <el-table-column label="含税金额" align="center" prop="amount" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="租户ID" align="center" prop="tenantId" />
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
            v-hasPermi="['reportwork:purchaseOrderDetail:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['reportwork:purchaseOrderDetail:remove']"
            >删除</el-button
          >
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

    <!-- 添加或修改生产制造-采购订单明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="采购订单id" prop="purchaseOrderId">
          <el-input
            v-model="form.purchaseOrderId"
            placeholder="请输入采购订单id"
          />
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
        <el-form-item label="产品id" prop="materialsId">
          <el-input v-model="form.materialsId" placeholder="请输入产品id" />
        </el-form-item>
        <el-form-item label="单位" prop="unitId">
          <el-input v-model="form.unitId" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="采购价" prop="price">
          <el-input v-model="form.price" placeholder="请输入采购价" />
        </el-form-item>
        <el-form-item label="采购数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入采购数量" />
        </el-form-item>
        <el-form-item label="含税金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入含税金额" />
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
  listPurchaseOrderDetail,
  getPurchaseOrderDetail,
  delPurchaseOrderDetail,
  addPurchaseOrderDetail,
  updatePurchaseOrderDetail,
} from "@/api/fabricate/purchaseOrderDetail";

export default {
  name: "PurchaseOrderDetail",
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
      // 生产制造-采购订单明细表格数据
      purchaseOrderDetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        purchaseOrderId: null,
        materialsNumber: null,
        materialsName: null,
        materialsId: null,
        unitId: null,
        price: null,
        count: null,
        amount: null,
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
    /** 查询生产制造-采购订单明细列表 */
    getList() {
      this.loading = true;
      listPurchaseOrderDetail(this.queryParams).then((response) => {
        this.purchaseOrderDetailList = response.rows;
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
        purchaseOrderId: null,
        materialsNumber: null,
        materialsName: null,
        materialsId: null,
        unitId: null,
        price: null,
        count: null,
        amount: null,
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
      this.resetForm("queryForm");
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
      this.open = true;
      this.title = "添加生产制造-采购订单明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPurchaseOrderDetail(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改生产制造-采购订单明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updatePurchaseOrderDetail(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPurchaseOrderDetail(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
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
          '是否确认删除生产制造-采购订单明细编号为"' + ids + '"的数据项？'
        )
        .then(function () {
          return delPurchaseOrderDetail(ids);
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
        "reportwork/purchaseOrderDetail/export",
        {
          ...this.queryParams,
        },
        `purchaseOrderDetail_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
