package com.example.diancan2.controller;

import com.example.diancan2.entity.User;
import com.example.diancan2.service.UserService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            return ResponseEntity.ok("Login success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @PostMapping
    @RequiresPermissions("user:manage")
    public User createUser(@RequestBody User user) {
        String salt = UUID.randomUUID().toString();
        String newPassword = new SimpleHash("md5", user.getPassword(), ByteSource.Util.bytes(salt), 2).toHex();
        user.setSalt(salt);
        user.setPassword(newPassword);
        userService.save(user);
        return user;
    }
    
    @GetMapping
    @RequiresPermissions("user:manage")
    public List<User> getAllUsers() {
        return userService.list();
    }
}
