package com.da.controller.system.appuser;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.da.entity.system.User;
import com.da.service.system.appuser.AppuserManager;
import com.da.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.system.appuser.AppUserWithdrawManager;

/** 
 * 说明：提现记录
 * 创建人：DA
 * 创建时间：2018-10-12
 */
@Controller
@RequestMapping(value="/appuserwithdraw")
public class AppUserWithdrawController extends BaseController {
	
	String menuUrl = "appuserwithdraw/list.do"; //菜单地址(权限用)
	@Resource(name="appuserwithdrawService")
	private AppUserWithdrawManager appuserwithdrawService;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;

	private Gson gson = new GsonBuilder().serializeNulls().create();

	/**
	 * app获取佣金
	 *
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/withdrawSave", produces = "application/json;charset=UTF-8")
	public String withdrawSave() throws Exception {
		int result = 1;
		String message = "提现成功";
		PageData pd = this.getPageData();
		String union_id = pd.getString("UNION_ID");//获取UNION_ID
		String user_id = appuserService.getUserIDByUnionID(union_id);

		double WITHDRAWTOTAL = Double.parseDouble(pd.getString("WITHDRAWTOTAL"));//提现金额
		if (StringUtil.isEmpty(user_id)) {
			result = 0;
			message = "提现失败，用户提现用户数据找不到!";
		}else if(WITHDRAWTOTAL<=0){
			result = 0;
			message = "提现失败，提现金额应大于0!";
		}else{
			pd.put("USERID",user_id);
			pd.put("WITHDRAWDATE",new Date());
			pd.put("APPUSERWITHDRAW_ID",this.get32UUID());
			pd.put("CHECKSTATUS",0);
			appuserwithdrawService.save(pd);
		}
		pd.put("RESULT", result);
		pd.put("MESSAGE", message);
		return gson.toJson(pd);
	}
	/**
	 * APP 获得提现列表
	 * 参数 USERID
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String applist() throws Exception {
		int result = 1;
		String message = "提现成功";
		Page page = new Page();
		PageData pd = this.getPageData();
		String union_id = pd.getString("UNION_ID");//获取UNION_ID
		String user_id = appuserService.getUserIDByUnionID(union_id);
		if (StringUtil.isEmpty(user_id)) {
			result = 0;
			message = "提现失败，用户提现用户数据找不到!";
			pd.put("RESULT", result);
			pd.put("MESSAGE", message);
			return gson.toJson(pd);
		}
		pd.put("USERID",user_id);
		page.setPd(pd);
		if (StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
			page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
			page.setShowCount(20);
		}
		List<PageData> varList = appuserwithdrawService.list(page); // 列出Goods列表
		return gson.toJson(varList);
	}

	/**
	 * APP 获得下线订单列表
	 * 参数 USERID
	 */
	@ResponseBody
	@RequestMapping(value = "/app/getSubUserOrderList", produces = "application/json;charset=UTF-8")
	public String getSubUserOrderList() throws Exception {
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
		List<PageData> varList = appuserwithdrawService.SubUserOrderlist(page); // 列出Goods列表
		return gson.toJson(varList);
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增AppUserWithdraw");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APPUSERWITHDRAW_ID", this.get32UUID());	//主键
		appuserwithdrawService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除AppUserWithdraw");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		appuserwithdrawService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改AppUserWithdraw");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		pd.put("CHECKUSERID",user.getUSERNAME());
		pd.put("CHECKDATE",new Date());
		appuserwithdrawService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表AppUserWithdraw");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = appuserwithdrawService.list(page);	//列出AppUserWithdraw列表
		logger.info("----------"+varList.size());
		mv.setViewName("system/appuser/appuserwithdraw_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/appuser/appuserwithdraw_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = appuserwithdrawService.findById(pd);	//根据ID读取
		mv.setViewName("system/appuser/appuserwithdraw_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除AppUserWithdraw");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			appuserwithdrawService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出AppUserWithdraw到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户ID");	//1
		titles.add("提现金额");	//2
		titles.add("提现时间");	//3
		titles.add("审核状态");	//4
		titles.add("审核用户");	//5
		titles.add("审核时间");	//6
		dataMap.put("titles", titles);
		List<PageData> varOList = appuserwithdrawService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USERID"));	    //1
			vpd.put("var2", varOList.get(i).get("WITHDRAWTOTAL").toString());	//2
			vpd.put("var3", varOList.get(i).getString("WITHDRAWDATE"));	    //3
			vpd.put("var4", varOList.get(i).getString("CHECKSTATUS"));	    //4
			vpd.put("var5", varOList.get(i).getString("CHECKUSERID"));	    //5
			vpd.put("var6", varOList.get(i).getString("CHECKDATE"));	    //6
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
