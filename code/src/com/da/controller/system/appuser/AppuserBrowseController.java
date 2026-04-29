package com.da.controller.system.appuser;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.goods.AttributeManager;
import com.da.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.system.appuser.AppuserBrowseManager;

/** 
 * 说明：商品浏览记录
 * 创建人：DA
 * 创建时间：2018-09-04
 */
@Controller
@RequestMapping(value="/appuserbrowse")
public class AppuserBrowseController extends BaseController {
	
	String menuUrl = "appuserbrowse/list.do"; //菜单地址(权限用)
	@Resource(name="appuserbrowseService")
	private AppuserBrowseManager appuserbrowseService;

	@Resource(name="attributeService")
	private AttributeManager attributeService;

	@Resource(name = "seckillService")
	private SecKillManager seckillService;

	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增AppuserBrowse");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("APPUSERBROWSE_ID", this.get32UUID());	//主键
		appuserbrowseService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除AppuserBrowse");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		appuserbrowseService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改AppuserBrowse");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		appuserbrowseService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表AppuserBrowse");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = appuserbrowseService.list(page);	//列出AppuserBrowse列表
		mv.setViewName("system/appuserbrowse/appuserbrowse_list");
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
		mv.setViewName("system/appuserbrowse/appuserbrowse_edit");
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
		pd = appuserbrowseService.findById(pd);	//根据ID读取
		mv.setViewName("system/appuserbrowse/appuserbrowse_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除AppuserBrowse");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			appuserbrowseService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出AppuserBrowse到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("会员ID");	//1
		titles.add("商品");	//2
		titles.add("浏览时间");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = appuserbrowseService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USER_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("GOODS_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("ADD_TIME"));	    //3
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**
	 * 添加商品浏览记录
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/save", produces = "application/json;charset=UTF-8")
	public String appSave() throws Exception{
		Map<String,Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		String userId = pd.getString("USER_ID");
		String goodsId = pd.getString("GOODS_ID");
		pd = appuserbrowseService.findByUserGoods(pd);
		if(pd == null){
			pd.put("APPUSERBROWSE_ID", this.get32UUID());	//主键
			pd.put("USER_ID", userId);
			pd.put("GOODS_ID", goodsId);
			pd.put("ADD_TIME",DatetimeUtil.getDatetime());
			appuserbrowseService.save(pd);
		}else{
			pd.put("ADD_TIME",DatetimeUtil.getDatetime());
			appuserbrowseService.edit(pd);
		}
		int result = 1;
		String message = "添加成功！";
		try{
			appuserbrowseService.save(pd);
		}catch (Exception e){
			result = 0;
			message = "添加失败！";
		}
		map.put("RESULT",result);
		map.put("MESSAGE",message);
		return gson.toJson(map);
	}

	/**
	 * 新闻评论列表
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String appList() throws Exception {
		Page page = this.getPage();
		PageData pd = this.getPageData();
		page.setPd(pd);
		if(StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
			page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
			page.setShowCount(20);
		}else{
			page.setCurrentPage(1);
			page.setShowCount(99999999);
		}
		List<PageData> varList = appuserbrowseService.listByUser(page);	//列出商品评价列表
		List<PageData> list = new ArrayList<>();
		for (int i = 0; i < varList.size(); i++) {
			PageData goods = varList.get(i);
			String minPrice = getGoodPrice(goods.getString("GOODS_ID"));
			goods.put("GOODS_MINPRICE",minPrice);
			goods.put("TotalPage",page.getTotalPage());
			list.add(goods);
		}
		return gson.toJson(varList);
	}

	//商品价格 实时计算
	public String getGoodPrice(String goods_id)throws Exception{
		return getGoodPrice(goods_id,null);
	}
	public String getGoodPrice(String goods_id, String attribute_id) throws Exception {
		PageData goodatt = new PageData();
		goodatt.put("GOODS_ID",goods_id);
		goodatt.put("ATTRIBUTE_ID",attribute_id);
		PageData attributepd = new PageData();
		if(StringUtil.isNotEmpty(attribute_id)){//属性ID不是空时
			attributepd=attributeService.findById(goodatt);
			//判断商品是否是秒杀商品
			if(isNoSecKill(goods_id,1)){
				return attributepd.getString("SECKILLPRICE");//秒杀价
			}else{
				return attributepd.getString("ATTRIBUTEPRICE");//正常价
			}
		}else{//根据商品ID，如果是秒杀商品则  得到秒杀价 否 正常价
			if(isNoSecKill(goods_id,1)){
				return attributeService.findMinSecByGoods(goods_id);
			}else{
				return attributeService.findMinByGoods(goods_id);
			}
		}
	}

	/**
	 * 判断商品是否是秒杀商品，如果是则取 秒杀价格
	 */
	public boolean isNoSecKill(String GOODSID,int useCount){
		int result =0;
		PageData pd = new PageData();
		pd.put("GOODS_ID",GOODSID);
		pd.put("GOODS_USENUM",useCount);
		try {
			result = seckillService.isNoSecGood(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			return true;
		}else {
			return false;
		}
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
