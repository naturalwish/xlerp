package com.da.service.shop.freight.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.util.PageData;
import com.da.service.shop.freight.ExpressConfigManager;

/** 
 * 说明： 快递参数
 * 创建人：DA
 * 创建时间：2018-06-21
 * @version
 */
@Service("expressconfigService")
public class ExpressConfigService implements ExpressConfigManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ExpressConfigMapper.edit", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ExpressConfigMapper.findById", pd);
	}
	
}

