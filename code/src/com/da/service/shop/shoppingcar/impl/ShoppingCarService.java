package com.da.service.shop.shoppingcar.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.shoppingcar.ShoppingCarManager;

/** 
 * 说明： 购物车
 * 创建人：DA
 * 创建时间：2018-06-28
 * @version
 */
@Service("shoppingcarService")
public class ShoppingCarService implements ShoppingCarManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ShoppingCarMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ShoppingCarMapper.delete", pd);
	}

	public void deleteByUserAndAtt(PageData pd)throws Exception{
		dao.delete("ShoppingCarMapper.deleteByUserAndAtt", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ShoppingCarMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ShoppingCarMapper.datalistPage", page);
	}

	/**用户列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> userList(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ShoppingCarMapper.userlistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ShoppingCarMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ShoppingCarMapper.findById", pd);
	}

	/**通过账号ID+属性ID获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUSERAtt(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ShoppingCarMapper.findByUserAtt", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ShoppingCarMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 用户购物车中商品数量
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int goodsCount(PageData pd)throws Exception{
		return (int) dao.findForObject("ShoppingCarMapper.goodsCount", pd);
	}
}

