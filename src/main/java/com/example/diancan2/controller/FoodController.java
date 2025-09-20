package com.example.diancan2.controller;

import com.example.diancan2.entity.Food;
import com.example.diancan2.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // 获取所有菜品
    @GetMapping
    public List<Food> getAllFoods() {
        return foodService.list();
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
