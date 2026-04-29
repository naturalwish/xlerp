package com.da.service.system.appuser.impl;

import java.util.List;

import javax.annotation.Resource;

import com.da.entity.system.AppUser;
import com.da.entity.system.Dictionaries;
import com.da.entity.system.User;
import org.springframework.stereotype.Service;

import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.service.system.appuser.AppuserManager;
import com.da.util.PageData;


/**类名称：AppuserService
 * @author DA Q2012
 * 修改时间：2015年11月6日
 */
@Service("appuserService")
public class AppuserService implements AppuserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**通过用户ID获取用户信息和角色信息
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public User getUserAndRoleById(String USER_ID) throws Exception {
		return (User) dao.findForObject("AppuserMapper.getUserAndRoleById", USER_ID);
	}
	
	/**列出某角色下的所有会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("AppuserMapper.listAllAppuserByRorlid", pd);
	}
	
	/**会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.userlistPage", page);
	}
	
	/**通过用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUsername", pd);
	}

	/**通过USER_ID获取名下会员体系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findByInnerCode(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.findByInnerCode", pd);
	}


	/**通过用户名密码获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserByNameAndPwd(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUserInfo", pd);
	}
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByEmail", pd);
	}
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByNumber", pd);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception{
		dao.save("AppuserMapper.saveU", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("AppuserMapper.deleteU", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("AppuserMapper.editU", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUiId", pd);
	}
	/**通过id获取佣金数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserCommission(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getUserCommission", pd);
	}

	/**通过user_id获取分销数据
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List getUserFenxiao(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.getUserFenxiao", page);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.listAllUser", pd);
	}
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception{
		dao.delete("AppuserMapper.deleteAllU", USER_IDS);
	}
	
	/**获取总数
	 * @param value
	 * @throws Exception
	 */
	public PageData getAppUserCount(String value)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUserCount", value);
	}

	/**获取总数
	 * @param id
	 * @throws Exception
	 */
	public String getAppUserInnerCode(String id)throws Exception{
		return (String)dao.findForObject("AppuserMapper.getAppUserInnerCode", id);
	}

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AppUser> listSubUserByParentId(String parentId) throws Exception {
		return (List<AppUser>) dao.findForList("AppuserMapper.listSubUserByParentId", parentId);
	}

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<AppUser> listTreeUser(String parentId) throws Exception {
		List<AppUser> userList = this.listSubUserByParentId(parentId);
		for(AppUser user : userList){
			user.setTreeurl("happuser/treeList.do?USER_ID="+user.getUSER_ID());
			user.setSubUser(this.listTreeUser(user.getUSER_ID()));
			user.setTarget("treeFrame");
		}
		return userList;
	}

	/**子级会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> subListPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.subUserlistPage", page);
	}

	/**通过当前用户ID获取其子级会员订单额，佣金
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> currentUserPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.currentUserlistPage", page);
	}

	/**获取下线一级或二级会员
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getSubUserList(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.getSubUserList", page);
	}

	@Override
	public String getUserIDByUnionID(String unionid) throws Exception {
		return (String)dao.findForObject("AppuserMapper.getUserIDByUnionID", unionid);
	}
}

