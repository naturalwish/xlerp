package com.da.service.web.news.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.web.news.NewsRateManager;

/** 
 * 说明： 新闻评价
 * 创建人：DA
 * 创建时间：2018-07-03
 * @version
 */
@Service("newsrateService")
public class NewsRateService implements NewsRateManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("NewsRateMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("NewsRateMapper.delete", pd);
	}

	/**删除根据NEWID
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByNEWID(PageData pd)throws Exception{
		dao.delete("NewsRateMapper.deleteByNEWID", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("NewsRateMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("NewsRateMapper.datalistPage", page);
	}

	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("NewsRateMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("NewsRateMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("NewsRateMapper.deleteAll", ArrayDATA_IDS);
	}

	/**批量删除根据NEWID
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllByNEWID(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("NewsRateMapper.deleteAllByNEWID", ArrayDATA_IDS);
	}
	
}

