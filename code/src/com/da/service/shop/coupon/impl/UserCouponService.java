package com.da.service.shop.coupon.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.coupon.UserCouponManager;

/** 
 * 说明： 会员优惠券
 * 创建人：DA
 * 创建时间：2018-07-03
 * @version
 */
@Service("usercouponService")
public class UserCouponService implements UserCouponManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("UserCouponMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("UserCouponMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("UserCouponMapper.edit", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editstate(PageData pd)throws Exception{
		dao.update("UserCouponMapper.editstate", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserCouponMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserCouponMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserCouponMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserCouponMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 已经领取数量
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int count(PageData pd)throws Exception{
		return (int) dao.findForObject("UserCouponMapper.count", pd);
	}
}

