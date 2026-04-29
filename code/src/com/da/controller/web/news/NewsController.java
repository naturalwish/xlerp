package com.da.controller.web.news;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.da.service.web.news.NewSortManager;
import com.da.service.web.news.NewsRateManager;
import com.da.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import com.da.controller.base.BaseController;
import com.da.entity.Page;
import com.da.service.web.news.NewsManager;

/**
 * 说明：新闻动态
 * 创建人：DA
 * 创建时间：2017-01-08
 */
@Controller
@RequestMapping(value = "/news")
public class NewsController extends BaseController {

    String menuUrl = "news/list.do"; //菜单地址(权限用)
    @Resource(name = "newsService")
    private NewsManager newsService;

    @Resource(name = "newsortService")
    private NewSortManager newsortService;

    @Resource(name = "newsrateService")
    private NewsRateManager newsrateService;

    private Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增News");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("NEWS_ID", this.get32UUID());    //主键
        pd.put("CTIME", Tools.date2Str(new Date()));    //发表时间
        pd.put("USER_ID", Jurisdiction.getUserID());
        pd.put("LIKES", 0);//点赞数
        newsService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 删除
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "删除News");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        newsService.delete(pd);
        newsrateService.deleteByNEWID(pd);
        out.write("success");
        out.close();
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改News");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        newsService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 修改审核状态
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editState")
    public ModelAndView editState() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改News审核状态");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        newsService.editState(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表News");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = newsService.list(page);    //列出News列表
        mv.setViewName("web/news/news_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 文章评论列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/listRate")
    public ModelAndView listRate(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表News");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = newsService.list(page);    //列出News列表
        List<PageData> list = new LinkedList<PageData>();
        for (int i = 0; i < varList.size(); i++) {
            PageData news = varList.get(i);
            List<PageData> newsrate = newsrateService.listAll(news);
            news.put("newsrate", newsrate);
            news.put("ratelength", newsrate.size());
            list.add(news);
        }
        mv.setViewName("web/news/news_rate_list");
        mv.addObject("varList", list);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 去新增页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goAdd")
    public ModelAndView goAdd() throws Exception {
        ModelAndView mv = this.getModelAndView();
        Page page = new Page();
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> sortlist = newsortService.list(page);
        pd.put("SORTLIST", sortlist);
        mv.setViewName("web/news/news_edit");
        mv.addObject("msg", "save");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
    public ModelAndView goEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        Page page = new Page();
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        pd = newsService.findById(pd);    //根据ID读取
        List<PageData> sortlist = newsortService.list(page);
        pd.put("SORTLIST", sortlist);
        mv.setViewName("web/news/news_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 批量删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "批量删除News");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        pd = this.getPageData();
        List<PageData> pdList = new ArrayList<PageData>();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (null != DATA_IDS && !"".equals(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            newsService.deleteAll(ArrayDATA_IDS);
            newsrateService.deleteAllByNEWID(ArrayDATA_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出News到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("标题");    //1
        titles.add("发表时间");    //2
        titles.add("来源");    //3
        titles.add("图片");    //4
        titles.add("简述");    //5
        titles.add("内容");    //6
        dataMap.put("titles", titles);
        List<PageData> varOList = newsService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("TITLE"));        //1
            vpd.put("var2", varOList.get(i).getString("CTIME"));        //2
            vpd.put("var3", varOList.get(i).getString("STEMFROM"));        //3
            vpd.put("var4", varOList.get(i).getString("IMAGE"));        //4
            vpd.put("var5", varOList.get(i).getString("SHORTCONTENT"));        //5
            vpd.put("var6", varOList.get(i).getString("CONTENT"));        //6
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    /**
     * app获取文章列表
     *
     * @param page
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/list", produces = "application/json;charset=UTF-8")
    public String applist(Page page) throws Exception {
        PageData pd = new PageData();
        pd = this.getPageData();
        if (StringUtil.isNotEmpty(pd.getString("CurrentPage"))) {
            page.setCurrentPage(Integer.parseInt(pd.getString("CurrentPage")));
            page.setShowCount(20);
        }
        pd.put("HIDE", "0");//隐藏状态
        pd.put("STATE", "1");//已审核
        pd.put("TARGET", "app");//APP显示
        page.setPd(pd);
        List<PageData> varlist = newsService.list(page);
        List<PageData> list = new ArrayList<PageData>();
        Iterator<PageData> it = varlist.iterator();
        while (it.hasNext()) {
            PageData news = it.next();
            pd.put("NEWS_ID", news.getString("NEWS_ID"));
            int c = 0;
            if(StringUtil.isNotEmpty(pd.getString("USER_ID"))){
                c = newsService.findByUserId(pd) >= 1 ? 1 : 0;
            }
            news.put("LIKES",StringUtil.isEmpty(String.valueOf(news.get("LIKES")))?"0":String.valueOf(news.get("LIKES")));
            news.put("ISLIKE", c);
            news.put("TotalPage",page.getTotalPage());
            list.add(news);
        }
        return gson.toJson(list);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    /**
     * App发布社区文章
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/save", produces = "application/json;charset=UTF-8")
    public String appSave(@RequestParam(value = "IMAGE", required = false) MultipartFile image, HttpServletRequest request) throws Exception {
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());

        Map<String, Object> map = new HashMap<>();
        int result = 1;
        String message = "发布成功，待审核!";

        //摘要图上传
        String imageDir = DateUtil.getDays(), imageName = "";
        if(image != null){
            if (!image.isEmpty() || image.getSize() > 0) {
                try {
                    String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + imageDir;        //文件上传路径
                    imageName = FileUpload.fileUp(image, filePath, this.get32UUID());               //执行上传
                } catch (Exception e) {
                    result = 0;
                    message = "摘要图上传失败！";
                }
            }}
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        //内容图片上传
        Map<String, String> imageMap = new HashMap<String, String>();
        String content = request.getParameter("CONTENT");// 获得发文content
        try {
            if (multipartResolver.isMultipart(request)) {
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
                Iterator<String> fileNameIterator = fileMap.keySet().iterator();
                while (fileNameIterator.hasNext()) {
                    MultipartFile file = fileMap.get(fileNameIterator.next());
                    if (!file.isEmpty() || file.getSize() > 0) {
                        //保存文件,/plugins/ueditor/jsp/
                        String fileDir = DateUtil.getDays(), fileName = "";
                        String filePath = PathUtil.getClasspath() + "plugins/ueditor/jsp/uploadapp/" + fileDir;  //文件上传路径
                        fileName = FileUpload.fileUp(file, filePath, this.get32UUID());                //执行上传
                        imageMap.put(file.getOriginalFilename(), basePath + "plugins/ueditor/jsp/uploadapp/" + fileDir + "/" + fileName);
                    }
                }
            }

            //替换APP中图片路径
            if (StringUtils.isNotEmpty(content)) {
                String searchImgReg = "<(img|IMG)\\b[^>]*\\b(src|SRC|src2|SRC2)\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>";
                Pattern pattern = Pattern.compile(searchImgReg);    // 讲编译的正则表达式对象赋给pattern
                Matcher matcher = pattern.matcher(content);        // 对字符串content执行正则表达式
                while (matcher.find()) {
                    String quote = matcher.group(3);
                    String imgsrc = (quote == null || quote.trim().length() == 0) ? matcher.group(4).split("\\s+")[0] : matcher.group(4);
                    content = content.replaceAll(imgsrc, imageMap.get(imgsrc.substring(imgsrc.lastIndexOf("/") + 1)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
            message = "内容图片上传失败！";
        }
        //封装发文信息
        PageData pd = this.getPageData();
        pd.put("NEWS_ID", this.get32UUID());    //主键
        pd.put("CTIME", Tools.date2Str(new Date()));    //发表时间
        pd.put("HIDE", "0");//隐藏状态
        pd.put("WEIGHT", 1);//权重
        pd.put("LIKES", 0);//点赞数
        pd.put("STATE", "0");//审核状态
        pd.put("TARGET", "app");//显示目标
        pd.put("TITLE", request.getParameter("TITLE"));//标题
        pd.put("SHORTCONTENT", stripHT(content));//摘要简述
        pd.put("STEMFROM", request.getParameter("NAME"));//文章来源，发文人名称
        pd.put("USER_ID", request.getParameter("USER_ID"));//用户ID
        pd.put("SORTID", request.getParameter("SORTID"));//文章分类
        pd.put("CONTENT", content);//内容

        //摘要图片
        pd.put("IMAGE", basePath + "uploadFiles/uploadImgs/" + imageDir + "/" + imageName);
        try {
            newsService.save(pd);
        } catch (Exception e) {
            result = 0;
            message = "发布失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * weixin发布社区文章
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/save", produces = "application/json;charset=UTF-8")
    public String weixinSave(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = 1;
        String message = "发布成功，待审核!";

        //封装发文信息
        PageData pd = this.getPageData();
        pd.put("NEWS_ID", this.get32UUID());    //主键
        pd.put("CTIME", Tools.date2Str(new Date()));    //发表时间
        pd.put("HIDE", "0");//隐藏状态
        pd.put("WEIGHT", 1);//权重
        pd.put("LIKES", 0);//点赞数
        pd.put("STATE", "0");//审核状态
        pd.put("TARGET", "app");//显示目标
        pd.put("TITLE", request.getParameter("TITLE"));//标题
        pd.put("SHORTCONTENT", request.getParameter("CONTENT"));//摘要简述
        pd.put("STEMFROM", request.getParameter("NAME"));//文章来源，发文人名称
        pd.put("USER_ID", request.getParameter("USER_ID"));//用户ID
        pd.put("SORTID", request.getParameter("SORTID"));//文章分类
        String src0="<img height=\"auto\"; width=\"95%\" src=";
        String src1="alt=\"\">";
        StringBuilder src = new StringBuilder();
        String [] IMAGES=request.getParameter("IMAGE").substring(1,request.getParameter("IMAGE").length()-1).split(",");
        for(int i=0;i<IMAGES.length;i++){
            if(i==0){
                pd.put("IMAGE",IMAGES[i].substring(1,IMAGES[i].length()-1));
            }else{
                src.append(src0);
                src.append(IMAGES[i]);
                src.append(src1);
            }
        }
        pd.put("CONTENT",src.toString()+request.getParameter("CONTENT"));
        try {
            newsService.save(pd);
        } catch (Exception e) {
            result = 0;
            message = "发布失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * weixin发布社区文章，图片上传
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/imageSave", produces = "application/json;charset=UTF-8")
    public String weixinImageSave(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int result = 1;
        //图片路径
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        //内容图片上传
        List<String> imageList = new ArrayList<String>();
        try {
            //创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
            if (multipartResolver.isMultipart(request)) {
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
                Iterator<String> fileNameIterator = fileMap.keySet().iterator();
                while (fileNameIterator.hasNext()) {
                    MultipartFile file = fileMap.get(fileNameIterator.next());
                    if (!file.isEmpty() || file.getSize() > 0) {
                        //保存文件,/plugins/ueditor/jsp/
                        String fileDir = DateUtil.getDays(), fileName = "";
                        String filePath = PathUtil.getClasspath() + "plugins/ueditor/jsp/uploadapp/" + fileDir;  //文件上传路径
                        fileName = FileUpload.fileUp(file, filePath, this.get32UUID());                //执行上传
                        imageList.add(basePath + "plugins/ueditor/jsp/uploadapp/" + fileDir + "/" + fileName);
                    }
                }
            }
        } catch (Exception e) {
            result = 0;
        }
        map.put("RESULT", result);
        map.put("LIST", imageList);
        return gson.toJson(map);
    }

    /**
     * App社区文章点赞
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/likes", produces = "application/json;charset=UTF-8")
    public String appLikes() throws Exception {
        Map<String, Object> map = new HashMap<>();
        PageData pd = this.getPageData();
        PageData pd2 = new PageData();    //根据ID读取
        pd2.put("NEWS_ID", pd.getString("NEWS_ID"));
        pd2 = newsService.findById(pd);
        String likeUsers = pd2.getString("LIKEUSERS");
        String userId = pd.getString("USER_ID");
        int result = 1;
        String message = "点赞成功!";
        if (StringUtil.isNotEmpty(likeUsers)) {
            if (likeUsers.indexOf("," + userId + ",") != -1) {
                userId = "," + userId + ",";
                result = -1;
                message = "取消点赞!";
            } else if (likeUsers.indexOf("," + userId) != -1) {
                userId = "," + userId;
                result = -1;
                message = "取消点赞!";
            } else if (likeUsers.indexOf(userId + ",") != -1) {
                userId = userId + ",";
                result = -1;
                message = "取消点赞!";
            } else if (likeUsers.indexOf(userId) != -1) {
                result = -1;
                message = "取消点赞!";
            }
            pd.put("LIKEUSERS", likeUsers.replace(userId, ""));
        }
        if (result == 1) {
            if (StringUtil.isEmpty(likeUsers)) {
                pd.put("LIKEUSERS", userId);
            } else {
                pd.put("LIKEUSERS", likeUsers + "," + userId);
            }
        }
        try {
            pd.put("RESULT", result);
            newsService.likes(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
            message = "点赞/取消失败！";
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }

    /**
     * App社区文章举报
     *
     * @param
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/app/tipOffs", produces = "application/json;charset=UTF-8")
    public String tipOffs() throws Exception {
        Map<String, Object> map = new HashMap<>();
        PageData pd = this.getPageData();
        PageData pd2 = newsService.findById(pd);
        pd2.put("NEWS_TIPOFFS", pd.getString("NEWS_TIPOFFS"));
        pd2.put("STATE", "0");
        int result = 1;
        String message = "谢谢你的反馈，已通知相关管理人员！";
        try{
            newsService.edit(pd2);
        }catch (Exception e){
            e.printStackTrace();
        }
        map.put("RESULT", result);
        map.put("MESSAGE", message);
        return gson.toJson(map);
    }


    //从html中提取纯文本
    public static String stripHT(String strHtml) {
        String txtcontent = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        if (txtcontent.length() < 51) {
            txtcontent = txtcontent.substring(0, txtcontent.length());
        } else {
            txtcontent = txtcontent.substring(0, 51) + "...";
        }
        return txtcontent;
    }
}
