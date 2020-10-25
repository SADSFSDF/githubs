package com.imooc.mall.mapper;

import com.imooc.mall.domain.entity.MallPayInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallPayInfoMapperTest {

    @Resource
    MallPayInfoMapper mallPayInfoMapper;

    @Test
    public void selectByOrderNo() {
        MallPayInfo mallPayInfo = mallPayInfoMapper.selectByOrderNo(Long.parseLong("12358556866375632"));
        System.out.println(mallPayInfo);
    }
}