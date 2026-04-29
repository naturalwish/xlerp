package com.da.controller.system.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.da.util.*;
import com.da.util.express.GetExpressMsg;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;

/**
 * 类名称：ToolController 系统工具
 * 创建人：DA
 * 修改时间：2015年11月23日
 */
@Controller
@RequestMapping(value = "/tool")
public class ToolController extends BaseController {

    private Gson gson = new Gson();
    /**
     * 去接口测试页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/interfaceTest")
    public ModelAndView goInterfaceTest() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("system/tools/interfaceTest");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 接口内部请求
     *
     * @param
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    @RequestMapping(value = "/severTest")
    @ResponseBody
    public Object severTest() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success", str = "", rTime = "";
        try {
            long startTime = System.currentTimeMillis();                    //请求起始时间_毫秒
            URL url = new URL(pd.getString("serverUrl"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(pd.getString("requestMethod"));        //请求类型  POST or GET
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            long endTime = System.currentTimeMillis();                        //请求结束时间_毫秒
            String temp = "";
            while ((temp = in.readLine()) != null) {
                str = str + temp;
            }
            rTime = String.valueOf(endTime - startTime);
        } catch (Exception e) {
            errInfo = "error";
        }
        map.put("errInfo", errInfo);//状态信息
        map.put("result", str);        //返回结果
        map.put("rTime", rTime);    //服务器请求时间 毫秒
        return AppUtil.returnObject(new PageData(), map);
    }

    /**快递查询页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryExpress")
    public ModelAndView queryExpress() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("system/tools/queryExpress");
        mv.addObject("pd", pd);
        return mv;
    }

    /**查看物流跟踪记录
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/queryWuliujilu")
    @ResponseBody
    public Object queryWuliujilu(){
        PageData pd = new PageData();
        pd = this.getPageData();
        String number = pd.getString("number");
        String jsonStr,msg="ok";
        JSONObject json;
        try {
            jsonStr = GetExpressMsg.get(number);
            json = JSONObject.fromObject(jsonStr);
            pd.put("value", json.get("body"));
        } catch (Exception e) {
            msg="error";
        }
        //System.out.println(jsonStr);
        //System.out.println(json.get("body"));
        pd.put("msg", msg);
        return pd;
    }

    /**手机查看物流跟踪记录
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/queryWL", produces = "application/json;charset=UTF-8")
    public String queryWL(){
        PageData pd = new PageData();
        pd = this.getPageData();
        String number = pd.getString("number");
        String jsonStr,msg="ok";
        JSONObject json;
        try {
            jsonStr = GetExpressMsg.get(number);
            json = JSONObject.fromObject(jsonStr);
            json = JSONObject.fromObject(json.get("body"));
            pd.put("value", json);
        } catch (Exception e) {
            msg="error";
        }
        //System.out.println(jsonStr);
        //System.out.println(json.get("body"));
        pd.put("msg", msg);
        return gson.toJson(pd);
    }

    /**
     * 表单构建页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goFormbuilder")
    public ModelAndView goFormbuilder() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("system/tools/form_builder");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 生成文件并下载（生成的表单构建页面代码放到jsp页面）
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadFormCode")
    public void downloadFormCode(HttpServletResponse response) throws Exception {
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> root = new HashMap<String, Object>();        //创建数据模型
        root.put("htmlCode", pd.getString("htmlCode"));
        DelAllFile.delFolder(PathUtil.getClasspath() + "admin/ftl"); //生成代码前,先清空之前生成的代码
        String filePath = "admin/ftl/code/";                        //存放路径
        String ftlPath = "createCode";                                //ftl路径
        /*生成controller*/
        Freemarker.printFile("newJsp.ftl", root, "newJsp.jsp", filePath, ftlPath);
        FileDownload.fileDownload(response, PathUtil.getClasspath() + "admin/ftl/code/newJsp.jsp", "newJsp.jsp");
    }

    /**二维码页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/goTwoDimensionCode")
    public ModelAndView goTwoDimensionCode() throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("PARENT_ID",this.getRequest().getSession());
        mv.setViewName("system/tools/twoDimensionCode");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     *	生成二维码
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/createTwoDimensionCode")
    @ResponseBody
    public Object createTwoDimensionCode(){
        Map<String,String> map = new HashMap<String,String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success", encoderImgId = this.get32UUID()+".png"; //encoderImgId此处二维码的图片名
        String encoderContent = pd.getString("encoderContent");				//内容
        if(null == encoderContent){
            errInfo = "error";
        }else{
            try {
                String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + encoderImgId;  //存放路径
                TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");							//执行生成二维码
            } catch (Exception e) {
                errInfo = "error";
            }
        }
        map.put("result", errInfo);						//返回结果
        map.put("encoderImgId", encoderImgId);			//二维码图片名
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     *	解析二维码
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/readTwoDimensionCode")
    @ResponseBody
    public Object readTwoDimensionCode(){
        Map<String,String> map = new HashMap<String,String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success",readContent="";
        String imgId = pd.getString("imgId");//内容
        if(null == imgId){
            errInfo = "error";
        }else{
            try {
                String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + imgId;  //存放路径
                readContent = TwoDimensionCode.decoderQRCode(filePath);//执行读取二维码
            } catch (Exception e) {
                errInfo = "error";
            }
        }
        map.put("result", errInfo);						//返回结果
        map.put("readContent", readContent);			//读取的内容
        return AppUtil.returnObject(new PageData(), map);
    }

}
// 创建人：DA