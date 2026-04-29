package com.da.service.shop.order.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.da.entity.shop.order.OrderDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.dao.DaoSupport;
import com.da.service.shop.order.OrderDetailManager;

/**
 * 说明： 订单 创建人：易钱科技 yyy 创建时间：2017-01-05
 * 
 * @version
 */
@Service("orderDetailService")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDetailService implements OrderDetailManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		dao.delete("OrderDetailMapper.delete", pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	
	public int edit(PageData pd){
		int result = 0 ;
		try {
			if((int)pd.get("status_count")==0){
				dao.update("OrderMapper.edit", pd);
			}
			dao.update("OrderDetailMapper.edit", pd);
			result=1;
		} catch (Exception e) {
			e.getStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page) throws Exception {
		return (List<PageData>) dao.findForList("OrderDetailMapper.datalistPage", page);
	}


	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("OrderDetailMapper.listAll", pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("OrderDetailMapper.findById", pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("OrderDetailMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 批量新增
	 *
	 * @param ArrayList
	 * @throws Exception
	 */
	public void save(List<OrderDetail> ArrayList) throws Exception {
		dao.delete("OrderDetailMapper.save", ArrayList);
	}

	public int editrate(PageData pd){
		int result = 0 ;
		try {
			dao.update("OrderDetailMapper.edit_rate_id", pd);
			if((int)pd.get("status")==6){
				dao.update("OrderMapper.edit", pd);
				dao.update("OrderDetailMapper.edit_order_id", pd);
			}
			result=1;
		} catch (Exception e) {
			e.getStackTrace();
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	/**查询是否有没评论商品
	 * @param pd
	 * @throws Exception
	 */
	public int isNoRate(PageData pd)throws Exception{
		return (int)dao.findForObject("OrderDetailMapper.isNoRate", pd);
	}


}
