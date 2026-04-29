package com.da.service.shop.goods;

import java.util.List;
import com.da.entity.Page;
import com.da.util.PageData;

/** 
 * 说明： 商品接口
 * 创建人：DA
 * 创建时间：2018-06-20
 * @version
 */
public interface GoodsManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;

	/**保存扩展分类
	 * @param pd
	 * @throws Exception
	 */
	public void saveCategory(PageData pd)throws Exception;

	/**修改主表价格做排序使用
	 * @param pd
	 * @throws Exception
	 */
	public void editOther(PageData pd)throws Exception;

	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;

	/**恢复
	 * @param pd
	 * @throws Exception
	 */
	public void undelete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
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

	/**获取现有商品最大排序
	 * @param pd
	 * @throws Exception
	 */
	public int findMaxOrder(PageData pd)throws Exception;

	/**通过Goods_ID获取最少价钱的规格
	 * @param pd
	 * @throws Exception
	 */
	public PageData findMinByGoodId(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

