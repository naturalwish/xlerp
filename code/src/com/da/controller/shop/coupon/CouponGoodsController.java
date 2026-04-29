package com.da.controller.shop.coupon;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.da.service.shop.goods.GoodsManager;
import com.da.util.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.shop.coupon.CouponGoodsManager;

/**
 * 说明：优惠商品
 * 创建人：DA
 * 创建时间：2018-06-26
 */
@Controller
@RequestMapping(value = "/coupongoods")
public class CouponGoodsController extends BaseController {

    String menuUrl = "coupongoods/list.do"; //菜单地址(权限用)
    @Resource(name = "coupongoodsService")
    private CouponGoodsManager coupongoodsService;

    @Resource(name = "goodsService")
    private GoodsManager goodsService;

    /**
     * 分配商品
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/assignGoods")
    public ModelAndView assignGoods() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "分配商品CouponGoods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String couponID = pd.getString("COUPON_ID");//优惠券ID
        String goodsIDS = pd.getString("GOODS_IDS");//商品IDS
        goodsIDS="'"+goodsIDS.replace(",","','")+"'";
        pd.put("GOODS_IDS",goodsIDS);
        List<PageData> goodsList = goodsService.listAll(pd);//商品列表
        List<PageData> varList = new ArrayList<PageData>();//优惠商品
        for (int i = 0; i < goodsList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("COUPONGOODS_ID", this.get32UUID());//主键
            vpd.put("COUPON_ID", couponID);//优惠券ID
            vpd.put("GOODS_ID", goodsList.get(i).getString("GOODS_ID"));//优惠商品ID
            vpd.put("GOODS_PRICE", goodsList.get(i).get("GOODS_PRICE"));//优惠商品价格
            vpd.put("GOODS_NUM", 20);//优惠商品数量
            //已经添加的商品则不需要重复添加
            PageData pd1 =coupongoodsService.findBygoodsIDAndCouponID(vpd);
            if(pd1!=null) continue;
            varList.add(vpd);
        }
        if(varList.size()>0){
            coupongoodsService.save(varList);
        }
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
        logBefore(logger, Jurisdiction.getUsername() + "删除CouponGoods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        coupongoodsService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改CouponGoods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        coupongoodsService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表CouponGoods");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = coupongoodsService.list(page);    //列出CouponGoods列表
        mv.setViewName("shop/coupon/coupongoods_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
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
        pd = coupongoodsService.findById(pd);    //根据ID读取
        mv.setViewName("shop/coupon/coupongoods_edit");
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除CouponGoods");
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
            coupongoodsService.deleteAll(ArrayDATA_IDS);
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
}
