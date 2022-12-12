package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookWithUser;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BookWithUserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookMapper BookMapper;

    @Resource
    BookWithUserMapper bookWithUserMapper;

    @PostMapping
    public Result<?> save(@RequestBody Book book){
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,book.getIsbn());
        Book selectOne = BookMapper.selectOne(wrapper);
        if (selectOne != null){
            return Result.error("-1","图书编号已存在!");
        }
        BookMapper.insert(book);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody Book book){
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,book.getIsbn()).ne(Book::getId,book.getId());
        Book selectOne = BookMapper.selectOne(wrapper);
        if (selectOne != null){
            return Result.error("-1","图书编号已存在!");
        }
        BookMapper.updateById(book);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    @Transactional
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        for (Integer id:ids){
            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getId,id);
            Book book = BookMapper.selectOne(wrapper);
            LambdaQueryWrapper<BookWithUser> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.eq(BookWithUser::getIsbn,book.getIsbn());
            BookWithUser bookWithUser = bookWithUserMapper.selectOne(wrapper1);
            if (bookWithUser != null){
                return Result.error("-1","书籍在借阅中,无法下架");
            }
        }
        BookMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    @Transactional
    public Result<?> delete(@PathVariable Long id){
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getId,id);
        Book book = BookMapper.selectOne(wrapper);
        LambdaQueryWrapper<BookWithUser> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(BookWithUser::getIsbn,book.getIsbn());
        BookWithUser bookWithUser = bookWithUserMapper.selectOne(wrapper1);
        if (bookWithUser != null){
            return Result.error("-1","书籍在借阅中,无法下架");
        }
        BookMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<Book> wrappers = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(Book::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(Book::getName,search2);
        }
        if(StringUtils.isNotBlank(search3)){
            wrappers.like(Book::getAuthor,search3);
        }
        wrappers.orderByDesc(Book::getBorrownum);    //按借阅次数排序
        Page<Book> BookPage =BookMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(BookPage);
    }
}
