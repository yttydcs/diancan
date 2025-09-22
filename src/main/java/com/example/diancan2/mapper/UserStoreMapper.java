package com.example.diancan2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserStoreMapper {

    @Select("SELECT store_id FROM user_store WHERE user_id = #{userId}")
    List<Long> findStoreIdsByUserId(Long userId);
}
