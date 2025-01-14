<template>
  <div>
    <!-- 展示并选择用户列表 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1000px"
      append-to-body
      if="open"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="24">
          <!--用户数据-->
          <el-col :span="24" :xs="24">
            <el-form
              :model="queryParams"
              ref="queryForm"
              size="small"
              :inline="true"
              v-show="showSearch"
              label-width="68px"
            >
              <img
                src="../../../assets/images/sousuo.png"
                style="margin-bottom: -13px; width: 37px"
              />
              <el-input
                v-model="queryParams.nickName"
                placeholder="名称"
                clearable
                @keyup.enter.native="handleQuery"
                style="width: 200px"
                class="sasda"
              >
              </el-input>

              <el-form-item label="" prop="userCode">
                <img
                  src="../../../assets/images/sousuo.png"
                  style="margin-bottom: -13px; width: 37px"
                />
                <el-input
                  v-model="queryParams.userCode"
                  placeholder="工号"
                  clearable
                  @keyup.enter.native="handleQuery"
                  style="width: 200px"
                  class="sasda"
                >
                </el-input>
              </el-form-item>

              <el-form-item label="" prop="deptId">
                <el-select
                  v-model="queryParams.deptId"
                  placeholder="部门"
                  clearable
                  style="width: 200px; margin-top: 10px"
                  class="sel_input"
                  @keyup.enter.native="handleQuery"
                >
                  <el-option
                    v-for="(val, key) in deptList"
                    :key="key"
                    :label="val.deptName"
                    :value="val.deptId"
                  />
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                  style="margin-top: 10px"
                  >搜索</el-button
                >
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                  style="margin-top: 10px"
                  >重置</el-button
                >
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-table
              v-loading="loading"
              :data="userList"
              @row-dblclick="clickRow"
              :row-key="getRowKeys"
              :reserve-selection="true"
              @selection-change="onSelect"
              ref="filterTable"
              border
              @select-all="onSelectAll"
              height="500"
            >
              <el-table-column
                type="selection"
                width="55"
                align="center"
                fixed="left"
                :reserve-selection="true"
              />
              <el-table-column
                label="用户名称"
                align="center"
                prop="nickName"
                width="180"
                :show-overflow-tooltip="true"
              >
              </el-table-column>
              <el-table-column
                label="员工编号"
                align="center"
                prop="userCode"
                width="180"
                show-overflow-tooltip
              />
              <el-table-column
                label="部门"
                align="center"
                prop="deptName"
                width="200"
                :show-overflow-tooltip="true"
              >
                <template scope="scope">
                  <span v-if="scope.row.deptId != null">
                    {{ scope.row.deptName }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                label="派发数量"
                prop="pfCount"
                show-overflow-tooltip
              >
                <template scope="scope">
                  <el-input
                    class="col_span"
                    v-model="scope.row.pfCount"
                    @blur="setPfCount(scope.row)"
                    maxlength="17"
                    oninput="value=value.toString()
																					.replace(/[^\d]/g,'')// 只能是整数																				
																					"
                  ></el-input>
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
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUserVo } from "@/api/system/user";
import { listDept } from "@/api/system/dept";

