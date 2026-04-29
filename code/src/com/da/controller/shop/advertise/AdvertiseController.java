package com.da.controller.shop.advertise;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.da.service.shop.advertise.impl.PositionService;
import com.da.service.shop.coupon.CouponGoodsManager;
import com.da.service.shop.coupon.CouponManager;
import com.da.service.shop.coupon.SecKillManager;
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
import com.da.service.shop.advertise.AdvertiseManager;

/**
 * 说明：广告管理
 * 创建人：DA
 * 创建时间：2018-06-20
 */
@Controller
@RequestMapping(value = "/advertise")
public class AdvertiseController extends BaseController {

    String menuUrl = "advertise/list.do"; //菜单地址(权限用)
    @Resource(name = "advertiseService")
    private AdvertiseManager advertiseService;

    @Resource(name = "positionService")
    private PositionService positionService;

    @Resource(name = "seckillService")
    private SecKillManager seckillService;

    @Resource(name = "couponService")
    private CouponManager couponService;

    @Resource(name = "coupongoodsService")
    private CouponGoodsManager coupongoodsService;

    private Gson gson = new GsonBuilder().serializeNulls().create();

    public static String A1 = "A1";   //首页轮播图
    public static String A2 = "A2";   //限时抢购广告
    public static String FL1 = "FL1";   //分栏一广告
    public static String FL2 = "FL2";   //分栏二广告
    public static String FL3 = "FL3";   //分栏三广告
    public static String FL4 = "FL4";   //分栏四广告
    public static String FL5 = "FL5";   //分栏五广告
    public static String FL6 = "FL6";   //分栏六广告


    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Advertise");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ADVERTISE_ID", this.get32UUID());    //主键
        String isEnable = pd.getString("IS_ENABLE");//是否启用
        if (null == isEnable || "".equals(isEnable)) {
            pd.put("IS_ENABLE", "0");
        }
        advertiseService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除Advertise");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        advertiseService.delete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改Advertise");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String isEnable = pd.getString("IS_ENABLE");//是否启用
        if (null == isEnable || "".equals(isEnable)) {
            pd.put("IS_ENABLE", "0");
        }
        advertiseService.edit(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "列表Advertise");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = advertiseService.list(page);    //列出Advertise列表
        mv.setViewName("shop/advertise/advertise_list");
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
        List<PageData> positionList = positionService.listAll(pd);//广告位置
        mv.setViewName("shop/advertise/advertise_edit");
        mv.addObject("msg", "save");
        mv.addObject("pd", pd);
        mv.addObject("positionList", positionList);
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
        pd = advertiseService.findById(pd);    //根据ID读取
        //广告位置
        List<PageData> positionList = positionService.listAll(pd);
        //秒杀活动
        if (StringUtils.isNotEmpty(pd.getString("SECKILL_ID"))) {
            PageData seckill = seckillService.findById(pd);
            pd.put("SECKILL_NAME", seckill.getString("SECKILL_NAME"));
        }
        //优惠活动
        if (StringUtils.isNotEmpty(pd.getString("COUPON_ID"))) {
            PageData coupon = couponService.findById(pd);
            pd.put("COUPON_NAME", coupon.getString("COUPON_NAME"));
        }
        mv.setViewName("shop/advertise/advertise_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        mv.addObject("positionList", positionList);
        return mv;
    }

    /**
     * 去广告图片页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEditDetailsPic")
    public ModelAndView goEditDetailsPic() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = advertiseService.findById(pd);    //根据ID读取
        mv.addObject("msg", "saveDetailsPic");
        mv.addObject("pd", pd);
        mv.setViewName("shop/advertise/advertise_detailspic_edit");
        return mv;
    }

    /**
     * 保存广告图片
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/saveDetailsPic")
    public ModelAndView saveDetailsPic() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "------保存广告详情图片");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        advertiseService.editDetailsPic(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Advertise");
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
            advertiseService.deleteAll(ArrayDATA_IDS);
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
        logBefore(logger, Jurisdiction.getUsername() + "导出Advertise到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("广告名称");    //1
        titles.add("广告位置");    //2
        titles.add("开始日期");    //3
        titles.add("结束日期");    //4
        titles.add("广告链接");    //5
        titles.add("广告图片");    //6
        titles.add("是否启用");    //7
        titles.add("联系人");    //8
        titles.add("联系人Email");    //9
        titles.add("联系电话");    //10
        dataMap.put("titles", titles);
        List<PageData> varOList = advertiseService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("ADVERTISE_NAME"));        //1
            vpd.put("var2", varOList.get(i).getString("ADVERTISE_POSITION"));        //2
            vpd.put("var3", varOList.get(i).getString("STARTDATE"));        //3
            vpd.put("var4", varOList.get(i).getString("ENDDATE"));        //4
            vpd.put("var5", varOList.get(i).getString("ADVERTISE_LINK"));        //5
            vpd.put("var6", varOList.get(i).getString("ADVERTISE_PIC"));        //6
            vpd.put("var7", varOList.get(i).getString("IS_ENABLE"));        //7
            vpd.put("var8", varOList.get(i).getString("LINK_MAN"));        //8
            vpd.put("var9", varOList.get(i).getString("EMAIL"));        //9
            vpd.put("var10", varOList.get(i).getString("TELEPHONE"));        //10
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/app/advertisePic", produces = "application/json;charset=UTF-8")
    public String appAdvertisePic() throws Exception {
        PageData pd = this.getPageData();
        String advFlag = pd.getString("ADV_FLAG");
        List<PageData> list = getAppAdvertisePic(pd, advFlag);
        return gson.toJson(list);
    }

    public List getAppAdvertisePic(PageData pd, String advFlag) throws Exception {
        Page page = this.getPage();
        pd.put("POSITION_TEMPLATE", advFlag);
        page.setPd(pd);
        List<PageData> varList = advertiseService.list(page);    //列出Advertise列表
        List<PageData> list = new ArrayList<>();
        for (int i = 0; i < varList.size(); i++) {
            PageData advertises = varList.get(i);
            String pic = advertises.getString("ADVERTISE_PIC");
            if (StringUtils.isNotEmpty(pic)) {
                if (pic.contains(",")) {
                    pic = pic.split(",")[0];
                }
            }
            advertises.put("ADVERTISE_PIC", pic);
            list.add(advertises);
        }
        return list;
    }

    /**
     * 根据广告ID获取广告详情图片和优惠活动
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/advertiseDetails", produces = "application/json;charset=UTF-8")
    public String appAdvertiseDetails() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        pd = advertiseService.findById(pd);
        //广告详情图片
        map.put("DETAILSPIC",pd.getString("ADVERTISE_DETAILSPIC"));
        //优惠活动
        PageData coupon = new PageData();
        coupon.put("COUPON_ID",pd.getString("COUPON_ID"));
        coupon = couponService.findById(coupon);
        map.put("COUPON",coupon);
        //取第一个活动商品
        Page page = this.getPage();
        page.setPd(pd);
        List<PageData> varList = coupongoodsService.list(page);
        for (int i = 0; i < 1 ; i++) {
            PageData var = varList.get(i);
            map.put("GOODS_ID",var.getString("GOODS_ID"));
        }
        return gson.toJson(map);
    }

    /**
     * 根据优惠活动ID获取活动商品
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/couponGoods", produces = "application/json;charset=UTF-8")
    public String appCouponGoods(Page page) throws Exception {
        PageData pd = this.getPageData();
        page.setPd(pd);
        if(StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
            page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
            page.setShowCount(20);
        }
        List<PageData> varList = coupongoodsService.list(page);
        List<PageData> list = new LinkedList<PageData>();
        for (int i = 0; i < varList.size(); i++) {
            PageData var = varList.get(i);
            var.put("TotalPage",page.getTotalPage());
            list.add(var);
        }
        return gson.toJson(list);
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
