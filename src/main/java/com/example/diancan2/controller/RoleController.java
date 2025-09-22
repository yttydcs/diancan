package com.example.diancan2.controller;

import com.example.diancan2.entity.Role;
import com.example.diancan2.mapper.RoleMapper;
import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiresRoles("admin")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    public ApiResponse<List<Role>> getAllRoles() {
        return ApiResponse.success(roleMapper.selectList(null));
    }
}
