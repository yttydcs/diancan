package com.example.diancan2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.diancan2.entity.OrderItem;
import com.example.diancan2.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-29
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
