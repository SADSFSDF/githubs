package com.imooc.mall.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PayService {
    /**
     * 发起创建支付
     */
    PayResponse create(Long orderId, BigDecimal amout , BestPayTypeEnum bestPayTypeEnum) throws Exception;


    /**
     * 验证正常支付
     * @param notifyData
     * @return
     */
     String asyncNotify(String notifyData);
}
