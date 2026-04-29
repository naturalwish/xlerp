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

import com.da.service.shop.coupon.CouponManager;
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
import com.da.service.shop.coupon.UserCouponManager;

/**
 * 说明：会员优惠券
 * 创建人：DA
 * 创建时间：2018-07-03
 */
@Controller
@RequestMapping(value = "/usercoupon")
public class UserCouponController extends BaseController {

    String menuUrl = "usercoupon/list.do"; //菜单地址(权限用)
    @Resource(name = "usercouponService")
    private UserCouponManager usercouponService;

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
        logBefore(logger, Jurisdiction.getUsername() + "新增UserCoupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("USERCOUPON_ID", this.get32UUID());    //主键
        usercouponService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除UserCoupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        usercouponService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改UserCoupon");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        usercouponService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表UserCoupon");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = usercouponService.list(page);    //列出UserCoupon列表
        mv.setViewName("shop/coupon/usercoupon_list");
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
        mv.setViewName("shop/coupon/usercoupon_edit");
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
        pd = usercouponService.findById(pd);    //根据ID读取
        //优惠券信息
        PageData coupon = couponService.findById(pd);
        pd.put("COUPON_NAME", coupon.getString("COUPON_NAME"));
        mv.setViewName("shop/coupon/usercoupon_edit");
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除UserCoupon");
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
            usercouponService.deleteAll(ArrayDATA_IDS);
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
     * 会员优惠券列表
     *
     * @param page
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
    public String appList(Page page) throws Exception {
        PageData pd = this.getPageData();
        page.setPd(pd);
        List<PageData> list = usercouponService.list(page);//列出Usercoupon列表
        return gson.toJson(list);
    }

    /**
     * 会员领用优惠券
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "app/save", produces = "application/json;charset=UTF-8")
    public String appSave() throws Exception {
        logger.info("--------------------------------领取优惠券");
        Map<String, Object> map = new HashMap<String, Object>();
        int result = 0;
        String message = "";
        PageData pd = this.getPageData();
        PageData coupon = couponService.findById(pd);
        //是否已经领取
        PageData usercoupon = usercouponService.findById(pd);
        if (usercoupon == null) {
            //用户已领数量
            int applyCount = usercouponService.count(pd);
            if (coupon != null) {
                //判断是否已经领完
                if (applyCount < (int) coupon.get("COUPON_NUM")) {
                    pd.put("COUPON_STATE", "0");
                    pd.put("APPLY_TIME", DatetimeUtil.getDatetime());
                    pd.put("USERCOUPON_ID", this.get32UUID());    //主键
                    usercouponService.save(pd);
                    result = 1;
                    message = StringUtil.success_message;
                } else {
                    message = StringUtil.max_message;
                }
            } else {
                message = StringUtil.except_message;
            }
        } else {
            message = StringUtil.had_message;
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }
}
