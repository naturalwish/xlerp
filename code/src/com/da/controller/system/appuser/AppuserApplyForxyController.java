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

import com.da.service.system.appuser.AppuserManager;
import com.da.util.*;
import com.da.util.weixin.Weixin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.system.appuser.AppuserApplyForxyManager;

/** 
 * 说明：代理商申请
 * 创建人：DA
 * 创建时间：2018-10-29
 */
@Controller
@RequestMapping(value="/appuserapplyforxy")
public class AppuserApplyForxyController extends BaseController {
	
	String menuUrl = "appuserapplyforxy/list.do"; //菜单地址(权限用)
	@Resource(name="appuserapplyforxyService")
	private AppuserApplyForxyManager appuserapplyforxyService;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;

	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增AppuserApplyForxy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APPUSERAPPLYFORXY_ID", this.get32UUID());	//主键
		appuserapplyforxyService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除AppuserApplyForxy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		appuserapplyforxyService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改AppuserApplyForxy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		appuserapplyforxyService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}

	/**通过申请
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/updateApply")
	public void updateApply(PrintWriter out) throws Exception{
		PageData pd = this.getPageData();
		pd = appuserapplyforxyService.findById(pd);	//根据ID读取
		pd.put("FORXY_STATE","1");
		appuserapplyforxyService.edit(pd);
		String union_id = pd.getString("UNION_ID");
		String user_id = appuserService.getUserIDByUnionID(union_id);
		PageData appUser = new PageData();
		appUser.put("USER_ID",user_id);
		appUser = appuserService.findByUiId(appUser);
		appUser.put("PROXYSTATE","1");
		appuserService.editU(appUser);
		out.write("success");
		out.close();
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logger.info("-------------------代理申请列表页面");
		logBefore(logger, Jurisdiction.getUsername()+"列表AppuserApplyForxy");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = appuserapplyforxyService.list(page);	//列出AppuserApplyForxy列表
		logger.info("-------------------------------size:"+varList.size());
		mv.setViewName("system/appuser/appuserapplyforxy_list");
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
		mv.setViewName("system/appuser/appuserapplyforxy_edit");
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
		pd = appuserapplyforxyService.findById(pd);	//根据ID读取
		mv.setViewName("system/appuser/appuserapplyforxy_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除AppuserApplyForxy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			appuserapplyforxyService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出AppuserApplyForxy到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("申请主键");	//1
		titles.add("UNION_ID");	//2
		titles.add("申请人");	//3
		titles.add("申请电话");	//4
		titles.add("申请备注");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = appuserapplyforxyService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("FORXY_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("UNION_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("FORXY_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("FORXY_PHONE"));	    //4
			vpd.put("var5", varOList.get(i).getString("FORXY_MEMO"));	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**代理申请提交
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/applySave", produces = "application/json;charset=UTF-8")
	public String applySave() throws Exception{
		logger.info("----------------------代理申请提交");
		int result = 1;
		String message = "请提交审核，请耐心等待审核结果..";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FORXY_STATE","0");
		pd.put("FORXY_TIME",DatetimeUtil.getDatetime());
		pd.put("APPUSERAPPLYFORXY_ID", this.get32UUID());	//主键
		try{
			appuserapplyforxyService.save(pd);
		}catch (Exception e){
			result = 0;
			message = "提交失败！";
		}
		pd.put("RESULT", result);
		pd.put("MESSAGE", message);
		logger.info("----------------------json:"+gson.toJson(pd));
		return gson.toJson(pd);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
