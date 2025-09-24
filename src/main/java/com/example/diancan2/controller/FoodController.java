package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Food;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.FoodService;
import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserStoreMapper userStoreMapper;

    // 公开接口：小程序端按店铺查询
    @GetMapping("/customer")
    public ApiResponse<List<Food>> getFoodsForCustomer(@RequestParam Long storeId) {
        return ApiResponse.success(foodService.list(new QueryWrapper<Food>().eq("store_id", storeId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Food>>> getAllFoods(@RequestParam(required = false) Long storeId) {
        // 若显式传入 storeId，沿用旧行为（兼容性），但推荐改用 /api/food/customer
        if (storeId != null) {
            return ResponseEntity.ok(ApiResponse.success(foodService.list(new QueryWrapper<Food>().eq("store_id", storeId))));
        }

        Subject subject = SecurityUtils.getSubject();
        User currentUser = (subject != null) ? (User) subject.getPrincipal() : null;
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("未登录"));
        }

        if (subject.hasRole("admin")) {
            return ResponseEntity.ok(ApiResponse.success(foodService.list()));
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
            }
            return ResponseEntity.ok(ApiResponse.success(foodService.list(new QueryWrapper<Food>().in("store_id", storeIds))));
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Food> getFoodById(@PathVariable Long id) {
        return ApiResponse.success(foodService.getById(id));
    }

    @PostMapping
    public ApiResponse<Food> addFood(@RequestBody Food food) {
        foodService.save(food);
        return ApiResponse.success("菜品创建成功", food);
    }

    @PutMapping
    public ApiResponse<Food> updateFood(@RequestBody Food food) {
        foodService.updateById(food);
        return ApiResponse.success("菜品更新成功", food);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFood(@PathVariable Long id) {
        foodService.removeById(id);
        return ApiResponse.success("菜品删除成功", null);
    }
}
