package com.example.diancan2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.diancan2.dto.OrderDTO;
import com.example.diancan2.entity.Food;
import com.example.diancan2.entity.Order;
import com.example.diancan2.entity.OrderItem;
import com.example.diancan2.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setSeatId(orderDTO.getSeatId());
        order.setStatus(0); // 未支付
        order.setCreateTime(LocalDateTime.now());

        List<OrderItem> orderItems = orderDTO.getItems().stream().map(itemDTO -> {
            Food food = foodService.getById(itemDTO.getFoodId());
            OrderItem orderItem = new OrderItem();
            orderItem.setFoodId(itemDTO.getFoodId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(food.getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        this.save(order);

        orderItems.forEach(orderItem -> orderItem.setOrderId(order.getId()));
        orderItemService.saveBatch(orderItems);

        return order;
    }
}
