package com.da.service.shop.freight.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.freight.FreightManager;

/**
 * 说明： 运费设置
 * 创建人：DA
 * 创建时间：2018-06-21
 */
@Service("freightService")
public class FreightService implements FreightManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd) throws Exception {
        dao.update("FreightMapper.edit", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("FreightMapper.findById", pd);
    }

}

