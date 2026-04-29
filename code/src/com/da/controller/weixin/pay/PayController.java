package com.da.controller.weixin.pay;

import com.da.controller.base.BaseController;
import com.da.controller.weixin.pay.util.PayUtil;
import com.da.controller.weixin.pay.util.XMLUtil;
import com.da.service.shop.order.OrderManager;
import com.da.util.*;
import com.da.util.weixin.Weixin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 类名称：PayController.java
 * 类描述： 微信公共平台开发
 *
 * @author DA
 *         作者单位：
 *         联系方式：
 *         创建时间：2018年7月6日
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/wxpay")
public class PayController extends BaseController {

    private static final String ORDER_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 统一下单
    private static final String ORDER_PAY_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery"; // 支付订单查询
    private static final String ORDER_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund"; // 申请退款
    private static final String ORDER_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery"; // 申请退款

    @Value("${wx.appid}")
    private String APP_ID;

    @Value("${wx.mchid}")
    private String MCH_ID;

    @Value("${wx.api.secret}")
    private String API_SECRET;

    @Value("${wx.appid_app}")
    private String APP_ID_APP;

    @Value("${wx.mchid_app}")
    private String MCH_ID_APP;

    @Value("${wx.api.secret_app}")
    private String API_SECRET_APP;

    @Resource(name = "orderService")
    private OrderManager orderService;

