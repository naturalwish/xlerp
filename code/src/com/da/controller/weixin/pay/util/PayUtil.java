package com.da.controller.weixin.pay.util;

import com.da.util.DatetimeUtil;
import com.da.util.MD5;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2018/7/6.
 */
public class PayUtil {
    private static Log logger = LogFactory.getLog(PayUtil.class);
    /**
     * 生成订单号
     *
     * @return
     */
    public static String getTradeNo() {
        // 自增8位数 00000001
        return "TNO" + DatetimeUtil.getDatetimemslong() + "00000001";
    }

    /**
     * 退款单号
     *
     * @return
     */
    public static String getRefundNo() {
        // 自增8位数 00000001
        return "RNO" + DatetimeUtil.getDatetimemslong() + "00000001";
    }

    /**
     * 退款单号
     *
     * @return
     */
    public static String getTransferNo() {
        // 自增8位数 00000001
        return "TNO" + DatetimeUtil.getDatetimemslong() + "00000001";
    }

    /**
     * 创建支付随机字符串
     *
     * @return
     */
    public static String getNonceStr() {
        return RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 32);
    }

    /**
     * 支付时间戳
     *
     * @return
     */
    public static String payTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getSign(Map<String, String> params, String paternerKey) throws UnsupportedEncodingException {
        return MD5.md5(createSign(params, false) + "&key=" + paternerKey).toUpperCase();
    }

    /**
     * 构造签名
     *
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (key == null || StringUtils.isEmpty(params.get(key))) // 参数为空不参与签名
                continue;
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueStr = "";
            if (null != value) {
                valueStr = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueStr, "UTF-8"));
            } else {
                temp.append(valueStr);
            }
        }
        return temp.toString();
    }

    /**
     * @param characterEncoding 编码格式
     * @param parameters        请求参数
     * @return
     * @Description：sign签名
     */
    public static String createSign(SortedMap<String, String> parameters, String paternerKey, String characterEncoding) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + paternerKey);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * @param params 请求参数
     * @return
     * @Description：将请求参数转换为xml格式的string
     */
    public static String getRequestXml(SortedMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = params.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     * @Description：返回给微信的参数
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    public static Map jsonToMap(JSONObject jsonObject) {
        Map result = new HashMap();
        try {
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = jsonObject.get(key);
                result.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args){
        logger.info("------createSign:"+getNonceStr());
    }
}
