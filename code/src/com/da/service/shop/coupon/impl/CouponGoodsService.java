package com.da.service.shop.coupon.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.da.dao.DaoSupport;
import com.da.entity.Page;
import com.da.util.PageData;
import com.da.service.shop.coupon.CouponGoodsManager;

/**
 * 说明： 优惠商品
 * 创建人：DA
 * 创建时间：2018-06-26
 */
@Service("coupongoodsService")
public class CouponGoodsService implements CouponGoodsManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 新增
     *
     * @param varList
     * @throws Exception
     */
    public void save(List<PageData> varList) throws Exception {
        dao.save("CouponGoodsMapper.save", varList);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd) throws Exception {
        dao.delete("CouponGoodsMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd) throws Exception {
        dao.update("CouponGoodsMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("CouponGoodsMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("CouponGoodsMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("CouponGoodsMapper.findById", pd);
    }

    /**
     * 通过GOODSID AND COUPONID获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findBygoodsIDAndCouponID(PageData pd) throws Exception {
        return (PageData) dao.findForObject("CouponGoodsMapper.findBygoodsIDAndCouponID", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("CouponGoodsMapper.deleteAll", ArrayDATA_IDS);
    }

}

