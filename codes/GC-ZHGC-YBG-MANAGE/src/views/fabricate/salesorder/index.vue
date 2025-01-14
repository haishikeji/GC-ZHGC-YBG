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
        v-model="queryParams.name"
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
      <el-select v-model="queryParams.status" placeholder="请选择订单状态">
        <el-option
          v-for="dict in dict.type.zcii_fabricate_sales_order_status"
          :key="dict.value"
          :label="dict.label"
          :value="dict.value"
        ></el-option>
      </el-select>
      <el-row :gutter="10" class="mb8">
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
          >搜索
          </el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置
          </el-button
          >
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['fabricate:salesorder:add']"
          >新增
          </el-button>
        </el-form-item>
      </el-row>
    </el-form>

    <el-table
      v-loading="loading"
      :data="salesorderList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column
        label="销售订单号"
        align="center"
        prop="number"
        width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-link type="primary" @click="salesOrderDetail(scope.row)">{{
              scope.row.number
            }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="订单完成进度"
        align="center"
        prop="schedule"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <!-- <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.status==1?60:100"
                       status="exception" :color="color"
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
        label="订单状态"
        align="center"
        prop="status"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_fabricate_sales_order_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="客户编号"
        align="center"
        prop="customerNumber"
        width="180"
        show-overflow-tooltip
      />
      <el-table-column
        label="客户名称"
        align="center"
        prop="customerName"
        width="180"
        show-overflow-tooltip
      />
      <el-table-column
        label="订单类型"
        align="center"
        prop="type"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_sales_order_type"
            :value="scope.row.type"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="是否签署合同"
        align="center"
        prop="isSign"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_fab_is_sign"
            :value="scope.row.isSign"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="合同编号"
        align="center"
        prop="contractNumber"
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        label="要求交货日期"
        align="center"
        prop="deliveryDate"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliveryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="创建人" align="center" prop="createBy" width="120" show-overflow-tooltip/>-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-right"
            @click="sendOrder(scope.row)"
            v-if="scope.row.status == '1' && scope.row.type == '1'"
          >下发
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-right"
            @click="sendOrder(scope.row)"
            v-else
            disabled
          >下发
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-left"
            @click="withdrawOder(scope.row)"
            v-if="scope.row.status == '0' && scope.row.type == '1'"
          >撤回
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-left"
            @click="withdrawOder(scope.row)"
            v-else
            disabled
          >撤回
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['fabricate:salesorder:edit']"
            v-if="scope.row.status == '1'"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['fabricate:salesorder:remove']"
            v-if="scope.row.status == '1'"
          >删除
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
  </div>
</template>

<script>
import {
  listSalesorder,
  delSalesorder,
  sendOrder,
  withdrawOder
} from '@/api/fabricate/salesorder'

export default {
  name: 'Salesorder',
  dicts: ['zcii_fab_is_sign', 'zcii_fabricate_sales_order_status','zcii_sales_order_type'],
  data() {
    return {
      //销售订单界面
      //进度条颜色
      color: '#67C23A',
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
      // 生产制造-销售订单表格数据
      salesorderList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: null,
        name: null,
        status: null,
        customerId: null,
        isSign: null,
        contractNumber: null,
        deliveryDate: null,
        consigneeName: null,
        consigneePhone: null,
        province: null,
        city: null,
        area: null,
        address: null,
        tenantId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询生产制造-销售订单列表 */
    getList() {
      this.loading = true
      listSalesorder(this.queryParams).then((response) => {
        this.salesorderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        status: 0,
        customerId: null,
        isSign: null,
        contractNumber: null,
        deliveryDate: null,
        consigneeName: null,
        consigneePhone: null,
        province: null,
        city: null,
        area: null,
        address: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        tenantId: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.number = null
      this.queryParams.name = null
      this.queryParams.status = null
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$tab.openPage('新增销售订单', '/production/addSalesOrder')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      let params = { id: id }
      this.$tab.openPage(
        '修改销售订单信息',
        '/production/addSalesOrder',
        params
      )
    },
    salesOrderDetail(row) {
      const id = row.id || this.ids
      let params = { id: id }
      this.$tab.openPage(
        '查看销售订单详情信息',
        '/production/salesOrderDetail',
        params
      )
    },
    /** 下发按钮操作 */
    sendOrder(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认下发该条销售订单的数据项？')
        .then(function() {
          return sendOrder(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('下发成功')
        })
        .catch(() => {
        })
    } /** 撤回按钮操作 */,
    withdrawOder(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认撤回该条销售订单的数据项？')
        .then(function() {
          return withdrawOder(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('撤回成功')
        })
        .catch(() => {
        })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal
        .confirm('是否确认删除生产制造-销售订单编号为"' + ids + '"的数据项？')
        .then(function() {
          return delSalesorder(ids)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'fabricate/salesorder/export',
        {
          ...this.queryParams
        },
        `salesorder_${new Date().getTime()}.xlsx`
      )
    }
  }
}
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
