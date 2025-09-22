package com.example.diancan2.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserStoreMapper {

    @Select("SELECT store_id FROM user_store WHERE user_id = #{userId}")
    List<Long> findStoreIdsByUserId(Long userId);

    @Select("SELECT user_id FROM user_store WHERE store_id = #{storeId}")
    List<Long> findUserIdsByStoreId(Long storeId);

    @Delete("DELETE FROM user_store WHERE store_id = #{storeId}")
    void deleteByStoreId(Long storeId);

    @Insert("INSERT INTO user_store (user_id, store_id) VALUES (#{userId}, #{storeId})")
    void insert(@Param("userId") Long userId, @Param("storeId") Long storeId);
}
