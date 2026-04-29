package com.da.controller.system.appuser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.da.controller.weixin.pay.util.RandomUtil;
import com.da.service.weixin.key.KeyManager;
import com.da.util.*;
import com.da.util.weixin.GetInfo;
import com.da.util.weixin.Weixin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.deploy.net.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.entity.system.Role;
import com.da.service.system.appuser.AppuserManager;
import com.da.service.system.role.RoleManager;

/**
 * 类名称：会员管理
 * 创建人：DA
 * 修改时间：2014年11月17日
 */
@Controller
@RequestMapping(value = "/happuser")
public class AppuserController extends BaseController {

    String menuUrl = "happuser/listUsers.do"; //菜单地址(权限用)
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "roleService")
    private RoleManager roleService;

    @Value("${wx.appid}")
    private String APP_ID;

    @Value("${wx.appsecret}")
    private String APP_SECRET;

    @Value("${wx.mchid}")
    private String MCH_ID;

    @Value("${wx.appid_gzh}")
    private String APP_ID_GZH;

    @Value("${wx.appsecret_gzh}")
    private String APP_SECRET_GZH;

    private Gson gson = new GsonBuilder().serializeNulls().create();


    /**
     * app判断用户是否是代理
     */
    @ResponseBody
    @RequestMapping(value = "/app/getUserProxyState", produces = "application/json;charset=UTF-8")
    public String getUserProxyState() throws Exception{
        PageData pd = this.getPageData();
        Map<String, String> map = new HashMap<String, String>();
        String proxyState="0";
        String union_id = pd.getString("UNION_ID");//获取UNION_ID
        pd.put("USERNAME",union_id);
        pd=appuserService.findByUsername(pd);
        if(pd!=null){
            proxyState = pd.getString("PROXYSTATE");
            if("1".equals(proxyState)){
                proxyState="1";
            }else {
                proxyState="0";
            }
        }
        map.put("PROXYSTATE",proxyState);
        return gson.toJson(map);
    }

    /**
     * app获取佣金
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/getUserCommission", produces = "application/json;charset=UTF-8")
    public String getUserCommission() throws Exception {
        int result = 1;
        String message = "获得成功";
        PageData pd = this.getPageData();
        PageData appUser;
        String union_id = pd.getString("UNION_ID");//获取UNION_ID
        String user_id = appuserService.getUserIDByUnionID(union_id);
        logger.info("-------------union_id+user_id------------:"+union_id+"      "+user_id);
        if (StringUtil.isEmpty(user_id)) {
            result = 0;
            message = "用户ID 为空!";
        } else {
            pd.put("USER_ID", user_id);
            appUser = appuserService.findByUiId(pd);
            pd = appuserService.getUserCommission(pd);
            pd.put("SFID",appUser.getString("SFID"));
            pd.put("NAME",appUser.getString("NAME"));
        }
        pd.put("RESULT", result);
        pd.put("MESSAGE", message);
        return gson.toJson(pd);
    }

    /**
     * app获取用户下线
     * 参数 USER_ID
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/getSubUser", produces = "application/json;charset=UTF-8")
    public String getSubUser() throws Exception {
        Page page = new Page();
        PageData pd = this.getPageData();
        String union_id = pd.getString("UNION_ID");//获取UNION_ID
        String user_id = appuserService.getUserIDByUnionID(union_id);
        if (StringUtil.isEmpty(user_id)) {
            pd.put("RESULT", 0);
            pd.put("MESSAGE", "找不到公众号关联的用户ID");
            return gson.toJson(pd);
        }
        pd.put("USER_ID",user_id);
        String innerCode = appuserService.getAppUserInnerCode(user_id);
        logger.info("-------------INNER_CODE------------:"+innerCode);
        if(StringUtil.isNotEmpty(innerCode)){
            pd.put("INNER_CODE",innerCode);
        }else{
            pd.put("INNER_CODE",user_id);
        }
        logger.info("-------------user_id------------:"+user_id);

        page.setPd(pd);
        if (StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
            page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
            page.setShowCount(20);
        }
        List<PageData> varList = appuserService.getSubUserList(page);
        return gson.toJson(varList);
    }

    /**
     * ztree列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/treeList")
    public ModelAndView treeList(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表AppUser");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                    //检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        String USER_ID = null == pd.get("USER_ID") ? "" : pd.get("USER_ID").toString();
        if (null != pd.get("id") && !"".equals(pd.get("id").toString())) {
            USER_ID = pd.get("id").toString();
        }
        logger.info("---------------------------0User_ID:"+USER_ID);
        pd.put("USER_ID", USER_ID);
        page.setPd(pd);
        List<PageData> userList = appuserService.getUserFenxiao(page);    //列出AppUser列表
        mv.addObject("pd", appuserService.getUserCommission(pd));    //传入上级所有信息
        mv.addObject("USER_ID", USER_ID);
        mv.setViewName("system/appuser/appuser_ztree_list");
        pd.put("ROLE_ID", "2");
        List<Role> roleList = roleService.listAllRolesByPId(pd);    //列出会员组角色
        mv.addObject("userList", userList);
        mv.addObject("roleList", roleList);
        mv.addObject("QX", Jurisdiction.getHC());                    //按钮权限*/
        return mv;
    }

    /**
     * 显示列表ztree
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/listTreeUsers")
    public ModelAndView listTreeUsers(Model model, String USER_ID) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            JSONArray arr = JSONArray.fromObject(appuserService.listTreeUser("0"));
            String json = arr.toString();
            json = json.replaceAll("USER_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subUser", "nodes").replaceAll("hasUser", "checked").replaceAll("treeurl", "url");
            model.addAttribute("zTreeNodes", json);
            mv.addObject("USER_ID", USER_ID);
            mv.addObject("pd", pd);
            mv.setViewName("system/appuser/appuser_ztree");
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 显示用户列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/listUsers")
    public ModelAndView listUsers(Page page) {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keywords = pd.getString("keywords");                            //检索条件 关键词
            if (null != keywords && !"".equals(keywords)) {
                pd.put("keywords", keywords.trim());
            }
            page.setPd(pd);
            List<PageData> userList = appuserService.listPdPageUser(page);        //列出会员列表
            pd.put("ROLE_ID", "2");
            List<Role> roleList = roleService.listAllRolesByPId(pd);            //列出会员组角色
            mv.setViewName("system/appuser/appuser_list");
            mv.addObject("userList", userList);
            mv.addObject("roleList", roleList);
            mv.addObject("pd", pd);
            mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 显示用户列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "/goBox")
    public ModelAndView goBox(Page page) {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keywords = pd.getString("keywords");                            //检索条件 关键词
            if (null != keywords && !"".equals(keywords)) {
                pd.put("keywords", keywords.trim());
            }
            page.setPd(pd);
            List<PageData> userList = appuserService.listPdPageUser(page);        //列出会员列表
            pd.put("ROLE_ID", "2");
            List<Role> roleList = roleService.listAllRolesByPId(pd);            //列出会员组角色
            mv.setViewName("system/appuser/appuser_box");
            mv.addObject("userList", userList);
            mv.addObject("roleList", roleList);
            mv.addObject("pd", pd);
            mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 去新增用户页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/goAddU")
    public ModelAndView goAddU() throws Exception {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ROLE_ID", "2");
        List<Role> roleList = roleService.listAllRolesByPId(pd);            //列出会员组角色
        mv.setViewName("system/appuser/appuser_edit");
        mv.addObject("msg", "saveU");
        mv.addObject("pd", pd);
        mv.addObject("roleList", roleList);
        return mv;
    }

    /**
     * 保存用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveU")
    public ModelAndView saveU() throws Exception {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        logBefore(logger, Jurisdiction.getUsername() + "新增会员");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("USER_ID", this.get32UUID());    //ID
        pd.put("RIGHTS", "");
        pd.put("LAST_LOGIN", "");                //最后登录时间
        pd.put("IP", "");                        //IP
        pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
        //初始化内码
        if (StringUtils.isNotEmpty(pd.getString("PARENT_ID"))) {
            String innerCode = appuserService.getAppUserInnerCode(pd.getString("PARENT_ID"));
            pd.put("INNER_CODE", innerCode.trim() + "" + pd.getString("USER_ID"));
        } else {
            pd.put("INNER_CODE", pd.getString("USER_ID"));
        }
        if (null == appuserService.findByUsername(pd)) {
            appuserService.saveU(pd);            //判断新增权限
            mv.addObject("msg", "success");
        } else {
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 判断用户名是否存在
     *
     * @return
     */
    @RequestMapping(value = "/hasU")
    @ResponseBody
    public Object hasU() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (appuserService.findByUsername(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 判断邮箱是否存在
     *
     * @return
     */
    @RequestMapping(value = "/hasE")
    @ResponseBody
    public Object hasE() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (appuserService.findByEmail(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 判断编码是否存在
     *
     * @return
     */
    @RequestMapping(value = "/hasN")
    @ResponseBody
    public Object hasN() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (appuserService.findByNumber(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 删除用户
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/deleteU")
    public void deleteU(PrintWriter out) throws Exception {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        logBefore(logger, Jurisdiction.getUsername() + "删除会员");
        PageData pd = new PageData();
        pd = this.getPageData();
        appuserService.deleteU(pd);
        out.write("success");
        out.close();
    }

    /**
     * 修改用户
     *
     * @param out
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editU")
    public ModelAndView editU(PrintWriter out) throws Exception {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        logBefore(logger, Jurisdiction.getUsername() + "修改会员");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (pd.getString("PASSWORD") != null && !"".equals(pd.getString("PASSWORD"))) {
            pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
        }
        /*//初始化内码(测试使用)
        if(StringUtils.isNotEmpty(pd.getString("PARENT_ID"))){
			String innerCode = appuserService.getAppUserInnerCode(pd.getString("PARENT_ID"));
			pd.put("INNER_CODE", innerCode+""+pd.getString("USER_ID"));
		}*/
        appuserService.editU(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 去修改用户页面
     *
     * @return
     */
    @RequestMapping(value = "/goEditU")
    public ModelAndView goEditU() {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd.put("ROLE_ID", "2");
            List<Role> roleList = roleService.listAllRolesByPId(pd);//列出会员组角色
            pd = appuserService.findByUiId(pd);                        //根据ID读取
            mv.setViewName("system/appuser/appuser_edit");
            mv.addObject("msg", "editU");
            mv.addObject("pd", pd);
            mv.addObject("roleList", roleList);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 批量删除
     *
     * @return
     */
    @RequestMapping(value = "/deleteAllU")
    @ResponseBody
    public Object deleteAllU() {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
        } //校验权限
        logBefore(logger, Jurisdiction.getUsername() + "批量删除会员");
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();
            String USER_IDS = pd.getString("USER_IDS");
            if (null != USER_IDS && !"".equals(USER_IDS)) {
                String ArrayUSER_IDS[] = USER_IDS.split(",");
                appuserService.deleteAllU(ArrayUSER_IDS);
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }
            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        } finally {
            logAfter(logger);
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 导出会员信息到excel
     *
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
        logBefore(logger, Jurisdiction.getUsername() + "导出会员资料");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if (Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
                String keywords = pd.getString("keywords");
                if (null != keywords && !"".equals(keywords)) {
                    pd.put("keywords", keywords.trim());
                }
                String lastLoginStart = pd.getString("lastLoginStart");
                String lastLoginEnd = pd.getString("lastLoginEnd");
                if (lastLoginStart != null && !"".equals(lastLoginStart)) {
                    pd.put("lastLoginStart", lastLoginStart + " 00:00:00");
                }
                if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
                    pd.put("lastLoginEnd", lastLoginEnd + " 00:00:00");
                }
                Map<String, Object> dataMap = new HashMap<String, Object>();
                List<String> titles = new ArrayList<String>();
                titles.add("用户名");        //1
                titles.add("编号");        //2
                titles.add("姓名");            //3
                titles.add("手机号");        //4
                titles.add("身份证号");        //5
                titles.add("等级");            //6
                titles.add("邮箱");            //7
                titles.add("最近登录");        //8
                titles.add("到期时间");        //9
                titles.add("上次登录IP");    //10
                dataMap.put("titles", titles);
                List<PageData> userList = appuserService.listAllUser(pd);
                List<PageData> varList = new ArrayList<PageData>();
                for (int i = 0; i < userList.size(); i++) {
                    PageData vpd = new PageData();
                    vpd.put("var1", userList.get(i).getString("USERNAME"));        //1
                    vpd.put("var2", userList.get(i).getString("NUMBER"));        //2
                    vpd.put("var3", userList.get(i).getString("NAME"));            //3
                    vpd.put("var4", userList.get(i).getString("PHONE"));        //4
                    vpd.put("var5", userList.get(i).getString("SFID"));            //5
                    vpd.put("var6", userList.get(i).getString("ROLE_NAME"));    //6
                    vpd.put("var7", userList.get(i).getString("EMAIL"));        //7
                    vpd.put("var8", userList.get(i).getString("LAST_LOGIN"));    //8
                    vpd.put("var9", userList.get(i).getString("END_TIME"));        //9
                    vpd.put("var10", userList.get(i).getString("IP"));            //10
                    varList.add(vpd);
                }
                dataMap.put("varList", varList);
                ObjectExcelView erv = new ObjectExcelView();
                mv = new ModelAndView(erv, dataMap);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 用户注册
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/regist", produces = "application/json;charset=UTF-8")
    public String regist() throws Exception {
        logger.info("------------------开始注册会员");
        Map<String, Object> map = new HashMap<String, Object>();
        int result = 0;
        String message = "";
        PageData pd = this.getPageData();
        String password = pd.getString("PASSWORD");
        pd.put("USERNAME", pd.getString("PHONE"));
        pd.put("PASSWORD", null);
        PageData isAppUser = appuserService.findByUsername(pd);
        pd.put("PASSWORD", password);
        if (isAppUser == null) {
            pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
            pd.put("PHONE", pd.getString("USERNAME"));
            pd.put("START_TIME", DatetimeUtil.getDatetime());
            pd.put("SFID", "static/upload/headimg.jpg");  //头像
            pd.put("USER_ID", this.get32UUID());
            pd.put("STATUS", 1);
            pd.put("ROLE_ID", "1b67fc82ce89457a8347ae53e43a347e"); //注册默认为初级会员
            pd.put("NAME", "TQ-" + RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 12));
            //初始化内码
            if (StringUtils.isNotEmpty(pd.getString("PARENT_ID"))) {
                String innerCode = appuserService.getAppUserInnerCode(pd.getString("PARENT_ID"));

                pd.put("INNER_CODE", innerCode + "" + pd.getString("USER_ID"));
            } else {
                pd.put("INNER_CODE", pd.getString("USER_ID"));
            }
            appuserService.saveU(pd);
            result = 1;
            message = "注册成功！";
        } else {
            result = 0;
            message = "用户已存在，请重新输入！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * 忘记密码
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/forget", produces = "application/json;charset=UTF-8")
    public String forget() throws Exception {
        logger.info("------------------忘记密码");
        Map<String, Object> map = new HashMap<String, Object>();
        int result = 0;
        String message = "";
        PageData pd = this.getPageData();
        String password = pd.getString("PASSWORD");
        pd.put("USERNAME", pd.getString("PHONE"));
        pd.put("PASSWORD", null);
        pd = appuserService.findByUsername(pd);
        if (pd != null) {
            pd.put("PASSWORD", MD5.md5(password));
            appuserService.editU(pd);
            result = 1;
            message = "修改密码成功！";
        } else {
            result = 0;
            message = "该手机号尚未注册！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * app登陆验证
     * 0 登录失败 1登录成功
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/login", produces = "application/json;charset=UTF-8")
    public String appLogin(HttpSession session) throws Exception {
        String session_id = StringUtil.get_session_id(this.getRequest());
        PageData pd = this.getPageData();
        String password = pd.getString("PASSWORD");
        pd.put("PASSWORD", MD5.md5(password));
        Map<String, Object> map = new HashMap<>();
        int result = 0;
        String message = "";
        PageData pd1 = this.getPageData();
        try {
            pd1 = appuserService.findByUsername(pd);
            pd = appuserService.getUserByNameAndPwd(pd);    //根据用户名和密码去读取会员信息
            if (pd != null) {
                String STATUS = pd.getString("STATUS");
                if ("1".equals(STATUS)) {
                    result = 1;
                    message = "登录成功！";
                } else {
                    result = 0;
                    message = "对不起，你的账户已被冻结！！！";
                }
            } else if (pd1 == null) {
                result = 0;
                message = "登录账号错误！";
            } else if (pd1 != null && pd == null) {
                result = 0;
                message = "登录密码错误！";
            } else {
                result = 0;
                message = "登录失败！";
            }
        } catch (Exception e) {
            result = 0;
            message = "登录失败!";
        }
        //设置Session信息
        session.setAttribute("appUser", pd);
        StringUtil.add_session(session);
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        map.put("APPUSER", pd);
        return gson.toJson(map);
    }

    /**
     * 第三方登陆统一接口判断
     *
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/otherlogin", produces = "application/json;charset=UTF-8")
    public String otherlogin(HttpSession session) throws Exception {
        String session_id = StringUtil.get_session_id(this.getRequest());
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap<>();
        int result = 1;
        String message = "登陆成功";
        pd.put("USERNAME", pd.getString("OPEN_ID"));
        PageData appUser;
        try {
            appUser = appuserService.findByUsername(pd);
            if (appUser == null) { // 用户不存在
                appUser = new PageData();
                appUser.put("USER_ID", this.get32UUID());//ID
                appUser.put("PASSWORD", MD5.md5("123456"));//默认密码123456
                appUser.put("USERNAME", "TQ-" + RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 14));
                //用户账号
                appUser.put("NAME", pd.getString("NAME"));//昵称
                appUser.put("SFID", pd.getString("SFID"));//头像
                appUser.put("START_TIME", DatetimeUtil.getDatetime());//注册时间
                appUser.put("SEX", pd.getString("SEX"));//性别
                appUser.put("RIGHTS", "");//权限
                appUser.put("STATUS", "1");//用户状态
                appUser.put("ROLE_ID", "1b67fc82ce89457a8347ae53e43a347e"); //注册默认为初级会员
                appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                appUser.put("IP", GetIp.getIp(this.getRequest()));//上次登录IP
                appUser.put("PHONE", "");//返回空手机号
                appUser.put("OPEN_ID", pd.getString("OPEN_ID"));//微信用户唯一标识
                appuserService.saveU(appUser);
            } else {
                appUser.put("IP", appUser.getString("LAST_LOGIN"));//上次登录IP
                appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                appuserService.editU(appUser);
            }
            session.setAttribute("appUser", appUser);
            map.put("APPUSER", appUser);
        } catch (Exception e) {
            result = 0;
            message = "登录失败!";
        }
        StringUtil.add_session(session);
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * app获取个人信息
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
    public String appList() throws Exception {
        PageData pd = this.getPageData();
        if (StringUtil.isEmpty(pd.getString("USERNAME"))) {
            return gson.toJson(new PageData());
        }
        pd = appuserService.findByUsername(pd);
        return gson.toJson(pd);
    }

    /**
     * app修改个人信息
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/save", produces = "application/json;charset=UTF-8")
    public String appSave() throws Exception {
        Map<String, Object> map = new HashMap<>();
        PageData pd = this.getPageData();
        String password = pd.getString("PASSWORD");
        if (StringUtil.isNotEmpty(password)) {
            pd.put("PASSWORD", MD5.md5(password));
        }
        if (StringUtil.isNotEmpty(pd.getString("PHONE"))) {//判断手机号是否存在。
            PageData pd1 = this.getPageData();
            pd1.put("USERNAME", pd.getString("PHONE"));
            PageData user = appuserService.findByUsername(pd1);
            if (user != null) {
                if (!pd.getString("USER_ID").equals(user.getString("USER_ID"))) {
                    map.put("RESULT", 0);
                    map.put("MESSAGE", "手机号已存在，请检查是否输入正确！");
                    return gson.toJson(map);
                }
            }
        }
        int result = 1;
        String message = "修改成功";
        try {
            appuserService.editU(pd);
        } catch (Exception e) {
            result = 0;
            message = "修改失败!";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * app获取个人名下的会员体系
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/innerlist", produces = "application/json;charset=UTF-8")
    public String appTreeList() throws Exception {
        logger.info("------------------------获取会员体系");
        PageData pd = this.getPageData();
        List<PageData> list = appuserService.findByInnerCode(pd);
        return gson.toJson(list);
    }

    /**
     * app通过当前用户ID获取其子级会员订单额，佣金
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/userOrderTotal", produces = "application/json;charset=UTF-8")
    public String appUserOrderTotal() throws Exception {
        Page pg = this.getPage();
        PageData pd = this.getPageData();
        pg.setPd(pd);
        List<PageData> list = appuserService.currentUserPdPageUser(pg);
        double order_total = 0.0D;
        double commision_total = 0.0D;
        for (int i = 0; i < list.size(); i++) {
            PageData vpd = list.get(i);
            order_total += Double.parseDouble(vpd.get("ORDER_TOTAL").toString());
            commision_total += Double.parseDouble(vpd.get("COMMISION_TOTAL").toString());
        }
        pd.put("ORDER_TOTAL", order_total);
        pd.put("COMMISION_TOTAL", commision_total);
        return gson.toJson(pd);
    }

    /**
     * 验证两次输入的密码
     *
     * @return
     */
    public boolean checkPassword() {
        PageData pd = new PageData();
        pd = this.getPageData();
        if (StringUtils.isEmpty(pd.getString("PASSWORD"))) return false;
        if (pd.getString("PASSWORD").equals(pd.getString("PASSWORD1"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * app邀请注册
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/register", produces = "application/json;charset=UTF-8")
    public ModelAndView appUserRegister() {
        logger.info("-----------------会员注册");
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        mv.setViewName("system/appuser/appuser_register");
        mv.addObject("PARENT_ID", pd.getString("PARENT_ID"));
        mv.addObject("pd", pd);
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    /**
     * 微信登陆
     *
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/login", produces = "application/json;charset=UTF-8")
    public String weixinlogin(HttpSession session, HttpServletRequest request) throws Exception {
        PageData pd = new PageData();
        //logger.info("-----------:" + request.getParameter("userInfo") + "--code--" + request.getParameter("code"));
        String session_id = StringUtil.get_session_id(this.getRequest());
        Map<String, Object> map = new HashMap<String, Object>();
        int result = 0;
        PageData appUser = new PageData();
        Map<String, Object> shop_user_map = StringUtil.appUser(this.getRequest());
        if (shop_user_map == null) {
            String code = request.getParameter("code");
            String grant_type = "authorization_code";// 授权（必填）
            String requestUrl = Weixin.mini_token_url.replace("APPID", APP_ID).
                    replace("SECRET", APP_SECRET).
                    replace("JSCODE", code).
                    replace("authorization_code", grant_type);
            JSONObject json = Weixin.httpRequst(requestUrl, "GET", null);
            //logger.info("---json--:" + json);
            String session_key = String.valueOf(json.get("session_key"));// 获取会话密钥（session_key）
            String openid = String.valueOf(json.get("openid"));// 获取微信用户唯一标识（openid）
            String unionid = "";// 获取微信用户唯一标识（unionid）
            if (json.has("unionid")) {
                unionid = String.valueOf(json.get("unionid"));
            }
            pd.put("session_key", session_key);
            pd.put("userInfo", request.getParameter("userInfo"));
            PageData info = GetInfo.userInfo2(pd);
            if (info != null) {
                result = 1;
                pd.put("USERNAME", openid);
                appUser = appuserService.findByUsername(pd);
                logger.info("--------appUser---1---------:"+appUser);
                if(appUser == null){//在确保 使用openID 查询为 null的  情况下，使用unionID 查询是否存在（查询公众号是否已经插入用户信息），存在的话 就使用这个
                    if(StringUtil.isNotEmpty(unionid)){
                        pd.put("USERNAME", unionid);
                        appUser = appuserService.findByUsername(pd);
                        logger.info("--------appUser---2---------:"+appUser);
                    }
                }
                if (appUser == null) { // 用户不存在
                    appUser = new PageData();
                    appUser.put("USER_ID", this.get32UUID());//ID
                    appUser.put("PASSWORD", MD5.md5("123456"));//默认密码123456
                    appUser.put("USERNAME", "TQ-" + RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 12));
                    //用户账号
                    appUser.put("NAME", info.get("nickName"));//用户名称
                    appUser.put("SFID", info.get("avatarUrl"));//头像
                    appUser.put("START_TIME", DatetimeUtil.getDatetime());//注册时间

                    appUser.put("RIGHTS", "");//权限
                    appUser.put("STATUS", "1");//用户状态
                    appUser.put("ROLE_ID", "1b67fc82ce89457a8347ae53e43a347e"); //注册默认为初级会员
                    appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                    appUser.put("IP", GetIp.getIp(this.getRequest()));//上次登录IP

                    appUser.put("OPEN_ID", openid);
                    appUser.put("UNION_ID", unionid);
                    appuserService.saveU(appUser);
                } else {
                    appUser.put("OPEN_ID", openid);  //写回小程序OPEN_ID
                    appUser.put("IP", appUser.getString("LAST_LOGIN"));//上次登录IP
                    appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                    appuserService.editU(appUser);
                }
                session.setAttribute("appUser", appUser);
                map.put("appUser", appUser);
            }
        } else {
            result = 1;
            map.put("appUser", shop_user_map);
            session.setAttribute("appUser", shop_user_map);
        }
        StringUtil.add_session(session);
        map.put("result", result);
        map.put("session_id", session_id);
        return gson.toJson(map);
    }

    /**
     * App头像上传
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/uploadH", produces = "application/json;charset=UTF-8")
    public String appUploadH(@RequestParam(value = "HEADIMG", required = false) MultipartFile image, HttpServletRequest request) throws Exception {
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());

        Map<String, Object> map = new HashMap<>();
        int result = 1;
        String message = "上传成功!";

        //摘要图上传
        String imageDir = "HEAD_SFID", imageName = "";
        if (!image.isEmpty() || image.getSize() > 0) {
            try {
                String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + imageDir;        //文件上传路径
                imageName = FileUpload.fileUp(image, filePath, this.get32UUID());               //执行上传
            } catch (Exception e) {
                result = 0;
                message = "头像上传失败！";
            }
        }
        //封装发文信息
        PageData pd = this.getPageData();
        pd.put("USER_ID", request.getParameter("USER_ID"));
        //头像
        pd.put("SFID", "uploadFiles/uploadImgs/" + imageDir + "/" + imageName);
        try {
            appuserService.editU(pd);
        } catch (Exception e) {
            result = 0;
            message = "上传失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * 微信公众号登陆
     *
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/gzh_login")
    public void weixinGzhLogin(HttpServletResponse response,HttpServletRequest request) throws Exception {
        //用户同意授权，获取code
        String redirect_uri = "https://app.xindongdongqing.com/dashop/happuser/weixin/gzh_login_notify?PARENTUNION_ID="+request.getParameter("PARENTUNION_ID");
        //String redirect_uri = "https://app.xindongdongqing.com/gzh/index.html";
        String requestUrl = Weixin.authorize_url.replace("APPID", APP_ID_GZH).
                replace("SECRET", APP_SECRET_GZH).
                replace("REDIRECT_URI", URLEncoder.encode(redirect_uri)).
                replace("SCOPE", "snsapi_userinfo").//静默方式（snsapi_base）；非静默方式（snsapi_userinfo）
                replace("STATE#wechat_redirect", "1#wechat_redirect");
        response.sendRedirect(requestUrl);
    }

    /**
     * 微信公众号登陆回调地址
     *
     * @param request
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/gzh_login_notify", produces = "application/json;charset=UTF-8")
    public void weixinGzhLoginNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        int result = 1;
        String message = "授权成功!";
        String code = request.getParameter("code");//获取微信服务器授权返回的code值
        String state = request.getParameter("state");//验证是否来自微信重定向的请求
        String unionid = "";// 获取微信用户唯一标识（unionid）
        String PARENTUNION_ID = request.getParameter("PARENTUNION_ID");
        logger.info("-----PARENTUNION_ID:" + PARENTUNION_ID);
        PageData appUser;
        boolean isNotLogin = false ;
        try {
            if ("1".equals(state)) {
                //通过code换取网页授权access_token，openid
                String grant_type = "authorization_code";// 授权（必填）
                String requestUrl = Weixin.token_url.replace("APPID", APP_ID_GZH).
                        replace("SECRET", APP_SECRET_GZH).
                        replace("CODE", code).
                        replace("authorization_code", grant_type);
                JSONObject json = Weixin.httpRequst(requestUrl, "GET", null);
                String access_token = String.valueOf(json.get("access_token"));//网页授权接口调用凭证
                String openid = String.valueOf(json.get("openid"));// 获取微信用户唯一标识（openid）
                //拉获取用户信息(需scope为 snsapi_userinfo)
                if (StringUtil.isNotEmpty(access_token) && StringUtil.isNotEmpty(openid)) {
                    requestUrl = Weixin.userinfo_url.replace("ACCESS_TOKEN", access_token).
                            replace("OPENID", openid);
                    json = Weixin.httpRequst(requestUrl, "GET", null);
                    if (json.has("unionid")) {
                        unionid = String.valueOf(json.get("unionid"));
                    }
                    //设置cookie信息
                    CookieUtil.addCookie(response, "openid", openid);
                    CookieUtil.addCookie(response, "unionid", unionid);
                    PageData pd =new PageData();
                    pd.put("USERNAME",unionid);
                    logger.info("-----unionid1:" + unionid);
                    appUser = appuserService.findByUsername(pd);
                    if (appUser == null) { // 用户不存在
                        isNotLogin = true;
                        logger.info("-----unionid2:" + unionid);
                        appUser = new PageData();
                        appUser.put("USER_ID", this.get32UUID());//ID
                        appUser.put("PASSWORD", MD5.md5("123456"));//默认密码123456
                        appUser.put("USERNAME", "TQ-" + RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 12));
                        //用户账号
                        appUser.put("NAME", json.get("nickname"));//用户名称
                        appUser.put("SFID", json.get("headimgurl"));//头像
                        appUser.put("START_TIME", DatetimeUtil.getDatetime());//注册时间
                        appUser.put("RIGHTS", "");//权限
                        appUser.put("STATUS", "1");//用户状态
                        appUser.put("ROLE_ID", "1b67fc82ce89457a8347ae53e43a347e"); //注册默认为初级会员
                        appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                        appUser.put("IP", GetIp.getIp(this.getRequest()));//上次登录IP
                        //添加上下级关系
                        if(StringUtil.isNotEmpty(PARENTUNION_ID)){
                            String parentUserID=appuserService.getUserIDByUnionID(PARENTUNION_ID);
                            String parentUserInnerCode = appuserService.getAppUserInnerCode(parentUserID);
                            appUser.put("INNER_CODE",parentUserInnerCode+appUser.getString("USER_ID"));
                            appUser.put("PARENT_ID",parentUserID);
                        }else{
                            appUser.put("INNER_CODE",appUser.getString("USER_ID"));
                        }

                        appUser.put("OPEN_ID", openid);
                        appUser.put("UNION_ID", unionid);
                        appuserService.saveU(appUser);
                    } else {
                        logger.info("-----unionid3:" + unionid);
                        appUser.put("IP", appUser.getString("LAST_LOGIN"));//上次登录IP
                        appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
                        appuserService.editU(appUser);
                    }
                }
                /*if(judgeIsFollow(access_token,openid)){
                    System.out.println("已经关注");
                }else {
                    System.out.println("没有关注");
                }*/
            } else {
                map.put("RESULT", 0);
                map.put("MESSAGE", "授权失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("RESULT", 0);
            map.put("MESSAGE", "发生后台异常!");
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        String redirect_uri = "https://app.xindongdongqing.com/gzh/index.html?unionid=" + unionid;
        if(isNotLogin){
            redirect_uri = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU0MTkyNjU5Ng==#wechat_redirect";
        }
        //String redirect_uri = "https://app.xindongdongqing.com/gzh/index.html?unionid=" + unionid;
        try {
            response.sendRedirect(redirect_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/weixin/gzh_check_login", produces = "application/json;charset=UTF-8")
    public String to_checklogin(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        int result = 0;
        String message = "没有登陆!";
        String unionid = request.getParameter("UNION_ID");
        Cookie[] cookie = request.getCookies();
        for (Cookie c : cookie) {
            if ("unionid".equals(c.getName())) {
                if (c.getValue().equals(unionid)) {
                    map.put("RESULT", 1);
                    map.put("MESSAGE", "已经登陆!");
                    map.put("unionid", unionid);
                }
            }
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     *	生成二维码
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/app/createTwoDimensionCode")
    @ResponseBody
    public Object createTwoDimensionCode(){
        Map<String,String> map = new HashMap<String,String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String parent_unionid = pd.getString("PARENT_UNIONID");
        String errInfo = "success", encoderImgId = parent_unionid+".png",encoderImgId_1 = parent_unionid+"_1.png"; //encoderImgId此处二维码的图片名
        String access_token = Weixin.getAccess_token(APP_ID_GZH, APP_SECRET_GZH);
        String encoderContent=createForeverStrTicket(access_token,parent_unionid);
        logger.info("----------encoderContent-----"+encoderContent);
        if(null == encoderContent){
            errInfo = "error";
        }else{
            try {
                String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + encoderImgId;  //存放路径
                TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");//执行生成二维码
                //第二种生二维码图片方法
                String filePath_1 = PathUtil.getClasspath() + Const.FILEPATHIMG + encoderImgId_1;  //存放路径
                String filePath_2 = PathUtil.getClasspath() + "static/images/logo.png";  //获取中间logo 图标
                TestVisualQRCode test = new TestVisualQRCode();
                boolean boo = test.createQRCode(encoderContent, filePath_1, filePath_2);
                logger.info("logo------2wm-----:"+boo);
            } catch (Exception e) {
                errInfo = "error";
            }
        }
        map.put("result", errInfo);						//返回结果
        map.put("encoderImgId", encoderImgId);			//二维码图片名
        map.put("encoderImgId_1", encoderImgId_1);			//二维码图片带logo图
        return AppUtil.returnObject(new PageData(), map);
    }

    public boolean judgeIsFollow(String token,String openid){
        Integer subscribe = null;
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            //System.out.println("JSON字符串："+demoJson);
            subscribe = demoJson.getInt("subscribe");

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1==subscribe?true:false;
    }


    // 永久二维码(字符串)
    private final static String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
    // 创建二维码
    private String create_ticket_path = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    //https://blog.csdn.net/u010486495/article/details/73287470
    /**
     * 创建永久二维码(字符串)
     *
     * @param accessToken
     * @param sceneStr 场景str
     * @return
     */
    public String createForeverStrTicket(String accessToken, String sceneStr){
        TreeMap<String,String> params = new TreeMap<String,String>();
        params.put("access_token", accessToken);
        //output data
        Map<String,String> intMap = new HashMap<String,String>();
        intMap.put("scene_str",sceneStr);
        Map<String,Map<String,String>> mapMap = new HashMap<String,Map<String,String>>();
        mapMap.put("scene", intMap);

        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("action_name", QR_LIMIT_STR_SCENE);
        paramsMap.put("action_info", mapMap);
        String data = new Gson().toJson(paramsMap);
        logger.info("----------:"+data);
        JSONObject json = Weixin.httpRequst(create_ticket_path+"?access_token="+accessToken, "POST", data);
        logger.info("--------------createForeverStrTicket-------------:"+json.toString());
        return json.getString("url");
    }

}
