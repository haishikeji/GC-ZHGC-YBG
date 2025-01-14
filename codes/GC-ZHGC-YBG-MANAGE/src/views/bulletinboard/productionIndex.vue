<template>
  <div class="index-container-back" id="bigScreen" ref="bigScreen">
    <div :gutter="10" style="width: 1780px; height: 980px">
      <el-row>
        <el-col :span="24">
          <div style="display: flex; text-align: center">
            <img
              style="width: 100%; height: 70px"
              src="../../assets/images/zicc/board/u40237.svg"
            />
            <div
              style="
                font-size: 38px;
                font-weight: bold;
                color: aliceblue;
                text-align: center;
                padding: 2px 2px 2px 2px;
                box-sizing: border-box;
                display: block;
                position: absolute;
                align-self: center;
                width: 100%;
              "
            >
              企业生产看板
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 0px;
              top: 11px;
              width: 100%;
              height: 20px;
              display: flex;
            "
          >
            <div style="color: #45f9fa; font-size: 17px">
              {{ formattedDate }}
            </div>
            <FullSrcreenButton
              style="
                position: absolute;
                right: 0px;
                top: 5px;
                height: 21px;
                display: flex;
              "
              container-id="bigScreen"
              :expand-src="require('@/assets/images/icon-full-screen.png')"
              :shrink-src="require('@/assets/images/icon-back-full-screen.png')"
            />
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <div style="display: flex; margin-top: -10px">
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40235.svg" />
            </div>
            <div
              style="
                color: rgb(104, 179, 245);
                width: 100%;
                font-size: 20px;
                text-align: center;
              "
            >
              产品产量TOP10
            </div>
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40234.svg" />
            </div>
            <div style="clear: both"></div>
          </div>
          <div
            style="
              border: 1.5px solid rgb(78, 77, 146);
              margin-top: 20px;
              border-color: rgb(115, 115, 197);
            "
          >
            <BarProductionboardChart></BarProductionboardChart>
          </div>
        </el-col>
        <el-col :span="11">
          <el-row style="margin-top: 20px; margin-left: 30px">
            <el-col :span="10">
              <span style="font-size: 20px; color: aliceblue">产品总产量</span>
              <span
                style="
                  margin-left: 40px;
                  font-size: 24px;
                  color: rgb(104, 179, 245);
                "
                >{{ materialsVo.sumAll }}</span
              >
            </el-col>
            <el-col :span="7">
              <span style="color: aliceblue; font-size: 20px">合格量</span>
              <span
                style="
                  color: rgb(109, 241, 131);
                  font-size: 20px;
                  margin-left: 40px;
                "
                >{{ materialsVo.sumQualified }}</span
              >
            </el-col>
            <el-col :span="7">
              <span style="color: aliceblue; font-size: 20px">不合格量</span>
              <span
                style="
                  color: rgb(245, 7, 7);
                  font-size: 20px;
                  margin-left: 40px;
                "
                >{{ materialsVo.sumUnqualified }}</span
              >
            </el-col>
          </el-row>
          <div
            style="
              position: absolute;
              left: 50px;
              top: 0px;
              width: 0px;
              height: 0px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 494px;
                top: 79px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 495px;
                top: 81px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 717px;
                top: 213px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 501px;
                top: 206px;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 590px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                "
                >待生产</span
              >

              <span
                style="
                  color: rgb(80, 164, 238);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ toProduceSum }}</span
              >
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 338px;
              top: 0px;
              width: 0px;
              height: 0px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 468px;
                top: 79px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 468px;
                top: 81px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 690px;
                top: 212px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 475px;
                top: 206px;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 560px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                "
                >生产中</span
              >

              <span
                style="
                  color: rgb(80, 164, 238);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ inProductionSum }}</span
              >
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 622px;
              top: 0px;
              width: 0px;
              height: 0px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 444px;
                top: 79px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 444px;
                top: 81px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 667px;
                top: 212px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 451px;
                top: 206px;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 541px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                  margin-left: -10px;
                "
                >生产完成</span
              >

              <span
                style="
                  color: rgb(73, 241, 31);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ overProductionSum }}</span
              >
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 50px;
              top: 0px;
              width: 0px;
              height: 0px;
              margin-top: 180px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 494px;
                top: 70px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 495px;
                top: 73px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 717px;
                top: 203px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 501px;
                top: 196px;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 590px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                  margin-left: -20px;
                "
                >生产订单数量</span
              >

              <span
                style="
                  color: rgb(80, 164, 238);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ reportTaskVo.orderCount }}</span
              >
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 340px;
              top: 0px;
              width: 0px;
              height: 0px;
              margin-top: 180px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 465px;
                top: 70px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 465px;
                top: 72px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 688px;
                top: 203px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 471px;
                top: 196px;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 560px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                  margin-left: -10px;
                "
                >计划数量</span
              >

              <span
                style="
                  color: rgb(80, 164, 238);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ reportTaskVo.planSum }}</span
              >
            </div>
          </div>
          <div
            style="
              position: absolute;
              left: 620px;
              top: 0px;
              width: 0px;
              height: 0px;
              margin-top: 180px;
            "
          >
            <div
              style="
                position: absolute;
                width: 217px;
                height: 139px;
                left: 445px;
                top: 70px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40252.svg"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 237px;
                  height: 149px;
                "
              />
            </div>
            <div
              style="
                position: absolute;
                width: 14px;
                height: 15px;
                left: 445px;
                top: 72px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40253.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 669px;
                top: 203px;
                width: 14px;
                height: 15px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40254.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 451px;
                top: 196cqb;
                width: 41px;
                height: 16px;
                display: flex;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40255.svg"
                style="position: absolute"
              />
            </div>
            <div
              style="
                position: absolute;
                left: 540px;
                top: 76px;
                display: flex;
                width: 100px;
                line-height: 100px;
                text-align: center;
              "
            >
              <span
                style="
                  color: rgb(80, 164, 238);
                  font-size: 16px;
                  position: absolute;
                  margin-left: -10px;
                "
                >工单数量</span
              >

              <span
                style="
                  color: rgb(73, 241, 31);
                  margin-top: 50px;
                  font-size: 17px;
                  position: absolute;
                "
                >{{ reportTaskVo.taskQuantity }}</span
              >
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="display: flex; margin-top: -10px">
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40235.svg" />
            </div>
            <div
              style="
                color: rgb(104, 179, 245);
                width: 100%;
                font-size: 20px;
                text-align: center;
              "
            >
              产品产量分布图
            </div>
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40234.svg" />
            </div>
            <div style="clear: both"></div>
          </div>
          <div
            style="
              border: 1.5px solid rgb(78, 77, 146);
              margin-top: 20px;
              border-color: rgb(115, 115, 197);
            "
          >
            <PieProductionDistributionChart></PieProductionDistributionChart>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="15">
          <div style="top: 0px; width: 0px; display: flex">
            <div
              style="
                position: absolute;
                width: 1103px;
                height: 253px;
                left: 0px;
                top: 26px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40247.png"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 1103px;
                  height: 223px;
                "
              />
            </div>
            <div style="display: flex; margin-top: 35px; margin-left: 220px">
              <div style="float: left">
                <img src="../../assets/images/zicc/board/u40235.svg" />
              </div>
              <div
                style="
                  color: rgb(104, 179, 245);
                  width: 400px;
                  font-size: 20px;
                  text-align: center;
                "
              >
                各生产线月产量趋势
              </div>
              <div style="float: left">
                <img src="../../assets/images/zicc/board/u40234.svg" />
              </div>
            </div>
            <div style="clear: both"></div>
            <div style="margin-top: 65px; display: flex; position: absolute">
              <LineProductYieldTopChart></LineProductYieldTopChart>
            </div>
          </div>
          <div style="top: 0px; width: 0px; display: flex">
            <div
              style="
                position: absolute;
                width: 1103px;
                height: 253px;
                left: 0px;
                top: 268px;
                display: flex;
                font-family: 'PingFang SC ', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 20px;
              "
            >
              <img
                src="../../assets/images/zicc/board/u40247.png"
                style="
                  position: absolute;
                  left: 0px;
                  top: 0px;
                  width: 1103px;
                  height: 223px;
                "
              />
            </div>
            <div style="display: flex; margin-top: 218px; margin-left: 220px">
              <div style="float: left">
                <img src="../../assets/images/zicc/board/u40235.svg" />
              </div>
              <div
                style="
                  color: rgb(104, 179, 245);
                  width: 400px;
                  font-size: 20px;
                  text-align: center;
                "
              >
                产品分类月产量趋势
              </div>
              <div style="float: left">
                <img src="../../assets/images/zicc/board/u40234.svg" />
              </div>
            </div>
            <div style="clear: both"></div>
            <div style="margin-top: 255px; display: flex; position: absolute">
              <LineProductTypeChart></LineProductTypeChart>
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div style="display: flex; margin-top: 40px; margin-left: 22px">
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40235.svg" />
            </div>
            <div
              style="
                color: rgb(104, 179, 245);
                width: 500px;
                font-size: 20px;
                text-align: center;
              "
            >
              产品合格占比分布图
            </div>
            <div style="float: left">
              <img src="../../assets/images/zicc/board/u40234.svg" />
            </div>
            <div style="clear: both"></div>
          </div>
          <div
            style="
              border: 1.5px solid rgb(78, 77, 146);
              margin-top: 30px;
              width: 96%;
              border-color: rgb(115, 115, 197);
              margin-left: 22px;
            "
          >
            <PieMatterboardChart></PieMatterboardChart>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import PieProductionDistributionChart from "@/views/dashboard/PieProductionDistributionChart";
