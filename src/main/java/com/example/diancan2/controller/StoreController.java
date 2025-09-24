package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Store;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.StoreService;
import com.example.diancan2.vo.ApiResponse;
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
    public ApiResponse<List<Store>> getAllStores() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return ApiResponse.success(storeService.list());
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return ApiResponse.success(Collections.emptyList());
            }
            return ApiResponse.success(storeService.list(new QueryWrapper<Store>().in("id", storeIds)));
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Store> getStoreById(@PathVariable Long id) {
        return ApiResponse.success(storeService.getById(id));
    }

    @GetMapping("/customer/{id}")
    public ApiResponse<Store> getStoreByIdForCustomer(@PathVariable Long id) {
        return ApiResponse.success(storeService.getById(id));
    }

    @PostMapping
    @RequiresPermissions("store:manage")
    public ApiResponse<Store> createStore(@RequestBody Store store) {
        storeService.save(store);
        return ApiResponse.success("店铺创建成功", store);
    }

    @PutMapping
    @RequiresPermissions("store:manage")
    public ApiResponse<Store> updateStore(@RequestBody Store store) {
        storeService.updateById(store);
        return ApiResponse.success("店铺更新成功", store);
    }

    @DeleteMapping("/{id}")
    @RequiresPermissions("store:manage")
    public ApiResponse<Void> deleteStore(@PathVariable Long id) {
        storeService.removeById(id);
        return ApiResponse.success("店铺删除成功", null);
    }

    @GetMapping("/{id}/managers")
    @RequiresPermissions("store:manage")
    public ApiResponse<List<Long>> getStoreManagers(@PathVariable Long id) {
        return ApiResponse.success(userStoreMapper.findUserIdsByStoreId(id));
    }

    @PostMapping("/{id}/managers")
    @RequiresPermissions("store:manage")
    public ApiResponse<Void> updateStoreManagers(@PathVariable Long id, @RequestBody List<Long> userIds) {
        userStoreMapper.deleteByStoreId(id);
        if (userIds != null && !userIds.isEmpty()) {
            for (Long userId : userIds) {
                userStoreMapper.insert(userId, id);
            }
        }
        return ApiResponse.success("店长分配成功", null);
    }
}
