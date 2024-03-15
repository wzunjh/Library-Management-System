# 图书馆管理系统（library-system）

## 主要技术

SpringBoot、Mybatis-Plus、MySQL、Vue3、ElementPlus等

## 主要功能

管理员模块：注册、登录、书籍管理、读者管理、借阅管理、借阅者权限管理、借阅状态、修改个人信息、修改密码

读者模块：注册、登录、查询图书信息、借阅和归还图书、查看个人借阅记录、修改个人信息、修改密码

## 代码结构

### 前端

```shell
library-ui
├─api			// api接口
├─assets		// 资源文件	
│  ├─icon	 	// 图标
│  ├─img	 	// 图片
│  └─styles	 	// 样式
├─components	// 自定义Vue组件
├─layout		// 页面布局
├─router		// Vue路由
├─utils			// 工具函数
└─views			// 页面
```

### 后端

```shell
library-serve
├─java
│  └─com
│      └─admin
│          └─library
│              ├─common			// 通用类
│              │  ├─base		// 基础类
│              │  └─config		// 配置类
│              ├─controller		// 控制层
│              ├─domain			// 实体类
│              ├─mapper			// 持久层
│              └─service		// 业务层
└─resources	// maven资源配置
```
### 前端启动
```shell
npm install  #安装所需依赖
npm run serve  # 启动前端项目
npm build      # 打包前端项目(dist)部署
```
### 后端启动
1.运行根目录中的sql文件，测试连接数据库

2.启动本地redis服务(window或者docker部署，可选)

3.添加阿里云短信服务Key和pwd（可选）

4.运行springboot启动类

5.当报错显示阿里云短信请求不存在时，请查看Issues解决问题 [点击这里](https://github.com/wzunjh/Library-Management-System/issues/1)

### 部分页面展示
| ![输入图片说明](vue/public/%E5%9B%BE%E7%89%873.png)  | ![输入图片说明](vue/public/%E5%9B%BE%E7%89%876.png)  | ![输入图片说明](vue/public/%E5%9B%BE%E7%89%87.png)  |
|---|---|---|
| ![输入图片说明](vue/public/%E5%9B%BE%E7%89%871.png)  |  ![输入图片说明](vue/public/%E5%9B%BE%E7%89%872.png) | ![输入图片说明](vue/public/%E5%9B%BE%E7%89%878.png)  |
|   | ![输入图片说明](vue/public/%E5%9B%BE%E7%89%879.png)  |   |

