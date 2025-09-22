package com.example.diancan2.controller;

import com.example.diancan2.entity.User;
import com.example.diancan2.service.UserService;
import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.lang.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            return ApiResponse.success("登录成功", null);
        } catch (Exception e) {
            return ApiResponse.error("登录失败，请检查用户名和密码");
        }
    }

    @PostMapping
    @RequiresPermissions("user:manage")
    public ApiResponse<User> createUser(@RequestBody User user) {
        String salt = UUID.randomUUID().toString();
        String newPassword = new SimpleHash("md5", user.getPassword(), ByteSource.Util.bytes(salt), 2).toHex();
        user.setSalt(salt);
        user.setPassword(newPassword);
        userService.save(user);
        return ApiResponse.success("用户创建成功", user);
    }
    
    @GetMapping
    @RequiresPermissions("user:manage")
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.list());
    }

    @PutMapping
    @RequiresPermissions("user:manage")
    public ApiResponse<User> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return ApiResponse.success("用户更新成功", user);
    }

    @DeleteMapping("/{id}")
    @RequiresPermissions("user:manage")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return ApiResponse.success("用户删除成功", null);
    }

    @PostMapping("/{id}/reset-password")
    @RequiresPermissions("user:manage")
    public ApiResponse<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        User user = userService.getById(id);
        if (user != null) {
            String newPassword = payload.get("password");
            String salt = UUID.randomUUID().toString();
            String encryptedPassword = new SimpleHash("md5", newPassword, ByteSource.Util.bytes(salt), 2).toHex();
            user.setSalt(salt);
            user.setPassword(encryptedPassword);
            userService.updateById(user);
            return ApiResponse.success("密码重置成功", null);
        }
        return ApiResponse.error("用户不存在");
    }
}
