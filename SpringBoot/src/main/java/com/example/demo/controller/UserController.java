package com.example.demo.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.LoginUser;
import com.example.demo.commom.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.RegexUtils;
import com.example.demo.utils.SmsUtils;
import org.springframework.web.bind.annotation.*;
import com.example.demo.utils.TokenUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;


    @GetMapping("/getcode")
    public Result<?> getcode(@RequestParam String phone, HttpSession session){

        //校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("-1","手机号错误");
        }

        String code = RandomUtil.randomNumbers(6);  //六位随机验证码
        session.setAttribute(phone,code); //code存入session
        SmsUtils.sendSms(phone,code);   //发送验证码
        System.out.println(code);
        return Result.success();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res != null)
        {
            return Result.error("-1","用户名已重复");
        }
        userMapper.insert(user);
        return Result.success();
    }
    @CrossOrigin
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(res == null)
        {
            return Result.error("-1","用户名或密码错误");
        }
        String token = TokenUtils.genToken(res);
        res.setToken(token);
        LoginUser.addVisitCount();
        return Result.success(res);
    }
    @PostMapping
    public Result<?> save(@RequestBody User user){
        if(user.getPassword() == null){
            user.setPassword("abc123456");
        }
        userMapper.insert(user);
        return Result.success();
    }
    @PutMapping("/password")
    public  Result<?> update( @RequestParam Integer id,
                              @RequestParam String password2){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        User user = new User();
        user.setPassword(password2);
        userMapper.update(user,updateWrapper);
        return Result.success();
    }
    @PutMapping
    public  Result<?> password(@RequestBody User user,HttpSession session){
        String code = session.getAttribute(user.getPhone()).toString();
        if (user.getCode().equals(code)){
            userMapper.updateById(user);
            session.removeAttribute(user.getPhone()); //移除验证码
            return Result.success();
        }
        return Result.error("-1","验证码错误");
    }
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        userMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrappers = Wrappers.<User>lambdaQuery();
        if(StringUtils.isNotBlank(search)){
            wrappers.like(User::getNickName,search);
        }
        wrappers.like(User::getRole,2);
        Page<User> userPage =userMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(userPage);
    }
    @GetMapping("/usersearch")
    public Result<?> findPage2(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                               @RequestParam(defaultValue = "") String search2,
                               @RequestParam(defaultValue = "") String search3,
                               @RequestParam(defaultValue = "") String search4){
        LambdaQueryWrapper<User> wrappers = Wrappers.<User>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(User::getId,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(User::getNickName,search2);
        }
        if(StringUtils.isNotBlank(search3)){
            wrappers.like(User::getPhone,search3);
        }
        if(StringUtils.isNotBlank(search4)){
            wrappers.like(User::getAddress,search4);
        }
        wrappers.like(User::getRole,2);
        wrappers.orderByAsc(User::getId);   //按编号排序
        Page<User> userPage =userMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(userPage);
    }
}