    private Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * 小程序支付下订单
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/pay", produces = "application/json;charset=UTF-8")
    public String pay() throws Exception {
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject requestJson = new JSONObject();
        int result = 1;
        String message = "订单创建成功！";
        logger.info("-----------pd---------:"+pd);
        try {
            SortedMap<String, String> params = new TreeMap<String, String>();
            params.put("appid", APP_ID);//微信支付分配的公众账号ID（企业号corpid即为此appId
            params.put("mch_id", MCH_ID);//微信支付分配的商户号
            params.put("device_info", "WEB");//自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
            params.put("nonce_str", PayUtil.getNonceStr());//随机字符串，长度要求在32位以内。推荐随机数生成算法
            params.put("body", pd.getString("NAME"));//商品描述
            params.put("attach", "DA_SHOP");//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            params.put("out_trade_no", pd.getString("ORDER_ID"));//商户系统内部订单号
            logger.info("--------------------:"+Math.round(Double.parseDouble(pd.getString("ORDER_TOTAL"))*100)+"");
            params.put("total_fee", Math.round(Double.parseDouble(pd.getString("ORDER_TOTAL"))*100)+"");//订单总金额，单位为分
            params.put("spbill_create_ip", GetIp.getIp(this.getRequest()));//用户端实际ip
            params.put("notify_url", "https://app.xindongdongqing.com/dashop/wxpay/weixin/notify.do"); //微信服务器异步通知支付结果地址，下面的/notify 方法
            params.put("trade_type", "JSAPI");//支付类型 app:APP 微信：JSAPI
            params.put("openid", pd.getString("openid"));//oVP8n4_fQCVmimiV11rw-9luc71U

            params.put("sign", PayUtil.createSign(params, API_SECRET, "UTF-8"));
            String requestXML = PayUtil.getRequestXml(params);
            requestJson = Weixin.httpRequst(ORDER_PAY, "POST", requestXML);
            //requestJson:{"return_code":"FAIL","return_msg":"商户号该产品权限未开通，请前往商户平台>产品中心检查后重试"}
            if ("FAIL".equals(requestJson.getString("return_code"))) {
                result = 0;
                message = requestJson.getString("return_msg");
            }
        } catch (Exception e) {
            result = 0;
            message = "统一下单异常！" + e.getMessage();
        }
        if (requestJson.has("result_code")) {
            try {
                SortedMap<String, String> payMap = new TreeMap<String, String>();
                if (!requestJson.isEmpty() && "SUCCESS".equals(requestJson.getString("result_code"))) {
                    payMap.put("appId", APP_ID);
                    payMap.put("timeStamp", PayUtil.payTimestamp());
                    payMap.put("nonceStr", PayUtil.getNonceStr());
                    payMap.put("package", "prepay_id="+requestJson.getString("prepay_id"));
                    payMap.put("signType","MD5");
                    payMap.put("paySign", PayUtil.createSign(payMap, API_SECRET, "UTF-8"));
                    map.put("DATA", payMap);
                } else {
                    result = 0;
                    message = requestJson.getString("err_code_des");
                }
            } catch (Exception e) {
                result = 0;
                message = "订单创建异常！" + e.getMessage();
            }
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * 小程序订单支付微信服务器异步通知
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/notify", produces = "application/xml;charset=UTF-8")
    public String notify(HttpServletRequest request, HttpServletResponse response) {
        String result = "SUCCESS";
        String message = "";
        try {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 获取微信调用我们notify_url的返回信息
            String resultXML = new String(outSteam.toByteArray(), "UTF-8");
            SortedMap<String, String> requestMap = XMLUtil.doXMLParse(resultXML);
            logger.info("支付结果通知：" + requestMap);
            if ("SUCCESS".equals(requestMap.get("return_code"))) {
                // 订单支付成功 业务处理
                String out_trade_no = requestMap.get("out_trade_no"); // 商户订单号
                // 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
                String sign = requestMap.get("sign"); // 返回的签名
                requestMap.remove("sign");
                String nowSign = PayUtil.createSign(requestMap, API_SECRET, "UTF-8");
                if (nowSign.equals(sign)) {
                    // 进行业务处理
                    message = "订单支付通知： 支付成功，订单号" + out_trade_no;

                    PageData pd = new PageData();
                    pd.put("order_id",out_trade_no);//支付时间
                    pd.put("status","1");//修改成代发货
                    pd.put("paymenttime",DatetimeUtil.getDatetime());//支付时间
                    pd.put("record_note", "订单付款");
                    pd.put("pay_way", "wechat");// alipay  wechat  bank
                    pd.put("addtime", DatetimeUtil.getDatetime());
                    pd.put("record_id", this.get32UUID());
                    orderService.edit(pd);
                } else {
                    result = "FAIL";
                    message = "订单支付通知：签名错误";
                }
            } else {
                message = "订单支付通知：支付失败，" + requestMap.get("err_code") + ":" + requestMap.get("err_code_des");
            }
        } catch (Exception e) {
            result = "FAIL";
            message = e.getMessage();
        }
        logger.info("---notify:" + PayUtil.setXML(result, message));
        return PayUtil.setXML(result, message);
    }
    /**
     * app支付下订单
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/app_pay", produces = "application/json;charset=UTF-8")
    public String app_pay() throws Exception {
        logger.info("-----------微信支付下订单");
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject requestJson = new JSONObject();
        int result = 1;
        String message = "订单创建成功！";
        try {
            SortedMap<String, String> params = new TreeMap<String, String>();
            params.put("appid", APP_ID_APP);//微信支付分配的公众账号ID（企业号corpid即为此appId
            params.put("mch_id", MCH_ID_APP);//微信支付分配的商户号
            params.put("nonce_str", PayUtil.getNonceStr());//随机字符串，长度要求在32位以内。推荐随机数生成算法
            params.put("body", pd.getString("NAME"));//商品描述
            params.put("attach", "DA_SHOP");//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            params.put("out_trade_no", pd.getString("ORDER_ID"));//商户系统内部订单号(Math.Round((decimal)pd.getString("ORDER_TOTAL") * 100, 0)).ToString()
            params.put("total_fee", Math.round(Double.parseDouble(pd.getString("ORDER_TOTAL"))*100)+"");//订单总金额，单位为分 pd.get("ORDER_TOTAL")
            String total_fee = pd.getString("ORDER_TOTAL");
            PageData order =  new PageData();
            order.put("order_id",pd.getString("ORDER_ID"));
            try{
                order = orderService.findById(order);
            }catch (Exception e){
                e.printStackTrace();
            }
            logger.info("----------------------order_total:"+00000);
            Object obj = order.get("order_total");
            double order_total = Double.parseDouble(obj.toString());
            logger.info("----------------------order_total:"+order_total);
            if(order_total != 0){
                double chae = Math.abs(Double.valueOf(order_total) - Double.valueOf(total_fee));
                logger.info("------------------差："+chae);
                if(chae > 0.1){
                    logger.info("-------------------订单金额异常,请联系管理员！");
                    return null;
                }
            }
            params.put("spbill_create_ip", GetIp.getIp(this.getRequest()));//用户端实际ip
            params.put("notify_url", "https://app.xindongdongqing.com/dashop/wxpay/weixin/notify_app.do"); //微信服务器异步通知支付结果地址，下面的/notify 方法
            params.put("trade_type", "APP");//支付类型 app:APP 微信：JSAPI
            params.put("sign", PayUtil.createSign(params, API_SECRET_APP, "UTF-8"));
            String requestXML = PayUtil.getRequestXml(params);
            requestJson = Weixin.httpRequst(ORDER_PAY, "POST", requestXML);
            if ("FAIL".equals(requestJson.getString("return_code"))) {
                result = 0;
                message = requestJson.getString("return_msg");
            }
        } catch (Exception e) {
            result = 0;
            message = "统一下单异常！" + e.getMessage();
        }
        if (requestJson.has("result_code")) {
            try {
                SortedMap<String, String> payMap = new TreeMap<String, String>();
                if (!requestJson.isEmpty() && "SUCCESS".equals(requestJson.getString("result_code"))) {
                    payMap.put("appid", APP_ID_APP);
                    payMap.put("partnerid", MCH_ID_APP);
                    payMap.put("prepayid", requestJson.getString("prepay_id"));//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
                    payMap.put("package", "Sign=WXPay");
                    payMap.put("noncestr", PayUtil.getNonceStr());
                    payMap.put("timestamp", PayUtil.payTimestamp());
                    payMap.put("sign", PayUtil.createSign(payMap, API_SECRET_APP, "UTF-8"));
                    map.put("DATA", payMap);
                } else {
                    result = 0;
                    message = requestJson.getString("err_code_des");
                }
            } catch (Exception e) {
                result = 0;
                message = "订单创建异常！" + e.getMessage();
            }
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * app订单支付微信服务器异步通知
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/notify_app", produces = "application/xml;charset=UTF-8")
    public String notify_app(HttpServletRequest request, HttpServletResponse response) {
        logger.info("-----------微信支付异步通知");
        String result = "SUCCESS";
        String message = "";
        try {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 获取微信调用我们notify_url的返回信息
            String resultXML = new String(outSteam.toByteArray(), "UTF-8");
            SortedMap<String, String> requestMap = XMLUtil.doXMLParse(resultXML);
            logger.info("支付结果通知：" + requestMap);
            if ("SUCCESS".equals(requestMap.get("return_code"))) {
                // 订单支付成功 业务处理
                String out_trade_no = requestMap.get("out_trade_no"); // 商户订单号
                // 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
                String sign = requestMap.get("sign"); // 返回的签名
                requestMap.remove("sign");
                String nowSign = PayUtil.createSign(requestMap, API_SECRET_APP, "UTF-8");
                if (nowSign.equals(sign)) {
                    logger.info("-----------微信支付成功");
                    // 进行业务处理
                    message = "订单支付通知： 支付成功，订单号" + out_trade_no;

                    PageData pd = new PageData();
                    pd.put("order_id",out_trade_no);//支付时间
                    pd.put("status","1");//修改成代发货
                    pd.put("paymenttime",DatetimeUtil.getDatetime());//支付时间
                    pd.put("record_note", "订单付款");
                    pd.put("pay_way", "wechat");// alipay  wechat  bank
                    pd.put("addtime", DatetimeUtil.getDatetime());
                    pd.put("record_id", this.get32UUID());
                    orderService.edit(pd);
                } else {
                    result = "FAIL";
                    message = "订单支付通知：签名错误";
                }
            } else {
                message = "订单支付通知：支付失败，" + requestMap.get("err_code") + ":" + requestMap.get("err_code_des");
            }
        } catch (Exception e) {
            result = "FAIL";
            message = e.getMessage();
        }
        logger.info("---notify:" + PayUtil.setXML(result, message));
        return PayUtil.setXML(result, message);
    }
}
