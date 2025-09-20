package com.example.diancan2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.diancan2.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT r.name FROM role r INNER JOIN \"user\" u ON r.id = u.role_id WHERE u.id = #{userId}")
    Set<String> findRolesByUserId(Long userId);
}
