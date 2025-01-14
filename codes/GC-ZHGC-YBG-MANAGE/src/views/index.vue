<template>
  <div
    id="bigScreen"
    ref="bigScreen"
    class="index-container big-screen"
    style="width: 100%; overflow-x: auto"
  >
    <el-row :gutter="10" style="width: 100%">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>首页</span>
            <span style="margin-left: 10px">
              <FullSrcreenButton
                style="
                  position: absolute;
                  right: 12px;
                  top: 6px;
                  height: 19px;
                  display: flex;
                "
                container-id="bigScreen"
                :expand-src="require('@/assets/images/icon-full-screen.png')"
                :shrink-src="
                  require('@/assets/images/icon-back-full-screen.png')
                "
              />
            </span>
          </div>
          <el-row :gutter="10" class="pt-10" style="height: 50%">
            <el-col :span="5">
              <el-row>
                <el-card class="card_height">
                  <el-row style="line-height: 30px; margin-left: 10px">
                    <el-col :span="24">产品总产量</el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="24" class="font_top">{{
                      materialsVo.sumAll
                    }}</el-col>
                  </el-row>
                  <el-row style="line-height: 50px; margin-left: 10px">
                    <el-col :span="6" style="font-size: 14px">合格量</el-col>
                    <el-col
                      :span="6"
                      style="color: rgb(18, 245, 112) d; font-size: 16px"
                      >{{ materialsVo.sumQualified }}</el-col
                    >
                    <el-col :span="6" style="font-size: 14px">不合格量</el-col>
                    <el-col :span="6" style="color: red; font-size: 16px">{{
                      materialsVo.sumUnqualified
                    }}</el-col>
                  </el-row>
                </el-card>
                <el-card class="card_height" style="margin-top: 10px">
                  <el-row class="font_center">
                    <el-col :span="5" class="font_left">{{
                      reportTaskVo.orderCount
                    }}</el-col>
                    <el-col :span="4" class="font_left">{{
                      reportTaskVo.taskQuantity
                    }}</el-col>
                    <el-col :span="5" class="font_right">{{
                      reportTaskVo.noProduction
                    }}</el-col>
                    <el-col :span="5" class="font_right">{{
                      reportTaskVo.inProduction
                    }}</el-col>
                    <el-col :span="5" class="font_right">{{
                      reportTaskVo.completionQuantity
                    }}</el-col>
                  </el-row>
                  <el-row class="font_center_bom">
                    <el-col :span="5">订单数</el-col>
                    <el-col :span="4">工单数</el-col>
                    <el-col :span="5">待报工数</el-col>
                    <el-col :span="5">已报工数</el-col>
                    <el-col :span="5">已完工数</el-col>
                  </el-row>
                  <el-row style="margin-top: 20px">
                    <el-col :span="24">
                      <el-button-group style="float: right">
                        <el-button
                          size="small"
                          @click="taskReportCount(0)"
                          :class="{ active: taskType == 0 }"
                          >当天</el-button
                        >
                        <el-button
                          size="small"
                          @click="taskReportCount(1)"
                          :class="{ active: taskType == 1 }"
                          >近一周</el-button
                        >
                        <el-button
                          size="small"
                          @click="taskReportCount(2)"
                          :class="{ active: taskType == 2 }"
                          >近一月</el-button
                        >
                        <el-button
                          size="small"
                          @click="taskReportCount(3)"
                          :class="{ active: taskType == 3 }"
                          >近半年</el-button
                        >
                      </el-button-group>
                    </el-col>
                  </el-row>
                </el-card>
              </el-row>
            </el-col>
            <el-col :span="5">
              <el-row>
                <el-card class="card_height">
                  <el-row class="font_center">
                    <el-col :span="5" class="font_left">{{
                      toProduceSum
                    }}</el-col>
                    <el-col :span="6" class="font_left">{{
                      inProductionSum
                    }}</el-col>
                    <el-col :span="7" class="font_right">{{
                      materialsStatusVo.sumQualified
                    }}</el-col>
                    <el-col :span="6" class="font_right_red">{{
                      materialsStatusVo.sumUnqualified
                    }}</el-col>
                  </el-row>
                  <el-row class="font_center_bom">
                    <el-col :span="5">待生产</el-col>
                    <el-col :span="6">生产中</el-col>
                    <el-col :span="7">合格数</el-col>
                    <el-col :span="6">不合格数</el-col>
                  </el-row>
                  <el-row style="margin-top: 20px">
                    <el-col :span="24">
                      <el-button-group style="float: right">
                        <el-button
                          size="small"
                          @click="materialsStatusSum(0)"
                          :class="{ active: materialsType == 0 }"
                          >当天</el-button
                        >
                        <el-button
                          size="small"
                          @click="materialsStatusSum(1)"
                          :class="{ active: materialsType == 1 }"
                          >近一周</el-button
                        >
                        <el-button
                          size="small"
                          @click="materialsStatusSum(2)"
                          :class="{ active: materialsType == 2 }"
                          >近一月</el-button
                        >
                        <el-button
                          size="small"
                          @click="materialsStatusSum(3)"
                          :class="{ active: materialsType == 3 }"
                          >近半年</el-button
                        >
                      </el-button-group>
                    </el-col>
                  </el-row>
                </el-card>
                <el-card class="card_height" style="margin-top: 10px">
                  <el-row class="font_center">
                    <el-col :span="8" class="font_left">
                      <el-button
                        type="text"
                        @click="viewCheck"
                        class="jumpStyle"
                        >{{ inventoryCount }}
                      </el-button></el-col
                    >
                    <el-col :span="8" class="font_left">
                      <el-button
                        type="text"
                        @click="viewCheck"
                        class="jumpStyle"
                        >{{ enterCount }}</el-button
                      ></el-col
                    >
                    <el-col :span="8" class="font_left">
                      <el-button
                        type="text"
                        @click="viewCheck"
                        class="jumpStyle"
                        >{{ sockCount }}</el-button
                      ></el-col
                    >
                  </el-row>

                  <el-row class="font_center_bom">
                    <el-col :span="8">库存总数</el-col>
                    <el-col :span="8">入库数量</el-col>
                    <el-col :span="8">出库数量</el-col>
                  </el-row>
                  <el-row style="margin-top: 20px">
                    <el-col :span="24">
                      <el-button-group style="float: right">
                        <el-button
                          size="small"
                          @click="storageCount(0)"
                          :class="{ active: storageType == 0 }"
                          >当天</el-button
                        >
                        <el-button
                          size="small"
                          @click="storageCount(1)"
                          :class="{ active: storageType == 1 }"
                          >近一周</el-button
                        >
                        <el-button
                          size="small"
                          @click="storageCount(2)"
                          :class="{ active: storageType == 2 }"
                          >近一月</el-button
                        >
                        <el-button
                          size="small"
                          @click="storageCount(3)"
                          :class="{ active: storageType == 3 }"
                          >近半年</el-button
                        >
                      </el-button-group>
                    </el-col>
                  </el-row>
                </el-card>
              </el-row>
            </el-col>
            <el-col :span="7">
              <el-card>
                <PieTypesProductYieldChart></PieTypesProductYieldChart>
              </el-card>
            </el-col>
            <el-col :span="7">
              <el-card>
                <PieProductionLineOutputChart></PieProductionLineOutputChart>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="10" class="pt-10" style="height: 50%">
            <el-col :span="10">
              <el-card>
                <LineMonthlyProductionTrendChart></LineMonthlyProductionTrendChart>
              </el-card>
            </el-col>
            <el-col :span="7">
              <el-card>
                <BarYieldachievementRankingChart></BarYieldachievementRankingChart>
              </el-card>
            </el-col>
            <el-col :span="7">
              <el-card>
                <PieMaterialdefectChart></PieMaterialdefectChart>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PieTypesProductYieldChart from "@/views/dashboard/index/PieTypesProductYieldChart";
