package com.imooc.mall.controller;

import com.imooc.mall.service.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequestMapping("/pay")
@Slf4j
public class Create {

    /**
     * 微信支付是返回二维码图片
     * 支付宝是返回url
     */

    @Resource
    PayService payService;

    @GetMapping("/create")
    public HashMap createWx(@RequestParam Long orderId, @RequestParam BigDecimal amout , @RequestParam BestPayTypeEnum bestPayTypeEnum) throws Exception {
        //http://localhost:8082/pay/create?orderId=123565632&amout=0.01&bestPayTypeEnum=WXPAY_NATIVE
        //http://localhost:8082/pay/create?orderId=123565632&amout=0.01&bestPayTypeEnum=ALIPAY_PC
        PayResponse payResponse = payService.create(orderId, amout , bestPayTypeEnum);
//        微信的返回值
//        PayResponse(prePayParams=null, payUri=null, appId=wxd898fcb01713c658, timeStamp=1599203287, nonceStr=Fbm3BJxNx95lAYbK, packAge=prepay_id=wx041509208772563446313d65c60ee10000, signType=MD5, paySign=9A28EECFF21CC0E2FE7B93D45B813D52, orderAmount=null, orderId=null, outTradeNo=null, mwebUrl=null, body=null, codeUrl=weixin://wxpay/bizpayurl?pr=LdCxENe00, attach=null, payPlatformEnum=null)

//        支付宝的返回值  返回页面
//        PayResponse(prePayParams=null, payUri=null, appId=null, timeStamp=null, nonceStr=null, packAge=null, signType=null, paySign=null, orderAmount=null, orderId=null, outTradeNo=null, mwebUrl=null, body=<form id='bestPayForm' name="punchout_form" method="post" action="https://openapi.alipay.com//gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=Ktucl%2FXEXSQGhJj7bMmHFqSrJPfpihQC4mi8FYhbSyySztDcYfbdt7LJtWThrhnEDIwSseq3HNUVrm8m0vIGMdK9uLNoqdTNVR807ckuM7Fl48Z85Q18C40R2Xsid1CCUCrpOfLLrgMczqDlVB8s8d4hWFBRz9%2BW%2Fv2RhjweqwD91cIveeiQaPOI6l0%2F%2Ft1ybdKpv7Q2RU7r6gQyjJGmwTK%2ByXX75FXkGXWFXfrzWmpZ%2BdB3uPIq%2FqKhnM%2FWXRe%2B6j4FxO7cf0Y8ngHrRd33KOKFw1kNV%2F8yA8ELsin7yePsUSqr6bkX4LZeg%2Bfpn67a%2BpjEzhVr22HA7ztI8qnTgw%3D%3D&return_url=http%3A%2F%2F127.0.0.1&notify_url=http%3A%2F%2F33r9o84153.zicp.vip%2Fpay%2FnotifyAli&app_id=2018062960540016&sign_type=RSA2&version=1.0&timestamp=2020-09-04+15%3A10%3A20">
//        <input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;12356875632&quot;,&quot;total_amount&quot;:&quot;0.01&quot;,&quot;subject&quot;:&quot;4559066-最好的支付sdk&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
//        <input type="submit" value="立即支付" style="display:none" >
//        </form>
//        <script>document.getElementById('bestPayForm').submit();</script>, codeUrl=null, attach=null, payPlatformEnum=null)
//        //这里要判断是微信还是支付宝
        HashMap hashMap = new HashMap();
        hashMap.put("codeUrl",payResponse.getCodeUrl());
        return hashMap;
    }

    /**
     * vx在支付后台会成功或者失败发送来的数据
     * @param notifyData
     */
    @PostMapping("/notify")
    public void asyncNotify(@RequestBody String notifyData){
        /**
         * 校验签名
         */
        String s = payService.asyncNotify(notifyData);


        /**
         * 金额验证，去数据库查
         */
    }

}
