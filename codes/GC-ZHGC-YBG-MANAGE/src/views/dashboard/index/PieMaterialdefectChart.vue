<template>
  <div
    id="myChart"
    :class="className"
    :style="{ height: height, width: width }"
    v-loading="loading"
  />
</template>

<script>
import { getMterialQualifiedpercentageChartPie } from "@/api/fabricate/workpeport";
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme

export default {
  name: "productChart",
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "98%",
    },
    height: {
      type: String,
      default: "380px",
    },
    autoResize: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      chart: null,
      options: {},
      mapList: [],
      loading: true,
    };
  },
  mounted() {
    this.getList();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getList() {
      getMterialQualifiedpercentageChartPie().then((response) => {
        console.log("....productionUnitNamePie...", response.data);
        this.mapList = response.data;
        this.initOptions();
        this.initCharts();
      });
    },
    initOptions() {
      this.options = {
        title: {
          text: "产品合格占比分布图",
          // subtext: 'Fake Data',
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          left: "center",
          icon: "circle",
          itemGap: 30,
          right: "0",
          bottom: "0",
          top: "92%",
        },
        series: [
          {
            // name: 'Access From',
            type: "pie",
            radius: ["40%", "70%"],
            itemStyle: {
              borderColor: "#fff",
              borderWidth: 1,
            },
            data: this.mapList,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
    },
    initCharts() {
      this.chart = echarts.init(this.$el, "myChart");
      this.chart.setOption(this.options);
    },
  },
};
</script>

<style></style>
