package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.BookWithUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookWithUserMapper extends BaseMapper<BookWithUser> {

}
