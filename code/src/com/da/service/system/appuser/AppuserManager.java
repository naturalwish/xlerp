package com.da.service.system.appuser;

import java.util.List;

import com.da.entity.Page;
import com.da.entity.system.AppUser;
import com.da.entity.system.Dictionaries;
import com.da.entity.system.User;
import com.da.util.PageData;


/** 会员接口类
 * @author da
 * 修改时间：2015.11.2
 */
public interface AppuserManager {


	/**通过用户ID获取用户信息和角色信息
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public User getUserAndRoleById(String USER_ID) throws Exception;
	
	/**列出某角色下的所有会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception;
	
	/**会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageUser(Page page)throws Exception;
	
	/**通过用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception;

	/**通过USER_ID获取名下会员体系
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findByInnerCode(PageData pd)throws Exception;

	/**通过用户名密码获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserByNameAndPwd(PageData pd)throws Exception;
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception;
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception;
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception;
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */

	public PageData findByUiId(PageData pd)throws Exception;
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserCommission(PageData pd)throws Exception;

	/**通过user_id获取分销数据
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List getUserFenxiao(Page page)throws Exception;
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUser(PageData pd)throws Exception;

	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception;
	
	/**获取总数
	 * @param value
	 * @throws Exception
	 */
	public PageData getAppUserCount(String value)throws Exception;

	/**获取总数
	 * @param id
	 * @throws Exception
	 */
	public String getAppUserInnerCode(String id)throws Exception;

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<AppUser> listTreeUser(String parentId) throws Exception;

	/**子级会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> subListPdPageUser(Page page)throws Exception;

	/**子级会员列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> currentUserPdPageUser(Page page)throws Exception;

	/**获取下线一级或二级会员
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getSubUserList(Page page)throws Exception;

	/**获取总数
	 * @param unionid
	 * @throws Exception
	 */
	public String getUserIDByUnionID(String unionid)throws Exception;
}

