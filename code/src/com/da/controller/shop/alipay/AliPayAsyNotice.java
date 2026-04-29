package com.da.controller.shop.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AliPayAsyNotice {

    /**
     * 支付宝异步通知
     */
    @RequestMapping(value = "/notify/ali", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ali(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            System.out.println("name:" + name + "    value:" + valueStr);
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        boolean flag = true; // 校验公钥正确性防止意外
        try {
            flag = AlipaySignature.rsaCheckV1(params, AliPayController.ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //下面代码为业务处理代码，刘洋暂时屏蔽
        /*if (flag) {
            Integer ordersId = Integer.parseInt(params.get("out_trade_no"));
            String tradeStatus = params.get("trade_status");
            Orders orders = new Orders();
            orders.setId(ordersId);
            orders.setPayWay("2"); // 支付方式为支付宝
            // orders.setOrderState("1"); // 订单状态位已支付
            switch (tradeStatus) // 判断交易结果
            {
                case "TRADE_FINISHED": // 完成
                    orders.setOrderState("1");
                    break;
                case "TRADE_SUCCESS": // 完成
                    orders.setOrderState("1");
                    break;
                case "WAIT_BUYER_PAY": // 待支付
                    orders.setOrderState("0");
                    break;
                case "TRADE_CLOSED": // 交易关闭
                    orders.setOrderState("0");
                    break;
                default:
                    break;
            }
            ordersService.updateByPrimaryKeySelective(orders); // 更新后台交易状态
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        AlipayNotice alipayNotice = new AlipayNotice();
        try // set一堆日期和属性
        {
            alipayNotice.setAppId(params.get("app_id"));
            alipayNotice.setBody(params.get("body"));
            alipayNotice.setBuyerId(params.get("buyer_id"));
            alipayNotice.setBuyerLogonId(params.get("buyer_logon_id"));
            alipayNotice.setBuyerPayAmount(Double.parseDouble(params.get("buyer_pay_amount")));
            alipayNotice.setCharset(params.get("charset"));
            alipayNotice.setFundBillList(params.get("fund_bill_list"));
            alipayNotice.setGmtClose(f.parse(params.get("gmt_close")));
            alipayNotice.setGmtCreate(f.parse(params.get("gmt_create")));
            alipayNotice.setGmtPayment(f.parse(params.get("gmt_payment")));
            alipayNotice.setGmtRefund(f1.parse(params.get("gmt_refund")));
            alipayNotice.setNotifyTime(f.parse(params.get("notify_time")));
        } catch (ParseException e) {
            PayCommonUtil.saveE("C\\logs\\aliParseException", e); // 由于需要在外网测试，所以将错误日志保存一下方便调试
            System.out.println("--------------------------------日期转换异常");
            e.printStackTrace();
        }
        try {
            alipayNotice.setInvoiceAmount(Double.parseDouble(params.get("invoice_amount")));
            alipayNotice.setNotifyId(params.get("notify_id"));
            alipayNotice.setNotifyType(params.get("notify_type"));
            alipayNotice.setOutBizNo(params.get("out_biz_no"));
            alipayNotice.setOutTradeNo(params.get("out_trade_no"));
            alipayNotice.setPassbackParams(params.get("passback_params"));
            alipayNotice.setPointAmount(Double.parseDouble(params.get("point_amount")));
            alipayNotice.setReceiptAmount(Double.parseDouble(params.get("receipt_amount")));
            alipayNotice.setRefundFee(Double.parseDouble(params.get("refund_fee")));
            alipayNotice.setSellerEmail(params.get("seller_email"));
            alipayNotice.setSellerId(params.get("seller_id"));
            alipayNotice.setSign(params.get("sign"));
            alipayNotice.setSignType(params.get("sign_type"));
            alipayNotice.setSubject(params.get("subject"));
            alipayNotice.setTotalAmount(Double.parseDouble(params.get("total_amount")));
            alipayNotice.setTradeNo(params.get("trade_no"));
            alipayNotice.setTradeStatus(params.get("trade_status"));
            alipayNotice.setVersion(params.get("version"));
            alipayNotice.setVoucherDetailList(params.get("voucher_detail_list"));
            alipayNotice.setCreateTime(new Date());
            PayCommonUtil.saveLog("C\\logs\\支付宝实体类.txt", alipayNotice.toString());
            int res = alipayNoticeMapper.insertSelective(alipayNotice); // 保存结果
            PayCommonUtil.saveLog("C\\logs\\支付宝结果.txt", res + "");
            return "success";
        } catch (Exception e) {
            PayCommonUtil.saveE("C\\logs\\支付宝异常了.txt", e);
        }*/
        System.out.println("----------------------------执行到了最后！！！--------------");
        return "success";
    }
}
