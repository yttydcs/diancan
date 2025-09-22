package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.dto.OrderDTO;
import com.example.diancan2.entity.Order;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.OrderService;
import com.example.diancan2.vo.ApiResponse;
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

    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        return ApiResponse.success("订单创建成功", orderService.createOrder(orderDTO));
    }

    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return ApiResponse.success(orderService.list());
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return ApiResponse.success(Collections.emptyList());
            }
            return ApiResponse.success(orderService.list(new QueryWrapper<Order>().in("store_id", storeIds)));
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable Long id) {
        return ApiResponse.success(orderService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Boolean> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(status);
            return ApiResponse.success("订单状态更新成功", orderService.updateById(order));
        }
        return ApiResponse.error("订单未找到");
    }
}
