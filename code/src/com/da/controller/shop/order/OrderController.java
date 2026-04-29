package com.da.controller.shop.order;

import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.entity.system.User;
import com.da.service.shop.address.AddressManager;
import com.da.service.shop.coupon.CouponManager;
import com.da.service.shop.coupon.SecKillManager;
import com.da.service.shop.coupon.UserCouponManager;
import com.da.service.shop.freight.ExpressManager;
import com.da.service.shop.freight.FreightManager;
import com.da.service.shop.goods.AttributeManager;
import com.da.service.shop.goods.GoodsManager;
import com.da.service.shop.goods.GoodsRateManager;
import com.da.service.shop.order.InvoiceManager;
import com.da.service.shop.order.OrderDetailManager;
import com.da.service.shop.order.OrderManager;
import com.da.service.shop.order.ReturnBillManager;
import com.da.service.shop.record.RecordManager;
import com.da.service.shop.shoppingcar.ShoppingCarManager;
import com.da.service.system.appuser.AppuserManager;
import com.da.util.*;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 说明：订单 创建人：yyy 创建时间：2017-01-05
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController {
	//private Logger log = Logger.getLogger(this.getClass());
	String menuUrl = "order/list.do"; // 菜单地址(权限用)
	@Resource(name = "orderService")
	private OrderManager orderService;
	@Resource(name="addressService")
	private AddressManager addressService;
	
	@Resource(name = "orderDetailService")
	private OrderDetailManager orderDetailService;

	@Resource(name="recordService")
	private RecordManager recordService;
	
	@Resource(name="expressService")
	private ExpressManager expressService;

	@Resource(name="invoiceService")
	private InvoiceManager invoiceService;

	@Resource(name="returnBillService")
	private ReturnBillManager returnBillService;

	@Resource(name = "freightService")
	private FreightManager freightService;
	@Resource(name = "shoppingcarService")
	private ShoppingCarManager shoppingcarService;
	@Resource(name = "couponService")
	private CouponManager couponService;
	@Resource(name = "goodsService")
	private GoodsManager goodsService;
	@Resource(name = "attributeService")
	private AttributeManager attributeService;
	@Resource(name = "seckillService")
	private SecKillManager seckillService;
    @Resource(name = "usercouponService")
    private UserCouponManager usercouponService;

	@Resource(name="goodsrateService")
	private GoodsRateManager goodsrateService;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;
	
	private Gson gson = new Gson();



	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime = sf.format(new Date());
		pd.put("addtime", addtime);
		pd.put("user_ip", GetIp.getIp(this.getRequest())); //用户ip
		//pd.put("ip_address", GetIp.getAddresses(GetIp.getIp(this.getRequest()), "UTF-8")); //用户ip地址
		pd.put("user_id", user.getUSER_ID());
		pd.put("username", user.getUSERNAME() );
		//pd.put("phone", user.getPHONE() );
		pd.put("order_id", StringUtil.getId()); // 主键
		mv.setViewName("shop/order/order_add");
		mv.addObject("pd", pd);
		mv.addObject("qx", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public ModelAndView save() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "新增Order");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} // 校验权限
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("buyerrate",0);//是否评论，0没有 1评论
		String zindext = pd.getString("zindex");
		int zindex = 0;
		if(null != zindext && !"".equals(zindext)){
			zindex = Integer.parseInt(zindext);
		}
		double goodsTotal=0;
		List<PageData> fieldList = new ArrayList<>();
		PageData goods;
		for(int i=0; i< zindex; i++){
			goods = new PageData();
			String test[]=pd.getString("field"+i).split(",da,");
			goods.put("goods_name",test[0]);
			goods.put("attribute_detail_name",test[1]);
			goods.put("goods_price",test[2]);
			goods.put("goods_count",test[3]);
			goods.put("order_detail_id",this.get32UUID());
			goods.put("order_id",pd.getString("order_id"));
			goods.put("goods_id",test[4]);
			goods.put("goods_pic",test[5]);
			goods.put("sort",i);
			goods.put("status",0);
			goods.put("goods_total",Double.parseDouble(test[2])*Double.parseDouble(test[3]));
			goodsTotal=goodsTotal+(double)goods.get("goods_total");
			fieldList.add(goods);
			logger.info(goods);
		}
		pd.put("order_total",(-Double.parseDouble(pd.getString("coupon_price"))+Double.parseDouble(pd.getString("freight_price"))+goodsTotal));
		pd.put("status",0);//待付款
		pd.put("detaillist",fieldList);
		orderService.save(pd);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String express_name = pd.getString("express_name");
		if (StringUtils.isNotEmpty(express_name)) {
			pd.put("express_name", java.net.URLDecoder.decode(express_name, "utf-8"));
		}
		pd.put("record_note","修改订单");
		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("record_id",this.get32UUID());
		orderService.edit1(pd);
		mv.addObject("msg","success");
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
		logBefore(logger, Jurisdiction.getUsername() + "删除Order");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return;
		} // 校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		orderService.delete(pd);
		out.write("success");
		out.close();
	}

	/**
	 * 关闭订单
	 *
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/close")
	public void close(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "关闭Order");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return;
		} // 校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("closetime",DatetimeUtil.getDatetime());//关闭时间
		pd.put("status",99);
		pd.put("record_note","关闭时间");
		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("record_id",this.get32UUID());
		orderService.edit(pd);
		out.write("success");
		out.close();
	}


	
	/**
	 * 发货
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/send" , produces = "application/json;charset=UTF-8")
	public String send() throws Exception {
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		Map<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		String message = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		String express_name = pd.getString("express_name");
		if (StringUtils.isNotEmpty(express_name)) {
			pd.put("express_name", java.net.URLDecoder.decode(express_name, "utf-8"));
		}
		pd.put("record_note","发货时间");
		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("record_id",this.get32UUID());
		pd.put("status",2);
		pd.put("invoicetime",DatetimeUtil.getDatetime());//发货时间
		//发货单字段信息填写-----------start------------
		//如果已经有发货单，就把已有的全部关闭，状态 1 关闭
		PageData pd2 = new PageData();
		pd2.put("ORDER_ID",pd.getString("order_id"));//订单ID
		pd2.put("STATUS",1);//已关闭
		invoiceService.close(pd2);
		PageData pd1 = new PageData();
		pd1.put("CODE","FHD"+StringUtil.getId());//发货单号
		pd1.put("ORDER_ID",pd.getString("order_id"));//订单ID
		pd1.put("ADDORDERTIME",pd.get("orderaddtime"));//订单时间
		pd1.put("INVOICETIME",DatetimeUtil.getDatetime());//发货时间
		pd1.put("INVOICE_ID",this.get32UUID());//发货单ID
		pd1.put("STATUS",0);//已发货
		pd1.put("CONSIGNEE",pd.get("addr_realname"));//发货时间
		pd1.put("OPERATOR",user.getUSERNAME());
		pd1.put("OPERATIONTIME",DatetimeUtil.getDatetime());//
		invoiceService.save(pd1);
		//发货单字段信息填写-----------end------------
		result = orderService.edit(pd);
		if(result==1){
			message = StringUtil.success_message;
		}else{
			message = StringUtil.error_message;
		}
		map.put("result", result);
		map.put("message", message);
		return gson.toJson(map);
	}

	/**
	 * 退货
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/returnBill" , produces = "application/json;charset=UTF-8")
	public String returnBill() throws Exception {
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		Map<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		String message = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		String express_name = pd.getString("express_name");
		if (StringUtils.isNotEmpty(express_name)) {
			pd.put("express_name", java.net.URLDecoder.decode(express_name, "utf-8"));
		}
		pd.put("record_note","退货时间");
		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("record_id",this.get32UUID());
		pd.put("status",3);
		//退货单字段信息填写-----------start------------
		PageData pd1 = new PageData();
		pd1.put("CODE","THD"+StringUtil.getId());//发货单号
		pd1.put("ORDER_ID",pd.getString("order_id"));//订单ID
		pd1.put("RETURNTIME",DatetimeUtil.getDatetime());//发货时间
		pd1.put("RETURNBILL_ID",this.get32UUID());//退货单ID
		pd1.put("STATUS",0);//已退货
		pd1.put("OPERATOR",user.getUSERNAME());
		pd1.put("OPERATIONTIME",DatetimeUtil.getDatetime());//
		returnBillService.save(pd1);
		//退货单字段信息填写-----------end------------
		result = orderService.edit(pd);
		if(result==1){
			message = StringUtil.success_message;
		}else{
			message = StringUtil.error_message;
		}
		map.put("result", result);
		map.put("message", message);
		return gson.toJson(map);
	}
	
	/*@ResponseBody
	@RequestMapping(value = "/refund" , produces = "application/json;charset=UTF-8")
	public String refund() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		String message = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("record_note","退款时间");
		pd.put("addtime",DatetimeUtil.getDatetime());
		pd.put("record_id",this.get32UUID());
		pd.put("status",4);
		result = orderService.refund(pd);
		if(result==1){
			message = StringUtil.success_message;
		}else{
			message = StringUtil.error_message;
		}
		map.put("result", result);
		map.put("message", message);
		return gson.toJson(map);
	}*/
	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表Order");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData> orderlist = orderService.list(page); // 列出Order列表
		List<PageData> list = new LinkedList<PageData>();
		for (int i = 0; i < orderlist.size(); i++) {
			PageData order = orderlist.get(i);
			List<PageData> orderdetail = orderDetailService.listAll(order);
			order.put("orderdetail", orderdetail);
			order.put("detaillength", orderdetail.size());
			list.add(order);
		}
		mv.setViewName("shop/order/order_list");
		mv.addObject("list", list);
		mv.addObject("pd", pd);
		mv.addObject("qx", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}


	/**
	 * 查看
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/order")
	public ModelAndView order() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = orderService.findById(pd); // 根据ID读取
		List<PageData> orderdetail =  orderDetailService.listAll(pd);
		List<PageData> record = recordService.listAll(pd);
		List<PageData> express = expressService.listAll(pd);
		pd.put("detaillength", orderdetail.size());
		mv.setViewName("shop/order/order_edit");
		mv.addObject("pd", pd);
		mv.addObject("record", record);
		mv.addObject("msg","edit");
		mv.addObject("express", express);
		mv.addObject("orderdetail",orderdetail);
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
		logBefore(logger, Jurisdiction.getUsername() + "批量删除Order");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return null;
		} // 校验权限
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (null != DATA_IDS && !"".equals(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			orderService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		} else {
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 批量删除
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAllDetail")
	@ResponseBody
	public Object deleteAllDetail() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除OrderDetail");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return null;
		} // 校验权限
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (null != DATA_IDS && !"".equals(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			orderDetailService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		} else {
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 获取订单列表(账号:userName,状态)
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
	public String appList() throws Exception {
		Page page = new Page();
		PageData pd = this.getPageData();
		pd.put("user_id",pd.getString("USER_ID"));
		page.setPd(pd);
		if(StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
			page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
			page.setShowCount(20);
		}
		List<PageData> orderlist = orderService.list(page); // 列出Order列表
		List<PageData> list = new LinkedList<PageData>();
		for (int i = 0; i < orderlist.size(); i++) {
			PageData order = orderlist.get(i);
			String goods_names = "";
			List<PageData> orderdetail = orderDetailService.listAll(order);
			order.put("orderdetail", orderdetail);
			if (orderdetail.size() > 1) {
				goods_names = orderdetail.get(0).getString("goods_name") + " 等多件";
			} else {
				goods_names = orderdetail.get(0).getString("goods_name");
			}
			order.put("goods_name",goods_names);
			order.put("TotalPage",page.getTotalPage());
			list.add(order);
		}
		return gson.toJson(list);
	}

	/**
	 * 根据优惠券等 计算总价
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/order_total", produces = "application/json;charset=UTF-8")
	public String order_total() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String goods_id = pd.getString("GOODS_ID");//商品ID
		String goods_count = pd.getString("GOODS_COUNT");//商品数量
		String attribute_detail_id = pd.getString("ATTRIBUTE_DETAIL_ID");//规格型号
		BigDecimal order_total = new BigDecimal(0);// 订单总价
		if (StringUtils.isNotEmpty(goods_id)) {
			if (goods_id.contains(",")) {// 多个商品
				String[] idsArray = goods_id.split(",");
				String[] countsArray = goods_count.split(",");
				String[] detail_idArray = attribute_detail_id.split(",");
				for (int i = 0; i < idsArray.length; i++) {
					PageData goods = new PageData();
					goods.put("GOODS_ID", idsArray[i]);
					PageData info = goodsService.findById(goods);
					//商品已下架，或删除
					if(0 == info.get("GOODS_FORSALE")||1==info.get("GOODS_DELFLAG")){
						map.put("result", 0);
						map.put("message", " 选择商品不存在！请检查！！！");
						return gson.toJson(map);
					}
					BigDecimal goods_price = new BigDecimal(0);
					int count = Integer.parseInt(countsArray[i]);
					PageData detail = null;
					PageData detailpd = new PageData();
					if(!detail_idArray[i].equals("1")){//如果属性id不等于1，则商品有属性，根据属性获取属性详情
						detailpd.put("ATTRIBUTE_ID", detail_idArray[i]);
						detail = attributeService.findById(detailpd);
					}
					//没有属性，则返回
					if(detail==null){
						map.put("result", 0);
						map.put("message", info.getString("GOODS_NAME")+" 选择型号不存在！请检查");
						return gson.toJson(map);
					}
					//判断库存是否充足
					if(!judgeCount(count,(int)detail.get("ATTRIBUTENUM"))){
						map.put("result", 0);
						map.put("message", info.getString("GOODS_NAME")+" 型号为："+detail.getString("ATTRIBUTE")+"的数量库存不足！");
						return gson.toJson(map);
					}
					//判断是否是必杀商品，是的话则使用必杀价格
					if(isNoSecKill(idsArray[i],count)){
						if(detail.get("SECKILLPRICE")!=null){//秒杀价为空的话取值 正常价
							goods_price = (BigDecimal)detail.get("SECKILLPRICE");
						}else{
							goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
						}
					} else {
						goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
					}

					order_total = order_total.add(goods_price.multiply(new BigDecimal(count)));
				}
			} else {// 单个商品
				PageData goods = new PageData();
				goods.put("GOODS_ID", goods_id);
				goods.put("GOODS_FORSALE", 1);
				PageData info = goodsService.findById(goods);
				//能商品已下架，或删除
				if(0 == info.get("GOODS_FORSALE")||1==info.get("GOODS_DELFLAG")){
					map.put("result", 0);
					map.put("message", " 选择商品不存在！请检查！！！");
					return gson.toJson(map);
				}
				BigDecimal goods_price = new BigDecimal(0);
				int count = Integer.parseInt(goods_count);
				PageData detail = null;
				PageData detailpd = new PageData();
				if(!attribute_detail_id.equals("1")){//如果属性id不等于1，则商品有属性，根据属性获取属性详情
					detailpd.put("ATTRIBUTE_ID", attribute_detail_id);
					detail = attributeService.findById(detailpd);
				}
				//没有属性，则返回
				if(detail==null){
					map.put("result", 0);
					map.put("message", info.getString("GOODS_NAME")+" 选择型号不存在！请检查");
					return gson.toJson(map);
				}
				//判断库存是否充足
				if(!judgeCount(count,(int)detail.get("ATTRIBUTENUM"))){
					map.put("result", 0);
					map.put("message", info.getString("GOODS_NAME")+" 型号为："+detail.getString("ATTRIBUTE")+"的数量库存不足！");
					return gson.toJson(map);
				}
				//判断是否是必杀商品，是的话则使用必杀价格
				if(isNoSecKill(goods_id,count)){
					if(detail.get("SECKILLPRICE")!=null){//秒杀价为空的话取值 正常价
						goods_price = (BigDecimal)detail.get("SECKILLPRICE");
					}else{
						goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
					}
				} else {
					goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
				}

				order_total = goods_price.multiply(new BigDecimal(count));
			}
		}
		// 获取运费
		PageData freight = freightService.findById(pd);
		BigDecimal freight_price = (BigDecimal) freight.get("FREIGHT_PRICE");//运费价格
		BigDecimal freight_free_price = (BigDecimal) freight.get("FREIGHT_FREE_PRICE");//免邮价格
		// 运费不为0
		if (freight_price.compareTo(new BigDecimal("0")) != 0) {
			// 免邮金额小于等于订单金额，运费为0
			if (freight_free_price.compareTo(order_total) <= 0) {
				freight_price = new BigDecimal("0");
			}
		}
		map.put("FREIGHT_PRICE",freight_price);
		order_total = order_total.add(freight_price);
		// 根据coupon_id查询优惠券金额,//判断是否选择优惠券，没有的话返回优惠券列表，并选择使用最大优惠的优惠券
		List<PageData> listCOUP = new LinkedList<PageData>();
		PageData coupon = new PageData();
		coupon.put("COUPON_ID", "0");
		coupon.put("COUPON_NAME", "不使用优惠");
		listCOUP.add(coupon);
		Page page = new Page();
		PageData couponpd = new PageData();
		couponpd.put("USER_ID",pd.getString("USER_ID"));
		couponpd.put("NOWTIME",DateUtil.getTime());
		couponpd.put("USE_PRICE",order_total);
		couponpd.put("COUPON_STATE","0");
		page.setPd(couponpd);
		//查询本用户所有的优惠券，按照订单接进行过滤后，以优惠价最高倒序排列
		if(pd.getString("USER_ID")!=null&&pd.getString("USER_ID")!="") {
			List<PageData> varlist = usercouponService.list(page);
			for (int i = 0; i < varlist.size(); i++) {
				listCOUP.add(varlist.get(i));
			}
		}
		//优惠券选择后，计算总金额
		if (pd.getString("COUPON_ID") != null&&!pd.getString("COUPON_ID").equals("")&&!pd.getString("COUPON_ID").equals("0")) {
			coupon = couponService.findById(pd);
			BigDecimal coupon_price = (BigDecimal) coupon.get("COUPON_PRICE");
			order_total = order_total.subtract(coupon_price);
			map.put("CHECKCOUPON_ID", pd.getString("COUPON_ID"));
		} else if (pd.getString("COUPON_ID") == null||pd.getString("COUPON_ID").equals("")){//提交商品时 自动加载最优优惠券，如果存在的话。
			if(listCOUP.size()>1){//用户有大于订单价格的 优惠券，则取最大的优惠券
				coupon = listCOUP.get(1);
				BigDecimal coupon_price = (BigDecimal) coupon.get("COUPON_PRICE");
				order_total = order_total.subtract(coupon_price);
				map.put("CHECKCOUPON_ID", coupon.getString("COUPON_ID"));
			}else{//没有可用优惠券
				map.put("CHECKCOUPON_ID", "0");
			}
		} else if (pd.getString("COUPON_ID").equals("0")){//不适用优惠券
			map.put("CHECKCOUPON_ID", "0");
		}


		map.put("COUPONLIST", listCOUP);
		map.put("ORDER_TOTAL", order_total);
		return gson.toJson(map);
	}

	/**
	 * APP保存
	 *
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/addorder", produces = "application/json;charset=UTF-8")
	public synchronized String addorder() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// PageData pay = new PageData();
		//Map<String, Object>  shopUser  = StringUtil.User(this.getRequest());
		String ip = GetIp.getIp(this.getRequest());
		String ipAddr = GetIp.getAddresses("ip=" + ip, "UTF-8");
		PageData pd = new PageData();
		pd = this.getPageData();
		int result = 0;
		String message = "";
		String cart_id = pd.getString("cart_id");
		String goods_id = pd.getString("GOODS_ID");//传递参数
		String goods_count = pd.getString("GOODS_COUNT");//传递参数
		String attribute_detail_id = pd.getString("ATTRIBUTE_DETAIL_ID");//传递参数
		String pay_way = pd.getString("pay_way");
		String order_id = StringUtil.getId();
		String goods_name = "";
		BigDecimal order_total = new BigDecimal(0);// 订单总价
		BigDecimal total_price = new BigDecimal(0);// 订单原价
		List<PageData> detaillist = new LinkedList<PageData>();
		if (StringUtils.isNotEmpty(goods_id)) {
			if (goods_id.contains(",")) {// 多个商品
				String[] idsArray = goods_id.split(",");
				String[] countsArray = goods_count.split(",");
				String[] detail_idArray = attribute_detail_id.split(",");
				for (int i = 0; i < idsArray.length; i++) {
					PageData goods = new PageData();
					PageData goodspd = new PageData();
					goodspd.put("GOODS_ID", idsArray[i]);
					goods.put("goods_id", idsArray[i]);
					// 某商品的信息
					PageData info = goodsService.findById(goodspd);
					//商品已下架，或删除
					if(0 == info.get("GOODS_FORSALE")||1==info.get("GOODS_DELFLAG")){
						map.put("result", 0);
						map.put("message", " 选择商品不存在！请检查！！！");
						return gson.toJson(map);
					}
					BigDecimal goods_price = new BigDecimal(0);
					int count = Integer.parseInt(countsArray[i]);
					PageData detail = null;
					PageData detailpd = new PageData();
					if(!detail_idArray[i].equals("1")){//如果属性id不等于1，则商品有属性，根据属性获取属性详情
						detailpd.put("ATTRIBUTE_ID", detail_idArray[i]);
						detail = attributeService.findById(detailpd);
					}
					//没有属性，则返回
					if(detail==null){
						map.put("result", 0);
						map.put("message", info.getString("GOODS_NAME")+" 选择型号不存在！请检查");
						return gson.toJson(map);
					}
					//判断库存是否充足
					if(!judgeCount(count,(int)detail.get("ATTRIBUTENUM"))){
						map.put("result", 0);
						map.put("message", info.getString("GOODS_NAME")+" 型号为："+detail.getString("ATTRIBUTE")+"的数量库存不足！");
						return gson.toJson(map);
					}
					goods.put("attribute_detail_id", detail_idArray[i]);
					//判断是否是必杀商品，是的话则使用必杀价格 isNoSecKill
					if(isNoSecKill(idsArray[i],count)){
						if(detail.get("SECKILLPRICE")!=null){//秒杀价为空的话取值 正常价
							goods_price = (BigDecimal)detail.get("SECKILLPRICE");
						}else{
							goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
						}
					}else {
						goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
					}
					goods.put("attribute_detail_name", detail.getString("ATTRIBUTE"));
					goods.put("goods_count", count);
					goods.put("goods_price", goods_price);
					goods.put("goods_name", info.getString("GOODS_NAME"));
					if (i == 0) {
						goods_name = info.getString("GOODS_NAME") + " 等多件";
					}
					String goods_pic = info.getString("GOODS_PIC");
					if (StringUtils.isNotEmpty(goods_pic)) {
						if (goods_pic.contains(",")) {
							goods_pic = goods_pic.split(",")[0];
						}
					}
					BigDecimal goods_total = goods_price.multiply(new BigDecimal(count));
					goods.put("goods_pic", goods_pic);
					goods.put("order_id", order_id);
					goods.put("goods_total", goods_total);
					goods.put("status", 0);
					goods.put("sort", i);
					goods.put("isnotrate", 0);//评价 默认 0 未评价
					goods.put("order_detail_id", this.get32UUID());
					detaillist.add(goods);
					order_total = order_total.add(goods_total);
				}
			} else {// 单个商品
				PageData goods = new PageData();
				PageData goodspd = new PageData();
				goodspd.put("GOODS_ID", goods_id);
				goods.put("goods_id", goods_id);
				PageData info = goodsService.findById(goodspd);
				//商品已下架，或删除
				if(0 == info.get("GOODS_FORSALE")||1 == info.get("GOODS_DELFLAG")){
					map.put("result", 0);
					map.put("message", " 选择商品不存在！请检查！！！");
					return gson.toJson(map);
				}
				BigDecimal goods_price = new BigDecimal("0");
				PageData detail = null;
				PageData detailpd = new PageData();
				int count = Integer.parseInt(goods_count);
				if(!attribute_detail_id.equals("1")){//如果属性id不等于1，则商品有属性，根据属性获取属性详情
					detailpd.put("ATTRIBUTE_ID", attribute_detail_id);
					detail = attributeService.findById(detailpd);
				}
				//没有属性，则返回
				if(detail==null){
					map.put("result", 0);
					map.put("message", info.getString("GOODS_NAME")+" 选择型号不存在！请检查");
					return gson.toJson(map);
				}
				//判断库存是否充足
				if(!judgeCount(count,(int)detail.get("ATTRIBUTENUM"))){
					map.put("result", 0);
					map.put("message", info.getString("GOODS_NAME")+" 型号为："+detail.getString("ATTRIBUTE")+"的数量库存不足！");
					return gson.toJson(map);
				}
				goods.put("attribute_detail_id", attribute_detail_id);
				//判断是否是必杀商品，是的话则使用必杀价格 判断方法 isNoSecKill
				if(isNoSecKill(goods_id,count)){
					if(detail.get("SECKILLPRICE")!=null){//秒杀价为空的话取值 正常价
						goods_price = (BigDecimal)detail.get("SECKILLPRICE");
					}else{
						goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");
					}
				}else {
					goods_price = (BigDecimal)detail.get("ATTRIBUTEPRICE");//型号价格
				}
				goods.put("attribute_detail_name", detail.getString("ATTRIBUTE"));//商品规格型号
				goods.put("goods_count", count);
				goods.put("goods_price", goods_price);
				goods.put("goods_name", info.get("GOODS_NAME"));
				goods_name = info.getString("GOODS_NAME");
				String goods_pic = info.getString("GOODS_PIC");
				if (StringUtils.isNotEmpty(goods_pic)) {
					if (goods_pic.contains(",")) {
						goods_pic = goods_pic.split(",")[0];
					}
				}
				BigDecimal goods_total = goods_price.multiply(new BigDecimal(count));
				goods.put("goods_pic", goods_pic);
				goods.put("order_id", order_id);
				goods.put("goods_total", goods_total);
				goods.put("status", 0);
				goods.put("isnotrate", 0);//评价 默认 0 未评价
				goods.put("sort", 0);
				goods.put("order_detail_id", this.get32UUID());
				detaillist.add(goods);
				order_total = goods_total;
			}
			// 收货地址
			PageData address = addressService.findById(pd);
			if(address == null){
				map.put("result", 0);
				map.put("message", "地址不存在，请选择正确地址！");
				return gson.toJson(map);
			}

			// 获取运费
			PageData freight = freightService.findById(pd);
			BigDecimal freight_price = (BigDecimal) freight.get("FREIGHT_PRICE");
			BigDecimal freight_free_price = (BigDecimal) freight.get("FREIGHT_FREE_PRICE");
			// 运费不为0
			if (freight_price.compareTo(new BigDecimal("0")) != 0) {
				// 免邮金额小于等于订单金额，运费为0
				if (freight_free_price.compareTo(order_total) <= 0) {
					freight_price = new BigDecimal("0");
				}
			}
			order_total = order_total.add(freight_price);
			total_price = order_total;
			// 优惠券信息
			BigDecimal coupon_price = new BigDecimal(0);
			String coupon_id = pd.getString("COUPON_ID");
			if (coupon_id!=null&&!coupon_id.equals("0")&&!coupon_id.equals("")) {
				PageData coupon = couponService.findById(pd);
				coupon_price = (BigDecimal) coupon.get("COUPON_PRICE");
				order_total = order_total.subtract(coupon_price);
			}

			PageData order = new PageData();
			order.put("order_id", order_id);
			order.put("addtime", DatetimeUtil.getDatetime());
			order.put("order_total", order_total);
			order.put("total_price", total_price);
			order.put("coupon_price", coupon_price);
			order.put("coupon_id", coupon_id);
			order.put("freight_price", freight_price);
			order.put("pay_way", pay_way);
			order.put("user_id", pd.getString("USER_ID"));
			if(StringUtil.isEmpty(address.getString("ADDR_REALNAME"))){
				map.put("result", 0);
				map.put("message", "收货人名称为空，请完善！");
				return gson.toJson(map);
			}
			if(StringUtil.isEmpty(address.getString("ADDR_PHONE"))){
				map.put("result", 0);
				map.put("message", "收货人电话为空，请完善！");
				return gson.toJson(map);
			}
			if(StringUtil.isEmpty(address.getString("ADDR_CITY"))){
				map.put("result", 0);
				map.put("message", "收货人城市为空，请完善！");
				return gson.toJson(map);
			}
			if(StringUtil.isEmpty(address.getString("ADDR_DETAILS"))){
				map.put("result", 0);
				map.put("message", "收货人详细地址为空，请完善！");
				return gson.toJson(map);
			}
			order.put("addr_realname", address.getString("ADDR_REALNAME"));
			order.put("addr_phone", address.getString("ADDR_PHONE"));
			order.put("addr_city", address.getString("ADDR_CITY"));
			order.put("address", address.getString("ADDR_DETAILS"));
			order.put("ip_address", ipAddr);
			order.put("user_ip", ip);
			order.put("status", 0);
			order.put("cart_id", cart_id);
			order.put("detaillist", detaillist);
			order.put("record_id", this.get32UUID());
			order.put("record_note", "下单时间");
			result = orderService.save(order);
			if (result == 1) {
				//更新优惠券使用情况，更新限时秒杀数量，更新本型号商品的数量，更新本次购买商品购车车去除等。
				updateAll(order,detaillist);
				map.put("order_id", order_id);
				// 消费金额
				map.put("order_total", order_total);
				// 商品名称
				map.put("goods_name", goods_name);
				message = "提交成功！";
			} else {
				message = "提交订单失败！";
			}
			map.put("result", result);
			map.put("message", message);

		} else {
			map.put("result", 0);
			map.put("message", "购买商品异常！");
		}
		return gson.toJson(map);
	}

	/**
	 * 获取订单
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/getorder", produces = "application/json;charset=UTF-8")
	public String getorder() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// PageData pay = new PageData();
		//Map<String, Object>  shopUser  = StringUtil.shopUser(this.getRequest());
		PageData pd = new PageData();
		pd = this.getPageData();
		int result = 0;
		String message = "";
		String goods_names = "";
		PageData order = orderService.findById(pd);
		if (order != null) {
			String order_id = pd.getString("order_id");
			BigDecimal order_total = (BigDecimal) order.get("order_total");// 订单总价
			pd.put("order_id", order_id);
			List<PageData> list = orderDetailService.listAll(pd);
			if (list.size() > 1) {
				goods_names = list.get(0).getString("goods_name") + " 等多件";
			} else {
				goods_names = list.get(0).getString("goods_name");
			}
			order.put("goods_names", goods_names);
			order.put("orderdetail", list);
			result = 1;
			message = "获取成功！";
		} else {
			message = "订单获取异常！";
		}
		map.put("result", result);
		map.put("message", message);

		return gson.toJson(order);
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
	 * 判断商品库存是否充足
	 */
	public boolean judgeCount(int useNum,int stock){
		if(useNum <= stock){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 更新优惠券使用情况，更新限时秒杀数量，更新本型号商品的数量，更新本次购买商品购车车去除等。
	 */
	public void updateAll(PageData order,List<PageData> detaillist){
		try {
			if(StringUtil.isNotEmpty(order.getString("coupon_id"))&&order.getString("coupon_id")!="0"){//优惠券
				PageData pd = new PageData();
				pd.put("COUPON_ID",order.getString("coupon_id"));
				pd.put("USER_ID",order.getString("user_id"));
				pd.put("USE_TIME",DateUtil.getTime());
				pd.put("COUPON_STATE","1");
				usercouponService.editstate(pd);
			}
			for (PageData detail:detaillist) {
				//循环每个商品型号去掉库存, 去掉本用户购物车中的商品
				PageData pd =new PageData();
				pd.put("ADD_OR_REDUCE","REDUCE");
				pd.put("ATTRIBUTENUM",(int)detail.get("goods_count"));
				pd.put("ATTRIBUTE_ID",detail.getString("attribute_detail_id"));
				attributeService.editNum(pd);
				//删除购物车的商品
				pd.put("GOODS_ATTRIBUTE_ID",detail.getString("attribute_detail_id"));
				pd.put("USERNAME",order.getString("user_id"));
				shoppingcarService.deleteByUserAndAtt(pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 修改订单状态
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/editStatus", produces = "application/json;charset=UTF-8")
	public String editStatus() throws Exception {
		int result = 0;
		String message = "";
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String status = pd.getString("status");
		logger.info("---------------------status:"+pd);
		if(status.equals("1")){//支付完成，代发货
			pd.put("paymenttime",DatetimeUtil.getDatetime());//支付时间
			pd.put("record_note", "订单付款");
			pd.put("pay_way", pd.getString("pay_way"));// alipay  wechat  bank
			pd.put("addtime", DatetimeUtil.getDatetime());
			pd.put("record_id", this.get32UUID());
		} else if (status.equals("5")) {
			pd.put("endtime",DatetimeUtil.getDatetime());//交易成功实践
			pd.put("record_note", "确认收货");
			pd.put("addtime", DatetimeUtil.getDatetime());
			pd.put("record_id", this.get32UUID());
		}else if (status.equals("99")){
			pd.put("closetime",DatetimeUtil.getDatetime());//关闭时间
			pd.put("record_note","关闭订单");
			pd.put("addtime",DatetimeUtil.getDatetime());
			pd.put("record_id",this.get32UUID());
			List<PageData> detaillist=orderDetailService.listAll(pd);
			for (PageData detail:detaillist) {
				//循环每个商品型号增加库存
				PageData pd1 =new PageData();
				pd1.put("ADD_OR_REDUCE","ADD");
				pd1.put("ATTRIBUTENUM",(int)detail.get("goods_count"));
				pd1.put("ATTRIBUTE_ID",detail.getString("attribute_detail_id"));
				attributeService.editNum(pd1);
			}
		}
		result = orderService.edit(pd);
		if (result == 1) {
			message = StringUtil.success_message;
		} else {
			message = StringUtil.error_message;
		}
		map.put("result", result);
		map.put("message", message);
		return gson.toJson(map);
	}

	/**
	 * 商品评价接口
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/app/rate", produces = "application/json;charset=UTF-8")
	public String goodrate(HttpServletRequest request) throws Exception {
		logBefore(logger, "---------------------新增GoodsRate");
		int result = 0;
		String message = "";
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String order_id=pd.getString("ORDER_ID");
		String goods_id=pd.getString("RATE_GOODS");
		String attribute_detail_id = pd.getString("RATE_ATTRIBUTEID");
		pd.put("GOODSRATE_ID",this.get32UUID());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime = sf.format(new Date());
		pd.put("RATE_ADDTIME", addtime);
		goodsrateService.save(pd);//保存评论
		PageData pd2 = new PageData();
		pd2.put("order_id",order_id);
		pd2.put("goods_id",goods_id);
		pd2.put("attribute_detail_id",attribute_detail_id);
		pd2.put("isnotrate",1);
		//查询下本订单下，没有评论的商品数量，除掉现在正在评论的
		int  notrate = orderDetailService.isNoRate(pd2);
		if(notrate==0) {//所有商品已经评论
			pd2.put("status",6);
		}else{
			pd2.put("status",999);//只是在后面Service判断，执行语句，并无实际意义
		}
		/**--------开始处理图片--------------**/
		//图片路径
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		//内容图片上传
		List<String> imageList = new ArrayList<String>();
		StringBuffer ratePicsBf = new StringBuffer();
		try {
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
			if (multipartResolver.isMultipart(request)) {
				//转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
				Iterator<String> fileNameIterator = fileMap.keySet().iterator();
				while (fileNameIterator.hasNext()) {
					MultipartFile file = fileMap.get(fileNameIterator.next());
					if (!file.isEmpty() || file.getSize() > 0) {
						//保存文件,/plugins/ueditor/jsp/
						String fileDir = DateUtil.getDays(), fileName = "";
						String filePath = PathUtil.getClasspath() + "plugins/ueditor/jsp/uploadapp/" + fileDir;  //文件上传路径
						fileName = FileUpload.fileUp(file, filePath, this.get32UUID());                //执行上传
						String picUrl = basePath + "plugins/ueditor/jsp/uploadapp/" + fileDir + "/" + fileName;
						imageList.add(picUrl);
						ratePicsBf.append(picUrl);
						ratePicsBf.append(",");
					}
				}
			}
		} catch (Exception e) {
			result = 0;
		}
		String ratePics = ratePicsBf.toString();
		if(ratePics.endsWith(",")) ratePics = ratePics.substring(0,ratePics.length()-1);
		pd2.put("RATE_PICS",ratePics);
		/**--------结束处理图片--------------**/
		result = orderDetailService.editrate(pd2);
		if(result==1){
			message="评论成功";
		}
		map.put("RESULT", result);
		map.put("MESSAGE", message);
		return gson.toJson(map);
	}

	/**
	 * 显示分销订单列表
	 *
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/fenxiaoList")
	public ModelAndView fenxiaoList(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表Order");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		logger.info("-------------USER_ID------------:"+pd.getString("USER_ID"));
		logger.info("-------------LEVEL------------:"+pd.getString("LEVEL"));
		String innerCode = appuserService.getAppUserInnerCode(pd.getString("USER_ID"));
		logger.info("-------------INNER_CODE------------:"+innerCode);
		if(StringUtil.isNotEmpty(innerCode)){
			pd.put("INNER_CODE",innerCode);
		}else{
			pd.put("INNER_CODE",pd.getString("USER_ID"));
		}
		page.setPd(pd);
		List<PageData> orderlist = orderService.list(page); // 列出Order列表
		List<PageData> list = new LinkedList<PageData>();
		for (int i = 0; i < orderlist.size(); i++) {
			PageData order = orderlist.get(i);
			List<PageData> orderdetail = orderDetailService.listAll(order);
			order.put("orderdetail", orderdetail);
			order.put("detaillength", orderdetail.size());
			list.add(order);
		}
		mv.setViewName("shop/order/order_fenxiaolist");
		mv.addObject("list", list);
		mv.addObject("pd", pd);
		mv.addObject("qx", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}
}
