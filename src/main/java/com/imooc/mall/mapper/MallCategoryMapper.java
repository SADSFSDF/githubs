package com.imooc.mall.mapper;

import com.imooc.mall.domain.entity.MallCategory;

public interface MallCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallCategory record);

    int insertSelective(MallCategory record);

    MallCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallCategory record);

    int updateByPrimaryKey(MallCategory record);
}