package com.example.diancan2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user_store")
public class UserStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long storeId;
}
