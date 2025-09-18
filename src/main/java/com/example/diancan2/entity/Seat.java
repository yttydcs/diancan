package com.example.diancan2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 座位实体类
 */
@Data
@TableName("seat")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 状态 0:空闲 1:占用 2:已预约
     */
    private Integer status;

    /**
     * 二维码
     */
    private String qrCode;
}
