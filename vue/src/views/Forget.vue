<template>
  <div  class="login-container"  >
    <el-form ref="form" :model="form"    :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">密码重置</h2>
      <el-form-item prop="username" >
        <el-input v-model="form.username" placeholder="请输入您的用户名" clearable >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password"  placeholder="请输入新密码" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="请再次确认密码" clearable show-password>
        </el-input>
      </el-form-item>
      <el-form-item>
          <el-input  v-model="form.code" style="width: 45%;" placeholder="请输入验证码"></el-input>
          <el-button type="success" plain  style="margin-left: 10px" @click="getcode" :disabled="isDisabled">{{buttonName}}</el-button>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" style=" width: 100%;font-size: 18px" @click="register">密码重置</el-button>
      </el-form-item>
      <el-form-item><el-button type="text" style="font-size: 16px"  @click="$router.push('/login')">前往登录>> </el-button></el-form-item>
    </el-form>
  </div>

</template>

<script>
import request from "../utils/request";
import {ElMessage} from "element-plus";
export default {
  name: "Forget",
  data(){
    return{
      buttonName: "获取短信验证码",
      isDisabled: false,
      time: 60,
      form:{},
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
          {
            min: 2,
            max: 13,
            message: '长度要求为2到13位',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          }
        ],
        confirm:[
          {
            required:true,
            message:"请确认密码",
            trigger:"blur"
          }
        ],
      }
    }
  },

  methods:{
    getcode(){
      if (!this.form.username) {
        ElMessage.error("请填写用户名")
        return
      }
      if (!this.form.password) {
        ElMessage.error("请填写新密码")
        return
      }
      if(this.form.password != this.form.confirm)
      {
        ElMessage.error("两次密码输入不一致")
        return
      }
      request.get("forget/getcode",{
        params:{
          username:this.form.username
        }
      }).then(res=>{
        if (res.code == 0) {
          ElMessage.success("验证码发送成功")
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
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
    register(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.code) {
            ElMessage.error("请填写验证码")
            return
          }
          if (!this.form.password) {
            ElMessage.error("请填写新密码")
            return
          }
          if(this.form.password != this.form.confirm)
          {
            ElMessage.error("两次密码输入不一致")
            return
          }
          request.post("forget/register",this.form).then(res=>{
            if(res.code == 0)
            {
              ElMessage.success("密码修改成功")
              this.$router.push("/login")
            }
            else {ElMessage.error(res.msg)}
          })
        }
      })

    }
  }

}

</script>

<style scoped>
.login-container {
  position: fixed;
  width: 100%;
  height: 100%;
  background: url('../img/bg2.svg');
  background-size: contain;
}
.login-page {
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
</style>