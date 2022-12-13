<template xmlns="http://www.w3.org/1999/html">
  <div>
    <el-card style="width: 40%; margin-left: 120px; margin-top: 40px" >
      <el-form
          ref="form"
          :model="form"
          status-icon
          :rules="rules"
          label-width="100px"
          class="demo-ruleForm"
      >
        <el-form-item label="验证码" prop="code">
          <el-input
              v-model="form2.code"
              type="code"
              autocomplete="off"
              style="width: 150px"
          ></el-input>
          <el-button type="success" plain  style="margin-left: 20px" @click="getcode" :disabled="isDisabled">{{buttonName}}</el-button>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input
              v-model="form2.password"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="checkpassword">
          <el-input
              v-model="form.checkpassword"
              type="password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" style="text-align: center;margin-left: 120px">提交</el-button>
          <el-button @click="resetForm('form')" style="text-align: center;margin-left: 20px">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
  name: "Password",
  data() {

    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        callback()
      }
    }
    const validatePass3 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form2.password) {
        callback(new Error("两次输入密码不匹配"))
      } else {
        callback()
      }
    }
    return {

      buttonName: "获取短信验证码",
      isDisabled: false,
      time: 60,

      phone:'',
      form: {
        checkpassword: '',
      },
      form2:{
        phone:'',
        code: '',
        password:'',
        id:0
      },
      rules: {
        password: [{ validator: validatePass, trigger: 'blur' ,required: true}],
        checkpassword: [{ validator: validatePass3, trigger: 'blur',required: true,}],
      },
    }
  },
  created() {
    let userJson = sessionStorage.getItem("user")
    if(!userJson)
    {
      router.push("/login")
    }
    let user = JSON.parse(sessionStorage.getItem("user"))
    this.phone= user.phone
    this.form2.id = user.id
    this.form2.phone = user.phone
  },
  methods: {

    getcode(){

      if (this.phone == null){
        ElMessage.error("获取失败! 请先将个人信息补充完整")
        this.$router.push("/person")//跳转个人信息界面
        return;
      }

      let me = this;
      me.isDisabled = true;
      let interval = window.setInterval(function() {
        me.buttonName = '（' + me.time + '秒）后重新发送';
        --me.time;
        if(me.time < 0) {
          me.buttonName = "重新发送";
          me.time = 60;
          me.isDisabled = false;
          window.clearInterval(interval);
        }
      }, 1000);
      request.get("user/getcode",{
        params:{
          phone:this.phone
        }
      }).then(res=>{
        if (res.code == 0) {
          ElMessage.success("验证码发送成功")
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {

          request.put("/user", this.form2).then(res => {
            if (res.code == 0) {
              ElMessage.success("密码修改成功,请重新登录")
              sessionStorage.removeItem("user")//清空缓存的用户信息
              this.$router.push("/login")//跳转登录界面
            } else {
              ElMessage.error(res.msg)
            }
          })
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>

<style scoped>

</style>
