<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      class="sasda"
    >
      <el-col>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane
            label="全部工单"
            name="first"
            v-if="this.myStatus == 'true'"
          ></el-tab-pane>
          <el-tab-pane
            label="待派发"
            name="second"
            v-if="this.myStatus == 'true'"
          ></el-tab-pane>
          <el-tab-pane label="待报工" name="third"></el-tab-pane>
          <el-tab-pane label="执行中" name="fourth"></el-tab-pane>
          <el-tab-pane label="已完成" name="five"></el-tab-pane>
          <el-tab-pane label="我的工单" name="six"></el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col>
        <el-form-item label="" prop="name">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-input
            v-model="queryParams.number"
            placeholder="工单号"
            clearable
            class="input_first"
            @keyup.enter.native="handleQuery"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="" prop="productPlanNumber">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-input
            v-model="queryParams.productPlanNumber"
            placeholder="生产计划号"
            clearable
            class="input_first"
            @keyup.enter.native="handleQuery"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="" prop="productOrderNumber">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-input
            v-model="queryParams.productOrderNumber"
            placeholder="生产订单号"
            clearable
            class="input_first"
            @keyup.enter.native="handleQuery"
          >
          </el-input>
        </el-form-item>
      </el-col>

      <el-form-item>
        <el-button
          type="success"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查找</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          >重置</el-button
        >
        <el-button
          type="success"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          disabled
          v-hasPermi="['fabricate:workorder:add']"
          v-if="
            this.myStatus == 'false' ||
            this.activeName == 'six' ||
            this.activeName == 'fourth' ||
            this.activeName == 'five' ||
            this.activeName == 'third'
          "
          >新增</el-button
        >
        <el-button
          type="success"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['fabricate:workorder:add']"
          v-else-if="this.myStatus == 'true'"
          >新增</el-button
        >
        <!-- <el-button
          type="primary"
          size="mini"
          @click="handleDistribution"
          v-hasPermi="['fabricate:workorder:add']"
          v-if="
            this.activeName == 'six' ||
            this.activeName == 'fourth' ||
            this.activeName == 'five' ||
            this.activeName == 'third'
          "
          disabled
          >工单派发</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleDistribution"
          v-hasPermi="['fabricate:workorder:add']"
          v-else-if="this.myStatus == 'true'"
          >工单派发</el-button
        > -->
        <el-button
          type="primary"
          size="mini"
          @click="handleWithdraw"
          v-hasPermi="['fabricate:workorder:add']"
          v-if="
            this.activeName == 'six' ||
            this.activeName == 'fourth' ||
            this.activeName == 'five' ||
            this.activeName == 'third'
          "
          disabled
          >工单撤回</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleWithdraw"
          v-hasPermi="['fabricate:workorder:edit']"
          v-else-if="this.myStatus == 'true'"
          >工单撤回</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleWorkReport"
          v-hasPermi="['fabricate:workorder:edit']"
          v-if="this.activeName == 'six'"
          >报工</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleWorkReport"
          disabled
          v-hasPermi="['fabricate:workorder:edit']"
          v-else
          >报工</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleReporting"
          v-hasPermi="['fabricate:workorder:edit']"
          v-if="this.activeName == 'six'"
          >完工</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="handleReporting"
          disabled
          v-hasPermi="['fabricate:workorder:edit']"
          v-else
          >完工</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading"
      :data="workorderList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        fixed="left"
      />
      <el-table-column
        label="工单号"
        align="center"
        prop="number"
        fixed="left"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <router-link
            :to="{
              path: '/production/workorderdetails',
              query: { id: scope.row.id, myStatus: myStatus },
            }"
          >
            <span style="color: rgb(7, 171, 247)">{{ scope.row.number }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column
        label="生产计划号"
        align="center"
        prop="productPlanNumber"
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        label="生产订单号"
        align="center"
        prop="productOrderNumber"
        width="140"
        show-overflow-tooltip
      />
      <el-table-column
        label="工单状态"
        align="center"
        prop="status"
        width="90"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_fabricate_work_order_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="工单完成进度"
        align="center"
        prop="schedule"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
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
        label="工单执行人"
        align="center"
        prop="executorName"
        show-overflow-tooltip
        width="100"
      />
      <el-table-column
        label="任务数量"
        align="center"
        prop="taskQuantity"
        width="80"
        show-overflow-tooltip
      />
      <el-table-column
        label="待派发数量"
        align="center"
        prop="distributedCount"
        width="100"
        show-overflow-tooltip
      />
      <el-table-column
        label="完工数量"
        align="center"
        prop="completionQuantity"
        width="80"
        show-overflow-tooltip
      />
      <el-table-column
        label="工序编号"
        align="center"
        prop="processNumber"
        width="80"
        show-overflow-tooltip
      />
      <el-table-column
        label="工艺路线名称"
        align="center"
        prop="routingName"
        show-overflow-tooltip
        width="100"
      />
      <!-- <el-table-column label="工序名称" align="center" prop="processName"  width="100" show-overflow-tooltip />
      <el-table-column label="工序类型" align="center" prop="processType" width="120" show-overflow-tooltip >
        <template slot-scope="scope">
            <dict-tag :options="dict.type.zcii_bas_gx_type" :value="scope.row.processType"/>
          </template>
      </el-table-column>      
      <el-table-column label="所属工艺路线类型" align="center" prop="routingType" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
            <dict-tag :options="dict.type.zcii_bas_routing_type" :value="scope.row.routingType"/>
          </template>
      </el-table-column>-->
      <el-table-column
        label="工艺路线步骤"
        align="center"
        prop="routingStep"
        width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <div v-if="scope.row.routingStep != undefined">
            第 {{ scope.row.routingStep }} 步
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="目标物料编号"
        align="center"
        prop="matterNumber"
        width="120"
        show-overflow-tooltip
      />
      <!--<el-table-column label="目标物料名称" align="center" prop="matterName" width="110" show-overflow-tooltip/>
       <el-table-column label="计划开始时间" align="center" prop="startTime" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划结束时间" align="center" prop="endTime" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->

      <el-table-column
        label="合格数量"
        align="center"
        prop="qualifiedQuantity"
        show-overflow-tooltip
      />
      <el-table-column
        label="不合格数量"
        align="center"
        prop="unqualifiedQuantity"
        show-overflow-tooltip
        width="100"
      />
      <!-- <el-table-column label="开始时间" align="center" prop="workStartTime" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="workEndTime" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生产单元" align="center" prop="productUnitName" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.factory != null">{{scope.row.factory}}</span>
          <span v-if="scope.row.workshop != null">>>{{scope.row.workshop}}</span>
          <span v-if="scope.row.productionLine != null">>>{{scope.row.productionLine}}</span>
          <span v-if="scope.row.team != null">>>{{scope.row.team}}</span>
        </template>
      </el-table-column>  
      <el-table-column label="责任人" align="center" prop="responsiblePersonName" width="120" show-overflow-tooltip/>-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        v-if="this.activeName == 'six'"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 5"
            size="mini"
            type="text"
            @click="handleWorkReport(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >报工</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleWorkReport(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >报工</el-button
          >
          <el-button
            v-if="scope.row.status == 6"
            size="mini"
            type="text"
            @click="handleReporting(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >完工确认</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleReporting(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >完工确认</el-button
          >
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        v-else
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 4"
            size="mini"
            type="text"
            @click="handleDistribution(scope.row)"
            v-hasPermi="['fabricate:workorder:add']"
            >工单派发</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleDistribution(scope.row)"
            v-hasPermi="['fabricate:workorder:add']"
            >工单派发</el-button
          >
          <el-button
            v-if="scope.row.status == 5"
            size="mini"
            type="text"
            @click="handleWithdraw(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >工单撤回</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleWithdraw(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >工单撤回</el-button
          >
          <!-- <el-button
            v-if="scope.row.status == 4"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >修改</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['fabricate:workorder:edit']"
            >修改</el-button
          >
          <el-button
            v-if="scope.row.status == 4"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['fabricate:workorder:remove']"
            >删除</el-button
          >
          <el-button
            v-else
            disabled
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['fabricate:workorder:remove']"
            >删除</el-button 
          >-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getPageData"
    />

    <ChooseTaskUserComponent
      ref="chooseTaskUserRef"
      @chooseSelectRow="chooseRow_User"
    ></ChooseTaskUserComponent>
  </div>
</template>

<script>
import {
  listWorkorder,
  delWorkorder,
  editDistribution,
  editWithdraw,
  editWorkReport,
  listWorkOrderVoByUser,
} from "@/api/fabricate/workorder";
import ChooseTaskUserComponent from "@/views/components/fabricate/ChooseTaskUserComponent.vue";
export default {
  name: "Workorder",
  dicts: [
    "zcii_bas_gx_type",
    "zcii_bas_routing_type",
    "zcii_fabricate_work_order_status",
  ],
  components: {
    ChooseTaskUserComponent,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      myStatus: null,
      // 非多个禁用
      multiple: true,
      multipleSelection: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 生产制造-工单管理表格数据
      workorderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      activeName: null,
      //进度条颜色
      color: "#5cb87a",
      //进度条底色
      backcolor: "#80f0a5",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: null,
        status: null,
        myStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    if (this.$route.query.myStatus != undefined) {
      this.myStatus = this.$route.query.myStatus;
      if (this.myStatus == "false") {
        this.activeName = "six";
        this.queryParams.status = null;
        this.getListByUser();
      } else {
        this.activeName = "first";
        this.getList();
      }
    }
  },
  methods: {
    /** 查询生产制造-工单管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.myStatus = this.myStatus;
      listWorkorder(this.queryParams).then((response) => {
        this.workorderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    getListByUser() {
      this.loading = true;
      listWorkOrderVoByUser(this.queryParams).then((response) => {
        this.workorderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    getPageData() {
      //console.log("this.queryParams....",this.queryParams);

      if (
        this.activeName == "six" ||
        this.activeName == "third" ||
        this.activeName == "fourth"
      ) {
        this.getListByUser();
      } else {
        this.getList();
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        number: null,
        status: null,
      };
      this.resetForm("queryForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (
        this.activeName == "six" ||
        this.activeName == "third" ||
        this.activeName == "fourth"
      ) {
        this.getListByUser();
      } else {
        this.getList();
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.reset();
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      if (
        this.activeName == "six" ||
        this.activeName == "third" ||
        this.activeName == "fourth"
      ) {
        this.ids = selection.map((item) => item.reportId);
      } else {
        this.ids = selection.map((item) => item.id);
      }

      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      this.multipleSelection = selection;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.$tab.openPage("新增工单", "/production/newWorkOrder");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      this.$tab.openPage("修改工单", "/production/newWorkOrder", { id: id });
    },

    /** 完工上报按钮操作 */
    handleReporting(row) {
      this.reset();
      const id = row.reportId || this.ids;
      if (row.reportId) {
        this.$tab.openPage("完工上报", "/production/singlereportingforwork", {
          id: id,
          myStatus: this.myStatus,
        });
      } else {
        if (this.ids.length < 1) {
          this.$modal.msgError("请选择工单！");
        } else {
          const singles = this.multipleSelection.filter(
            (item) => item.status == 6
          );
          if (singles == undefined || singles.length < 1) {
            this.$modal.msgError("请选择未完工的工单！");
          } else {
            const ids = singles.map((item) => item.reportId);
            this.$tab.openPage("完工上报", "/production/batchreportingwork", {
              id: ids,
              myStatus: this.myStatus,
            });
          }
        }
      }
    },

    /**
     * 报工
     */
    handleWorkReport(row) {
      const id = row.reportId || this.ids;
      // console.log("报工。。。",id);
      console.log("报工。。。", id);
      console.log("报工。row。。", row.reportId);
      if (row.reportId) {
        editWorkReport(id).then((response) => {
          if (response.code == 200) {
            this.$modal.msgSuccess("报工成功");
            this.activeName = "six";
            this.getListByUser();
          }
        });
      } else {
        if (this.ids.length < 1) {
          this.$modal.msgError("请选择工单！");
        } else {
          editWorkReport(id).then((response) => {
            if (response.code == 200) {
              this.$modal.msgSuccess("报工成功");
              this.activeName = "six";
              this.getListByUser();
            }
          });
        }
      }
    },

    /**
     * 工单派发
     */
    handleDistribution(row) {
      if (row.id) {
        this.$refs["chooseTaskUserRef"].showDialog(row);
      } else {
        if (this.ids.length < 1) {
          this.$modal.msgError("请选择工单！");
        } else if (this.ids.length > 1) {
          this.$modal.msgError("请勿一次选择多个工单！");
        } else {
          this.$refs["chooseTaskUserRef"].showDialog(this.multipleSelection[0]);
        }
      }
    },

    /**
     * 选择派工人，确定
     */
    chooseRow_User(rows, id) {
      if (id == undefined || id < 1) {
        // console.log("派工...工单id...",id);
        this.$modal.msgError("工单Id不能为空");
      } else {
        // console.log("派工，确定...",rows,id);
        let distributionVo = {
          workOrderId: id,
          userVos: rows,
        };
        editDistribution(distributionVo).then((response) => {
          this.$modal.msgSuccess("派发成功");
          this.open = false;
          this.getList();
        });
      }
    },

    /**
     * 工单撤回
     */
    handleWithdraw(row) {
      const id = row.id || this.ids;

      if (row.id) {
        editWithdraw(id).then((response) => {
          if (response.code == 200) {
            this.$modal.msgSuccess("撤回成功");
            this.open = false;
            this.getList();
          }
        });
      } else {
        if (this.ids.length < 1) {
          this.$modal.msgSuccess("请选择工单！");
        } else {
          editWithdraw(id).then((response) => {
            if (response.code == 200) {
              this.$modal.msgSuccess("撤回成功");
              this.open = false;
              this.getList();
            }
          });
        }
      }
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除生产制造-工单管理编号为"' + ids + '"的数据项？')
        .then(function () {
          return delWorkorder(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "fabricate/workorder/export",
        {
          ...this.queryParams,
        },
        `workorder_${new Date().getTime()}.xlsx`
      );
    },

    /**
     * tab页签切换
     * @param {*} tab
     * @param {*} event
     */
    handleClick(tab, event) {
      // console.log("tab..."+tab+"...."+event)
      this.reset();
      switch (this.activeName) {
        case "first":
          this.queryParams.status = null;
          this.getList();
          break;
        case "second":
          this.queryParams.status = 4;
          this.getList();
          break;
        case "third":
          this.queryParams.status = 5;
          this.getList();
          break;
        case "fourth":
          this.queryParams.status = 6;
          this.getList();
          break;
        case "five":
          this.queryParams.status = 8;
          this.getList();
          break;
        case "six":
          this.queryParams.status = undefined;
          this.getListByUser();
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.col_span {
  width: 200px;
}
.col_span_min {
  width: 100px;
  margin-right: 20px;
}
.input_first {
  margin-left: 8px;
  width: 200px;
}

.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}
</style>