import LineMonthlyProductionTrendChart from "@/views/dashboard/index/LineMonthlyProductionTrendChart";
import PieMaterialdefectChart from "@/views/dashboard/index/PieMaterialdefectChart";
import PieProductionLineOutputChart from "@/views/dashboard/index/PieProductionLineOutputChart";
import BarYieldachievementRankingChart from "@/views/dashboard/index/BarYieldachievementRankingChart";
import FullSrcreenButton from "@/views/components/FullSrcreenButton";
import {
  getMaterialsSum,
  getTaskQuantitySum,
} from "@/api/fabricate/workpeport";
import {
  getTaskReportCount,
  getReportAllStatus,
} from "@/api/fabricate/workorder";
import { getInventoryCount } from "@/api/storage/inventory";
import { getStorageEnterCount } from "@/api/storage/enter";
import { getStorageStockCount } from "@/api/storage/stock";

export default {
  name: "Index",
  components: {
    PieTypesProductYieldChart,
    LineMonthlyProductionTrendChart,
    PieMaterialdefectChart,
    FullSrcreenButton,
    PieProductionLineOutputChart,
    BarYieldachievementRankingChart,
  },
  data() {
    return {
      ProductMap: "ProductMap",
      materialsVo: {}, //产品总产量
      reportTaskVo: {}, //订单、工单数量
      reportStatusVo: {},
      taskQuantityList: [],
      materialsStatusVo: {}, //根据状态统计-报工数量
      toProduceSum: 0, //待生产 状态为5 待报工
      inProductionSum: 0, //生产中 状态为6 执行中
      type: 4, //0默认当天  1 近一周 2 近一月  3近半年 4 所有
      taskType: 0, //0默认当天
      materialsType: 0,
      storageType: 0,
      inventoryCount: 0,
      sockCount: 0,
      enterCount: 0,
    };
  },

  created() {
    this.materialsSum(this.type); //生产订单数
    this.taskReportCount(this.taskType);
    this.reportAllStatus();
    this.materialsStatusSum(this.materialsType);
    this.inventoryAllCount(this.storageType);
    this.storageEnterCount(this.storageType);
    this.storageStockCount(this.storageType);
  },

  methods: {
    viewCheck() {
      this.$tab.openPage("库存管理", "/stort/inventory");
    },
    materialsSum(type) {
      this.type = type;
      getMaterialsSum(this.type).then((response) => {
        this.materialsVo = response.data;
      });
    },

    //合格数，不合格数
    materialsStatusSum(materialsType) {
      this.materialsType = materialsType;
      getMaterialsSum(this.materialsType).then((response) => {
        this.materialsStatusVo = response.data;
      });
      //待生产、生产中
      getTaskQuantitySum(this.materialsType).then((response) => {
        this.taskQuantityList = response.data;
        if (this.taskQuantityList.length == 0) {
          this.toProduceSum = 0;
          this.inProductionSum = 0;
        } else {
          this.taskQuantityList.forEach((element) => {
            if (element.status == 5) {
              this.toProduceSum = element.taskQuantity;
            }
            if (element.status == 6) {
              this.inProductionSum = element.taskQuantity;
            }
          });
        }
      });
    },

    //订单数量、工单数量、未报工、已报工、完工数量
    taskReportCount(taskType) {
      this.taskType = taskType;
      getTaskReportCount(this.taskType).then((response) => {
        this.reportTaskVo = response.data;
        console.log(
          "订单数量、工单数量、待报工、已报工、完工数量...",
          this.reportTaskVo
        );
      });
    },

    //获取所有工单状态数量
    reportAllStatus() {
      getReportAllStatus().then((response) => {
        this.reportStatusVo = response.data;
      });
    },

    storageCount(storageType) {
      this.storageType = storageType;
      getInventoryCount(this.storageType).then((response) => {
        this.inventoryCount = response.data;
      });
      getStorageStockCount(this.storageType).then((response) => {
        this.sockCount = response.data;
      });
      getStorageEnterCount(this.storageType).then((response) => {
        this.enterCount = response.data;
      });
    },
    //库存
    inventoryAllCount(taskType) {
      this.taskType = taskType;
      getInventoryCount(this.taskType).then((response) => {
        this.inventoryCount = response.data;
      });
    },
    //入库
    storageEnterCount(taskType) {
      this.taskType = taskType;
      getStorageEnterCount(this.taskType).then((response) => {
        this.enterCount = response.data;
      });
    },
    //出库
    storageStockCount(taskType) {
      this.taskType = taskType;
      getStorageStockCount(this.taskType).then((response) => {
        this.sockCount = response.data;
      });
    },
  },
};
</script>

<style lang="scss">
.index-container {
  padding: 0 !important;
  background: #f5f7f8 !important;
  .el-card__header {
    padding: 5px;
    min-height: 0 !important;
  }
  .el-card__body {
    padding: 5px;
  }

  .pt-10 el-card {
    border: 0px;
    padding: 0px;
  }
  .card_height {
    height: 190px;
  }
  .font_left {
    color: rgb(25, 120, 243);
    font-size: 16px;
  }
  .font_right {
    color: rgb(18, 245, 112);
    font-size: 16px;
  }
  .font_right_red {
    color: #f51212;
    font-size: 16px;
  }

  .font_top {
    font-size: 30px;
    color: rgb(25, 120, 243);
    line-height: 90px;
    margin-left: 10px;
  }
  .font_center {
    margin-top: 50px;
    line-height: 30px;
    font-size: 16px;
    margin-left: 10px;
    text-align: center;
  }
  .font_center_bom {
    line-height: 30px;
    font-size: 14px;
    margin-left: 10px;
    text-align: center;
  }
  .active {
    background-color: #e5f7f8;
  }
  .jumpStyle {
    border: none;
    color: #1978f3;
    font-size: 16px;
  }
}
</style>
