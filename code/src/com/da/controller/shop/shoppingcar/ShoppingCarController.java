package com.da.controller.shop.shoppingcar;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.da.controller.shop.goods.GoodsController;
import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.goods.AttributeManager;
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
import com.da.service.shop.shoppingcar.ShoppingCarManager;

/** 
 * 说明：购物车
 * 创建人：DA
 * 创建时间：2018-06-28
 */
@Controller
@RequestMapping(value="/shoppingcar")
public class ShoppingCarController extends BaseController {
	
	String menuUrl = "shoppingcar/list.do"; //菜单地址(权限用)
	@Resource(name="shoppingcarService")
	private ShoppingCarManager shoppingcarService;
	@Resource(name="goodsService")
	private GoodsManager goodsService;
	@Resource(name = "seckillService")
	private SecKillManager seckillService;

	@Resource(name="attributeService")
	private AttributeManager attributeService;

	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ShoppingCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SHOPPINGCAR_ID", this.get32UUID());	//主键
		shoppingcarService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除ShoppingCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		shoppingcarService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ShoppingCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		shoppingcarService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表ShoppingCar");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	carList = shoppingcarService.userList(page);	//列出ShoppingCar列表
		List<PageData> list = new LinkedList<PageData>();
		for (int i = 0; i < carList.size(); i++) {
			PageData car = carList.get(i);
			pd.put("USERNAME",car.getString("USER_ID"));
			page.setPd(pd);
			List<PageData> cardetail = shoppingcarService.list(page);
			car.put("cardetail", cardetail);
			car.put("goodtatol", cardetail.size());
			list.add(car);
		}
		mv.setViewName("shop/shoppingcar/shoppingcar_list");
		mv.addObject("varList", list);
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
		mv.setViewName("shop/shoppingcar/shoppingcar_edit");
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
		pd = shoppingcarService.findById(pd);	//根据ID读取
		mv.setViewName("shop/shoppingcar/shoppingcar_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除ShoppingCar");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			shoppingcarService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出ShoppingCar到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("商品ID");	//1
		titles.add("商品名称");	//2
		titles.add("规格型号");	//3
		titles.add("数量");	//4
		titles.add("小计");	//5
		titles.add("单价");	//6
		titles.add("用户ID");	//7
		titles.add("姓名");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = shoppingcarService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("GOODS_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("GOODS_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("GOODS_ATTRIBUTE"));	    //3
			vpd.put("var4", varOList.get(i).get("GOODS_NUM").toString());	//4
			vpd.put("var5", varOList.get(i).get("GOODS_TOTAL").toString());	//5
			vpd.put("var6", varOList.get(i).get("GOODS_PRICE").toString());	//6
			vpd.put("var7", varOList.get(i).getString("USERNAME"));	    //7
			vpd.put("var8", varOList.get(i).getString("REALNAME"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**
	 * 添加到购物车
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/add", produces = "application/json;charset=UTF-8")
	public String appAdd() throws Exception {
		logger.info("--------------------添加至购物车");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		//查看购物车是否存在同一规格的商品
		int result = 1;
		String message = "添加成功！";
		if(StringUtils.isNotEmpty(pd.getString("GOODS_ATTRIBUTE_ID"))){
			PageData pd2 = shoppingcarService.findByUSERAtt(pd);
			try{
				if(pd2!=null){
					int goodsNum = (int)pd2.get("GOODS_NUM");
					pd2.put("GOODS_NUM",goodsNum+1);
					shoppingcarService.edit(pd2);
				}else{
					logger.info("-------------------新增至购物车");
					String goods_id = pd.getString("GOODS_ID");
					PageData goodPd = new PageData();
					goodPd.put("GOODS_ID",goods_id);
					goodPd = goodsService.findById(goodPd);
					pd.put("GOODS_NAME",goodPd.getString("GOODS_NAME"));
					pd.put("SHOPPINGCAR_ID", this.get32UUID());	//主键
					shoppingcarService.save(pd);
				}
			}catch (Exception e){
				result = 0;
				message = "添加失败!";
			}
		}else{
			result = 0;
			message = "请选择商品规格!";
		}
		map.put("RESULT",result);
		map.put("MESSAGE",message);
		return gson.toJson(map);
	}

	/**
	 * 从购物车删除商品
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/delete", produces = "application/json;charset=UTF-8")
	public String appDelete() throws Exception {
		logger.info("------------------从购物车移除商品");
		Map<String, Object> map = new HashMap<String, Object>();
		int resulst = 1;
		String message = "删除成功！";
		PageData pd = this.getPageData();
		try{
			shoppingcarService.delete(pd);
		}catch (Exception e){
			resulst = 0;
			message = "删除失败！";
		}
		map.put("RESULT",resulst);
		map.put("MESSAGE",message);
		return gson.toJson(map);
	}

	/**
	 * 从修改购物车商品数量，及时保存
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/update", produces = "application/json;charset=UTF-8")
	public String appUpdate() throws Exception {
		logger.info("------------------修改购物车商品数量");
		Map<String, Object> map = new HashMap<String, Object>();
		int resulst = 1;
		String message = "修改成功！";
		PageData pd = this.getPageData();
		try{
			shoppingcarService.edit(pd);
		}catch (Exception e){
			resulst = 0;
			message = "修改失败！";
		}
		map.put("RESULT",resulst);
		map.put("MESSAGE",message);
		return gson.toJson(map);
	}

	/**
	 * 购物车列表
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String appList() throws Exception {
		logger.info("------------------根据用户查询购物车列表");
		Page page = this.getPage();
		PageData pd = this.getPageData();
		pd.put("GOODS_FORSALE",1);
		page.setPd(pd);
		if(StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
			page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
			page.setShowCount(20);
		}
		List<PageData> varList = shoppingcarService.list(page);	//列出ShoppingCar列表
		List<PageData> list = new ArrayList<PageData>();
		for(int i=0;i<varList.size();i++){
			PageData cartpd= varList.get(i);
				cartpd.put("ATTRIBUTE_ID",cartpd.getString("GOODS_ATTRIBUTE_ID"));
				PageData attributepd=attributeService.findById(cartpd);
				if(isNoSecKill(cartpd.getString("GOODS_ID"),1)){
					cartpd.put("GOODS_PRICE",attributepd.get("SECKILLPRICE"));
				}else{
					cartpd.put("GOODS_PRICE",attributepd.get("ATTRIBUTEPRICE"));
				}
				String pic = "";
				if (StringUtils.isNotEmpty(cartpd.getString("GOODS_PIC"))) {
					if (cartpd.getString("GOODS_PIC").contains(",")) {
						pic = cartpd.getString("GOODS_PIC").split(",")[0];
					}else{
						pic = cartpd.getString("GOODS_PIC");
					}
				}
				cartpd.put("GOODS_PIC", pic);
				cartpd.put("TotalPage",page.getTotalPage());
				list.add(cartpd);
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

	/**
	 * 用户购物车中商品数量
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/goodsCount", produces = "application/json;charset=UTF-8")
	public String goodsCount() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		int count = shoppingcarService.goodsCount(pd);
		map.put("RESULT",1);
		map.put("MESSAGE",count);
		return gson.toJson(map);
	}
}
