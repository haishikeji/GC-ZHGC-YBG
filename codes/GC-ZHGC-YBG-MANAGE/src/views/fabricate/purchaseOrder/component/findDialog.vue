<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="35">
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
          <div style="text-align: left; margin-right: 20px">
            <span class="dialog-footer">
              <el-button
                type="primary"
                size="mini"
                @click="backPrev"
                style="background-color: rgb(51, 171, 160); border: 0"
                icon="el-icon-top-left"
                >返 回</el-button
              >
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
          <b><span style="color: red; width: 10px">*</span>基本信息</b>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="供应商编号" prop="supplierNumber">
            {{ form.supplierNumber }}
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="供应商名称" prop="supplierName">
            {{ form.supplierName }}
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="要求交货日期" prop="deliveryDate">
            {{ form.deliveryDate }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <div class="order_type" style="margin-left: 7%; margin-right: 4%">
            工单状态
          </div>
          <span class="order_type"
            ><dict-tag
              :options="dict.type.zcii_purchase_order_type"
              :value="form.status"
          /></span>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="采购合同号" prop="contractNumber">
            {{ form.contractNumber }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="margin-top: 20px">
        <el-col :span="24" class="mb20">
          <b><span style="color: red; width: 10px">*</span>产品信息</b>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="22" style="margin-left: 50px">
          <el-table
            v-loading="loading"
            :data="materialsList"
            border=""
            :summary-method="getSummaries"
            show-summary
          >
            <el-table-column
              label="产品编码"
              align="center"
              prop="materialsNumber"
              width="260"
            >
            </el-table-column>
            <el-table-column
              label="产品名称"
              align="center"
              prop="materialsName"
            >
            </el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitId">
              <template slot-scope="scope">
                <dict-tag
                  :options="dict.type.zcii_bas_unit_type"
                  :value="scope.row.unitId"
                />
              </template>
            </el-table-column>
            <el-table-column label="采购数量" align="center" prop="count">
              <!-- <template slot-scope="scope">
                {{scope.row.count}}
              </template> -->
            </el-table-column>
            <el-table-column label="采购价" align="center" prop="price">
            </el-table-column>
            <el-table-column label="小计" align="center" prop="amount">
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <el-row style="margin-top: 26px">
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="采购员" prop="purchaseName">
            {{ form.purchaseName }}
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="备注" prop="remark">
            {{ form.remark }}
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {
  addPurchaseOrder,
  getPurchaseOrderVo,
  updatePurchaseOrder,
} from "@/api/fabricate/purchaseOrder";

import { pcaa } from "area-data";
import ChooseSupplierComponent from "@/views/components/basic/ChooseSupplierComponent.vue";
import ChooseMaterialsComponent from "@/views/components/fabricate/ChooseMaterialsComponent.vue";
import ChooseUserComponent from "@/views/components/storage/ChooseUserComponent.vue";
import Enumerable from "linq";

export default {
  name: "addPurchaseOrder",
  components: {
    ChooseMaterialsComponent,
    ChooseSupplierComponent,
    ChooseUserComponent,
  },
  dicts: ["zcii_fab_is_sign", "zcii_bas_unit_type", "zcii_purchase_order_type"],

  data() {
    return {
      // 遮罩层
      loading: true,
      pcaaData: pcaa,
      isRequired: false, // 控制文本框是否必填
      // 区域数据
      provinces: pcaa["86"],
      citys: [],
      id: undefined,
      areas: [],
      //产品集合
      materialsList: [],
      // 显示搜索条件
      showSearch: true,

      chooseRow: null,
      // 弹出层标题
      title: "",
      deliveryDate: null,
      // 是否显示弹出层

      // 表单参数
      form: {
        supplierName: "",
        supplierId: "",
        isSign: 0,
      },
      // 表单校验
      rules: {
        supplierName: [
          { required: true, message: "供应商名称不能为空", trigger: "blur" },
        ],
        isSign: [
          {
            required: true,
            message: "是否签署合同不能为空",
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    if (this.$route.query.id != undefined) {
      let id = this.$route.query.id;
      this.id = id;
      this.getList();
    } else {
      //交货时间默认当前时间往后推一周
      const now = new Date();
      const sevenDaysInMs = 7 * 24 * 60 * 60 * 1000; // 7天的毫秒数
      now.setTime(now.getTime() + sevenDaysInMs);
      this.deliveryDate = now;

      setTimeout(() => {
        //调用子组件的方法
        this.$refs["reset_child"].reset_data();
      }, 10);
      this.loading = false;
    }
  },
  methods: {
    // 处理下拉选中事件
    handleSelectChange(value) {
      if (value == 0) {
        this.isRequired = true;
      } else {
        this.isRequired = false;
      }
    },
    chooseRow_Material(row, index) {
      let data = Object.assign({}, this.materialsList[index]);
      data.materialsId = row.id;
      data.materialsName = row.name;
      data.materialsNumber = row.number;
      data.unitId = row.unitId;
      data.price = row.unitPrice;
      this.materialsList.splice(index, 1, data);
    },
    // 采购人选择
    chooseRow_user(data) {
      this.form.purchaseId = data.userId;
    },
    // 取消按钮
    cancel() {
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        status: 0,
        supplierId: null,
        isSign: 1,
        province: null,
        city: null,
        area: null,
        address: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        tenantId: null,
        supplierName: null,
        materialsList: [],
      };
      this.resetForm("form");
    },

    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总价";
          return;
        }
        const values = data.map((item) => Number(item[column.property]));
        if (
          column.label == "小计" ||
          column.label == "采购价" ||
          column.label == "采购数量"
        ) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              let val = parseFloat(prev) + parseFloat(curr);
              // console.log("val...",val);
              return parseFloat(val).toFixed(2);
            } else {
              // console.log("prev...",prev);
              return parseFloat(prev).toFixed(2);
            }
          }, 0);
          if (column.label != "采购数量") {
            sums[index] += " 元";
          } else {
            sums[index] += "";
          }
        } else {
          sums[index] = "";
        }
      });

      return sums;
    },
    //返回页面
    backPrev() {
      this.reset();
      this.$tab.openPage("采购订单信息", "/production/purchaseOrder");
    },
    addRow() {
      this.materialsList.push({});
    },

    disabledDate(time) {
      return time.getTime() < Date.now() - 8.64e7; // 8.64e7 毫秒数代表一天
    },

    //减行
    delRow() {
      let rowIndex = Enumerable.from(this.materialsList).indexOf(
        this.chooseRow
      );
      this.materialsList.splice(rowIndex, 1);
      this.chooseRow = null;
    },

    chooseRow_supplier(data) {
      this.form.supplierId = data.id;
      this.form.supplierName = data.name;
    },

    getList() {
      getPurchaseOrderVo(this.id).then((response) => {
        this.form = response.data;
        this.deliveryDate = this.form.deliveryDate;
        this.materialsList = response.data.materialsList;
        this.loading = false;
        //延迟加载
        setTimeout(() => {
          //调用子组件的方法
          this.$refs["reset_child"].assign_check_id(response.data.purchaseId);
          this.$refs["reset_child_supplier"].assign_check(
            response.data.supplierId
          );
        }, 10);
      });
    },

    sumAmount(row) {
      if (row.count != undefined && row.price != undefined) {
        let amount = row.count * parseFloat(row.price);
        if (!isNaN(amount)) {
          row.amount = parseFloat(amount).toFixed(2);
        } else {
          row.amount = "";
        }
        return row;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.input_first {
  margin-left: 8px;
  width: 200px;
}

.addr_fo_sel .el-select {
  margin-right: 20px;
}
.order_type {
  float: left;
  font-size: 14px;
  color: #606266;
}

.col_span {
  width: 300px;
}
</style>
