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
        <!-- 互检-->
        <el-tabs
          v-if="this.detectionType == 0"
          v-model="activeName"
          @tab-click="handleClick"
        >
          <el-tab-pane label="全部完工" name="all"></el-tab-pane>
          <el-tab-pane label="待检测" name="to_be_check"></el-tab-pane>
          <el-tab-pane label="待我检测" name="me_to_check"></el-tab-pane>
        </el-tabs>

        <el-tabs
          v-if="this.detectionType == 1 || this.detectionType == 2"
          v-model="activeName"
          @tab-click="handleClick"
        >
          <el-tab-pane label="可检测" name="stay_check"></el-tab-pane>
          <el-tab-pane label="已检测" name="already_check"></el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col>
        <el-form-item label="" prop="detectionStatus">
          <img
            src="../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-select
            v-model="queryParams.detectionStatus"
            v-if="this.detectionType == 0"
            placeholder="互检状态"
            class="col_span"
          >
            <el-option
              v-for="dict in dict.type.zcii_qualitymanage_check_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>

          <el-select
            v-model="queryParams.inspectorStatus"
            v-if="this.detectionType == 1"
            placeholder="自检状态"
            class="col_span"
          >
            <el-option
              v-for="dict in dict.type.zcii_qualitymanage_check_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>

          <el-select
            v-model="queryParams.spotStatus"
            v-if="this.detectionType == 2"
            placeholder="抽检状态"
            class="col_span"
          >
            <el-option
              v-for="dict in dict.type.zcii_qualitymanage_check_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <img
          src="../../assets/images/sousuo.png"
          style="margin-bottom: -13px; width: 37px"
        />
        <el-input
          v-model="queryParams.completionNo"
          placeholder="完工确认号"
          clearable
          @keyup.enter.native="handleQuery"
          class="input_first"
        >
        </el-input>
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
          @click="batchCheck"
          v-hasPermi="['fabricate:work:query']"
          >检测
        </el-button>
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
        :selectable="selectable"
        align="center"
        fixed="left"
      />
      <el-table-column
        label="工单号"
        align="center"
        prop="workNumber"
        show-overflow-tooltip
        fixed="left"
      />
      <el-table-column
        label="完工确认号"
        align="center"
        prop="completionNo"
        show-overflow-tooltip
        fixed="left"
      />
      <el-table-column
        label="完工确认人"
        align="center"
        prop="completionBy"
        show-overflow-tooltip
        fixed="left"
      />
      <el-table-column
        label="完工确认时间"
        align="center"
        prop="completionTime"
        fixed="left"
        show-overflow-tooltip
      />
      <el-table-column
        label="完工数量"
        align="center"
        prop="completionQuantity"
        show-overflow-tooltip
        width="100"
      />
      <el-table-column
        label="抽检状态"
        v-if="this.detectionType == 2"
        align="center"
        prop="spotStatus"
        show-overflow-tooltip
        width="100"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_qualitymanage_check_status"
            :value="scope.row.spotStatus"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="互检状态"
        v-if="this.detectionType == 0"
        align="center"
        prop="detectionStatus"
        show-overflow-tooltip
        width="100"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_qualitymanage_check_status"
            :value="scope.row.detectionStatus"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="自检状态"
        v-if="this.detectionType == 1"
        align="center"
        prop="inspectorStatus"
        show-overflow-tooltip
        width="100"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_qualitymanage_check_status"
            :value="scope.row.inspectorStatus"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="检测员"
        v-if="this.detectionType == 0"
        align="center"
        prop="inspectorName"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="检测员"
        v-if="this.detectionType == 2"
        align="center"
        prop="spotInspectorName"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="抽检数量"
        align="center"
        prop="spotCheck"
        v-if="this.detectionType == 2"
        show-overflow-tooltip
        width="120"
      >
      </el-table-column>
      <!-- 抽检 -->
      <el-table-column
        label="合格数量"
        v-if="this.detectionType == 2"
        align="center"
        prop="spotCheckQualified"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="不合格数量"
        v-if="this.detectionType == 2"
        align="center"
        prop="spotCheckUnqualified"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="检测时间"
        v-if="this.detectionType == 2"
        align="center"
        prop="spotInspectorTime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{
            parseTime(scope.row.spotInspectorTime, "{y}-{m}-{d} {h}:{i}:{s}")
          }}
        </template>
      </el-table-column>
      <!-- 自检 -->
      <el-table-column
        label="合格数量"
        v-if="this.detectionType == 1"
        align="center"
        prop="qualifiedQuantity"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="不合格数量"
        v-if="this.detectionType == 1"
        align="center"
        prop="unqualifiedQuantity"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="检测时间"
        v-if="this.detectionType == 1"
        align="center"
        prop="inspectorTime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ parseTime(scope.row.inspectorTime, "{y}-{m}-{d} {h}:{i}:{s}") }}
        </template>
      </el-table-column>
      <!-- 互检检 -->
      <el-table-column
        label="合格数量"
        v-if="this.detectionType == 0"
        align="center"
        prop="mutualQualifiedQuantity"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="不合格数量"
        v-if="this.detectionType == 0"
        align="center"
        prop="mutualUnqualifiedQuantity"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        label="检测时间"
        v-if="this.detectionType == 0"
        align="center"
        prop="mutualInspectionTime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{
            parseTime(scope.row.mutualInspectionTime, "{y}-{m}-{d} {h}:{i}:{s}")
          }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        v-if="this.detectionType == 0"
        class-name="small-padding fixed-width"
        width="140"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            v-if="
              scope.row.detectionStatus == '0' ||
              scope.row.detectionStatus == null ||
              scope.row.detectionStatus == '2'
            "
            type="text"
            icon="el-icon-edit"
            @click="check(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >检测
          </el-button>
          <el-button
            size="mini"
            v-else
            type="text"
            icon="el-icon-edit"
            @click="viewCheck(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        v-if="this.detectionType == 1"
        class-name="small-padding fixed-width"
        width="180"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            v-if="scope.row.inspectorStatus == 0"
            type="text"
            icon="el-icon-edit"
            @click="check(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >检测
          </el-button>
          <el-button
            size="mini"
            v-else
            type="text"
            icon="el-icon-edit"
            @click="viewCheck(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        v-if="this.detectionType == 2"
        class-name="small-padding fixed-width"
        width="180"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            v-if="
              scope.row.spotStatus == '0' ||
              scope.row.spotStatus == null ||
              scope.row.spotStatus == '2'
            "
            type="text"
            icon="el-icon-edit"
            @click="check(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >检测
          </el-button>
          <el-button
            size="mini"
            v-else
            type="text"
            icon="el-icon-edit"
            @click="viewCheck(scope.row)"
            v-hasPermi="['fabricate:work:query']"
            >查看
          </el-button>
          <!--          <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="batchCheck(scope.row)"
                      v-hasPermi="['fabricate:workorder:remove']"
                    >审核通过
                    </el-button>-->
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
  </div>
