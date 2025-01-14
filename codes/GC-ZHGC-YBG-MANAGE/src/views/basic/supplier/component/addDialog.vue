<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="35">
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
          <div style="text-align: left; margin-right: 20px">
            <span class="dialog-footer">
              <el-button
                type="primary"
                size="mini"
                @click="backPrev"
                style="background-color: rgb(51, 171, 160); border: 0"
                icon="el-icon-top-left"
                >返 回</el-button
              >
              <el-button
                type="warning"
                size="mini"
                @click="submitForm"
                icon="el-icon-circle-check"
                >确 定</el-button
              >
              <el-button
                @click="cancel"
                size="mini"
                icon="el-icon-remove-outline"
                >取 消</el-button
              >
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="供应商名称" prop="name">
            <el-input
              class="input_first"
              v-model="form.name"
              placeholder="请输入供应商名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="供应商类型">
            <el-select v-model="form.firmType" placeholder="请选择供应商类型">
              <el-option
                v-for="dict in dict.type.zcii_bas_fim"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="所属行业" prop="industry">
            <el-select v-model="form.industry" placeholder="请选择所属行业">
              <el-option
                v-for="dict in dict.type.zcii_bas_Industry"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="法人代表" prop="represent">
            <el-input
              class="input_first"
              v-model="form.represent"
              placeholder="请输入法人代表"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联 系 人" prop="contact">
            <el-input
              class="input_first"
              style="margin-left: -1px; width: 208px"
              v-model="form.contact"
              placeholder="请输入联系人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联系电话" prop="tel">
            <el-input
              class="input_first"
              style="margin-left: -1px; width: 208px"
              v-model="form.tel"
              placeholder="请输入联系电话"
              type="number"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="16">
          <el-form-item
            label="注册地址"
            prop="enrollAddr"
            :model="addrform"
            class="addr_fo_sel"
          >
            <el-select
              v-model="addrform.province"
              clearable
              placeholder="省"
              @change="seletekey($event, 'province')"
            >
              <el-option
                v-for="(val, key) in provinces"
                :key="key"
                :label="val"
                :value="val"
              />
            </el-select>
            <el-select
              v-model="addrform.city"
              clearable
              placeholder="市"
              @change="seletekey($event, 'city')"
            >
              <el-option
                v-for="(val, key) in citys"
                :key="key"
                :label="val"
                :value="val"
              />
            </el-select>
            <el-select
              v-model="addrform.area"
              clearable
              placeholder="区"
              @change="seletekey($event, 'area')"
            >
              <el-option
                v-for="(val, key) in areas"
                :key="key"
                :label="val"
                :value="val"
              />
            </el-select>
            <el-input
              v-model="addrform.address"
              placeholder="详细地址"
              style="width: 300px"
            />
          </el-form-item>
          <el-form-item label="公司简介" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="4"
              maxlength="90"
              show-word-limit
            />
          </el-form-item>
      </el-col>
      <el-col :span="8">
          <el-form-item label="邮箱" prop="tel">
            <el-input
              class="input_first"
              style="margin-left: -1px; width: 208px"
              v-model="form.email"
              placeholder="请输入邮箱地址"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { addSupplier, getSupplier, updateSupplier } from "@/api/basic/supplier";

import { pcaa } from "area-data";
export default {
  name: "AddCustomer",
  dicts: ["zcii_bas_fim", "zcii_bas_Industry"],

  data() {
    return {
      addrform: {
        province: null,
        city: null,
        area: null,
        enrollAddr: null,
      },
      pcaaData: pcaa,
      // 区域数据
      provinces: pcaa["86"],
      citys: [],
      areas: [],
      loading: true,
      // 显示搜索条件
      showSearch: true,
      chooseRow: null,
      // 弹出层标题
      title: "",
      queryParams: {
        id: 0,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "供应商名称不能为空", trigger: "blur" },
        ],
        firmType: [
          { required: true, message: "供应商类型不能为空", trigger: "change" },
        ],
        // represent: [
        //   { required: true, message: "法人代表不能为空", trigger: "blur" },
        // ],
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
    chooseRow_Material(row, index) {},
    // 取消按钮
    cancel() {
      this.reset();
    },
    //返回页面
    backPrev() {
      this.$tab.closeOpenPage();
      this.$tab.openPage("供应商信息", "/basic/supplier");
    },
    addRow() {
      this.packList.push({});
    },

    /** 查询基础数据-物料列表 */
    getList() {
      this.loading = true;
      getSupplier(this.queryParams.id).then((response) => {
        this.form = response.data;
        this.addrform = response.data;
        this.form.firmType = this.form.firmType + "";
        this.form.industry = this.form.industry + "";
        this.loading = false;
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.addrform.province != undefined) {
            this.form.province = this.addrform.province;
            this.form.enrollAddr = this.addrform.province;
            // if (
            //   this.addrform.area == undefined ||
            //   this.addrform.address == undefined
            // ) {
            //   return this.$modal.msgSuccess("请填写完整的注册地址！");
            // }
            this.form.city = this.addrform.city;
            this.form.area = this.addrform.area;
            this.form.address = this.addrform.address;

            if (this.form.city != undefined) {
              this.form.enrollAddr =
                this.addrform.province + this.addrform.city;
            }

            if (this.form.area != undefined) {
              this.form.enrollAddr =
                this.addrform.province +
                this.addrform.city +
                this.addrform.area;
            }

            if (this.form.address != undefined) {
              this.form.enrollAddr =
                this.addrform.province +
                this.addrform.city +
                this.addrform.area +
                this.addrform.address;
            }
          }

          if (this.form.id != null) {
            updateSupplier(this.form)
              .then((response) => {
                if (response.code == 200) {
                  this.$modal.msgSuccess("修改成功");
                  this.$tab.closeOpenPage();
                  this.$tab.openPage("供应商信息", "/basic/supplier");
                }
              })
              .catch((error) => {
                console.log(error);
              });
          } else {
            addSupplier(this.form)
              .then((response) => {
                if (response.code == 200) {
                  this.$modal.msgSuccess("新增成功");
                  this.$tab.closeOpenPage();
                  this.$tab.openPage("供应商信息", "/basic/supplier");
                }
              })
              .catch((error) => {
                console.log(error);
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
  },
};
</script>

<style lang="scss" scoped>
.input_first {
  margin-left: 8px;
  width: 200px;
}
.addr_fo_sel .el-select {
  margin-right: 20px;
}
</style>