import BarProductionboardChart from "@/views/dashboard/BarProductionboardChart";
import LineProductTypeChart from "@/views/dashboard/LineProductTypeChart";
import LineProductYieldTopChart from "@/views/dashboard/LineProductYieldTopChart";
import PieMatterboardChart from "@/views/dashboard/PieMatterboardChart";
import FullSrcreenButton from "@/views/components/FullSrcreenButton";

import {
  getMaterialsSum,
  getTaskQuantitySum,
} from "@/api/fabricate/workpeport";
import { getReportTaskSum } from "@/api/fabricate/workorder";

export default {
  name: "production",
  components: {
    PieProductionDistributionChart,
    BarProductionboardChart,
    LineProductYieldTopChart,
    LineProductTypeChart,
    FullSrcreenButton,
    PieMatterboardChart,
  },
  data() {
    return {
      date: new Date(),
      ProductionMap: "ProductionMap",
      toProduceSum: 0, //待生产 状态为5 待报工
      inProductionSum: 0, //生产中 状态为6 执行中
      overProductionSum: 0, //已完工 状态为8
      taskType: 4,
      reportTaskVo: {}, //订单、工单、计划数量
      materialsVo: {}, //产品产量
    };
  },
  created() {
    this.taskQuantitySum();
    this.reportTaskSum();
    this.materialsSum();
  },

  methods: {
    materialsSum() {
      getMaterialsSum(this.taskType).then((response) => {
        console.log("materialsVo...", response);
        this.materialsVo = response.data;
      });
    },

    taskQuantitySum() {
      getTaskQuantitySum(this.taskType).then((response) => {
        this.taskQuantityList = response.data;
        this.taskQuantityList.forEach((element) => {
          if (element.status == 5) {
            this.toProduceSum = element.taskQuantity;
          }
          if (element.status == 6) {
            this.inProductionSum = element.taskQuantity;
          }
          if (element.status == 8) {
            this.overProductionSum = element.completionQuantity;
          }
        });
      });
    },
    reportTaskSum() {
      getReportTaskSum().then((response) => {
        console.log("reportTaskVo...", response);
        this.reportTaskVo = response.data;
      });
    },
  },
  mounted: function () {
    let _this = this;
    this.timer = setInterval(function () {
      _this.date = new Date();
    }, 1000);
  },
  computed: {
    formattedDate: function () {
      let date = this.date;
      let year = date.getFullYear();
      let month = (date.getMonth() + 1).toString().padStart(2, "0");
      let day = date.getDate().toString().padStart(2, "0");
      let hours = date.getHours().toString().padStart(2, "0");
      let minutes = date.getMinutes().toString().padStart(2, "0");
      let seconds = date.getSeconds().toString().padStart(2, "0");
      return (
        year +
        "-" +
        month +
        "-" +
        day +
        (parseInt(hours) > 12 ? "下午" : "上午") +
        parseInt(hours) +
        ":" +
        minutes +
        ":" +
        seconds
      );
    },
  },
};
</script>

<style lang="scss">
.index-container-back {
  background-image: url("../../assets/images/zicc/board/bg.svg");
  height: 100%;
  width: 100%;
  overflow-x: auto;
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-origin: border-box;
  position: relative;
  z-index: 1004;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 5px;
  color: rgb(77, 230, 31);
}
</style>