export default {
  name: "ChooseTaskUserComponent",
  props: {
    value: {
      type: String,
      default: "",
    },
    text: {
      type: String,
      default: "",
    },
    sourceurl: {
      type: Number,
      default: 0,
    },
    size: {
      type: String,
      default: "",
    },
    class_input: {
      type: String,
      default: "col_span",
    },
  },
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
      //判断来源
      source: 0,
      // 总条数
      total: 0,
      //仓储-用户表格数据
      userList: [],
      deptList: [],
      filterTable: [],
      // 弹出层标题
      title: "用户选择",
      // 是否显示弹出层
      open: false,
      //input
      row: {},
      radio: 0,
      isPage: false,
      workOrderId: [],
      distributedCount: 0,
      //多选
      multipleSelection: [],
      selectData: [],
      textValue: "",
      // 查询参数
      queryParams: {
        nickName: undefined,
        userCode: undefined,
        deptId: undefined,
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.selectDept();
  },
  methods: {
    /** 查询基础数据-包装列表 */
    getList() {
      this.loading = true;
      listUserVo(this.queryParams).then((response) => {
        this.userList = response.rows;
        this.initPfCount();
        this.total = response.total;
        this.loading = false;
        this.open = true;
      });
    },

    //初始化派发数量
    initPfCount() {
      //选中的行默认选中
      this.selectData.forEach((val) => {
        this.userList.forEach((row, index) => {
          if (row.userId == val.userId) {
            this.userList[index].pfCount = val.pfCount;
          }
        });
      });
    },

    showDialog(row) {
      // console.log("工单派发...待派发数量....",row.distributedCount);
      this.filterTable = [];
      this.selectData = [];
      this.multipleSelection = [];
      this.reset();
      this.workOrderId = row.id;
      this.distributedCount = row.distributedCount;
      if (this.$refs.filterTable) {
        this.$refs.filterTable.clearSelection();
      }
      this.getList();
    },

    Reassign() {
      if (this.sourceurl != 0) {
        this.textValue = this.text;
        this.radio = this.text.replace(/[0-9]/g, "");
      }
    },

    // 表单重置
    reset() {
      this.queryParams = {
        nickName: undefined,
        userCode: undefined,
        deptId: undefined,
        pageNum: 1,
        pageSize: 10,
      };
      this.resetForm("queryForm");
    },

    //获取每行唯一标识
    getRowKeys(row) {
      return row.userId;
    },

    // 重置数据
    reset_data() {
      this.textValue = "";
      this.radio = 0;
    },
    //赋值选中table数据
    assign_check(val) {
      this.textValue = val;
      this.radio = val;
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    selectDept() {
      listDept("").then((response) => {
        this.deptList = response.data;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.multipleSelection = [];
      this.selectData = [];
    },

    // 双击选中某一行
    clickRow(row, column, event) {
      let refsElTable = this.$refs["filterTable"];
      refsElTable.toggleRowSelection(row);
    },

    //input 失去焦点事件
    setPfCount(row) {
      // console.log("pfcount....", row.pfCount);
      this.selectData.forEach((item, index) => {
        if (item.userId == row.userId) {
          this.selectData[index].pfCount = row.pfCount;
        }
      });
      // console.log(" 失去焦点事件...", this.selectData);
    },

    // 多选框选中数据
    onSelect(selection, row) {
      this.selectData = selection;

      const userId = row.userId;
      // console.log("nickName...", row.nickName);
      // //是否已选
      const index = selection.findIndex((i) => i.userId === userId);
      // console.log("index...", index);

      // 如果已经包含该项 说明本次是取消勾选
      if (index > -1) {
        // console.log("push....");
        this.selectData.push(row);
      } else {
        // 否则，将已勾选的push进去
        // console.log("splice....");
        this.selectData.splice(index, 1);
      }

      // console.log("selection...", selection);
      // console.log("当前选中数据...selectData.", this.selectData);
    },

    /**
     * selection.length >0
     * 表示全选操作
     * 把存在于selection且不存在于this.selection的元素
     * 添加到this.selection中
     *
     * */
    onSelectAll(selection) {
      //  console.log("all....selection...",selection.length)
      //  console.log("all.....this.selectData...",this.selectData.length)

      if (selection.length > 0) {
        selection.forEach((item) => {
          var index = this.selectData.findIndex((data) => {
            return data.userId == item.userId;
          });

          //不存在添加
          if (index < 0) {
            this.selectData.push(item);
          }
        });
      } else {
        /**
         * this.selection.length < selection.length
         * 表示全选操作
         * 那么就要把存在于this.selection中
         * 且不存在于selection中的元素从this.selection中删除
         */
        this.userList.forEach((item) => {
          var index = this.selectData.findIndex((data) => {
            return data.userId == item.userId;
          });

          //存在，移除
          if (index != -1) {
            this.selectData.splice(index, 1);
          }
        });
      }

      // console.log("all...selection...",selection)
      // console.log("all...当前选中数据...selectData.",this.selectData);
    },

    /**
     * 确定按钮
     */
    submitForm() {
      if (this.selectData.length < 1) {
        this.$modal.msgError("请选择派发人！");
        return;
      } else {
        let isOk = true;
        var pfAllCout = 0;
        try {
          console.log("this.selectData......", this.selectData);
          this.selectData.forEach((element) => {
            console.log("element....", element);
            if (element.pfCount < 1) {
              this.$modal.msgError("派发数量不能小于1！");
              isOk = false;
              throw Error();
            } else if (element.pfCount > this.distributedCount) {
              this.$modal.msgError(
                "派发数量：{" +
                  element.pfCount +
                  "}不能大于待派发数量:{" +
                  this.distributedCount +
                  "}！"
              );
              isOk = false;
              throw Error();
            }
            pfAllCout = parseInt(pfAllCout) + parseInt(element.pfCount);
          });
        } catch (error) {
          console.log(error);
        }

        // console.log("pfAllCount...."+pfAllCout+"....distributedCount:",this.distributedCount);

        if (isOk) {
          if (pfAllCout > this.distributedCount) {
            this.$modal.msgError(
              "派发总数量：{" +
                pfAllCout +
                "}不能 <大于> 待派发总数量:{" +
                this.distributedCount +
                "}！"
            );
            isOk = false;
            return;
          } else if (pfAllCout < this.distributedCount) {
            this.$modal.msgError(
              "派发总数量：{" +
                pfAllCout +
                "}不能<小于>待派发总数量:{" +
                this.distributedCount +
                "}！"
            );
            isOk = false;
            return;
          }
          if (isOk) {
            this.$emit("chooseSelectRow", this.selectData, this.workOrderId);
            this.open = false;
          }
        }
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.reset();
      this.handleQuery();
    },
  },
};
</script>

<style lang="scss" scoped>
.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}

.col_span_max {
  width: 300px;
}

.col_span {
  width: 200px;
}

.col_span_min {
  width: 100px;
  margin-right: 20px;
}

.sel_input {
  width: 120px;
  margin-right: 20px;
}
</style>
