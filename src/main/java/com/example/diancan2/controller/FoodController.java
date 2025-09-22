package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Food;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.FoodService;
import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ApiResponse<List<Food>> getAllFoods() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return ApiResponse.success(foodService.list());
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return ApiResponse.success(Collections.emptyList());
            }
            return ApiResponse.success(foodService.list(new QueryWrapper<Food>().in("store_id", storeIds)));
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
