package com.da.service.shop.order;

import java.util.ArrayList;
import java.util.List;
import com.da.entity.Page;
import com.da.entity.shop.order.OrderDetail;
import com.da.util.PageData;

/** 
 * 说明： 订单接口
 * 创建人：yyy
 * 创建时间：2017-01-05
 * @version
 */
public interface OrderDetailManager{

	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public int edit(PageData pd)throws Exception;
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**批量新增
	 * @param ArrayList
	 * @throws Exception
	 */
	public void save(List<OrderDetail> ArrayList)throws Exception;

	/**修改评价状态和单据状态
	 * @param pd
	 * @throws Exception
	 */
	public int editrate(PageData pd)throws Exception;

	/**查询是否有没评论商品
	 * @param pd
	 * @throws Exception
	 */
	public int isNoRate(PageData pd)throws Exception;
}

