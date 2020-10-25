package com.imooc.mall.mapper;

import com.imooc.mall.domain.entity.MallPayInfo;

public interface MallPayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallPayInfo record);

    int insertSelective(MallPayInfo record);

    MallPayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallPayInfo record);

    int updateByPrimaryKey(MallPayInfo record);

    MallPayInfo selectByOrderNo(Long orderId);
}