package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Store;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.StoreService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserStoreMapper userStoreMapper;

    @GetMapping
    @RequiresPermissions("store:manage")
    public List<Store> getAllStores() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return storeService.list();
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return Collections.emptyList();
            }
            return storeService.list(new QueryWrapper<Store>().in("id", storeIds));
        }
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        storeService.save(store);
        return store;
    }

    @PutMapping
    public Store updateStore(@RequestBody Store store) {
        storeService.updateById(store);
        return store;
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id) {
        storeService.removeById(id);
    }
}
