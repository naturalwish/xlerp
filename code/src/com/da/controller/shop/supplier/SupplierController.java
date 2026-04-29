package com.da.controller.shop.supplier;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.da.service.system.dictionaries.DictionariesManager;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.da.util.Tools;
import com.da.service.shop.supplier.SupplierManager;

/** 
 * 说明：供应商
 * 创建人：DA
 * 创建时间：2018-06-21
 */
@Controller
@RequestMapping(value="/supplier")
public class SupplierController extends BaseController {
	
	String menuUrl = "supplier/list.do"; //菜单地址(权限用)
	@Resource(name="supplierService")
	private SupplierManager supplierService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Supplier");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SUPPLIER_ID", this.get32UUID());	//主键
		supplierService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Supplier");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		supplierService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Supplier");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		supplierService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Supplier");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String sortId = pd.getString("id");
		if(StringUtils.isNotEmpty(sortId)) pd.put("supplier_sort",sortId);
		page.setPd(pd);
		List<PageData>	varList = supplierService.list(page);	//列出Supplier列表
		mv.setViewName("shop/supplier/supplier_list");
		mv.addObject("varList", varList);
		mv.addObject("supplier_sort", sortId);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}

	/**
	 * 显示列表ztree
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listSort")
	public ModelAndView listAllDict(Model model, String DICTIONARIES_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			//be4a8c5182c744d28282a5345783a77f为数据字典地区ID
			JSONArray arr = JSONArray.fromObject(supplierService.listAreaDict("0"));
			String json = arr.toString();
			json = json.replaceAll("DICTIONARIES_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subDict", "nodes").replaceAll("hasDict", "checked").replaceAll("treeurl", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("DICTIONARIES_ID",DICTIONARIES_ID);
			mv.addObject("pd", pd);
			mv.setViewName("shop/supplier/supplier_ztreelist");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
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
		String supplier_sort = null == pd.get("supplier_sort")?"":pd.get("supplier_sort").toString();
		pd.put("SUPPLIER_SORT", supplier_sort);					//分类ID
		pd.put("DICTIONARIES_ID", supplier_sort);					//上级ID必须为DICTIONARIES_ID与主键字段名保持一致
		mv.addObject("pds",dictionariesService.findById(pd));		//传入上级所有信息
		mv.setViewName("shop/supplier/supplier_edit");
		mv.addObject("SUPPLIER_SORT",supplier_sort);
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
		pd = supplierService.findById(pd);	//根据ID读取
		String supplier_sort = null == pd.get("SUPPLIER_SORT")?"":pd.get("SUPPLIER_SORT").toString();
		pd.put("DICTIONARIES_ID", supplier_sort);					//上级ID必须为DICTIONARIES_ID与主键字段名保持一致
		mv.addObject("pds",dictionariesService.findById(pd));		//传入上级所有信息
		mv.setViewName("shop/supplier/supplier_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Supplier");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			supplierService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Supplier到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("分类");	//1
		titles.add("编码");	//2
		titles.add("名称");	//3
		titles.add("所属行业");	//4
		titles.add("地址");	//5
		titles.add("邮编");	//6
		titles.add("纳税人登记号");	//7
		titles.add("开户银行");	//8
		titles.add("开户账号");	//9
		titles.add("法人");	//10
		titles.add("联系人");	//11
		titles.add("联系方式");	//12
		titles.add("备注");	//13
		dataMap.put("titles", titles);
		List<PageData> varOList = supplierService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SUPPLIER_SORT"));	    //1
			vpd.put("var2", varOList.get(i).getString("SUPPLIER_CODE"));	    //2
			vpd.put("var3", varOList.get(i).getString("SUPPLIER_NAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("SUPPLIER_INDUSTRY"));	    //4
			vpd.put("var5", varOList.get(i).getString("SUPPLIER_ADDRESS"));	    //5
			vpd.put("var6", varOList.get(i).getString("SUPPLIER_POSTCODE"));	    //6
			vpd.put("var7", varOList.get(i).getString("SUPPLIER_TAXPAYER"));	    //7
			vpd.put("var8", varOList.get(i).getString("SUPPLIER_BANK"));	    //8
			vpd.put("var9", varOList.get(i).getString("SUPPLIER_BANKCODE"));	    //9
			vpd.put("var10", varOList.get(i).getString("SUPPLIER_LEGAL"));	    //10
			vpd.put("var11", varOList.get(i).getString("SUPPLIER_TELER"));	    //11
			vpd.put("var12", varOList.get(i).getString("SUPPLIER_TEL"));	    //12
			vpd.put("var13", varOList.get(i).getString("SUPPLIER_MEMO"));	    //13
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
