package com.da.controller.system.appuser;

import com.da.controller.base.BaseController;

import com.da.util.PageData;
import com.da.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.util.Random;

/**
 * 类名称：创瑞短信
 * 创建人：DA
 * 修改时间：2018年07月16日
 */
@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController {
    //HTTP请求地址
    private final static String url = "";
    //Key
    private final static String accesskey = "";
    //秘钥
    private final static String accessSecret = "";
    //签名
    private final static String sign = "【动情商城】";
    //短信模板ID(需申请)
    private final String templateId = "";

    private Gson gson = new GsonBuilder().serializeNulls().create();

    //普通短信(注册)
    @ResponseBody
    @RequestMapping(value = "/app/sendsms", produces = "application/json;charset=UTF-8")
    public String sendsms() throws Exception {
        PageData pd = this.getPageData();
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        String content = get4Random();


        NameValuePair[] data = {
                new NameValuePair("accesskey", accesskey),
                new NameValuePair("secret", accessSecret),
                new NameValuePair("sign", sign),
                new NameValuePair("templateId", "8333"),
                new NameValuePair("mobile", pd.getString("PHONE")),
                new NameValuePair("content", URLEncoder.encode(content, "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
        };
        postMethod.setRequestBody(data);

        int statusCode = httpClient.executeMethod(postMethod);
        //System.out.println("statusCode: " + statusCode + ", body: "+ postMethod.getResponseBodyAsString());
        pd.put("STATUSCODE",content);
        //pd.put("STATUSCODE","9999");
        //pd.put("BODY",postMethod.getResponseBodyAsString());
        return gson.toJson(pd);
    }

    /**
     * 产生4位随机数(0000-9999)
     * @return 4位随机数
     */
    public String get4Random(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0" + fourRandom  ;
        }
        return fourRandom;
    }

    /*public static void main(String args[]) throws Exception{
        sendsms();
    }*/
}
