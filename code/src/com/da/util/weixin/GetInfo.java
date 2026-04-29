package com.da.util.weixin;

import com.da.util.PageData;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/16.
 */
public class GetInfo {

    private static Logger logger = Logger.getLogger(GetInfo.class);

    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) {
        try {
            // 将解析结果存储在HashMap中
            Map<String, String> map = new HashMap<String, String>();
            // 从request中取得输入流
            InputStream inputStream = request.getInputStream();
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());

            // 释放资源
            inputStream.close();
            inputStream = null;
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public static PageData baseInfo(PageData pd, HttpServletRequest request) {
        String openid = "";
        String code = "";
        // String unionid = "";
        String access_token = "";
        //PageData config = Config.getconfig();
        PageData config = new PageData();
        code = pd.getString("code");// 获取code值
        logger.info("通过确认授权获取信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(pd);
        if (code != null) {
            String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + config.getString("appid")
                    + "&secret=" + config.getString("appsecret") + "&code=" + code + "&grant_type=authorization_code";
            // 获取用户的openid
            JSONObject json = new JSONObject();
            // CommonUtil commonUtil=new CommonUtil();
            json = Weixin.httpRequst(token_url, "GET", null);
            logger.info("通过code获取信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.info(json);
            if (json != null) {
                openid = json.getString("openid");
                access_token = json.getString("access_token");
                // unionid = json.getString("unionid");
            }
        }
        pd.put("openid", openid);
        // pd.put("unionid", unionid);
        pd.put("access_token", access_token);
        logger.info("code==" + code);
        logger.info("WXUTIL 96 --oppen_id==" + openid);
        return pd;
    }

    public static PageData userInfo(PageData pd) {
        PageData user = new PageData();
        String iv = pd.getString("iv");
        String session_key = pd.getString("session_key");
        String encryptedData = pd.getString("encryptedData");
        try {
            String result = Pkcs7Encoder.toString(encryptedData, session_key, iv);
            //String result2 = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                JSONObject json = JSONObject.fromObject(result);
                user.put("openId", json.get("openId"));
                user.put("nickName", json.get("nickName"));
                user.put("gender", json.get("gender"));
                user.put("city", json.get("city"));
                user.put("province", json.get("province"));
                user.put("country", json.get("country"));
                user.put("avatarUrl", json.get("avatarUrl"));
                // user.put("unionId", json.get("unionId"));
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.info("获取用户信息出错 ---> " + e.getMessage());
            user = null;
        }
        return user;
    }

    public static PageData userInfo2(PageData pd) {
        PageData user = new PageData();
        String userInfo = pd.getString("userInfo");
        try {
            if (null != userInfo && userInfo.length() > 0) {
                JSONObject json = JSONObject.fromObject(userInfo);
                user.put("openId", json.get("openId"));
                user.put("nickName", json.get("nickName"));
                user.put("gender", json.get("gender"));
                user.put("city", json.get("city"));
                user.put("province", json.get("province"));
                user.put("country", json.get("country"));
                user.put("avatarUrl", json.get("avatarUrl"));
                // user.put("unionId", json.get("unionId"));
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.info("获取用户信息出错 ---> " + e.getMessage());
            user = null;
        }
        return user;
    }
}
