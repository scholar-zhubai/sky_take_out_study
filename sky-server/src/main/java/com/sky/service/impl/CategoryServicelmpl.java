package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServicelmpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新建菜单
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //使用工具类来将DTO对象的属性复制给category对象
        BeanUtils.copyProperties(categoryDTO,category);

        //设置分类的可用性，一般新建会没有所属的菜品，故设置为Disable
        category.setStatus(StatusConstant.DISABLE);

        //设置分类的创建时间和最后更新时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        //设置这个分类的创建人ID和最后更新者的ID
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        //调用Mapper来插入数据库
        categoryMapper.save(category);

    }
}
