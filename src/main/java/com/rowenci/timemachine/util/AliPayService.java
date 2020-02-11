package com.rowenci.timemachine.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.rowenci.timemachine.entity.VipOrder;
import com.rowenci.timemachine.service.IVipOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/11 19:18
 */
@Slf4j
@Service
public class AliPayService {

    @Resource
    private IVipOrderService iVipOrderService;

    /**
     * 支付服务
     * @param userId
     * @param money
     * @return
     * @throws AlipayApiException
     */
    public String aliPay(String userId, Integer money, Integer vipMode) throws AlipayApiException {

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String order_id = createUUID.create();

        //保存订单信息
        VipOrder vipOrder = new VipOrder();
        vipOrder.setUserId(userId);
        vipOrder.setOrderId(order_id);
        vipOrder.setMoney(money);
        vipOrder.setVipMode(vipMode);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNow = df.format(new Date());
        vipOrder.setTime(dateNow);

        iVipOrderService.save(vipOrder);

        // 构建支付数据信息
        Map<String, String> data = new HashMap<>();
        data.put("subject", "会员充值"); //订单标题
        data.put("out_trade_no", order_id); //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复      //此处模拟订单号为时间
        data.put("total_amount", money.toString()); //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        data.put("product_code", "FAST_INSTANT_TRADE_PAY"); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY


        //构建客户端
        DefaultAlipayClient alipayRsa2Client = new DefaultAlipayClient(
                AliPayConfig.gatewayUrl,
                AliPayConfig.APP_ID,
                AliPayConfig.APP_PRIVATE_KEY,
                "json",
                AliPayConfig.CHARSET,
                AliPayConfig.ALIPAY_PUBLIC_KEY,
                AliPayConfig.sign_type);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 网页支付
        request.setNotifyUrl(AliPayConfig.notify_url);
        request.setReturnUrl(AliPayConfig.return_url);
        request.setBizContent(JSON.toJSONString(data));
        log.info(JSON.toJSONString(data));
        String response = alipayRsa2Client.pageExecute(request).getBody();
        log.info(response);
        return response;
    }

}
