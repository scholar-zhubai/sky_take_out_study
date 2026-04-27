package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServicelmpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新建菜单分类
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

    /**
     * 菜单种类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //1.开启pageHelper开始分页
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        //2.执行Mapper的查询，PageHelper自动拦截后面的SQL
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        //3.从page对象里面取出总数和列表，封装为统一的返回结果
        long total = page.getTotal();
        List<Category> result = page.getResult();

        //4.统一封装结果返回
        return new PageResult(total,result);
    }

    /**
     * 启停员工账号
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);

        categoryMapper.update(category);
    }

    /**
     * 删除菜单分类
     * @param id
     */
    public void deleteByID(long id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    public List<Category> queryByType(Integer type) {
        return categoryMapper.queryByType(type);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        //新建一个category对象，将dto的属性拿过来，并用工具类进行copy
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //更新更改的时间和操作人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        //调用Mapper层的对象来进行数据库层面的更改
        categoryMapper.update(category);
    }
}
