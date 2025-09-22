package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.dto.OrderDTO;
import com.example.diancan2.entity.Order;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.OrderService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserStoreMapper userStoreMapper;

    // 创建订单
    @PostMapping
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    // 获取所有订单
    @GetMapping
    public List<Order> getAllOrders() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return orderService.list();
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return Collections.emptyList();
            }
            return orderService.list(new QueryWrapper<Order>().in("store_id", storeIds));
        }
    }

    // 根据ID获取订单
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    // 更新订单状态
    @PutMapping("/{id}/status")
    public boolean updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(status);
            return orderService.updateById(order);
        }
        return false;
    }
}
