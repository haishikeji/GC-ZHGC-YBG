<template>
  <el-select
    :clearable="clearable"
    :disabled="disabled"
    :multiple="multiple"
    :multiple-limit="multipleLimit"
    :placeholder="placeholder"
    :filter-method="onFilterMethod"
    :value="value"
    filterable
    :collapse-tags="collapseTags"
    no-data-text="无数据"
    @change="changeOption"
    @visible-change="changeVisible"
  >
    <slot :options="options" />
    <el-pagination
      :current-page.sync="page.pageNum"
      :page-size="pageSize"
      :total="page.totalCount"
      layout="prev, pager, next"
      @current-change="changePageNum"
    />
  </el-select>
</template>

<script>
export default {
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: [String, Array, Number],
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    valueKey: {
      type: String,
      required: true,
    },
    pageSize: {
      type: Number,
      default: 5,
    },
    placeholder: {
      type: String,
      default: "请选择",
    },
    remoteMethod: {
      type: Function,
      required: true,
    },
    queryKey: {
      type: String,
      default: "str",
    },
    extendParams: {
      type: Object,
      default: () => {},
    },
    multiple: {
      type: Boolean,
      default: false,
    },
    multipleLimit: {
      type: Number,
    },
    clearable: {
      type: Boolean,
      default: true,
    },
    collapseTags: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      page: {
        totalCount: 0,
        pageNum: 1,
        pageSize: this.pageSize,
      },
      options: [],
      query: "",
    };
  },
  methods: {
    getOptions(query, searchFirstPage) {
      if (searchFirstPage) {
        this.page.pageNum = 1;
      }
      const params = {
        pageNum: this.page.pageNum,
        pageSize: this.page.pageSize,
        ...this.extendParams,
      };
      if (query) {
        this.query = query;
        params[this.queryKey] = this.query;
      }
      Promise.resolve(this.remoteMethod(params))
        .then((res) => {
          // console.log("RemoteSelect。。。。。", res);
          const data = res || {};
          this.options = data.rows || [];
          this.page.totalCount = data.total || 0;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    changeVisible(val) {
      if (val) {
        this.getOptions();
      } else {
        this.query = "";
        this.page.pageNum = 1;
      }
      this.$emit("visibleChange", val);
    },
    changeOption(val) {
      if (this.multiple) {
        if (this.value.length < val.length) {
          // 添加
          const difference = this.value
            .concat(val)
            .filter((e) => val.includes(e) && !this.value.includes(e));
          const selectArr =
            this.options.filter((option) =>
              difference.includes(option[this.valueKey])
            ) || [];
          this.$emit("addTag", selectArr);
        } else {
          // 删除
          const difference = this.value
            .concat(val)
            .filter((e) => this.value.includes(e) && !val.includes(e));
          this.$emit("removeTag", difference);
        }
        this.$emit("change", val);
      } else {
        const select =
          this.options.find((option) => option[this.valueKey] === val) || {};
        this.$emit("change", val, select);
      }
    },
    changePageNum(val) {
      this.page.pageNum = val;
      this.getOptions(this.query);
    },
    onFilterMethod(val) {
      // console.log(val);
      this.getOptions(val, true);
    },
  },
};
</script>

<style lang="scss">
.el-popper .el-select-dropdown__item {
  height: auto !important;
  .a-option__title {
    span:first-child {
      width: 90px;
    }
    span:nth-child(2) {
      padding-left: 25px;
    }
  }
  .a-option__detail {
    list-style: none;
    line-height: 24px;
    padding-left: 10px;
    font-style: italic;
    color: rgb(168, 165, 165);
  }
}
</style>
