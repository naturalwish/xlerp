package com.da.service.shop.coupon;

import java.util.List;
import com.da.entity.Page;
import com.da.util.PageData;

/** 
 * 说明： 会员优惠券接口
 * 创建人：DA
 * 创建时间：2018-07-03
 * @version
 */
public interface UserCouponManager{

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

	/**修改状态和时间
	 * @param pd
	 * @throws Exception
	 */
	public void editstate(PageData pd)throws Exception;
	
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

	/**
	 * 已经领取数量
	 * @param pd
	 * @return
	 * @throws Exception
     */
	public int count(PageData pd)throws Exception;
}

