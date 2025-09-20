package com.example.diancan2.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Permission;
import com.example.diancan2.entity.Role;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.PermissionMapper;
import com.example.diancan2.mapper.RoleMapper;
import com.example.diancan2.mapper.RolePermissionMapper;
import com.example.diancan2.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;


    @Override
    public void run(String... args) throws Exception {
        // 1. 创建权限
        Permission userManage = createPermissionIfNotFound("user:manage", "用户管理");
        Permission storeManage = createPermissionIfNotFound("store:manage", "店铺管理");
        Permission selfStoreManage = createPermissionIfNotFound("store:self:manage", "自家店铺管理");

        // 2. 创建角色
        Role adminRole = createRoleIfNotFound("admin");
        Role managerRole = createRoleIfNotFound("manager");

        // 3. 分配权限给角色
        assignPermissionToRole(adminRole, userManage);
        assignPermissionToRole(adminRole, storeManage);
        assignPermissionToRole(managerRole, selfStoreManage);


        // 4. 创建默认管理员
        if (userService.findByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            String salt = UUID.randomUUID().toString();
            String newPassword = new SimpleHash("md5", "password", ByteSource.Util.bytes(salt), 2).toHex();
            user.setSalt(salt);
            user.setPassword(newPassword);
            user.setRoleId(adminRole.getId());
            userService.save(user);
        }
    }
    
    private Permission createPermissionIfNotFound(String name, String description) {
        Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("name", name));
        if (permission == null) {
            permission = new Permission();
            permission.setName(name);
            permission.setDescription(description);
            permissionMapper.insert(permission);
        }
        return permission;
    }

    private Role createRoleIfNotFound(String name) {
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", name));
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleMapper.insert(role);
        }
        return role;
    }

    private void assignPermissionToRole(Role role, Permission permission) {
        if (rolePermissionMapper.countByRoleIdAndPermissionId(role.getId(), permission.getId()) == 0) {
            rolePermissionMapper.insert(role.getId(), permission.getId());
        }
    }
}
