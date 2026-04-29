package com.da.service.system.appuser.impl;

import java.util.List;
import javax.annotation.Resource;

import com.da.service.system.appuser.AppuserBrowseManager;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.system.appuser.AppuserBrowseManager;

/** 
 * 说明： 商品浏览记录
 * 创建人：DA
 * 创建时间：2018-09-04
 * @version
 */
@Service("appuserbrowseService")
public class AppuserBrowseService implements AppuserBrowseManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AppuserBrowseMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AppuserBrowseMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AppuserBrowseMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppuserBrowseMapper.datalistPage", page);
	}

	/**根据用户查询商品浏览记录
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listByUser(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppuserBrowseMapper.listByUser", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AppuserBrowseMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserBrowseMapper.findById", pd);
	}

	/**通过用户及商品获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserGoods(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserBrowseMapper.findByUserGoods", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AppuserBrowseMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

