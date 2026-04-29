package com.da.controller.shop.goods;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;

import com.da.entity.system.Menu;
import com.da.service.shop.collection.CollectionManager;
import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.goods.AttributeManager;
import com.da.service.shop.goods.BrandManager;
import com.da.service.shop.goods.CategoryManager;
import com.da.service.shop.shoppingcar.ShoppingCarManager;
import com.da.util.*;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.shop.goods.GoodsManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 说明：商品
 * 创建人：DA
 * 创建时间：2018-06-25
 */
@Controller
@RequestMapping(value = "/goods")
public class GoodsController extends BaseController {

    String menuUrl = "goods/list.do"; //菜单地址(权限用)
    String menuUrl2 = "goods/undeletelist.do"; //菜单地址(权限用)
    @Resource(name = "goodsService")
    private GoodsManager goodsService;

    @Resource(name = "categoryService")
    private CategoryManager categoryService;

    @Resource(name = "attributeService")
    private AttributeManager attributeService;

    @Resource(name = "brandService")
    private BrandManager brandService;

    @Resource(name = "seckillService")
    private SecKillManager seckillService;

    @Resource(name = "shoppingcarService")
    private ShoppingCarManager shoppingcarService;
    @Resource(name = "collectionService")
    private CollectionManager collectionService;


    private Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * @param GOODS_ID： 商品ID
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chooseCategory")
    public ModelAndView chooseCategory(@RequestParam String GOODS_ID, Model model) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd.put("GOODS_ID", GOODS_ID);
        try {
            List<Menu> menuList = categoryService.listAllMenuQx("0"); //获取所有菜单
            PageData goods = goodsService.findById(pd);          //根据商品ID获取商品
            String roleRights = goods.getString("GOODS_CATEGORY1");    //查看权限

            menuList = this.readMenu(menuList, roleRights + "");        //根据角色权限处理菜单权限状态(递归处理)
            JSONArray arr = JSONArray.fromObject(menuList);
            String json = arr.toString();
            json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
            model.addAttribute("zTreeNodes", json);
            mv.addObject("GOODS_ID", GOODS_ID);
            mv.addObject("msg", "save");
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        mv.setViewName("shop/goods/menucategory");
        return mv;
    }

