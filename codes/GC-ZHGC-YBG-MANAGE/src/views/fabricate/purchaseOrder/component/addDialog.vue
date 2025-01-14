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
              <el-button
                type="warning"
                size="mini"
                @click="submitForm"
                icon="el-icon-circle-check"
                >确 定</el-button
              >
              <el-button
                @click="cancel"
                size="mini"
                icon="el-icon-remove-outline"
                >取 消</el-button
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
          <el-form-item label="供应商编号" prop="supplierId">
            <ChooseSupplierComponent
              @chooseRow="chooseRow_supplier"
              ref="reset_child_supplier"
              :text="form.supplierName"
              :sourceurl="1"
              v-model="form.supplierId"
            ></ChooseSupplierComponent>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="供应商名称" prop="supplierName">
            <el-input
              v-model="form.supplierName"
              placeholder="请输入供应商名称"
              class="col_span"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="采购合同号" prop="contractNumber">
            <el-input
              v-model="form.contractNumber"
              placeholder="请输入采购合同号"
              class="col_span"
            />
          </el-form-item>
        </el-col>
       
      </el-row>

      <el-row>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="采购订单类型" prop="type">
            <el-select
              v-model="form.type"
              placeholder="请选择采购订单类型"
              class="col_span"
            >
              <el-option
                v-for="dict in dict.type.zcii_purchase_order_type"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="要求交货日期" prop="deliveryDate">
            <el-date-picker
              v-model="deliveryDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="请选择交货日期"
              :picker-options="{
                disabledDate: (time) =>
                  time.getTime() < new Date(new Date().setHours(0, 0, 0, 0)),
              }"
              class="col_span"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px">
        <el-col :span="18" class="mb20">
          <b><span style="color: red; width: 10px">*</span>产品信息</b>
        </el-col>
        <el-col :span="4">
          <div
            style="
              text-align: right;
              margin-right: 20px;
              color: rgb(32, 142, 245);
              font-size: 14px;
              margin-bottom: 10px;
            "
          >
            <a type="primary" plain @click="addRow()" size="mini">增行</a>
            <a
              type="primary"
              plain
              @click="delRow()"
              size="mini"
              style="margin-left: 20px"
              >减行</a
            >
          </div>
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
            height="400"
          >
            <el-table-column
              label="产品编码"
              align="center"
              prop="materialsNumber"
              width="260"
            >
              <template slot-scope="scope">
                <ChooseMaterialsComponent
                  @chooseRow="chooseRow_Material"
                  ref="material_child"
                  :index="scope.$index"
                  :text="scope.row.materialsNumber"
                  :value="scope.row.materialsId"
                ></ChooseMaterialsComponent>
              </template>
            </el-table-column>
            <el-table-column
              label="产品名称"
              align="center"
              prop="materialsName"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.materialsName" disabled />
              </template>
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
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.count"
                  maxlength="15"
                  @change="sumAmount(scope.row)"
                  oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字	
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位																			
																					"
                />
              </template>
            </el-table-column>
            <el-table-column label="采购价" align="center" prop="price">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.price"
                  maxlength="15"
                  @input="sumAmount(scope.row)"
                  oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
                />
              </template>
            </el-table-column>
            <el-table-column label="小计" align="center" prop="amount">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.amount"
                  maxlength="17"
                  oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <el-row style="margin-top: 26px">
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="采购员" prop="purchaseId">
            <ChooseUserComponent
              @chooseRow="chooseRow_user"
              ref="reset_child"
              :text="form.purchaseName"
              :sourceurl="1"
              v-model="form.purchaseId"
            ></ChooseUserComponent>
          </el-form-item>
        </el-col>
        <!-- <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="赠品数量" prop="giftCount">
            <el-input
              v-model="form.giftCount"
              placeholder="请输入赠品数量"
              class="col_span"
            />
          </el-form-item>
        </el-col> -->
        <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              placeholder="请输入备注"
              class="col_span"
            />
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
        type: [
          {
            required: true,
            message: "采购订单类型不能为空",
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
    chooseRow_Material(row, index) {
      let data = Object.assign({}, this.materialsList[index]);
      data.materialsId = row.id;
      data.materialsName = row.name;
      data.materialsNumber = row.number;
      data.unitId = row.unitId;
      data.price = row.unitPrice;
      let materId = this.materialsList.find((obj) => obj.materialsId == row.id);

      if (materId) {
        this.$refs.material_child.reset_data();
        this.$modal.msgError(
          "[" + data.materialsName + "] 物料已存在，请勿重复添加！"
        );
      } else {
        this.materialsList.splice(index, 1, data);
      }
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

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.deliveryDate == null || this.deliveryDate == undefined) {
            this.$modal.msgError("采购订单交货日期不能为空！");
            return;
          }
          if (!this.materialsList || this.materialsList.length < 1) {
            this.$modal.msgError("采购订单产品信息为空，请补充产品信息！");
            return;
          }

          this.form.deliveryDate = this.deliveryDate;
          let isOk = true;
          this.materialsList.forEach((element) => {
            if (element.price <= 0) {
              this.$modal.msgError("请输入采购订单产品价格！");
              isOk = false;
              throw Error();
            } else if (element.count <= 0) {
              this.$modal.msgError("请输入采购订单产品数量！");
              isOk = false;
              throw Error();
            }
          });

          this.form.materialsList = this.materialsList;

          if (isOk) {
            if (this.form.id != null) {
              updatePurchaseOrder(this.form)
                .then((response) => {
                  if (response.code == 200) {
                    this.$modal.msgSuccess("修改成功");
                    this.$tab.openPage(
                      "采购订单信息",
                      "/production/purchaseOrder"
                    );
                  }
                })
                .catch((error) => {
                  console.log(error);
                });
            } else {
              addPurchaseOrder(this.form)
                .then((response) => {
                  if (response.code == 200) {
                    this.$modal.msgSuccess("新增成功");
                    this.$tab.openPage(
                      "采购订单信息",
                      "/production/purchaseOrder"
                    );
                  }
                })
                .catch((error) => {
                  console.log(error);
                });
            }
          }
        }
      });
    },

    seletekey(e, type) {
      if (e) {
        switch (type) {
          case "province":
            Object.keys(this.provinces).filter((item) => {
              if (this.provinces[item] === e) {
                this.citys = this.pcaaData[item];
              }
            });
            break;
          case "city":
            Object.keys(this.citys).filter((item) => {
              if (this.citys[item] === e) {
                this.areas = this.pcaaData[item];
              }
            });
            break;
          case "area":
            break;
        }
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

.col_span {
  width: 300px;
}
</style>
