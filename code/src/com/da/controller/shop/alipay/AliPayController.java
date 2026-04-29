package com.da.controller.shop.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.da.controller.base.BaseController;
import com.da.service.shop.order.OrderManager;
import com.da.util.DatetimeUtil;
import com.da.util.PageData;
import com.da.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value="/alipay")
public class AliPayController extends BaseController {

    @Resource(name = "orderService")
    private OrderManager orderService;

    //应用私钥
    public static String APP_PRIVATE_KEY = "";
    //支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "";
    //应用公钥
    public static String APP_PUBLIC_KEY = "";
    //应用ID
    public static String APP_ID = "";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";

    private Gson gson = new GsonBuilder().serializeNulls().create();
    /**
     * 加密、加签
     * @throws AlipayApiException
     */
    private static void encryptAndSign() throws AlipayApiException {
        // 参数构建
        String bizContent = "<XML><ToUserId><![CDATA[2088102122494786]]></ToUserId><AppId><![CDATA[2013111100036093]]></AppId><AgreementId><![CDATA[20131111000001895078]]></AgreementId>"
                + "<CreateTime>12334349884</CreateTime>"
                + "<MsgType><![CDATA[image-text]]></MsgType>"
                + "<ArticleCount>1</ArticleCount>"
                + "<Articles>"
                + "<Item>"
                + "<Title><![CDATA[[回复测试加密解密]]></Title>"
                + "<Desc><![CDATA[测试加密解密]]></Desc>"
                + "<Url><![CDATA[http://m.taobao.com]]></Url>"
                + "<ActionName><![CDATA[立即前往]]></ActionName>"
                + "</Item>"
                + "</Articles>" + "<Push><![CDATA[false]]></Push>" + "</XML>";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
        String privateKey = "";
        String responseContent = AlipaySignature.encryptAndSign(bizContent, publicKey, privateKey,
                "UTF-8", true, true);
        System.out.println(responseContent);
    }

