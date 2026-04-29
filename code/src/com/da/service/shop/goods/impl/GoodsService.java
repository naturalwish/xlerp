package com.da.service.shop.goods.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.goods.GoodsManager;

/** 
 * 说明： 商品
 * 创建人：DA
 * 创建时间：2018-06-20
 * @version
 */
@Service("goodsService")
public class GoodsService implements GoodsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GoodsMapper.save", pd);
	}

	/**保存扩展分类
	 * @param pd
	 * @throws Exception
	 */
	public void saveCategory(PageData pd)throws Exception{
		dao.save("GoodsMapper.saveCategory", pd);
	}

	/**修改主表价格做排序使用
	 * @param pd
	 * @throws Exception
	 */
	public void editOther(PageData pd)throws Exception{
		dao.save("GoodsMapper.editOther", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("GoodsMapper.delete", pd);
	}

	/**恢复
	 * @param pd
	 * @throws Exception
	 */
	public void undelete(PageData pd)throws Exception{
		dao.delete("GoodsMapper.undelete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("GoodsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("GoodsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoodsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoodsMapper.findById", pd);
	}

	/**获取现有商品最大排序
	 * @param pd
	 * @throws Exception
	 */
	public int findMaxOrder(PageData pd)throws Exception{
		return (int)dao.findForObject("GoodsMapper.findMaxOrder", pd);
	}

	/**通过Goods_ID获取最少价钱的规格
	 * @param pd
	 * @throws Exception
	 */
	public PageData findMinByGoodId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoodsMapper.findMinByGoodId", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("GoodsMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

