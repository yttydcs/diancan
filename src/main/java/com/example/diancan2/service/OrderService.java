package com.example.diancan2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.diancan2.dto.OrderDTO;
import com.example.diancan2.entity.Order;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-07-29
 */
public interface OrderService extends IService<Order> {
    Order createOrder(OrderDTO orderDTO);
}
