package com.da.service.shop.goods;

import java.util.List;
import com.da.entity.Page;
import com.da.util.PageData;

/** 
 * 说明： 订单管理(明细)接口
 * 创建人：DA
 * 创建时间：2018-06-28
 * @version
 */
public interface AttributeManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editNum(PageData pd)throws Exception;
	
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

	/**通过id获取数据
	 * @param goodsID
	 * @throws Exception
	 */
	public String findMinByGoods(String goodsID)throws Exception;

	/**通过商品id获取总数量
	 * @param goodsID
	 * @throws Exception
	 */
	public int getSumNum(String goodsID)throws Exception;

	/**通过id获取数据
	 * @param goodsID
	 * @throws Exception
	 */
	public String findMinSecByGoods(String goodsID)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception;
	
}

