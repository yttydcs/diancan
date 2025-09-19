package com.example.diancan2.common;

import com.example.diancan2.entity.User;
import com.example.diancan2.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            String salt = UUID.randomUUID().toString();
            String newPassword = new SimpleHash("md5", "admin123!", ByteSource.Util.bytes(salt), 2).toHex();
            user.setSalt(salt);
            user.setPassword(newPassword);
            userService.save(user);
        }
    }
}
