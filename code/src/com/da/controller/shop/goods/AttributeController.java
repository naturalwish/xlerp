package com.da.controller.shop.goods;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.goods.GoodsManager;
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
import com.da.service.shop.goods.AttributeManager;

/** 
 * 说明：订单管理(明细)
 * 创建人：DA
 * 创建时间：2018-06-28
 */
@Controller
@RequestMapping(value="/attribute")
public class AttributeController extends BaseController {
	
	String menuUrl = "attribute/list.do"; //菜单地址(权限用)
	@Resource(name="attributeService")
	private AttributeManager attributeService;


	@Resource(name = "seckillService")
	private SecKillManager seckillService;

	@Resource(name="goodsService")
	private GoodsManager goodsService;

	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Attribute");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ATTRIBUTE_ID", this.get32UUID());	//主键
		if(StringUtil.isEmpty(pd.getString("SECKILLPRICE"))){
			pd.put("SECKILLPRICE","0.00");
		}
		attributeService.save(pd);
		double MINPRICE = Double.parseDouble(attributeService.findMinByGoods(pd.getString("GOODS_ID")));
		PageData pd1 = new PageData();
		pd1.put("GOODS_ID",pd.getString("GOODS_ID"));
		pd1.put("GOODS_PRICE",MINPRICE);
		goodsService.editOther(pd1);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Attribute");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		attributeService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Attribute");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(StringUtil.isEmpty(pd.getString("SECKILLPRICE"))){
			pd.put("SECKILLPRICE","0.00");
		}
		attributeService.edit(pd);
		double MINPRICE = Double.parseDouble(attributeService.findMinByGoods(pd.getString("GOODS_ID")));
		PageData pd1 = new PageData();
		pd1.put("GOODS_ID",pd.getString("GOODS_ID"));
		pd1.put("GOODS_PRICE",MINPRICE);
		goodsService.editOther(pd1);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Attribute");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = attributeService.list(page);	//列出Attribute列表
		mv.setViewName("shop/goods/attribute_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}

	@RequestMapping(value="/getAttribute")
	@ResponseBody
	public Object getAttribute(Page page){
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData>	varList = attributeService.list(page); //用传过来的ID获取此ID下的子列表数据
			map.put("list", varList);
		} catch(Exception e){
			errInfo = "error";
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
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
		mv.setViewName("shop/goods/attribute_edit");
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
		pd = attributeService.findById(pd);	//根据ID读取
		mv.setViewName("shop/goods/attribute_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Attribute");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			attributeService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Attribute到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品主键");	//1
		titles.add("属性");	//2
		titles.add("数量");	//3
		titles.add("单价");	//4
		titles.add("市场价格");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = attributeService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GOODS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("ATTRIBUTE"));	    //2
			vpd.put("var3", varOList.get(i).get("ATTRIBUTENUM").toString());	//3
			vpd.put("var4", varOList.get(i).get("ATTRIBUTEPRICE").toString());	//4
			vpd.put("var5", varOList.get(i).get("MARKETPRICE").toString());	//5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**
	 * 商品规格列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String applist() throws Exception {
		Page page = new Page();
		PageData pd = this.getPageData();
		page.setPd(pd);
		pd = goodsService.findById(pd);
		if(0 == pd.get("GOODS_FORSALE")||1 == pd.get("GOODS_DELFLAG")){
			return gson.toJson(new ArrayList<PageData>());
		}
		List<PageData> list = attributeService.list(page); // 列出规格列表
		if(isNoSecKill(pd.getString("GOODS_ID"),1)){
			for(int i=0;i<list.size();i++){
				PageData attpd=list.get(i);
				attpd.put("ATTRIBUTEPRICE",attpd.get("SECKILLPRICE"));
				list.set(i,attpd);
			}
		}
		return gson.toJson(list);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
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
}
