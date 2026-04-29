package com.da.util;

import com.da.listener.MySessionContext;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关方法
 */
public class StringUtil {

    public static String success_message = "提交成功";
    public static String error_message = "提交失败";
    public static String except_message = "服务器异常";
    public static String max_message = "已超出数量";
    public static String had_message = "您已经领取过啦";
    private static Gson gson = new Gson();

    @SuppressWarnings("unchecked")
    public static Map<String, Object> appUser(HttpServletRequest request) { //获取用户信息
        HttpSession session = request.getSession();
        System.out.println("---appUser---session:" + session.getAttribute("appUser"));
        return (PageData) session.getAttribute("appUser");
    }

    public static HttpSession session(HttpServletRequest request) {
        String cookie = request.getHeader("cookie");
        if (StringUtils.isEmpty(cookie)) {
            return null;
        }
        String session_id = cookie.split("=")[1];
        return MySessionContext.getSession(session_id);
    }

    public static void add_session(HttpSession session) {
        MySessionContext.AddSession(session);
    }

    public static void remove_session(HttpServletRequest request) {
        HttpSession session = MySessionContext.getSession(get_session_id(request));
        MySessionContext.DelSession(session);
    }

    public static String get_session_id(HttpServletRequest request) {
        String cookie = request.getHeader("cookie");
        String session_id = "";
        if (cookie == null) {
            System.err.println("...request.getSession()=" + gson.toJson(request.getSession()));
            session_id = request.getSession().getId();
        } else {
            session_id = cookie.split("=")[1];
        }
        return session_id;
    }

    public static void setAppUser(HttpSession session) { // 模拟用户信息
        PageData appUser = new PageData();
        appUser.put("user_id", "dc4c0243eb694bbf9ece88b715460998");
        appUser.put("phone", "13800138000");
        appUser.put("username", "大岸信息");
        appUser.put("openid", "oa-8a0cdWhvD_SFJLp16EM75hzHs");
        appUser.put("head_img", "http://wx.qlogo.cn/mmopen/H2EOvRxc9g1UaDT9FTajN4HS7c6ueZ0ZzUl3uzzQ9HAeibxmKSTiczKreh5SNPjpEMfMOSVbeXm61V0LzTf9SudnDI2pyGuMNd/0");
        appUser.put("cart_count", "5");
        session.setAttribute("appUser", appUser);
    }

    public static String getId() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 将以逗号分隔的字符串转换成字符串数组
     *
     * @param valStr
     * @return String[]
     */
    public static String[] StrList(String valStr) {
        int i = 0;
        String TempStr = valStr;
        String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
        valStr = valStr + ",";
        while (valStr.indexOf(',') > 0) {
            returnStr[i] = valStr.substring(0, valStr.indexOf(','));
            valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

            i++;
        }
        return returnStr;
    }

    /**
     * 获取字符串编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 字符串是否为空，null或空字符串时返回true,其他情况返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "undefined".equals(str) || "null".equals(str) || "".equals(str) || "''".equals(str);
    }

    /**
     * 字符串是否不为空，null或空字符串时返回false,其他情况返回true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static void main(String[] args) {
        List<String> imgList = new ArrayList<String>();    // 存放图片的list
        String searchImgReg = "<(img|IMG)\\b[^>]*\\b(src|SRC|src2|SRC2)\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>";
        String content = "\n" +
                "\n" +
                "<p style=\"text-indent: 30px;\"><img title=\"特种.jpg\" style=\"float: right;\" src=\"/storage/emulated/0/DCIM/Camera/IMG_20180707_110855.jpg\"/><img title=\"特种.jpg\" style=\"float: right;\" src=\"/wz/plugins/ueditor/jsp/upload1/20180606/88851528249879845.jpg\"/><span style=\"font-family: 宋体;\">特种设备安全检验管理系统按照国家对特种设备相关管理制度，对特种设备进行分权限、有组织、有计划、有针对性和目的性的管理，建立特种设备台账，并按照规定定期开展检验工作、日常维保工作和修理工作，保障特种设备的可靠性，杜绝安全事故发生。加强特种设备安全工作，有利于预防特种设备事故，保障人身和财产安全，促进经济社会发展。</span></p><p><br/></p><p><br/></p><p><br/></p><h2 style=\"margin: 16px 0px; text-align: justify; -ms-text-justify: inter-ideograph;\"><img title=\"特种.jpg\" style=\"float: right;\" src=\"/wz/plugins/ueditor/jsp/upload1/20180606/88851528249879845.jpg\"/></h2><p style=\"margin: 16px 0px; text-align: justify; line-height: 1.5em; -ms-text-justify: inter-ideograph;\"><span style=\"font-family: 宋体,SimSun;\"><br/></span></p><p style=\"margin: 16px 0px; text-align: justify; line-height: 1.5em; -ms-text-justify: inter-ideograph;\"><span style=\"font-family: 宋体,SimSun;\"><br/></span></p><p style=\"margin: 16px 0px; text-align: justify; line-height: 1.5em; -ms-text-justify: inter-ideograph;\"><strong><span style=\"font-family: 宋体,SimSun;\"><a name=\"_Toc515896620\" style=\"color: rgb(0, 0, 0); font-family: 宋体,SimSun; font-size: 20px; text-decoration: none;\"><span style=\"font-family: 宋体,SimSun; font-size: 20px;\">系统效果</span></a></span></strong></p><p style=\"margin: 16px 0px; text-align: justify; line-height: 1.5em; -ms-text-justify: inter-ideograph;\"><span style=\"font-family: 宋体,SimSun;\">1.及时有效的安全检查、维护，提高特种设备使用安全性</span></p><p style=\"margin: 16px 0px; text-align: justify; line-height: 1.5em; -ms-text-justify: inter-ideograph;\"><span style=\"font-family: 宋体,SimSun;\"><span style=\"font-family: 宋体,SimSun;\"><span style=\"font-family: Calibri;\">2.</span><span style=\"font-family: 宋体;\">实现特种设备的按期按规维护保养，增强特种设备运行稳定性</span></span></span></p><p style=\"line-height: 1.5em;\"><span style=\"font-family: 宋体,SimSun;\"><span style=\"font-family: Calibri;\">3.</span><span style=\"font-family: 宋体;\">全面掌控特种设备管理信息，提高特种设备管理水平</span></span></p><p>&nbsp;</p>";// 获得content
        Pattern pattern = Pattern.compile(searchImgReg);    // 讲编译的正则表达式对象赋给pattern
        Matcher matcher = pattern.matcher(content);        // 对字符串content执行正则表达式
        while (matcher.find()) {
            String quote = matcher.group(3);
            String imgsrc = (quote == null || quote.trim().length() == 0) ? matcher.group(4).split("\\s+")[0] : matcher.group(4);
            System.out.println("----------imgsrc:" + imgsrc);
            System.out.println("----------imgsrc:" + imgsrc.substring(imgsrc.lastIndexOf("/") + 1));
            imgList.add(imgsrc);
        }
        System.out.println(content.replaceAll("特种.jpg", "11"));
    }

}
