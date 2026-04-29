package com.da.service.web.news.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.web.news.NewsManager;

/** 
 * 说明： 新闻动态
 * 创建人：DA
 * 创建时间：2017-01-08
 * @version
 */
@Service("newsService")
public class NewsService implements NewsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("NewsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("NewsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("NewsMapper.edit", pd);
	}

	/**修改审核状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		dao.update("NewsMapper.editState", pd);
	}

	/**点赞
	 * @param pd
	 * @throws Exception
	 */
	public void likes(PageData pd)throws Exception{
		dao.update("NewsMapper.likes", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("NewsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("NewsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("NewsMapper.findById", pd);
	}

	/**通过USER_ID获取是否点赞
	 * @param pd
	 * @throws Exception
	 */
	public int findByUserId(PageData pd)throws Exception{
		return (int)dao.findForObject("NewsMapper.findByUserId", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("NewsMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

