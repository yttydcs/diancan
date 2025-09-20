package com.example.diancan2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.diancan2.entity.Store;
import com.example.diancan2.mapper.StoreMapper;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}
