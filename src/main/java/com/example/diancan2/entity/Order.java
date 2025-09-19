package com.example.diancan2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("\"order\"")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 座位ID
     */
    private Long seatId;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 订单状态 0:未支付 1:已支付 2:准备中 3:已完成 4:已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
