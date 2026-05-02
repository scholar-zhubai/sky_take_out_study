package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Category;

import java.util.List;

public interface DishService {
    /**
     * 新建菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

}
