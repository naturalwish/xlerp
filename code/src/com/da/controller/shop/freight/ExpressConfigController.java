package com.da.controller.shop.freight;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.util.PageData;
import com.da.util.Jurisdiction;
import com.da.service.shop.freight.ExpressConfigManager;

/**
 * 说明：快递参数
 * 创建人：DA
 * 创建时间：2018-06-21
 */
@Controller
@RequestMapping(value="/expressconfig")
public class ExpressConfigController extends BaseController {

	String menuUrl = "expressconfig/list.do"; //菜单地址(权限用)
	@Resource(name="expressconfigService")
	private ExpressConfigManager expressconfigService;

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ExpressConfig");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		expressconfigService.edit(pd);
		mv.setViewName("redirect:goEdit");
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
		pd = expressconfigService.findById(pd);	//根据ID读取
		mv.setViewName("shop/freight/expressconfig_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
}
