package com.example.diancan2.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RolePermissionMapper {

    @Select("SELECT COUNT(*) FROM role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int countByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Insert("INSERT INTO role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
