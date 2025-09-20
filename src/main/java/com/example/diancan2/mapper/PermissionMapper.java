package com.example.diancan2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.diancan2.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT p.name FROM permission p INNER JOIN role_permission rp ON p.id = rp.permission_id WHERE rp.role_id = #{roleId}")
    Set<String> findPermissionsByRoleId(Long roleId);
}
