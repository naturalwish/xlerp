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

import com.da.service.shop.goods.GoodsManager;
import com.da.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.shop.coupon.CouponManager;

/**
 * 说明：优惠券
 * 创建人：DA
 * 创建时间：2018-06-21
 */
@Controller
@RequestMapping(value = "/coupon")
public class CouponController extends BaseController {

    String menuUrl = "coupon/list.do"; //菜单地址(权限用)
    @Resource(name = "couponService")
    private CouponManager couponService;
    private Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Coupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("COUPON_ID", this.get32UUID());    //主键
        pd.put("ADDTIME", DatetimeUtil.getDate());    //添加时间
        couponService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除Coupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        couponService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改Coupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        couponService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表Coupon");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = couponService.list(page);    //列出Coupon列表
        mv.setViewName("shop/coupon/coupon_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 选择优惠券列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/goBox")
    public ModelAndView goBox(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Coupon");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = couponService.list(page);    //列出Coupon列表
        mv.setViewName("shop/coupon/coupon_box");
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
        mv.setViewName("shop/coupon/coupon_edit");
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
        pd = couponService.findById(pd);    //根据ID读取
        mv.setViewName("shop/coupon/coupon_edit");
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Coupon");
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
            couponService.deleteAll(ArrayDATA_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 分配商品
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/assignGoods")
    @ResponseBody
    public Object assignGoods() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "分配商品Coupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        pd = this.getPageData();
        List<PageData> pdList = new ArrayList<PageData>();
        String couponID = pd.getString("COUPON_ID");//优惠券ID
        String goodsIDS = pd.getString("GOODS_IDS");//商品IDS
        if (null != goodsIDS && !"".equals(goodsIDS)) {
            String ArrayGoods_IDS[] = goodsIDS.split(",");
            couponService.deleteAll(ArrayGoods_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出Coupon到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("名称");    //1
        titles.add("领取方式");    //2
        titles.add("开始时间");    //3
        titles.add("结束时间");    //4
        titles.add("优惠券价格");    //5
        titles.add("使用额度");    //6
        titles.add("兑换码");    //7
        titles.add("优惠券数量");    //8
        titles.add("添加时间");    //9
        dataMap.put("titles", titles);
        List<PageData> varOList = couponService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("COUPON_NAME"));        //1
            vpd.put("var2", varOList.get(i).getString("COUPON_TYPE"));        //2
            vpd.put("var3", varOList.get(i).getString("STARTTIME"));        //3
            vpd.put("var4", varOList.get(i).getString("ENDTIME"));        //4
            vpd.put("var5", varOList.get(i).get("COUPON_PRICE").toString());    //5
            vpd.put("var6", varOList.get(i).get("USE_PRICE").toString());    //6
            vpd.put("var7", varOList.get(i).getString("CHANGE_CODE"));        //7
            vpd.put("var8", varOList.get(i).get("COUPON_NUM").toString());    //8
            vpd.put("var9", varOList.get(i).getString("ADDTIME"));        //9
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    /**
     * 优惠券列表
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
    public String appList(Page page) throws Exception {
        PageData pd = this.getPageData();
        pd.put("CurrentTime",DateUtil.getDay());
        page.setPd(pd);
        String userId = pd.getString("USER_ID");
        List<PageData> couponList = couponService.list(page); //列出Coupon列表
        return gson.toJson(couponList);
    }
}
