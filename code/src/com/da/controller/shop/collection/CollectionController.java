package com.da.controller.shop.collection;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.da.entity.system.Menu;
import com.da.entity.system.Role;
import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.goods.CategoryManager;
import com.da.service.shop.goods.GoodsManager;
import com.da.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.shop.collection.CollectionManager;

/** 
 * 说明：收藏夹
 * 创建人：DA
 * 创建时间：2018-08-09
 */
@Controller
@RequestMapping(value="/collection")
public class CollectionController extends BaseController {
	
	String menuUrl = "collection/list.do"; //菜单地址(权限用)
	@Resource(name="collectionService")
	private CollectionManager collectionService;

	@Resource(name="goodsService")
	private GoodsManager goodsService;

	@Resource(name = "seckillService")
	private SecKillManager seckillService;

	private Gson gson = new GsonBuilder().serializeNulls().create();

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Collection");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("COLLECTION_ID", this.get32UUID());	//主键
		collectionService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Collection");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		collectionService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Collection");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		collectionService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Collection");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = collectionService.list(page);	//列出Collection列表
		mv.setViewName("shop/collection/collection_list");
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
		mv.setViewName("shop/collection/collection_edit");
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
		pd = collectionService.findById(pd);	//根据ID读取
		mv.setViewName("shop/collection/collection_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Collection");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			collectionService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Collection到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品ID");	//1
		titles.add("会员ID");	//2
		titles.add("添加时间");	//3
		titles.add("备注");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = collectionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GOODS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("USER_ID"));	    //2
			vpd.put("var3", varOList.get(i).getString("ADD_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("MEMO"));	    //4
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
	 * 会员商品收藏列表
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String appList() throws Exception {
		Page page = new Page();
		PageData pd = this.getPageData();
		page.setPd(pd);
		if(StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
			page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
			page.setShowCount(20);
		}
		List<PageData> varList = collectionService.list(page);
		List<PageData> list = new LinkedList<PageData>();
		for (int i = 0; i < varList.size(); i++) {
			PageData var = varList.get(i);
			PageData pd2 = goodsService.findMinByGoodId(var);
			var.put("GOODS_MINATTRIBUTEID",pd2.get("ATTRIBUTE_ID"));
			if(isNoSecKill(var.getString("GOODS_ID"),1)){
				var.put("GOODS_MINPRICE",pd2.get("SECKILLPRICE"));//秒杀价
			}else{
				var.put("GOODS_MINPRICE",pd2.get("ATTRIBUTEPRICE"));//正常价
			}
			var.put("TotalPage",page.getTotalPage());
			list.add(var);
		}
		return gson.toJson(list);
	}

	/**
	 * 添加商品到收藏夹
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/save", produces = "application/json;charset=UTF-8")
	public String appSave() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		PageData pd1 =collectionService.findById(pd);
		if(pd1!=null){
			map.put("RESULT", 0);
			map.put("MESSAGE", "已经收藏的商品！");
			map.put("ADDRESS",pd1);
			return gson.toJson(map);
		}
		pd.put("COLLECTION_ID", this.get32UUID());    //主键
		pd.put("ADD_TIME", DatetimeUtil.getDatetime());    //添加时间
		int result = 1;
		String message = "添加成功！";
		try {
			collectionService.save(pd);
		} catch (Exception e) {
			result = 0;
			message = "添加失败！";
		}
		map.put("RESULT", result);
		map.put("MESSAGE", message);
		map.put("ADDRESS",pd);
		return gson.toJson(map);
	}

	/**
	 * 删除商品从收藏夹
	 *
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/delete", produces = "application/json;charset=UTF-8")
	public String appDelete() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		int result = 1;
		String message = "删除成功！";
		try {
			collectionService.delete(pd);
		} catch (Exception e) {
			result = 0;
			message = "删除失败！";
		}
		map.put("RESULT", result);
		map.put("MESSAGE", message);
		return gson.toJson(map);
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
