package com.da.controller.shop.coupon;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.da.service.shop.goods.AttributeManager;
import com.da.service.shop.goods.GoodsManager;
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
import com.da.util.AppUtil;
import com.da.util.ObjectExcelView;
import com.da.util.PageData;
import com.da.util.Jurisdiction;
import com.da.service.shop.coupon.SeckillGoodsManager;

/** 
 * 说明：秒杀商品
 * 创建人：DA
 * 创建时间：2018-06-29
 */
@Controller
@RequestMapping(value="/seckillgoods")
public class SeckillGoodsController extends BaseController {
	
	String menuUrl = "seckillgoods/list.do"; //菜单地址(权限用)
	@Resource(name="seckillgoodsService")
	private SeckillGoodsManager seckillgoodsService;

	@Resource(name = "goodsService")
	private GoodsManager goodsService;

	@Resource(name="attributeService")
	private AttributeManager attributeService;

	private Gson gson = new GsonBuilder().serializeNulls().create();
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增SeckillGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SECKILLGOODS_ID", this.get32UUID());	//主键
		seckillgoodsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除SeckillGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		seckillgoodsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改SeckillGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		seckillgoodsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表SeckillGoods");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = seckillgoodsService.list(page);	//列出SeckillGoods列表
		mv.setViewName("shop/coupon/seckillgoods_list");
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
		mv.setViewName("shop/coupon/seckillgoods_edit");
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
		pd = seckillgoodsService.findById(pd);	//根据ID读取
		//商品信息
		PageData goods = goodsService.findById(pd); //商品信息
		pd.put("GOODS_NAME", goods.getString("GOODS_NAME"));
		mv.setViewName("shop/coupon/seckillgoods_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除SeckillGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			seckillgoodsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出SeckillGoods到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("秒杀活动ID");	//1
		titles.add("秒杀商品ID");	//2
		titles.add("秒杀价格");	//3
		titles.add("商品数量");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = seckillgoodsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SECKILL_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("GOODS_ID"));	    //2
			vpd.put("var3", varOList.get(i).get("GOODS_PRICE").toString());	//3
			vpd.put("var4", varOList.get(i).get("GOODS_NUM").toString());	//4
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

	/**
	 * 秒杀活动商品列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/getSecKillGoods", produces = "application/json;charset=UTF-8")
	public String appGetSecKillGoods() throws Exception {
		Page page = new Page();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> varList = seckillgoodsService.list(page);
		List<PageData> list = new ArrayList<>();
		for (int i = 0; i < varList.size(); i++) {
			PageData goods = varList.get(i);
			String pic = goods.getString("GOODS_PIC");
			if (StringUtils.isNotEmpty(pic)) {
				if (pic.contains(",")) {
					pic = pic.split(",")[0];
				}
			}
			goods.put("GOODS_PIC", pic);
			String minPrice = attributeService.findMinSecByGoods(goods.getString("GOODS_ID"));
			PageData pd2 = goodsService.findMinByGoodId(goods);
			goods.put("GOODS_PRICE",minPrice);
			if(pd2!=null){
				goods.put("GOODS_MARKETPRICE",pd2.get("MARKETPRICE"));
			}else{
				goods.put("GOODS_MARKETPRICE",0);
			}
			list.add(goods);
		}

		return gson.toJson(list);
	}
}
