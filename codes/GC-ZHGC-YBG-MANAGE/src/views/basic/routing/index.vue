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
        <el-form-item label="" prop="name">
          <img
            src="../../../assets/images/sousuo.png"
            style="margin-bottom: -13px; width: 37px"
          />
          <el-input
            v-model="queryParams.name"
            placeholder="路线名称/编码"
            clearable
            class="input_first"
            @keyup.enter.native="handleQuery"
          >
          </el-input>
        </el-form-item>
      </el-col>
      <el-col>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >查找</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['basic:routing:add']"
            >新增</el-button
          >
      
        </el-form-item>
      </el-col>
    </el-form>

    <el-table
      v-loading="loading"
      :data="routingList"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="工艺路线编码"
        align="center"
        prop="number"
        show-overflow-tooltip
      />
      <el-table-column
        label="工艺路线名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="工艺路线类型"
        align="center"
        prop="routingType"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_routing_type"
            :value="scope.row.routingType"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="适用对象" align="center" prop="applyMode" width="140" show-overflow-tooltip>
        <template slot-scope="scope">
          <dict-tag :options="dict.type.zcii_bas_apply_mode" :value="scope.row.applyMode"/>
        </template>
      </el-table-column> -->
      <el-table-column
        label="产品编号"
        align="center"
        prop="matterNumber"
        show-overflow-tooltip
      />
      <el-table-column
        label="产品名称"
        align="center"
        prop="matterName"
        show-overflow-tooltip
      />
      <el-table-column
        label="产品类型"
        align="center"
        prop="categoryName"
        width="160"
        show-overflow-tooltip
      >
        <!-- <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcii_bas_matter_type"
            :value="scope.row.categoryId"
          />
        </template> -->
      </el-table-column>
      <!-- <el-table-column label="物料类型" align="center" prop="supplyMode" width="140" show-overflow-tooltip>
        <template slot-scope="scope">
          <dict-tag :options="dict.type.zcii_bas_supply_mode" :value="scope.row.supplyMode"/>
        </template>
      </el-table-column> -->
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zicc_bas_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{
            parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
          }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建人" align="center" prop="createBy" width="140" show-overflow-tooltip/>      -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['basic:routing:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="copyUpdate(scope.row)"
            v-hasPermi="['basic:routing:edit']"
            >复制</el-button
          >
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['basic:routing:remove']"
            >删除</el-button
          > -->
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
     <!-- 复制工艺路线对话框 -->
     <el-dialog title="复制工艺路线" :visible.sync="copyOpen" width="500px" append-to-body>
      <el-form ref="routing" :model="routing"  label-width="120px">
      
        <el-form-item label="工艺路线编号" prop="number">
          <el-input v-model="routing.number"  disabled />
        </el-form-item>
        <el-form-item label="工艺路线名称" prop="name">
          <el-input v-model="routing.name" disabled />
        </el-form-item>
        <el-form-item label="物料信息" prop="matterNumber">
          <ChooseMaterialsBomComponent
                      @chooseRow="chooseRow_Material"
                      :text="routing.matterNumber"
                      :value="routing.matterId"
                      ref="ref_matter_bom"
                    ></ChooseMaterialsBomComponent>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="copySubmitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
  
</template>

<script>
import {
  listRouting,
  delRouting,
  addRouting,
  copyAddRouting,
  existRouting,
  updateRouting,
} from "@/api/basic/routing";
import ChooseMaterialsBomComponent from "@/views/components/basic/ChooseMaterialsBomComponent.vue";
export default {
  name: "Routing",
  dicts: [
    "zcii_bas_routing_type",
    "zcii_bas_apply_mode",
    "zicc_bas_status",
  ],
  components: {
    ChooseMaterialsBomComponent,
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
      // 总条数
      total: 0,
      // 基础数据-工艺路线表格数据
      routingList: [],
      // 工艺路线信息
      routing:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      copyOpen: false,
      exist: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null,
        routingType: null,
        applyMode: null,
        matterId: null,
        productionUnitId: null,
        upkeepId: null,
        status: null,
        deptId: null,
        remork: null,
        tenantId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询基础数据-工艺路线列表 */
    getList() {
      this.loading = true;
      listRouting(this.queryParams).then((response) => {
        this.routingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.copyOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        number: null,
        routingType: null,
        applyMode: null,
        matterId: null,
        productionUnitId: null,
        upkeepId: null,
        status: 0,
        deptId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remork: null,
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        name: null,
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$tab.openPage("新增工艺路线", "/basic/addRouting");
    },
    /** 新增按钮操作 */
    copyUpdate(row) {
      this.reset();
      this.copyOpen = true;
      this.routing = JSON.parse(JSON.stringify(row));  
      // this.routing = row;
      
      this.routing.matterId = -1;
      this.routing.matterNumber = '';
      this.routing.matterName = '';
      setTimeout(() => {
          //调用子组件的方法
          this.$refs["ref_matter_bom"].init();
        }, 10);
    },
    //物料信息
    chooseRow_Material(row, index) {
      this.routing.matterId =row.id;
      this.routing.matterNumber = row.number;
      this.routing.matterName = row.name;
      let rout = {
        matterId : row.id,
      }
      existRouting(rout).then((response) => {
        if (response.data.length > 0) {
          this.$modal.msgError(
            "产品编码【" +
              this.routing.matterNumber +
              "】的工艺路线名称【" +
              this.routing.name +
              "】已存在！"
          );
          this.exist = true;
        } else { 
           this.exist = false;
        }
      });
    },
    /** 
     * 复制工艺路线
     */
     copySubmitForm(){
        if(this.routing.matterId == -1){
          this.$message({
            message: "最最最重要的物料信息未选择噢！请选择您要同步工艺路线的物料！",
            type: "warning",
          });
          return;
        }
        if (this.exist) {
            this.$modal.msgError(
              "产品编码[" +
                this.routing.matterNumber +
                "]的[" +
                this.routing.name +
                "]工艺路线名称已存在！"
            );
            return;
          }
      copyAddRouting(this.routing).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.cancel();
              this.getList();
            });
           
     },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;

      let params = { id: id };

      this.$tab.openPage("修改工艺路线", "/basic/addRouting", params);
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateRouting(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRouting(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm(
          '是否确认删除基础数据-工艺路线编号为"' + row.number + '"的数据项？'
        )
        .then(function () {
          return delRouting(ids);
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
        "basic/routing/export",
        {
          ...this.queryParams,
        },
        `routing_${new Date().getTime()}.xlsx`
      );
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
