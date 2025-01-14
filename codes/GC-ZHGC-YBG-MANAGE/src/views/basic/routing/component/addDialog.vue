<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="160px">
      <el-row :gutter="35">
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
          <div style="text-align: left; margin-right: 20px">
            <el-button
              type="primary"
              size="mini"
              @click="backPrev"
              style="background-color: rgb(51, 171, 160); border: 0"
              icon="el-icon-top-left"
              >返 回</el-button
            >
            <el-button
              type="primary"
              @click="submitForm"
              size="mini"
              icon="el-icon-circle-check"
              >确 定</el-button
            >
            <el-button
              @click="cancel"
              size="mini"
              icon="el-icon-remove-outline"
              v-if="this.queryParams.id == 0"
              >取 消</el-button
            >
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-left: 20px; height: 30px">
          工艺路线信息：
        </el-col>
        <el-col :span="8">
          <el-form-item label="产品编码" prop="matterNumber">
            <ChooseMatterComponent
              @chooseRow="choose_Material"
              :text="form.matterNumber"
              ref="ref_matter_main"
              :value="form.matterId"
              v-if="form.id == undefined || form.id == 0"
            ></ChooseMatterComponent>
            <ChooseMatterComponent
              :text="form.matterNumber"
              ref="ref_matter_main"
              :value="form.matterId"
              :disabled="true"
              v-else
            ></ChooseMatterComponent>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="产品名称" prop="matterName">
            <el-input
              class="col_span"
              disabled
              v-model="form.matterName"
              placeholder="产品名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="form.status"
              placeholder="请选择状态"
              class="col_span"
            >
              <el-option
                v-for="dict in dict.type.zicc_bas_status"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="工艺路线名称" prop="name">
            <el-input
              class="col_span"
              v-model="form.name"
              placeholder="请输入工艺路线名称"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="工艺路线类型" prop="routingType">
            <el-select
              v-model="form.routingType"
              placeholder="请选择工艺路线类型"
              class="col_span"
            >
              <el-option
                v-for="dict in dict.type.zcii_bas_routing_type"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col
          :xs="24"
          :sm="24"
          :md="24"
          :lg="24"
          :xl="24"
          style="margin-left: 20px; height: 30px"
        >
          工艺路线配置：
        </el-col>
        <el-card
          shadow="hover"
          style="margin-top: 18px; height: 150px; margin-left: 60px; width: 90%"
        >
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <ChooseUpkeepComponent
              @chooseUpkeepRow="chooseRow_Upkeep"
              :value="this.form.matterId"
            ></ChooseUpkeepComponent>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <div class="block" v-for="(fit, index) in worksList" :key="fit.id">
              <div
                class="step_image"
                @click="getKeepMatterList(fit.upkeepId, index)"
                :class="istrue[index] ? 'click_yes' : 'click_no'"
              >
                <img
                  @click="delWorks(index)"
                  class="step_del_image"
                  src="../../../../assets/images/zicc/u3168.svg"
                />
                <div class="step_font">第{{ index + 1 }}步：{{ fit.name }}</div>
              </div>
            </div>
          </el-col>
        </el-card>
      </el-row>
      <el-row>
        <el-col
          :xs="24"
          :sm="24"
          :md="24"
          :lg="24"
          :xl="24"
          style="margin-left: 20px; height: 30px; margin-top: 18px"
        >
          其他物料需求：
        </el-col>
        <div v-for="(innerArray, innerIndex) in matterList" :key="innerIndex">
          <el-card
            v-show="innerIndex == innerindex"
            shadow="hover"
            style="
              margin-top: 18px;
              height: 280px;
              margin-left: 60px;
              width: 70%;
            "
          >
            <div
              style="
                text-align: right;
                margin-right: 20px;
                color: rgb(32, 142, 245);
                font-size: 14px;
                margin-bottom: 10px;
              "
            >
              <a type="primary" plain @click="addRow()" size="mini">增行</a>

              <a
                type="primary"
                plain
                @click="delRow()"
                size="mini"
                style="margin-left: 20px"
                >减行</a
              >
              <!--  -->
              <el-table
                v-loading="loading"
                :data="innerArray"
                @selection-change="handleSelectionChange"
                border
                height="230"
              >
                <el-table-column
                  label="产品编码"
                  align="center"
                  prop="matterNumber"
                  width="280"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    <ChooseMaterialsBomComponent
                      @chooseRow="chooseRow_Material"
                      :index="scope.$index"
                      :text="scope.row.materialsNumber"
                      :value="scope.row.materialsId"
                      ref="ref_matter_bom"
                    ></ChooseMaterialsBomComponent>
                  </template>
                </el-table-column>
                <el-table-column
                  label="产品名称"
                  align="center"
                  prop="materialsName"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  label="计量单位"
                  align="center"
                  prop="unitId"
                  width="200"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    <dict-tag
                      :options="dict.type.zcii_bas_unit_type"
                      :value="scope.row.unitId"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="基础用量" align="center" prop="count">
                  <template slot-scope="scope">
                    <el-input
                      v-model="scope.row.count"
                      @blur="handleBlur($event, scope.row, scope.$index)"
                      oninput="value=value.toString()
																					.replace(/[^\d.]/g,'')// 只能是数字
																					.replace(/^\./g, '') // 保证第一位只能是数字，不能是点
																					.replace(/^0{2}$/g, '0')//只能输入一个小数点
																					.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
																					.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 小数点后面保留2位
																					"
                    />
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
          </el-card>
        </div>
      </el-row>
      <el-row>
        <el-col
          :span="24"
          style="margin-top: 20px; margin-left: 20px; height: 30px"
        >
          <span style="float: left; text-align: center">生产单元：</span>
          <ChooseProductionUnitComponent
            v-model="form.productionUnitId"
            @chooseRow="chooseRow_ProductUnit"
            :text="form.unitName"
            ref="ref_productUnit"
          ></ChooseProductionUnitComponent>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {
  getRouting,
  addRouting,
  updateRouting,
  existRouting,
} from "@/api/basic/routing";
import ChooseMatterComponent from "@/views/components/basic/ChooseMatterComponent.vue";
import ChooseMaterialsBomComponent from "@/views/components/basic/ChooseMaterialsBomComponent.vue";
import ChooseUpkeepComponent from "@/views/components/basic/ChooseUpkeepComponent.vue";
import ChooseProductionUnitComponent from "@/views/components/basic/ChooseProductionUnitComponent.vue";
import Enumerable from "linq";
export default {
  name: "AddRouting",
  dicts: [
    "zcii_bas_routing_type",
    "zcii_bas_matter_type",
    "zicc_bas_status",
    "zcii_bas_unit_type",
  ],
  components: {
    ChooseMatterComponent,
    ChooseUpkeepComponent,
    ChooseProductionUnitComponent,
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
      keepId_step: 0,
      // 基础数据-工艺路线表格数据
      matterList: [],
      matterAllList: [],
      worksList: [],
      keepId: null,
      keepIndex: 0,
      innerindex: 0,
      chooseRow: null,
      routings: [],
      checkedDetections: [],
      detectionObj: ["自检", "互检", "抽检"],
      // 弹出层标题
      title: "",
      url: "",
      istrue: [],
      exist: false,
      // 查询参数
      queryParams: {
        id: 0,
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null,
        routingType: null,
        applyMode: null,
        matterId: null,
        productionUnitId: null,
        status: null,
        deptId: null,
        remork: null,
        tenantId: null,
      },
      // 表单参数
      form: {
        name: null,
        number: null,
        routingType: null,
        applyMode: null,
        matterId: null,
        matterName: null,
        productionUnitId: null,
        status: 1,
        deptId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remork: null,
        matterList: [],
        worksList: [],
        tenantId: null,
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "工艺路线名称不能为空", trigger: "blur" },
        ],
        routingType: [
          { required: true, message: "工艺路线类型不能为空", trigger: "blur" },
        ],
        applyMode: [
          { required: true, message: "适用对象不能为空", trigger: "blur" },
        ],
        matterName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "工艺路线状态不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    if (this.$route.query.id != undefined) {
      let id = this.$route.query.id;
      this.queryParams.id = id;
      this.getList();
    } else {
      this.loading = false;
    }
  },
  methods: {
    /** 查询基础数据-工艺路线列表 */
    getList() {
      this.loading = true;
      getRouting(this.queryParams.id).then((response) => {
        this.form = response.data;
        this.matterAllList = response.data.matterList;
        this.worksList = response.data.worksList;
        if (this.worksList.length > 0) {
          for (var i = 0; i < this.worksList.length; i++) {
            var index = i + 1;
            let keep = this.worksList.find((item) => item.stepIndex == index);
            var keepId = keep.upkeepId;
            const matters = Enumerable.from(this.matterAllList)
              .where((item) => item.keepId == keepId)
              .toArray();

            if (matters.length > 0) {
              this.matterList.push(matters);
            } else {
              this.matterList.push([]);
            }

            if (i == 0) {
              this.keepId = keepId;
              for (let ind = 0; ind < this.worksList.length; ind++) {
                if (ind == 0) {
                  this.istrue.push(true);
                } else {
                  this.istrue.push(false);
                }
              }
            }
          }
          this.loading = false;
        } else {
          this.matterList = response.data.matterList;
        }

        setTimeout(() => {
          //调用子组件的方法
          this.$refs["ref_matter_main"].initText(this.form.matterNumber);
          this.$refs["ref_productUnit"].initText(this.form.unitName);
        }, 10);
      });
    },

    /**
     * 工序选择事件
     */
    getKeepMatterList(id, index) {
      this.keepId = id;
      this.keepIndex = index;

      for (let ind = 0; ind < this.istrue.length; ind++) {
        if (ind != index) {
          this.istrue.splice(ind, 1, false);
        } else if (ind == index) {
          this.istrue.splice(ind, 1, true);
        }
      }

      this.innerindex = index;
      this.keepId_step = index + 1;
      this.loading = false;
    },
    // 取消按钮
    cancel() {
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: null,
        number: null,
        routingType: null,
        applyMode: null,
        matterId: null,
        productionUnitId: null,
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.exist) {
            this.$modal.msgError(
              "产品编码[" +
                this.form.matterNumber +
                "]的[" +
                this.form.name +
                "]工艺路线名称已存在！"
            );
            return;
          }
          var matterAllList = this.matterList;
          var allList = [];

          var isOK = true;

          matterAllList.forEach((item, index) => {
            item.forEach((matter, ind) => {
              if (matter.materialsId == null) {
                this.$modal.msgError(
                  "工艺路线第[" +
                    (index + 1) +
                    "]步,第" +
                    (ind + 1) +
                    "行的[产品编码]不能为空！"
                );
                isOK = false;
                return;
              } else if (matter.count <= 0) {
                this.$modal.msgError(
                  "工艺路线第[" +
                    (index + 1) +
                    "]步,第" +
                    (ind + 1) +
                    "行的[产品基础用量]不能小于等于0！"
                );
                isOK = false;
                return;
              } else {
                allList.push(matter);
              }
            });
          });

          if (isOK) {
            this.form.matterList = allList;
            // console.log("this.matterAllList......", this.form.matterAllList);

            this.worksList.forEach((item, index) => {
              item["stepIndex"] = index + 1;
            });
            this.form.worksList = this.worksList;
            if (this.form.id != null) {
              updateRouting(this.form)
                .then((response) => {
                  if (response.code == 200) {
                    this.$modal.msgSuccess("修改工艺路线成功");
                    this.$tab.closeOpenPage();
                    this.$tab.openPage("工艺路线", "/basic/routing");
                  }
                })
                .catch((error) => {
                  console.log(error);
                });
            } else {
              addRouting(this.form)
                .then((response) => {
                  if (response.code == 200) {
                    this.$modal.msgSuccess("新增工艺路线成功");
                    this.$tab.closeOpenPage();
                    this.$tab.openPage("工艺路线", "/basic/routing");
                  }
                })
                .catch((error) => {
                  console.log(error);
                });
            }
          }
        }
      });
    },

    handleBlur(e, row, index) {
      const matters = Enumerable.from(this.matterAllList)
        .where((item) => item.keepId == row.keepId)
        .toArray();

      matters[index].count = e.target.value;
    },
    //返回
    backPrev() {
      this.$tab.closeOpenPage();
      this.$tab.openPage("工艺路线", "/basic/routing");
    },

    //增行
    addRow() {
      if (this.keepId != null) {
        var index = this.innerindex;
        this.matterList[index].push({});
      } else {
        this.$modal.msgSuccess("请选择工序！");
      }

      console.log("增行......", this.matterList[index]);
    },
    //减行
    delRow() {
      var index = this.innerindex;
      let rowIndex = Enumerable.from(this.matterList[index]).indexOf(
        this.chooseRow
      );
      this.matterList[index].splice(rowIndex, 1);
      console.log("减行......", this.matterList[index]);
    },

    //其他产品信息
    chooseRow_Material(row, index) {
      //console.log("其他物料需求。。。", row);
      let data = Object.assign({}, this.matterList[index]);
      data.materialsId = row.id;
      data.materialsNumber = row.number;
      data.materialsName = row.name;
      data.unitId = row.unitId;
      data.keepId = this.keepId;
      data.count = 0;
      this.matterList[this.innerindex].splice(index, 1, data);
    },

    //工艺路线配置-新增工序
    chooseRow_Upkeep(row) {
      const keeps = Enumerable.from(this.worksList)
        .where((item) => item.upkeepId == row.id)
        .toArray();

      if (keeps.length > 0) {
        this.$modal.msgError(
          "该工序名称：【" + row.processName + "】 已存在，请勿重复选择"
        );
        return;
      }

      this.worksList.push({
        upkeepId: row.id,
        number: row.processId,
        name: row.processName,
        isInspector: false,
        isDetection: false,
        isSpotCheck: false,
      });

      this.keepId = row.id;

      let index = this.worksList.length;
      this.innerindex = index - 1;
      this.matterList.push([]);
      this.istrue.push(false);

      for (let ind = 0; ind < this.istrue.length; ind++) {
        if (ind == index - 1) {
          this.istrue.splice(ind, 1, true);
        } else {
          this.istrue.splice(ind, 1, false);
        }
      }
    },

    //删除工序
    delWorks(index) {
      this.worksList.splice(index, 1);
      let length = this.worksList.length;
      this.istrue.splice(index, 1);

      for (let ind = 0; ind < length; ind++) {
        if (ind == length - 1) {
          this.istrue.splice(ind, 1, true);
        } else {
          this.istrue.splice(ind, 1, false);
        }
      }
    },

    //生产单元
    chooseRow_ProductUnit(row) {
      this.form.productionUnitId = row.id;
      this.form.unitName = row.unitName;
    },

    //表头产品
    choose_Material(row, index) {
      this.form.matterId = row.id;
      this.form.matterName = row.name;
      this.form.matterNumber = row.number;
      this.form.name = row.name;
      this.form.number = row.number;
      this.form.applyMode = row.categoryId;

      existRouting(this.form).then((response) => {
        // console.log(" this.routings.....", this.routings);
        this.routings = response.data;
        if (this.routings.length > 0) {
          this.$modal.msgError(
            "产品编码【" +
              this.form.matterNumber +
              "】的工艺路线名称【" +
              this.form.name +
              "】已存在！"
          );

          this.exist = true;
          return;
        } else {
          this.exist = false;
          return;
        }
      });
    },
  },
};
</script>

<style>
.col_span {
  width: 200px;
}
.col_span_min {
  width: 100px;
  margin-right: 20px;
}

.step_image {
  background-image: url("../../../../assets/images/zicc/u3164.svg");
  /* background-size: 80%; */
  background-repeat: no-repeat;
  background-position: center;
  width: 187px;
  height: 93px;
  margin-right: 20px;
  float: left;
}

.click_yes {
  border: 1px solid yellow; /* 高亮边框的颜色和宽度 */
  box-shadow: 0 0 2px yellow; /* 高亮时的阴影效果 */
}

.click_no {
  border: 1px solid rgb(248, 245, 245); /* 高亮边框的颜色和宽度 */
  box-shadow: 0 0 2px rgb(250, 250, 249); /* 高亮时的阴影效果 */
}

.block {
  margin-top: 10px;
}

.step_del_image {
  /* background-size: 80%; */
  background-repeat: no-repeat;
  background-position: center;
  width: 18px;
  height: 18px;
  float: left;
  margin-top: 4px;
  margin-left: 4px;
  z-index: 20px;
}
.step_font {
  width: 160px;
  line-height: 104px;
  text-align: left;
  font-size: 14px;
}
</style>
