package com.da.dao;

import java.util.List;

import javax.annotation.Resource;

import com.da.util.Jurisdiction;
import com.da.util.PageData;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author DA Q2012
 *         修改时间：2015、12、11
 */
@Repository("daoSupport")
public class DaoSupport implements DAO {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 保存对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object save(String str, Object obj) throws Exception {
        try {
            PageData pd = (PageData) obj;
            String mallUploadUrl = (String) Jurisdiction.getSession().getAttribute("mall_upload_url");
            //广告管理图片过滤
            if (pd.get("ADVERTISE_PIC") != null) {
                String picvalue = (String) pd.get("ADVERTISE_PIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("ADVERTISE_PIC", picvalue);
            }
            if (pd.get("GOODS_PIC") != null) {
                String picvalue = (String) pd.get("GOODS_PIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("GOODS_PIC", picvalue);
            }
            if (pd.get("CATEGORY_IMG") != null) {
                String picvalue = (String) pd.get("CATEGORY_IMG");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("CATEGORY_IMG", picvalue);
            }
            if (pd.get("GOODS_DETAILSPIC") != null) {
                String picvalue = (String) pd.get("GOODS_DETAILSPIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("GOODS_DETAILSPIC", picvalue);
            }
            if (pd.get("SORT_IMG") != null) {//文章分类 web_newsort
                String picvalue = (String) pd.get("SORT_IMG");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("SORT_IMG", picvalue);
            }
            obj = (Object) pd;
        } catch (Exception e) {
        }
        return sqlSessionTemplate.insert(str, obj);
    }


    /**
     * 批量更新
     *
     * @param str
     * @param
     * @return
     * @throws Exception
     */
    public Object batchSave(String str, List objs) throws Exception {
        return sqlSessionTemplate.insert(str, objs);
    }

    /**
     * 修改对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object update(String str, Object obj) throws Exception {
        try {
            PageData pd = (PageData) obj;
            String mallUploadUrl = (String) Jurisdiction.getSession().getAttribute("mall_upload_url");
            //广告管理图片过滤
            if (pd.get("ADVERTISE_PIC") != null) {
                String picvalue = (String) pd.get("ADVERTISE_PIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("ADVERTISE_PIC", picvalue);
            }
            if (pd.get("ADVERTISE_DETAILSPIC") != null) {
                String picvalue = (String) pd.get("ADVERTISE_DETAILSPIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("ADVERTISE_DETAILSPIC", picvalue);
            }
            if (pd.get("GOODS_PIC") != null) {
                String picvalue = (String) pd.get("GOODS_PIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("GOODS_PIC", picvalue);
            }
            if (pd.get("CATEGORY_IMG") != null) {
                String picvalue = (String) pd.get("CATEGORY_IMG");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("CATEGORY_IMG", picvalue);
            }
            if (pd.get("GOODS_DETAILSPIC") != null) {
                String picvalue = (String) pd.get("GOODS_DETAILSPIC");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("GOODS_DETAILSPIC", picvalue);
            }
            if (pd.get("SORT_IMG") != null) {//文章分类 web_newsort
                String picvalue = (String) pd.get("SORT_IMG");
                picvalue = picvalue.replaceAll(mallUploadUrl, "");
                pd.put("SORT_IMG", picvalue);
            }
            obj = (Object) pd;
        } catch (Exception e) {
        }
        return sqlSessionTemplate.update(str, obj);
    }

    /**
     * 批量更新
     *
     * @param str
     * @param objs
     * @return
     * @throws Exception
     */
    public void batchUpdate(String str, List objs) throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        //批量执行器
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            if (objs != null) {
                for (int i = 0, size = objs.size(); i < size; i++) {
                    sqlSession.update(str, objs.get(i));
                }
                sqlSession.flushStatements();
                sqlSession.commit();
                sqlSession.clearCache();
            }
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 批量更新
     *
     * @param str
     * @param objs
     * @return
     * @throws Exception
     */
    public Object batchDelete(String str, List objs) throws Exception {
        return sqlSessionTemplate.delete(str, objs);
    }

    /**
     * 删除对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object delete(String str, Object obj) throws Exception {
        return sqlSessionTemplate.delete(str, obj);
    }

    /**
     * 查找对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object findForObject(String str, Object obj) throws Exception {
        return sqlSessionTemplate.selectOne(str, obj);
    }

    /**
     * 查找对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object findForList(String str, Object obj) throws Exception {
        return sqlSessionTemplate.selectList(str, obj);
    }

    public Object findForMap(String str, Object obj, String key, String value) throws Exception {
        return sqlSessionTemplate.selectMap(str, obj, key);
    }

}


