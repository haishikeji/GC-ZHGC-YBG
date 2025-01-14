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
import { getInventoryTopTen } from "@/api/storage/inventory";

require("echarts/theme/macarons"); // echarts theme

export default {
  name: "enterChart",
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
  created() {},
  data() {
    return {
      chart: null,
      options: {},
      quantityList: [],
      nameList: [],
      loading: false,
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
          trigger: "axis",
          axisPointer: {
            type: "cross",
            show: true,
          },
        },
        legend: { data: ["库存数量"] },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
          },
        },
        calculable: true,
        grid: {
          left: "4%",
          right: "8%",
          bottom: "4%",
          top: "5%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          splitLine: {
            show: true,
            lineStyle: {
              type: "dashed",
            },
          },
          axisLine: {
            lineStyle: {
              color: "rgb(115, 122, 128)",
            },
          },
          axisLabel: {
            fontSize: 12, // 设置字体大小为14px
          },
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          show: true,
          type: "category",
          axisLine: {
            lineStyle: {
              color: "rgb(115, 122, 128)",
            },
          },
          axisLabel: {
            fontSize: 12, // 设置字体大小为14px
            formatter: function (value) {
              // 设置最大字数为5，并在超过后面加上省略号
              if (value.length > 8) {
                return value.substring(0, 8) + "...";
              } else {
                return value;
              }
            },
          },
          data: this.nameList,
        },

        series: [
          {
            type: "bar",
            barWidth: "60%",
            itemStyle: {
              color: "rgb(115, 122, 128)",
            },
            tooltip: {
              valueFormatter: (value) => value + " ml",
            },
            data: this.quantityList,
          },
        ],
      };
    },
    initCharts() {
      this.chart = echarts.init(this.$el, "myChart");
      this.chart.setOption(this.options);
    },
    getList() {
      getInventoryTopTen().then((response) => {
        response.data.forEach((i) => {
          this.nameList.push(i.materialName);
          this.quantityList.push(i.quantity);
        });

        this.initOptions();
        this.initCharts();
      });
    },
  },
};
</script>

<style></style>
