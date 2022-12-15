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
import com.example.demo.utils.TokenUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getcode")
    public Result<?> getcode(@RequestParam String phone){

        //校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error("-1","手机号错误");
        }

        String code = RandomUtil.randomNumbers(6);  //六位随机验证码

        stringRedisTemplate.opsForValue().set(phone,code,5L, TimeUnit.MINUTES);  //将验证码存入redis，5分钟有效
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
            user.setPassword("123456");
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
    public  Result<?> password(@RequestBody User user){

        String code = stringRedisTemplate.opsForValue().get(user.getPhone());  //从redis中取出验证码
        if (code==null){
            return Result.error("-1","请先获取验证码");
        }

        if (user.getCode().equals(code)){
            userMapper.updateById(user);
            stringRedisTemplate.delete(user.getPhone());
            return Result.success();
        }
        return Result.error("-1","验证码错误");
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user){
        //校验手机号
        if (RegexUtils.isPhoneInvalid(user.getPhone())) {
            return Result.error("-1","手机号格式错误");
        }
        userMapper.updateById(user);
        return Result.success();
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

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
        User user = userMapper.selectById(id);
        if (user == null){
            return Result.error("-1","授权失败,用户信息错误");
        }
        if (user.getAlow().equals("1")){
            return Result.error("-1","该用户已有借阅权限");
        }
        user.setAlow("1");
        userMapper.updateById(user);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
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
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
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
    @GetMapping("/alow/{id}")
    public Result<?> alow(@PathVariable Long id){
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getId,id).eq(User::getAlow,"1");
        User user = userMapper.selectOne(wrapper);
        if (user == null){
            return Result.error("-1","您没有管理员授予的借阅权!");
        }
        return Result.success();
    }
}
