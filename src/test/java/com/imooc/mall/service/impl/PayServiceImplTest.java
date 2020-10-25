package com.imooc.mall.service.impl;

import com.imooc.mall.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayServiceImplTest {

    @Resource
    PayService payService;

    @Test
    public void create() {
//        payService.create("1542154621", BigDecimal.valueOf(0.01));
//        <return_msg><![CDATA[OK]]></return_msg>
//        <appid><![CDATA[wxd898fcb01713c658]]></appid>
//        <mch_id><![CDATA[1483469312]]></mch_id>
//        <nonce_str><![CDATA[PM5Er6EAKHlt9B9F]]></nonce_str>
//        <sign><![CDATA[21CBB77E13C2826D1E90F43316236D55]]></sign>
//        <result_code><![CDATA[SUCCESS]]></result_code>
//        <prepay_id><![CDATA[wx0219414820760637fcf4abcabc9df10000]]></prepay_id>
//        <trade_type><![CDATA[NATIVE]]></trade_type>
//        <code_url><![CDATA[weixin://wxpay/bizpayurl?pr=0N7AUyW]]></code_url>
//        </xml>
//        <code_url> 拿去生成二维码 就可以去扫码了
    }
}