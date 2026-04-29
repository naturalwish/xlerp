package com.da.service.shop.freight;

import com.da.util.PageData;

/** 
 * 说明： 快递参数接口
 * 创建人：DA
 * 创建时间：2018-06-21
 * @version
 */
public interface ExpressConfigManager{
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
}

