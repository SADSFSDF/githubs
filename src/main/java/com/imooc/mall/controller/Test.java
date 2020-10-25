package com.imooc.mall.controller;

import com.imooc.mall.domain.entity.MallCategory;
import com.imooc.mall.mapper.MallCategoryMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Test {

    @Resource
    private AmqpTemplate amqpTemplate;

    @Resource
    MallCategoryMapper mallCategoryMapper;

    @GetMapping("test1")
    public void test1(){
        MallCategory mallCategory = mallCategoryMapper.selectByPrimaryKey(100001);
        System.out.println(mallCategory);
    }

    @GetMapping("test2")
    public void test2(){
        //发布到队列里面去，首先要新建一个队列
        amqpTemplate.convertAndSend("payNotify","hello");
    }
}
