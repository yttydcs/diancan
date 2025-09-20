package com.example.diancan2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜品实体类
 */
@Data
@TableName("food")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品描述
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 分类
     */
    private String category;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 店铺ID
     */
    private Long storeId;
}
