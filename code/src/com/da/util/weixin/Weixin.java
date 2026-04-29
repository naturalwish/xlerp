package com.da.util.weixin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.da.controller.weixin.pay.util.XMLUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类描述： 微信公众平台工具类
 *
 * @author DA
 *         作者单位：
 *         联系方式：
 *         创建时间：2016年11月1日
 * @version 1.0
 */
public class Weixin {
    private static Log logger = LogFactory.getLog(Weixin.class);
    //全局获取access_token
    /*access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。
    access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致
    上次获取的access_token失效*/
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //关注公众号，用户同意授权，获取code
    public final static String authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    //关注公众号,获取网页授权access_token
    public final static String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //关注公众号,获取用户信息,微信网页授权
    public final static String userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //获取用户基本信息（包括UnionID机制）
    public final static String userinfo_url2 = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //关注公众号,获取关注用户列表
    public final static String userlist_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";

    //关注小程序,获取授权用户信息接口
    public final static String mini_token_url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    /**
     * 获取access_token
     *
     * @param appid
     * @param appsecret
     * @return
     */
    public static String getAccess_token(String appid, String appsecret) {
        try {
            String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
            JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            return "errer";
        }
    }

    /**
     * 获取关注列表
     *
     * @param appid
     * @param appsecret
     */
    public void getGz(String appid, String appsecret) {
        try {
            String access_token = getAccess_token(appid, appsecret);
            String requestUrl = userlist_url.replace("ACCESS_TOKEN", access_token);
            JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
            System.out.println(jsonObject);
            // System.out.println(jsonObject.getString("total")+"============");

			/*
             * PrintWriter pw; try { pw = new PrintWriter( new FileWriter(
			 * "e:/gz.txt" ) ); pw.print(jsonObject.getString("total"));
			 * pw.close(); } catch (IOException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); }
			 * 
			 * out.write("success"); out.close();
			 */
        } catch (Exception e) {

        }
    }

    public static JSONObject httpRequst(String requestUrl, String requetMethod, String outputStr) {
        JSONObject jsonobject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的新人管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
            sslcontext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocktFactory对象
            SSLSocketFactory ssf = sslcontext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requetMethod);
            if ("GET".equalsIgnoreCase(requetMethod)) {
                httpUrlConn.connect();
            }
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            //判断是否是xml结构
            if (XMLUtil.isXML(buffer.toString())) {
                Map<String, String> map = XMLUtil.doXMLParse(buffer.toString());
                jsonobject = JSONObject.fromObject(map);
            } else {
                jsonobject = JSONObject.fromObject(buffer.toString());
            }
            logger.info("============httpRequst:" + buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonobject;
    }
}

class MyX509TrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }
}
