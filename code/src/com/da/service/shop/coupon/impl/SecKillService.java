package com.da.service.shop.coupon.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.coupon.SecKillManager;

/** 
 * 说明： 限时秒杀
 * 创建人：DA
 * 创建时间：2018-06-28
 * @version
 */
@Service("seckillService")
public class SecKillService implements SecKillManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SecKillMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SecKillMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SecKillMapper.edit", pd);
	}

	/**修改活动状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		dao.update("SecKillMapper.editState", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SecKillMapper.datalistPage", page);
	}

	/**列表APP
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listApp(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SecKillMapper.datalistPageApp", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SecKillMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SecKillMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SecKillMapper.deleteAll", ArrayDATA_IDS);
	}
	/**查询商品是否在必杀
	 * @param pd
	 * @throws Exception
	 */
	public int isNoSecGood(PageData pd)throws Exception{
		return (int)dao.findForObject("SecKillMapper.isNoSecGood", pd);
	}
}

