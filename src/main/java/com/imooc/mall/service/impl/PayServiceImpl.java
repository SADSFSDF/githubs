package com.imooc.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imooc.mall.domain.entity.MallPayInfo;
import com.imooc.mall.mapper.MallPayInfoMapper;
import com.imooc.mall.service.PayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    public static final String QUEUE_PAY_NOTIFY="payNotify";

    @Resource
    BestPayService bestPayService;

    @Resource
    MallPayInfoMapper mallPayInfoMapper;

    @Resource
    AmqpTemplate amqpTemplate;

    @Override
    public PayResponse create(Long orderId, BigDecimal amout ,BestPayTypeEnum bestPayTypeEnum) throws Exception {
        if(bestPayTypeEnum!=BestPayTypeEnum.WXPAY_NATIVE&&bestPayTypeEnum!=BestPayTypeEnum.ALIPAY_PC){
            throw new Exception("暂不支持");
        }
        //发起支付
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderId(orderId.toString()); //订单的id
        payRequest.setOrderName("4559066-最好的支付sdk"); //订单的名称
        payRequest.setOrderAmount(amout.doubleValue()); //支付金额
        payRequest.setPayTypeEnum(bestPayTypeEnum); //支付场景
        PayResponse pay = bestPayService.pay(payRequest);

        MallPayInfo mallPayInfo = new MallPayInfo();
        mallPayInfo.setOrderNo(orderId);
        mallPayInfo.setPayPlatform(1); //支付方式 用枚举
        mallPayInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name()); //支付状态
        mallPayInfo.setPayAmount(amout);

        mallPayInfoMapper.insertSelective(mallPayInfo);
        /**
         * 写入数据库
         */
        log.info("支付完成");
        return pay;

    }

    /**
     *  验证是否是正常的支付，不是黑客
     */
    @Override
    public String asyncNotify(String notifyData){



        //签名验证，看是否正常支付，因为是回调函数所以会执行
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("notifyData:"+notifyData);
//        notifyData的值:
//        <xml><appid><![CDATA[wxd898fcb01713c658]]></appid>
//        <bank_type><![CDATA[OTHERS]]></bank_type>
//        <cash_fee><![CDATA[1]]></cash_fee>
//        <fee_type><![CDATA[CNY]]></fee_type>
//        <is_subscribe><![CDATA[N]]></is_subscribe>
//        <mch_id><![CDATA[1483469312]]></mch_id>
//        <nonce_str><![CDATA[MENd4UoggUqaLOEM]]></nonce_str>
//        <openid><![CDATA[oTgZpwae361onIeqpAAVLJwjL07g]]></openid>
//        <out_trade_no><![CDATA[123565632]]></out_trade_no>
//        <result_code><![CDATA[SUCCESS]]></result_code>
//        <return_code><![CDATA[SUCCESS]]></return_code>
//        <sign><![CDATA[FF293CCBD8A80901BAEF98169608541F]]></sign>
//        <time_end><![CDATA[20200904145039]]></time_end>
//        <total_fee>1</total_fee>
//        <trade_type><![CDATA[NATIVE]]></trade_type>
//        <transaction_id><![CDATA[4200000759202009043121538088]]></transaction_id>
//        </xml>
//        payResponse:的值
        //查数据库金额是否一样
        MallPayInfo mallPayInfo = mallPayInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if(mallPayInfo==null){
            throw new RuntimeException("出现异常");
        }
        //不是已支付
        if(!mallPayInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            if(mallPayInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))!=0){
                //不相等
                throw new RuntimeException("金额不一样");
            }
            //修改数据库支付状态
            mallPayInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            mallPayInfo.setPlatformNumber(payResponse.getOutTradeNo());
            mallPayInfo.setUpdateTime(null);
            mallPayInfoMapper.updateByPrimaryKeySelective(mallPayInfo);
        }


        //发送mq mallPayInfo
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, JSONObject.toJSONString(mallPayInfo));

        //判断支付平台
        if(payResponse.getPayPlatformEnum()== BestPayPlatformEnum.WX){

            //告诉微信不要再通知了
//        <xml>
//          <return_code><![CDATA[SUCCESS]]></return_code>
//          <return_msg><![CDATA[OK]]></return_msg>
//        </xml>
            return "<xml>\n"+
                    " <return_code><![CDATA[SUCCESS]]></return_code>\n"+
                    " <return_msg><![CDATA[OK]]></return_msg>\n"+
                    "</xml>;";
        }else if(payResponse.getPayPlatformEnum()==BestPayPlatformEnum.ALIPAY){
            return "success";
        }
        throw new RuntimeException("错误的支付平台");

    }
}
