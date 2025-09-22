package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Food;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.FoodService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserStoreMapper userStoreMapper;

    // 获取所有菜品
    @GetMapping
    public List<Food> getAllFoods() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return foodService.list();
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return Collections.emptyList();
            }
            return foodService.list(new QueryWrapper<Food>().in("store_id", storeIds));
        }
    }

    // 根据ID获取菜品
    @GetMapping("/{id}")
    public Food getFoodById(@PathVariable Long id) {
        return foodService.getById(id);
    }

    // 新增菜品
    @PostMapping
    public boolean addFood(@RequestBody Food food) {
        return foodService.save(food);
    }

    // 更新菜品信息
    @PutMapping
    public boolean updateFood(@RequestBody Food food) {
        return foodService.updateById(food);
    }

    // 删除菜品
    @DeleteMapping("/{id}")
    public boolean deleteFood(@PathVariable Long id) {
        return foodService.removeById(id);
    }
}
