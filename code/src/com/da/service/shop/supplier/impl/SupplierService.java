package com.da.service.shop.supplier.impl;

import java.util.List;
import javax.annotation.Resource;

import com.da.entity.system.Dictionaries;
import com.da.service.system.dictionaries.DictionariesManager;
import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.supplier.SupplierManager;

/** 
 * 说明： 供应商
 * 创建人：DA
 * 创建时间：2018-06-21
 * @version
 */
@Service("supplierService")
public class SupplierService implements SupplierManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
    @Resource(name="dictionariesService")
    private DictionariesManager dictionariesService;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SupplierMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SupplierMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SupplierMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SupplierMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SupplierMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SupplierMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SupplierMapper.deleteAll", ArrayDATA_IDS);
	}

    /**
     * 获取所有数据并填充每条数据的子级列表(递归处理)
     * @param
     * @return
     * @throws Exception
     */
    public List<Dictionaries> listAreaDict(String parentId) throws Exception {
        List<Dictionaries> dictList = dictionariesService.listSubDictByParentId(parentId);
        for(Dictionaries dict : dictList){
            dict.setTreeurl("supplier/list.do?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
            dict.setSubDict(this.listAreaDict(dict.getDICTIONARIES_ID()));
            dict.setTarget("treeFrame");
        }
        return dictList;
    }
	
}

