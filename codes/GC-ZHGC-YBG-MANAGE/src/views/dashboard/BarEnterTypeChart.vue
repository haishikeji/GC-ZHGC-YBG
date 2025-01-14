<template>
  <div
    id="myChart"
    :class="className"
    :style="{ height: height, width: width }"
    v-loading="loading"
  />
</template>

<script>
import echarts from "echarts";
import {
  getEnterTypeProportion,
  getStorageTypeProportion,
} from "@/api/storage/enter";
require("echarts/theme/macarons"); // echarts theme

export default {
  name: "stockChart",
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "268px",
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
      loading: false,
      dataList: [],
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
    initOptions() {
      this.options = {
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
          textStyle: {
            color: "rgb(115, 122, 128)",
          },
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

            data: this.dataList,
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
    getList() {
      getStorageTypeProportion().then((response) => {
        this.dataList = response.data;
        this.initOptions();
        this.initCharts();
      });
    },
  },
};
</script>

<style></style>