    /**
     * 根据角色权限处理权限状态(递归处理)
     *
     * @param menuList：传入的总菜单
     * @param roleRights：加密的权限字符串
     * @return
     */
    public List<Menu> readMenu(List<Menu> menuList, String roleRights) {
        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setHasMenu(isCheckCategory(roleRights, menuList.get(i).getMENU_ID()));
            this.readMenu(menuList.get(i).getSubMenu(), roleRights);                    //是：继续排查其子菜单
        }
        return menuList;
    }

    public boolean isCheckCategory(String Categorys, String Category) {
        int result = Categorys.indexOf(Category);
        return result == -1 ? false : true;
    }

    /**
     * @param GOODS_ID
     * @param GOODS_CATEGORYS
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/saveCategory")
    public void saveCategory(@RequestParam String GOODS_ID, @RequestParam String GOODS_CATEGORYS, PrintWriter out) throws Exception {
        PageData pd = new PageData();
        try {
            if (null != GOODS_CATEGORYS && !"".equals(GOODS_CATEGORYS.trim())) {
                pd.put("GOODS_CATEGORY1", GOODS_CATEGORYS);
            } else {
                pd.put("GOODS_CATEGORY1", "");
            }
            pd.put("GOODS_ID", GOODS_ID);
            goodsService.saveCategory(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增Goods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        int order = goodsService.findMaxOrder(pd);

        pd.put("GOODS_ORDER", order + 1);
        pd.put("GOODS_ID", this.get32UUID());    //主键
        if (StringUtils.isEmpty(pd.getString("GOODS_FORSALE"))) pd.put("GOODS_FORSALE", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_BEST"))) pd.put("GOODS_BEST", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_NEW"))) pd.put("GOODS_NEW", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_HOT"))) pd.put("GOODS_HOT", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_CUSTOM"))) pd.put("GOODS_CUSTOM", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_FREETRAFFIC"))) pd.put("GOODS_FREETRAFFIC", "0");
        goodsService.save(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "删除Goods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        goodsService.delete(pd);
        out.write("success");
        out.close();
    }

    /**
     * 恢复
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/undelete")
    public void undelete(PrintWriter out) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "恢复Goods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl2, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        goodsService.undelete(pd);
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
        logBefore(logger, Jurisdiction.getUsername() + "修改Goods");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (StringUtils.isEmpty(pd.getString("GOODS_FORSALE"))) pd.put("GOODS_FORSALE", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_BEST"))) pd.put("GOODS_BEST", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_NEW"))) pd.put("GOODS_NEW", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_HOT"))) pd.put("GOODS_HOT", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_CUSTOM"))) pd.put("GOODS_CUSTOM", "0");
        if (StringUtils.isEmpty(pd.getString("GOODS_FREETRAFFIC"))) pd.put("GOODS_FREETRAFFIC", "0");
        goodsService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 更新活动状态
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/listUpdate")
    public ModelAndView updateState() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "---列表更新商品状态");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String goods_forsale = pd.getString("GOODS_FORSALE");
        pd = goodsService.findById(pd);
        pd.put("GOODS_FORSALE", goods_forsale);
        goodsService.edit(pd);
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
        logger.info("----------------------加载商品列表");
        logBefore(logger, Jurisdiction.getUsername() + "列表Goods");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        String delflag = pd.getString("delflag");
        if (StringUtils.isNotEmpty(delflag)) {
            pd.put("Goods_DelFlag", delflag);
        }
        page.setPd(pd);
        List<PageData> varList = goodsService.list(page);    //列出Goods列表
        List<PageData> list = new ArrayList<>();
        for (int i = 0; i < varList.size(); i++) {
            PageData goods = varList.get(i);
            String pic = goods.getString("GOODS_PIC");
            if (StringUtils.isNotEmpty(pic)) {
                if (pic.contains(",")) {
                    pic = pic.split(",")[0];
                }
            }
            goods.put("GOODS_PIC", pic);
            int sunNum = attributeService.getSumNum(goods.getString("GOODS_ID"));
            logger.info("---------------sunNum:" + sunNum);
            goods.put("GOODS_NUM", sunNum);
            list.add(goods);
        }
        mv.setViewName("shop/goods/goods_list");
        mv.addObject("varList", list);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 商品回收站列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/deleteList")
    public ModelAndView deleteList(Page page) throws Exception {
        logger.info("----------------------加载商品回收站列表1");
        logBefore(logger, Jurisdiction.getUsername() + "列表Goods");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("delflag", "1");
        page.setPd(pd);
        List<PageData> varList = goodsService.list(page);    //列出Goods列表
        mv.setViewName("shop/goods/goods_deletelist");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 选择商品列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/goBox")
    public ModelAndView goBox(Page page) throws Exception {
        logger.info("----------------------加载商品选择列表");
        logBefore(logger, Jurisdiction.getUsername() + "选择列表Goods");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        String delflag = pd.getString("delflag");
        if (StringUtils.isNotEmpty(delflag)) {
            pd.put("Goods_DelFlag", delflag);
        }
        page.setPd(pd);
        List<PageData> varList = goodsService.list(page);    //列出Goods列表
        mv.setViewName("shop/goods/goods_box");
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
        PageData category = new PageData();
        String super_id = pd.getString("SUPER_ID");
        if (StringUtils.isEmpty(super_id)) {
            super_id = "0";
            pd.put("SUPER_ID", super_id);
        }
        List<PageData> categoryList = categoryService.listAll(pd);
        for (int i = 0; i < categoryList.size(); i++) {
            pd.put("SUPER_ID", categoryList.get(i).getString("CATEGORY_ID"));
            List<PageData> childcategory = categoryService.listAll(pd);// 分类
            category.put("childcategory" + i, childcategory);
        }
        category.put("categoryList", categoryList);
        List<PageData> brandList = brandService.listAll(pd);//品牌
        mv.setViewName("shop/goods/goods_edit");
        mv.addObject("msg", "save");
        mv.addObject("category", category);
        mv.addObject("brandList", brandList);
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
        pd = goodsService.findById(pd);    //根据ID读取
        List<PageData> brandList = brandService.listAll(pd);//品牌
        PageData category = new PageData();
        String super_id = pd.getString("SUPER_ID");
        if (StringUtils.isEmpty(super_id)) {
            super_id = "0";
            pd.put("SUPER_ID", super_id);
        }
        List<PageData> categoryList = categoryService.listAll(pd);
        for (int i = 0; i < categoryList.size(); i++) {
            pd.put("SUPER_ID", categoryList.get(i).getString("CATEGORY_ID"));
            List<PageData> childcategory = categoryService.listAll(pd);// 分类
            category.put("childcategory" + i, childcategory);
        }
        category.put("categoryList", categoryList);
        pd.put("SUPER_ID", "0");
        mv.addObject("msg", "edit");
        mv.addObject("category", category);
        mv.addObject("pd", pd);
        mv.addObject("brandList", brandList);
        mv.setViewName("shop/goods/goods_edit");
        return mv;
    }

    /**
     * 调整商品详情排序
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEditOrder")
    public ModelAndView goEditOrder() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = goodsService.findById(pd);    //根据ID读取
        mv.addObject("msg", "saveDetails");
        mv.addObject("pd", pd);
        mv.setViewName("shop/goods/goods_editDetailsOrder");
        return mv;
    }

    /**保存排序
     * @param
     * @throws Exception
     *//*
    @RequestMapping(value="/saveOrder")
	public ModelAndView saveOrder() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"保存排序");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String detailPic = pd.getString("GOODS_DETAILPIC");
		pd = goodsService.findById(pd);
		pd.put("GOODS_DETAILPIC",detailPic);
		goodsService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}*/

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEditDetails")
    public ModelAndView goEditDetails() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = goodsService.findById(pd);    //根据ID读取
        mv.addObject("msg", "saveDetails");
        mv.addObject("pd", pd);
        mv.setViewName("shop/goods/goods_editDetails2");
        return mv;
    }

    /**
     * 保存商品详情
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/saveDetails")
    public ModelAndView saveDetails() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "------保存商品详情");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String goods_detailsPic = pd.getString("GOODS_DETAILSPIC");
        pd = this.goodsService.findById(pd);
        pd.put("GOODS_DETAILSPIC", goods_detailsPic);
        goodsService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 新增商品详情
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveDetails2")
    @ResponseBody
    public Object save(HttpServletRequest request) throws Exception {
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
        //校验权限
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        }
        String msg = "上传成功！";
        logBefore(logger, Jurisdiction.getUsername() + "新增商品详情..");
        String filePath = "", fileName = "";
        PageData pd = this.getPageData();
        pd = goodsService.findById(pd);
        String mallUploadUrl = (String) this.getRequest().getSession().getAttribute("mall_upload_path");
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
            Iterator<String> fileNameIterator = fileMap.keySet().iterator();
            while (fileNameIterator.hasNext()) {
                MultipartFile file = fileMap.get(fileNameIterator.next());
                if (!file.isEmpty() || file.getSize() > 0) {
                    if (mallUploadUrl.contains("\\")) {
                        filePath = mallUploadUrl.substring(0, mallUploadUrl.lastIndexOf("\\")) + "/mallupload"; //windows
                    } else {
                        filePath = mallUploadUrl.substring(0, mallUploadUrl.lastIndexOf("/")) + "/mallupload";  //linux
                    }
                    fileName = FileUpload.fileUp(file, filePath, new Date().getTime() + "");                //执行上传
                    String goods_detailspic = pd.getString("GOODS_DETAILSPIC");
                    String dirFile = "/mallupload/" + fileName;
                    if (StringUtil.isEmpty(goods_detailspic)) {
                        pd.put("GOODS_DETAILSPIC", dirFile);
                    } else {
                        pd.put("GOODS_DETAILSPIC", goods_detailspic + "," + dirFile);
                    }
                    try {
                        goodsService.edit(pd);
                    } catch (Exception e) {
                        msg = "保存失败！";
                    }
                } else {
                    msg = "上传失败";
                }

            }
        }
        return msg;
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
        logBefore(logger, Jurisdiction.getUsername() + "批量删除Goods");
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
            goodsService.deleteAll(ArrayDATA_IDS);
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
        logBefore(logger, Jurisdiction.getUsername() + "导出Goods到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("图片");    //1
        titles.add("名称");    //2
        titles.add("标题");    //3
        titles.add("本店售格");    //4
        titles.add("数量");    //5
        titles.add("编号");    //6
        titles.add("货号");    //7
        titles.add("上架");    //8
        titles.add("精品");    //9
        titles.add("新品");    //10
        titles.add("热销");    //11
        titles.add("品牌");    //12
        titles.add("会员价格");    //13
        titles.add("商品优惠数量");    //14
        titles.add("商品优惠价格");    //15
        titles.add("市场售价");    //16
        titles.add("赠送消费积分数");    //17
        titles.add("赠送等级积分数");    //18
        titles.add("积分购买金额");    //19
        titles.add("促销价");    //20
        titles.add("促销开始日期");    //21
        titles.add("缩略图");    //22
        titles.add("详细描述");    //23
        titles.add("重量");    //24
        titles.add("库存警告数量");    //25
        titles.add("能作为普通商品销售");    //26
        titles.add("是否为免运费商品");    //27
        titles.add("商品关键词");    //28
        titles.add("商品简介");    //29
        titles.add("排序");    //30
        titles.add("促销结束日期");    //31
        titles.add("删除标记");    //32
        titles.add("规格型号");    //33
        dataMap.put("titles", titles);
        List<PageData> varOList = goodsService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GOODS_PIC"));        //1
            vpd.put("var2", varOList.get(i).getString("GOODS_NAME"));        //2
            vpd.put("var3", varOList.get(i).getString("GOODS_TITLE"));        //3
            vpd.put("var4", varOList.get(i).get("GOODS_PRICE").toString());    //4
            vpd.put("var5", varOList.get(i).get("GOODS_NUM").toString());    //5
            vpd.put("var6", varOList.get(i).getString("GOODS_CODE"));        //6
            vpd.put("var7", varOList.get(i).getString("GOODS_NUMBER"));        //7
            vpd.put("var8", varOList.get(i).get("GOODS_FORSALE").toString());    //8
            vpd.put("var9", varOList.get(i).get("GOODS_BEST").toString());    //9
            vpd.put("var10", varOList.get(i).get("GOODS_NEW").toString());    //10
            vpd.put("var11", varOList.get(i).get("GOODS_HOT").toString());    //11
            vpd.put("var12", varOList.get(i).getString("GOODS_BRAND"));        //12
            vpd.put("var13", varOList.get(i).get("MEMBER_PRICE").toString());    //13
            vpd.put("var14", varOList.get(i).get("GOODS_DISCOUNT1").toString());    //14
            vpd.put("var15", varOList.get(i).get("GOODS_DISCOUNT2").toString());    //15
            vpd.put("var16", varOList.get(i).getString("MARKET_PRICE"));        //16
            vpd.put("var17", varOList.get(i).get("MEMBER_GIFTPOINTS").toString());    //17
            vpd.put("var18", varOList.get(i).get("MEMBER_LEVGIFTPOINTS").toString());    //18
            vpd.put("var19", varOList.get(i).get("MEMBER_POINTTOPRICE").toString());    //19
            vpd.put("var20", varOList.get(i).get("ACTIVITY_PRICE").toString());    //20
            vpd.put("var21", varOList.get(i).getString("ACTIVITY_DATE1"));        //21
            vpd.put("var22", varOList.get(i).getString("GOODS_THUMBNAIL"));        //22
            vpd.put("var23", varOList.get(i).getString("GOODS_DETAILS"));        //23
            vpd.put("var24", varOList.get(i).get("GOODS_WEIGHT").toString());    //24
            vpd.put("var25", varOList.get(i).get("GOODS_LOW").toString());    //25
            vpd.put("var26", varOList.get(i).get("GOODS_CUSTOM").toString());    //26
            vpd.put("var27", varOList.get(i).get("GOODS_FREETRAFFIC").toString());    //27
            vpd.put("var28", varOList.get(i).getString("GOODS_KEYWORDS"));        //28
            vpd.put("var29", varOList.get(i).getString("GOODS_INTRODUCTION"));        //29
            vpd.put("var30", varOList.get(i).get("GOODS_ORDER").toString());    //30
            vpd.put("var31", varOList.get(i).getString("ACTIVITY_DATE2"));        //31
            vpd.put("var32", varOList.get(i).get("GOODS_DELFLAG").toString());    //32
            vpd.put("var33", varOList.get(i).getString("GOODS_MODEL"));        //33
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    /**
     * 商品列表(大类SUPER_ID,小类GOODS_CATEGORY,为你推荐GOODS_HOT)
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
    public String applist() throws Exception {
        Page page = new Page();
        PageData pd = this.getPageData();
        pd.put("GOODS_FORSALE", 1);
        page.setPd(pd);
        if (StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
            page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
            page.setShowCount(20);
        }
        List<PageData> varList = goodsService.list(page); // 列出Goods列表
        List<PageData> list = new ArrayList<>();
        for (int i = 0; i < varList.size(); i++) {
            PageData goods = varList.get(i);
            String pic = goods.getString("GOODS_PIC");
            if (StringUtils.isNotEmpty(pic)) {
                if (pic.contains(",")) {
                    pic = pic.split(",")[0];
                }
            }
            goods.put("GOODS_PIC", pic);
            String minPrice = getGoodPrice(goods.getString("GOODS_ID"));
            goods.put("GOODS_MINPRICE", minPrice);
            goods.put("TotalPage", page.getTotalPage());
            list.add(goods);
        }
        return gson.toJson(list);
    }

    /**
     * 商品分栏更多(大类SUPER_ID,小类GOODS_CATEGORY)
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/getGoods", produces = "application/json;charset=UTF-8")
    public String appGetGoods() throws Exception {
        PageData pd = this.getPageData();
        String USERNAME = pd.getString("USER_ID");
        pd = goodsService.findById(pd);
        if (0 == pd.get("GOODS_FORSALE") || 1 == pd.get("GOODS_DELFLAG")) {
            return gson.toJson(new PageData());
        }
        pd.put("USER_ID", USERNAME);
        if (StringUtil.isNotEmpty(USERNAME)) {
            PageData pd1 = collectionService.findById(pd);
            if (pd1 != null) {
                pd.put("ISNOCOLLECTION", "YES");//已经收藏
                pd.put("COLLECTION_ID", pd1.getString("COLLECTION_ID"));//收藏ID
            } else {
                pd.put("ISNOCOLLECTION", "NO");//未被收藏
            }
        }
        PageData pd2 = goodsService.findMinByGoodId(pd);
        if (pd2 != null) {
            pd.put("GOODS_MINATTRIBUTEID", pd2.get("ATTRIBUTE_ID"));
            if (isNoSecKill(pd.getString("GOODS_ID"), 1)) {
                pd.put("GOODS_MINPRICE", pd2.get("SECKILLPRICE"));//秒杀价
            } else {
                pd.put("GOODS_MINPRICE", pd2.get("ATTRIBUTEPRICE"));//正常价
            }
            pd.put("GOODS_MARKETPRICE", pd2.get("MARKETPRICE"));//市场价
        } else {
            pd.put("GOODS_MINATTRIBUTEID", "");
            pd.put("GOODS_MINPRICE", 0);//秒杀价
            pd.put("GOODS_MARKETPRICE", 0);//市场价
        }

        //购物车中商品数量
        pd.put("USERNAME", USERNAME);//查购物车数量
        if (StringUtil.isNotEmpty(pd.getString("USERNAME"))) {
            int count = shoppingcarService.goodsCount(pd);
            pd.put("CAR_COUNT", count);
        } else {
            pd.put("CAR_COUNT", 0);
        }
        return gson.toJson(pd);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    //商品价格 实时计算
    public String getGoodPrice(String goods_id) throws Exception {
        return getGoodPrice(goods_id, null);
    }

    public String getGoodPrice(String goods_id, String attribute_id) throws Exception {
        PageData goodatt = new PageData();
        goodatt.put("GOODS_ID", goods_id);
        goodatt.put("ATTRIBUTE_ID", attribute_id);
        PageData attributepd = new PageData();
        if (StringUtil.isNotEmpty(attribute_id)) {//属性ID不是空时
            attributepd = attributeService.findById(goodatt);
            //判断商品是否是秒杀商品
            if (isNoSecKill(goods_id, 1)) {
                return attributepd.getString("SECKILLPRICE");//秒杀价
            } else {
                return attributepd.getString("ATTRIBUTEPRICE");//正常价
            }
        } else {//根据商品ID，如果是秒杀商品则  得到秒杀价 否 正常价
            if (isNoSecKill(goods_id, 1)) {
                return attributeService.findMinSecByGoods(goods_id);
            } else {
                return attributeService.findMinByGoods(goods_id);
            }
        }
    }

    /**
     * 判断商品是否是秒杀商品，如果是则取 秒杀价格
     */
    public boolean isNoSecKill(String GOODSID, int useCount) {
        int result = 0;
        PageData pd = new PageData();
        pd.put("GOODS_ID", GOODSID);
        pd.put("GOODS_USENUM", useCount);
        try {
            result = seckillService.isNoSecGood(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

}
