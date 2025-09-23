package com.example.diancan2.vo;

import lombok.Data;

import java.util.Set;

@Data
public class LoginVO {
    private String username;
    private Set<String> roles;
    private Set<String> permissions;
}