    /**
     * 验签&解密
     * @throws AlipayApiException
     */
    private static void checkSignAndDecrypt() throws AlipayApiException {
        // 参数构建
        String biz_content = "<XML><AppId><![CDATA[2013082200024893]]></AppId><FromUserId><![CDATA[2088102122485786]]></FromUserId><CreateTime>1377228401913</CreateTime><MsgType><![CDATA[click]]></MsgType><EventType><![CDATA[event]]></EventType><ActionParam><![CDATA[authentication]]></ActionParam><AgreementId><![CDATA[201308220000000994]]></AgreementId><AccountNo><![CDATA[null]]></AccountNo><UserInfo><![CDATA[{\"logon_id\":\"15858179811\",\"user_name\":\"许旦辉\"}]]></UserInfo></XML>";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
        String privateKey = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("biz_content", AlipaySignature.rsaEncrypt(biz_content, publicKey, "UTF-8"));
        params.put("charset", "UTF-8");
        params.put("service", "alipay.mobile.public.message.notify");
        params.put("sign_type", "RSA");
        params.put("sign", AlipaySignature.rsaSign(params, privateKey, "UTF-8"));

        // 验签&解密
        String resultContent = AlipaySignature.checkSignAndDecrypt(params, publicKey, privateKey,true, true);
        System.out.println(resultContent);
    }
    /**
     * 加签订单并返回(SDK自带验签)
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/alipaybiz", produces = "application/json;charset=UTF-8")
    public String alipaybiz() throws AlipayApiException {
        logger.info("-----------------------调用阿里支付");
        PageData pd = this.getPageData();
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数
        //此次只是参数展示，未进行字符串转义，实际情况下请转义
        String body = pd.getString("ORDER_BODY");                   //支付Body
        int len = body.length()>20?20:body.length();
        String subject = body.substring(0,len)+"...";                   //支付标题
        String out_trade_no = pd.getString("ORDER_ID");       //支付订单号
        String timeout_express = "30m";               //超时时间
        String total_amount = pd.getString("ORDER_TOTAL");                 //支付金额
        String product_code = "QUICK_MSECURITY_PAY";  //固定值
        JSONObject bizJson = new JSONObject();
        bizJson.put("body",body);
        bizJson.put("subject",subject);
        bizJson.put("out_trade_no",out_trade_no);
        bizJson.put("timeout_express",timeout_express);
        bizJson.put("total_amount",total_amount);
        PageData order =  new PageData();
        order.put("order_id",out_trade_no);
        try{
            order = orderService.findById(order);
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("-------------------order_total0:"+order.get("order_total"));
        Object obj = order.get("order_total");
        double order_total = Double.parseDouble(obj.toString());
        logger.info("-------------------order_total:"+order_total);
        if(order_total != 0){
            double chae = Math.abs(Double.valueOf(order_total) - Double.valueOf(total_amount));
            logger.info("------------------差："+chae);
            if(chae > 0.1){
                logger.info("-------------------订单金额异常,请联系管理员！");
                return null;
            }
        }
        bizJson.put("product_code",product_code);
        request.setBizContent(bizJson.toString());
        request.setNotifyUrl(notify_url);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        //调用成功，则处理业务逻辑
        if(response.isSuccess()){
            logger.info("---------------------支付成功！:"+response.getBody());
        }
        return response.getBody();
    }

    /**
     * 加签订单并返回(SDK自带验签)
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/alipay", produces = "application/json;charset=UTF-8")
    public String alipay() throws AlipayApiException {
        PageData pd = this.getPageData();
        logger.info("-----------------------------调用支付接口alipay");
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //支付宝订单模型
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //订单号
        model.setOutTradeNo(DatetimeUtil.getDatetimemslong());
        model.setTimeoutExpress("5m");
        //订单总金额
        model.setTotalAmount("0.01");
        //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        model.setProductCode("QUICK_MSECURITY_PAY");
        //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        model.setBody("对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body");
        //商品的标题、交易标题、订单....
        model.setSubject("商品的标题/交易标题/订单标题/订单关键字等");
        // 这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        //调用成功，则处理业务逻辑
        Map<String,Object> map = new HashMap<>();
        if(response.isSuccess()){
            //返回消息
            String resBody = response.getBody();
            map.put("RESULT",resBody);
        }
        return response.getBody();
    }

    /**
     * 执行回调地址
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/alipaycallback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String alipaycallback(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        PageData pd = this.getPageData();
        logger.info("-----------------------------调用支付回调接口");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = requestParams.entrySet();
        for (Map.Entry<String, String[]> entry : entrySet){
            String name = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            logger.info("-------name:" + name + "    value:" + valueStr);
            // 乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean flag = true; // 校验公钥正确性防止意外
        try
        {
            flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
        } catch (AlipayApiException e)
        {
            e.printStackTrace();
        }
        String tradeStatus = params.get("trade_status");
        String order_id = params.get("out_trade_no");
        logger.info("----------------------flag:"+flag);

        if(flag){
            int status = 1;
            // 判断交易结果
            switch (tradeStatus){
                case "TRADE_FINISHED": // 完成
                    break;
                case "TRADE_SUCCESS": // 完成
                    status = 1;
                    break;
                case "WAIT_BUYER_PAY": // 待支付
                    break;
                case "TRADE_CLOSED": // 交易关闭
                    break;
                default:
                    break;
            }
            try{
                String url = this.getRequest().getLocalAddr();
                int port = this.getRequest().getLocalPort();
                String serverUrl = "http://"+url+":"+port+"/dashop/order/app/editStatus.do?order_id="+order_id+"&status="+status;
                logger.info("---------------------serverUrl:"+serverUrl);
                java.net.URL url2 = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("POST");        //请求类型  POST or GET
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        // 公众号验签&解密
        //checkSignAndDecrypt();
        // 公众号加密&加签
        //encryptAndSign();
        //testPay();
    }
}
