package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookWithUser;
import com.example.demo.entity.LendRecord;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import com.example.demo.mapper.LendRecordMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookwithuser")
public class BookWithUserController {
    @Resource
    BookWithUserMapper BookWithUserMapper;

    @Resource
    BookMapper bookMapper;

    @Resource
    LendRecordMapper lendRecordMapper;


    @PostMapping("/insertNew")
    public Result<?> insertNew(@RequestBody BookWithUser BookWithUser){
        BookWithUserMapper.insert(BookWithUser);
        return Result.success();
    }
    @PostMapping
    public Result<?> update(@RequestBody BookWithUser BookWithUser){
        UpdateWrapper<BookWithUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("isbn",BookWithUser.getIsbn()).eq("id",BookWithUser.getId());
        BookWithUserMapper.update(BookWithUser, updateWrapper);
        return Result.success();
    }
//删除一条记录
    @PostMapping("/deleteRecord")
    @Transactional
    public  Result<?> deleteRecord(@RequestBody BookWithUser BookWithUser){
        Map<String,Object> map = new HashMap<>();
        map.put("isbn",BookWithUser.getIsbn());
        map.put("id",BookWithUser.getId());
        BookWithUserMapper.deleteByMap(map);
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,BookWithUser.getIsbn());
        Book book = bookMapper.selectOne(wrapper);
        book.setStatus("1"); //归还
        bookMapper.updateById(book);
        return Result.success();
    }

    @PostMapping("/deleteRecords")
    @Transactional
    public Result<?> deleteRecords(@RequestBody List<BookWithUser> BookWithUsers){
        Date date = new java.util.Date();//获取当前时间对象，也可以直接传入Date的对象
        for (BookWithUser curRecord : BookWithUsers) {
            Map<String, Object> map = new HashMap<>();
            map.put("isbn", curRecord.getIsbn());
            map.put("id", curRecord.getId());
            BookWithUserMapper.deleteByMap(map);
            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn,curRecord.getIsbn());
            Book book = bookMapper.selectOne(wrapper);
            book.setStatus("1"); //归还
            bookMapper.updateById(book);
            LambdaQueryWrapper<LendRecord> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.eq(LendRecord::getReaderId,curRecord.getId()).eq(LendRecord::getIsbn,curRecord.getIsbn()).eq(LendRecord::getStatus,"0");
            LendRecord lendRecord = lendRecordMapper.selectOne(wrapper1);
            lendRecord.setStatus("1");  //归还
            lendRecord.setReturnTime(date);
            lendRecordMapper.updateById(lendRecord);
        }
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<BookWithUser> wrappers = Wrappers.<BookWithUser>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(BookWithUser::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(BookWithUser::getBookName,search2);
        }
        if(StringUtils.isNotBlank(search3)){
            wrappers.like(BookWithUser::getId,search3);
        }
        wrappers.orderByDesc(BookWithUser::getLendtime);   //按借阅时间排序
        Page<BookWithUser> BookPage =BookWithUserMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(BookPage);
    }
}
