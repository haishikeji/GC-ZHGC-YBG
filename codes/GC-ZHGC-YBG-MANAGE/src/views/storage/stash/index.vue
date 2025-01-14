<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      class="sasda"
      label-width="68px"
    >
      <el-col>
        <img
          src="../../../assets/images/sousuo.png"
          style="margin-bottom: -13px; width: 37px"
        />
        <el-input
          v-model="queryParams.name"
          placeholder="仓库名称"
          clearable
          @keyup.enter.native="handleQuery"
          class="input_first"
        >
        </el-input>
      </el-col>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
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
          v-hasPermi="['storage:stash:add']"
          >新增
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="stashList"
      @selection-change="handleSelectionChange"
      border
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column
        label="仓库编号"
        align="center"
        prop="number"
        show-overflow-tooltip
      />
      <el-table-column
        label="仓库名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="仓库类型"
        align="center"
        prop="type"
        width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.zcli_stor_stash_type"
            :value="scope.row.type"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="仓库位置"
        align="center"
        prop="address"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.province }}
          {{ scope.row.city }}
          {{ scope.row.area }}
          {{ scope.row.address }}
        </template>
      </el-table-column>
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        width="140"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.cli_stor_stash_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="仓管员"
        align="center"
        prop="prosecutionName"
        show-overflow-tooltip
      />

      <!--      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="创建人" align="center" prop="createBy"/>-->

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
            v-hasPermi="['storage:stash:edit']"
            >修改
          </el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['storage:stash:remove']"
            >删除
          </el-button> -->
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

    <!-- 添加或修改仓储管理-仓库对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1200px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <div style="text-align: left; margin-right: 20px">
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="仓库名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入仓库名称"
                class="input_first"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="仓库类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择仓库类型">
                <el-option
                  v-for="dict in dict.type.zcli_stor_stash_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="仓库位置" prop="province">
              <el-select
                v-model="form.province"
                clearable
                placeholder="省"
                @change="seletekey($event, 'province')"
                class="sel_input"
              >
                <el-option
                  v-for="(val, key) in provinces"
                  :key="key"
                  :label="val"
                  :value="val"
                />
              </el-select>
              <!-- </el-form-item>
              <el-form-item prop="city"> -->
              <el-select
                v-model="form.city"
                clearable
                placeholder="市"
                @change="seletekey($event, 'city')"
                class="sel_input"
              >
                <el-option
                  v-for="(val, key) in citys"
                  :key="key"
                  :label="val"
                  :value="val"
                />
              </el-select>
              <!-- </el-form-item>
              <el-form-item prop="area"> -->
              <el-select
                v-model="form.area"
                clearable
                placeholder="区"
                @change="seletekey($event, 'area')"
                class="sel_input"
              >
                <el-option
                  v-for="(val, key) in areas"
                  :key="key"
                  :label="val"
                  :value="val"
                />
              </el-select>
              <!-- </el-form-item>
              <el-form-item prop="address"> -->
              <el-input
                v-model="form.address"
                placeholder="详细地址"
                class="col_input_max"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.cli_stor_stash_status"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                  >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="仓管员" prop="prosecution">
              <ChooseUserComponent
                @chooseRow="chooseRow_user"
                ref="reset_child"
              ></ChooseUserComponent>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  listStash,
  getStash,
  delStash,
  addStash,
  updateStash,
} from "@/api/storage/stash";

import { pcaa } from "area-data";
import ChooseUserComponent from "@/views/components/storage/ChooseUserComponent.vue";

export default {
  name: "Stash",
  components: { ChooseUserComponent },
  dicts: ["cli_stor_stash_status", "zcli_stor_stash_type"],
  data() {
    return {
      pcaaData: pcaa,
      // 区域数据
      provinces: pcaa["86"],
      citys: [],
      areas: [],
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
      // 仓储管理-仓库表格数据
      stashList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //用户列表是否显示曾
      userOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        province: null,
        city: null,
        area: null,
        address: null,
        status: null,
        prosecution: null,
        remork: null,
        tenantId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "仓库名称不能为空", trigger: "blur" },
        ],
        type: [
          { required: true, message: "仓库类型不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "仓库状态不能为空", trigger: "blur" },
        ],
        prosecution: [
          { required: true, message: "仓库员不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询仓储管理-仓库列表 */
    getList() {
      this.loading = true;
      listStash(this.queryParams).then((response) => {
        this.stashList = response.rows;
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
        name: null,
        type: null,
        province: null,
        city: null,
        area: null,
        address: null,
        status: 0,
        prosecution: null,
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

    /** 用户列表搜索按钮操作 */
    userListQuery() {
      this.userQueryParams.pageNum = 1;
      this.getUserList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 用户重置按钮操作 */
    resetUserQuery() {
      this.resetForm("userQueryForm");
      this.userListQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      setTimeout(() => {
        //调用子组件的方法
        this.$refs["reset_child"].reset_data();
      }, 10);
      this.open = true;
      this.title = "添加仓储管理-仓库";
    },
    chooseRow_user(data) {
      console.log(data.userId);
      this.form.prosecution = data.userId;
      console.log("prosecution...", this.form.prosecution);
    },
    openUserList() {
      this.reset();
      this.open = false;
      this.title = "用户列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getStash(id).then((response) => {
        this.form = response.data;
        //console.log("province...",this.form.province)
        console.log("city...", this.form.city);
        //this.form.prosecution=response.data.prosecution;
        //延迟加载
        setTimeout(() => {
          //调用子组件的方法
          this.$refs["reset_child"].assign_check(response.data.prosecutionName);
        }, 10);
        // this.form.province = this.form.province+""
        // this.form.city = this.form.city+""
        // this.form.area = this.form.area+""
        this.open = true;
        this.title = "修改仓储管理-仓库";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          console.log(this.from);
          if (this.form.province != undefined) {
            if (this.form.area == undefined) {
              return this.$modal.msgSuccess("注册地址选择需要精确到区");
            }
          }
          if (this.form.id != null) {
            updateStash(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStash(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    seletekey(e, type) {
      if (e) {
        switch (type) {
          case "province":
            Object.keys(this.provinces).filter((item) => {
              if (this.provinces[item] === e) {
                this.citys = this.pcaaData[item];
              }
            });
            break;
          case "city":
            Object.keys(this.citys).filter((item) => {
              if (this.citys[item] === e) {
                this.areas = this.pcaaData[item];
              }
            });
            break;
          case "area":
            break;
        }
      }
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除仓储管理-仓库编号为"' + ids + '"的数据项？')
        .then(function () {
          return delStash(ids);
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
        "stor/stash/export",
        {
          ...this.queryParams,
        },
        `stash_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.input_first {
  margin-left: 8px;
  width: 190px;
}

.sasda ::v-deep .el-input__inner {
  border: none;
  border-bottom: 1px solid #000;
  box-shadow: none;
  border-radius: 0px;
}

.sel_input {
  width: 120px;
  margin-right: 20px;
}

.col_input {
  width: 200px;
}

.col_input_max {
  width: 320px;
}
</style>
