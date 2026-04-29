package com.da.controller.shop.freight;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

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
import com.da.util.Tools;
import com.da.service.shop.freight.FreightManager;

/**
 * 说明：运费设置
 * 创建人：DA
 * 创建时间：2018-06-21
 */
@Controller
@RequestMapping(value = "/freight")
public class FreightController extends BaseController {

    String menuUrl = "freight/list.do"; //菜单地址(权限用)
    @Resource(name = "freightService")
    private FreightManager freightService;

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改Freight");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        freightService.edit(pd);
        mv.setViewName("redirect:goEdit");
        return mv;
    }

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
    public ModelAndView goEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = freightService.findById(pd);    //根据ID读取
        mv.setViewName("shop/freight/freight_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }
}
