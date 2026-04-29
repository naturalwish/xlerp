package com.da.service.system.log.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.util.Tools;
import com.da.util.UuidUtil;
import com.da.service.system.log.LogManager;

/** 
 * 说明： 操作日志记录
 * 创建人：DA
 * 创建时间：2016-05-10
 * @version
 */
@Service("logService")
public class LogService implements LogManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String USERNAME, String CONTENT)throws Exception{
		PageData pd = new PageData();
		pd.put("USERNAME", USERNAME);					//用户名
		pd.put("CONTENT", CONTENT);						//事件
		pd.put("LOG_ID", UuidUtil.get32UUID());		//主键
		pd.put("CZTIME", Tools.date2Str(new Date()));	//操作时间
		dao.save("LogMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("LogMapper.delete", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("LogMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("LogMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("LogMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("LogMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