</template>

<script>
import { selectWorkReportInfoList } from "@/api/fabricate/workpeport";

export default {
  name: "CrosscCheck",
  dicts: ["zcii_qualitymanage_check_status"],
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
      // 总条数
      total: 0,
      // 生产制造-工单管理表格数据
      workorderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      activeName: "all",
      userId: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        detectionStatus: null, //互检
        spotStatus: null, //抽检
        inspectorStatus: null, //自检
        inspector: null,
        params: [],
        qualifiedQuantity: null,
        spotCheckQualified: null,
        completionNo:null,
      },
      detectionType: null,
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.detectionType = this.$route.query.type;
    //获取当前登陆用户
    this.userId = this.$store.state.user.userId;
    this.getList();
  },
  methods: {
    /** 查询生产制造-工单管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.detectionType = this.detectionType;
      selectWorkReportInfoList(this.queryParams).then((response) => {
        this.workorderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        productPlanId: null,
        productOrderId: null,
        status: 0,
        schedule: null,
        executor: null,
        processName: null,
        processType: null,
        routingName: null,
        routingType: null,
        routingStep: null,
        processNumber: null,
        matterId: null,
        matterNumber: null,
        matterName: null,
        startTime: null,
        endTime: null,
        taskQuantity: null,
        completionQuantity: null,
        qualifiedQuantity: null,
        unqualifiedQuantity: null,
        workStartTime: null,
        workEndTime: null,
        productUnitId: null,
        responsiblePerson: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        tenantId: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.detectionStatus = null;
      this.queryParams.completionNo = null;
      this.queryParams.spotStatus= null, //抽检
      this.queryParams.inspectorStatus= null, //自检
      this.queryParams.inspector= null,
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    checkCount(row) {
      this.reset();
      const id = row.id || this.ids;
      let params = { id: id, type: this.detectionType };
      this.$tab.openPage(
        this.detectionType == 0 ? "互检明细" : "抽检明细",
        "/qualitymanage/detail",
        params
      );
    },
    check(row) {
      this.reset();
      const id = row.id || this.ids;
      let params = { id: id, type: this.detectionType };
      this.$tab.openPage("检查", "/qualitymanage/singleCheck", params);
    },
    viewCheck(row) {
      this.reset();
      const id = row.id || this.ids;
      let params = { id: id, type: this.detectionType, detect: false };
      this.$tab.openPage("检查", "/qualitymanage/singleCheck", params);
    },
    /** 批量检查按钮操作 */
    batchCheck() {
      if (this.ids.length < 1) {
        this.$modal.msgError("请勾选数据");
      } else {
        const ids = this.ids;
        let params = { ids: ids, type: this.detectionType };
        this.$tab.openPage("检查", "/qualitymanage/batchCheck", params);
      }
    },

    /**
     * tab页签切换
     * @param {*} tab
     * @param {*} event
     */
    handleClick(tab, event) {
      console.log(tab, event);

      switch (tab.name) {
        case "stay_check":
          switch (this.detectionType) {
            case "0":
              this.queryParams.detectionStatus = 0;
              break;
            case "1":
              this.queryParams.inspectorStatus = 0;
              break;
            case "2":
              this.queryParams.spotStatus = 0;
              break;
          }
          break;
        case "all":
          this.queryParams.inspector = null;
          this.queryParams.detectionStatus = null;
          break;
        case "to_be_check":
        case "0":
          this.queryParams.inspector = null;
          this.queryParams.detectionStatus = 0;
          break;
        case "me_to_check":
          switch (this.detectionType) {
            case "0":
              this.queryParams.inspector = this.userId;
              this.queryParams.detectionStatus = 0;
              break;
          }
          break;
        case "already_check":
          if (this.detectionType == "1") {
            this.queryParams.inspectorStatus = 1;
          } else if (this.detectionType == "2") {
            this.queryParams.spotStatus = 1;
          }
          break;
      }
      this.getList();
    },
    /**
     * 根据条件禁用勾选
     */
    selectable(row, rowIndex) {
      switch (this.detectionType) {
        case "0":
          if (row.detectionStatus == 1) {
            return false;
          } else {
            return true;
          }
        case "1":
          if (row.inspectorStatus == 1) {
            return false;
          } else {
            return true;
          }
        case "2":
          if (row.spotStatus == 1) {
            return false;
          } else {
            return true;
          }
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
