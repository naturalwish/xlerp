package com.da.controller.shop.address;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

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
import com.da.service.shop.address.AddressManager;

/**
 * 说明：收货地址
 * 创建人：DA
 * 创建时间：2023-07-04
 */
@Controller
@RequestMapping(value = "/address")
public class AddressController extends BaseController {

    String menuUrl = "address/list.do"; //菜单地址(权限用)
    @Resource(name = "addressService")
    private AddressManager addressService;

    private Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Address");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ADDRESS_ID", this.get32UUID());    //主键
        pd.put("ADD_TIME", DatetimeUtil.getDate());    //添加时间
        //如果新增的地址设置为默认地址，更改用户其他地址为非默认
        String IS_DEFAULT = pd.getString("IS_DEFAULT");
        if (StringUtils.isNotEmpty(IS_DEFAULT)) {
            addressService.editDefault(pd);
        }
        addressService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 删除
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "删除Address");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        addressService.delete(pd);
        out.write("success");
        out.close();
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改Address");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        //如果修改的地址设置为默认地址，更改用户其他地址为非默认
        String IS_DEFAULT = pd.getString("IS_DEFAULT");
        if (StringUtils.isNotEmpty(IS_DEFAULT)) {
            addressService.editDefault(pd);
        }
        addressService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Address");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = addressService.list(page);    //列出Address列表
        mv.setViewName("shop/address/address_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 去新增页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goAdd")
    public ModelAndView goAdd() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("shop/address/address_edit");
        mv.addObject("msg", "save");
        mv.addObject("pd", pd);
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
        pd = addressService.findById(pd);    //根据ID读取
        mv.setViewName("shop/address/address_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 批量删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Address");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        pd = this.getPageData();
        List<PageData> pdList = new ArrayList<PageData>();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (null != DATA_IDS && !"".equals(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            addressService.deleteAll(ArrayDATA_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    /**
     * 会员收货地址列表
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
        List<PageData> addressList = addressService.list(page);
        return gson.toJson(addressList);
    }

    /**
     * 保存
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/save", produces = "application/json;charset=UTF-8")
    public String appSave() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        pd.put("ADDRESS_ID", this.get32UUID());    //主键
        pd.put("ADD_TIME", DatetimeUtil.getDatetime());    //添加时间
        //如果新增的地址设置为默认地址，更改用户其他地址为非默认
        String IS_DEFAULT = pd.getString("IS_DEFAULT");
        if (StringUtils.isNotEmpty(IS_DEFAULT)&&"1".equals(IS_DEFAULT)) {
            addressService.editDefault(pd);
        }
        int result = 1;
        String message = "添加成功！";
        try {
            addressService.save(pd);
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
     * 修改
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/eidt", produces = "application/json;charset=UTF-8")
    public String appEdit() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        //如果修改的地址设置为默认地址，更改用户其他地址为非默认
        String IS_DEFAULT = pd.getString("IS_DEFAULT");
        if (StringUtils.isNotEmpty(IS_DEFAULT)&&"1".equals(IS_DEFAULT)) {
            addressService.editDefault(pd);
        }
        int result = 1;
        String message = "修改成功！";
        try {
            addressService.edit(pd);
        } catch (Exception e) {
            result = 0;
            message = "修改失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * 删除
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
            addressService.delete(pd);
        } catch (Exception e) {
            result = 0;
            message = "删除失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }
}
