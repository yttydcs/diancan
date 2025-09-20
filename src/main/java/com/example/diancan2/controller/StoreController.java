package com.example.diancan2.controller;

import com.example.diancan2.entity.Store;
import com.example.diancan2.service.StoreService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiresPermissions("store:manage")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.list();
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
