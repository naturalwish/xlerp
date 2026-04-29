package com.da.service.shop.goods.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.goods.AttributeManager;

/** 
 * 说明： 订单管理(明细)
 * 创建人：DA
 * 创建时间：2018-06-28
 * @version
 */
@Service("attributeService")
public class AttributeService implements AttributeManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AttributeMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AttributeMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AttributeMapper.edit", pd);
	}

	public void editNum(PageData pd)throws Exception{
		dao.update("AttributeMapper.editNum", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AttributeMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AttributeMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception {
		return (PageData) dao.findForObject("AttributeMapper.findById", pd);
	}

	/**通过goods_id获取最低价
	 * @param goodsID
	 * @throws Exception
	 */
	public String findMinByGoods(String goodsID)throws Exception{
		return (String) dao.findForObject("AttributeMapper.findMinByGoods", goodsID);
	}

	/**通过商品id获取总数量
	 * @param goodsID
	 * @throws Exception
	 */
	public int getSumNum(String goodsID)throws Exception{
		return (int)dao.findForObject("AttributeMapper.getSumNum", goodsID);
	}

	public String findMinSecByGoods(String goodsID)throws Exception{
		return (String) dao.findForObject("AttributeMapper.findMinSecByGoods", goodsID);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AttributeMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findCount(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AttributeMapper.findCount", pd);
	}
	
}

