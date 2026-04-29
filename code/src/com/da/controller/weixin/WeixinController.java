package com.da.controller.weixin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.da.controller.weixin.pay.util.RandomUtil;
import com.da.service.system.appuser.AppuserManager;
import com.da.util.*;
import com.da.util.weixin.Weixin;
import net.sf.json.JSONObject;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.MySecurity;
import org.marker.weixin.msg.Data4Item;
import org.marker.weixin.msg.Msg4Event;
import org.marker.weixin.msg.Msg4Image;
import org.marker.weixin.msg.Msg4ImageText;
import org.marker.weixin.msg.Msg4Link;
import org.marker.weixin.msg.Msg4Location;
import org.marker.weixin.msg.Msg4Text;
import org.marker.weixin.msg.Msg4Video;
import org.marker.weixin.msg.Msg4Voice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.da.controller.base.BaseController;
import com.da.service.weixin.command.CommandService;
import com.da.service.weixin.imgmsg.ImgmsgService;
import com.da.service.weixin.textmsg.TextmsgService;

/**
 * 
* 类名称：WeixinController.java
* 类描述： 微信公共平台开发 
* @author DA
* 作者单位： 
* 联系方式：
* 创建时间：2014年7月10日
* @version 1.0
 */
@Controller
@RequestMapping(value="/weixin")
public class WeixinController extends BaseController{
	
	@Resource(name="textmsgService")
	private TextmsgService textmsgService;
	@Resource(name="commandService")
	private CommandService commandService;
	@Resource(name="imgmsgService")
	private ImgmsgService imgmsgService;
	
	/**
	 * 接口验证,总入口
	 * @param out
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	 @RequestMapping(value="/index")
	 public void index(
			 PrintWriter out,
			 HttpServletRequest request,
			 HttpServletResponse response
			 ) throws Exception{     
		 logBefore(logger, "微信接口");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String signature = pd.getString("signature");		//微信加密签名
			String timestamp = pd.getString("timestamp");		//时间戳
			String nonce	 = pd.getString("nonce");			//随机数
			String echostr 	 = pd.getString("echostr");			//字符串

			if(null != signature && null != timestamp && null != nonce && null != echostr){/* 接口验证  */
				logBefore(logger, "进入身份验证");
			    List<String> list = new ArrayList<String>(3) { 
				    private static final long serialVersionUID = 2621444383666420433L; 
				    public String toString() {  // 重写toString方法，得到三个参数的拼接字符串
				               return this.get(0) + this.get(1) + this.get(2); 
				           } 
				         }; 
				   list.add(Tools.readTxtFile(Const.WEIXIN)); 		//读取Token(令牌)
				   list.add(timestamp); 
				   list.add(nonce); 
				   Collections.sort(list);							// 排序 
				   String tmpStr = new MySecurity().encode(list.toString(),MySecurity.SHA_1);								// SHA-1加密 
				    if (signature.equals(tmpStr)) { 
				           out.write(echostr);						// 请求验证成功，返回随机码 
				     }else{ 
				           out.write(""); 
			       } 
				out.flush();
				out.close(); 
			}else{/* 消息处理  */
				logBefore(logger, "进入消息处理");
				response.reset();
				sendMsg(request,response);
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	 /**
	  * 处理微信服务器发过来的各种消息，包括：文本、图片、地理位置、音乐等等 
	  * @param request
	  * @param response
	  * @throws Exception
	  */
	 public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{ 

         InputStream is = request.getInputStream(); 
         OutputStream os = response.getOutputStream(); 

         final DefaultSession session = DefaultSession.newInstance(); 
         session.addOnHandleMessageListener(new HandleMessageAdapter(){ 
        	 
        	/**
        	 * 事件
        	 */
        	@Override
        	public void onEventMsg(Msg4Event msg) {
        		/** msg.getEvent()
        		 * unsubscribe：取消关注 ; subscribe：关注
        		 */
				logBefore(logger, "进入关注事件");
        		if("subscribe".equals(msg.getEvent())){
        			logger.info("--msg----:"+msg);
        			returnMSg(msg,null,"关注");
					String toUserName,fromUserName,createTime;

					toUserName = msg.getToUserName();
					fromUserName = msg.getFromUserName();
					createTime = msg.getCreateTime();
					logger.info("------------fromUserName--------:"+fromUserName);
					logger.info("------------EventKey--------:"+msg.getEventKey());
					if(msg.getEventKey().indexOf("qrscene_")!=-1){
						String parentUinonid=msg.getEventKey().replace("qrscene_","").trim();
						logger.info("------------parentUinonid--------:"+parentUinonid);
						//关注后注册用户
						try {
							registerUser(fromUserName,parentUinonid);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}else if("CLICK".equals(msg.getEvent())){
        			returnMSg(msg,null,msg.getEventKey());
        		}
        	}
        	
        	 /**
        	  * 收到的文本消息
        	  */
        	 @Override 
             public void onTextMsg(Msg4Text msg) { 
                returnMSg(null,msg,msg.getContent().trim());
             }
        	 
        	 @Override
        	public void onImageMsg(Msg4Image msg) {
        		// TODO Auto-generated method stub
        		super.onImageMsg(msg);
        	}
        	 
        	 @Override
        	public void onLocationMsg(Msg4Location msg) {
        		// TODO Auto-generated method stub
        		super.onLocationMsg(msg);
        	}
        	 
        	@Override
        	public void onLinkMsg(Msg4Link msg) {
        		// TODO Auto-generated method stub
        		super.onLinkMsg(msg);
        	}
        	
        	@Override
        	public void onVideoMsg(Msg4Video msg) {
        		// TODO Auto-generated method stub
        		super.onVideoMsg(msg);
        	}
        	
        	@Override
        	public void onVoiceMsg(Msg4Voice msg) {
        		// TODO Auto-generated method stub
        		super.onVoiceMsg(msg);
        	}
        	
        	@Override
        	public void onErrorMsg(int errorCode) {
        		// TODO Auto-generated method stub
        		super.onErrorMsg(errorCode);
        	}
        	
        	/**
        	 * 返回消息
        	 * @param emsg
        	 * @param tmsg
        	 * @param getmsg
        	 */
        	public void returnMSg(Msg4Event emsg, Msg4Text tmsg, String getmsg){
        		 PageData msgpd;
                 PageData pd = new PageData();
                 String toUserName,fromUserName,createTime;
                 if(null == emsg){
                	 toUserName = tmsg.getToUserName();
                	 fromUserName = tmsg.getFromUserName();
                	 createTime = tmsg.getCreateTime();
                 }else{
                	 toUserName = emsg.getToUserName();
                	 fromUserName = emsg.getFromUserName();
                	 createTime = emsg.getCreateTime();
                 }
                 pd.put("KEYWORD", getmsg);
                 try {
 						msgpd = textmsgService.findByKw(pd);
 						if(null != msgpd){
 							 Msg4Text rmsg = new Msg4Text(); 
 		                     rmsg.setFromUserName(toUserName); 
 		                     //System.out.println(toUserName + "====" + fromUserName);
 		                     //fromUserName  关注者的身份ID
 		                     rmsg.setToUserName(fromUserName); 
 		                     //rmsg.setFuncFlag("0"); 
 		                     rmsg.setContent(msgpd.getString("CONTENT")); //回复文字消息
 		                     session.callback(rmsg); 
 						}else{
 							msgpd = imgmsgService.findByKw(pd);
 							if(null != msgpd){
 								 Msg4ImageText mit = new Msg4ImageText(); 
 				                 mit.setFromUserName(toUserName); 
 				                 mit.setToUserName(fromUserName);  
 				                 mit.setCreateTime(createTime);  
 								 //回复图文消息
 				                 if(null != msgpd.getString("TITLE1") && null != msgpd.getString("IMGURL1")){
 				                	 Data4Item d1 = new Data4Item(msgpd.getString("TITLE1"),msgpd.getString("DESCRIPTION1"),msgpd.getString("IMGURL1"),msgpd.getString("TOURL1")+"&FHWXID="+fromUserName);  
 				                	 mit.addItem(d1);
 				                	 
 				                	 if(null != msgpd.getString("TITLE2") && null != msgpd.getString("IMGURL2") && !"".equals(msgpd.getString("TITLE2").trim()) && !"".equals(msgpd.getString("IMGURL2").trim())){
 					                	 Data4Item d2 = new Data4Item(msgpd.getString("TITLE2"),msgpd.getString("DESCRIPTION2"),msgpd.getString("IMGURL2"),msgpd.getString("TOURL2"));  
 					                	 mit.addItem(d2);
 					                 }
 				                	 if(null != msgpd.getString("TITLE3") && null != msgpd.getString("IMGURL3") && !"".equals(msgpd.getString("TITLE3").trim()) && !"".equals(msgpd.getString("IMGURL3").trim())){
 					                	 Data4Item d3 = new Data4Item(msgpd.getString("TITLE3"),msgpd.getString("DESCRIPTION3"),msgpd.getString("IMGURL3"),msgpd.getString("TOURL3"));  
 					                	 mit.addItem(d3);
 					                 }
 				                	 if(null != msgpd.getString("TITLE4") && null != msgpd.getString("IMGURL4") && !"".equals(msgpd.getString("TITLE4").trim()) && !"".equals(msgpd.getString("IMGURL4").trim())){
 					                	 Data4Item d4 = new Data4Item(msgpd.getString("TITLE4"),msgpd.getString("DESCRIPTION4"),msgpd.getString("IMGURL4"),msgpd.getString("TOURL4"));  
 					                	 mit.addItem(d4);
 					                 }
 				                	 if(null != msgpd.getString("TITLE5") && null != msgpd.getString("IMGURL5") && !"".equals(msgpd.getString("TITLE5").trim()) && !"".equals(msgpd.getString("IMGURL5").trim())){
 					                	 Data4Item d5 = new Data4Item(msgpd.getString("TITLE5"),msgpd.getString("DESCRIPTION5"),msgpd.getString("IMGURL5"),msgpd.getString("TOURL5"));  
 					                	 mit.addItem(d5);
 					                 }
 				                	 if(null != msgpd.getString("TITLE6") && null != msgpd.getString("IMGURL6") && !"".equals(msgpd.getString("TITLE6").trim()) && !"".equals(msgpd.getString("IMGURL6").trim())){
 					                	 Data4Item d6 = new Data4Item(msgpd.getString("TITLE6"),msgpd.getString("DESCRIPTION6"),msgpd.getString("IMGURL6"),msgpd.getString("TOURL6"));  
 					                	 mit.addItem(d6);
 					                 }
 				                	 if(null != msgpd.getString("TITLE7") && null != msgpd.getString("IMGURL7") && !"".equals(msgpd.getString("TITLE7").trim()) && !"".equals(msgpd.getString("IMGURL7").trim())){
 					                	 Data4Item d7 = new Data4Item(msgpd.getString("TITLE7"),msgpd.getString("DESCRIPTION7"),msgpd.getString("IMGURL7"),msgpd.getString("TOURL7"));  
 					                	 mit.addItem(d7);
 					                 }
 				                	 if(null != msgpd.getString("TITLE8") && null != msgpd.getString("IMGURL8") && !"".equals(msgpd.getString("TITLE8").trim()) && !"".equals(msgpd.getString("IMGURL8").trim())){
 					                	 Data4Item d8 = new Data4Item(msgpd.getString("TITLE8"),msgpd.getString("DESCRIPTION8"),msgpd.getString("IMGURL8"),msgpd.getString("TOURL8"));  
 					                	 mit.addItem(d8);
 					                 }
 				                 }
 				                 //mit.setFuncFlag("0");   
 				                 session.callback(mit); 
 							}else{
 								msgpd = commandService.findByKw(pd);
 								if(null != msgpd){
 			             			Runtime runtime = Runtime.getRuntime(); 
 			             			runtime.exec(msgpd.getString("COMMANDCODE"));
 								}else{
 									 Msg4Text rmsg = new Msg4Text(); 
 				                     rmsg.setFromUserName(toUserName); 
 				                     rmsg.setToUserName(fromUserName); 
 				                     rmsg.setContent("无匹配结果");
 				                     session.callback(rmsg); 
 								}
 							}
 						}
 				} catch (Exception e1) {
 					logBefore(logger, "匹配错误");
 				}
        	}
        	
        }); 

         /*必须调用这两个方法   如果不调用close方法，将会出现响应数据串到其它Servlet中。*/ 
         session.process(is, os);	//处理微信消息  
         session.close();			//关闭Session 
     }

	@Value("${wx.appid_gzh}")
	private String APP_ID_GZH;

	@Value("${wx.appsecret_gzh}")
	private String APP_SECRET_GZH;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;

	public void registerUser(String openid,String parentUnionid ) throws Exception{
		String unionid = "";// 获取微信用户唯一标识（unionid）
		String access_token = Weixin.getAccess_token(APP_ID_GZH, APP_SECRET_GZH);
		String requestUrl = Weixin.userinfo_url2.replace("ACCESS_TOKEN", access_token).
				replace("OPENID", openid);
		JSONObject json = Weixin.httpRequst(requestUrl, "GET", null);
		if (json.has("unionid")) {
			unionid = String.valueOf(json.get("unionid"));
		}
		PageData pd =new PageData();
		pd.put("USERNAME",unionid);
		logger.info("-----unionid1:" + unionid);
		PageData appUser;
		appUser = appuserService.findByUsername(pd);
		if (appUser == null) { // 用户不存在
			logger.info("-----crate---user--:" + unionid);
			appUser = new PageData();
			appUser.put("USER_ID", this.get32UUID());//ID
			appUser.put("PASSWORD", MD5.md5("123456"));//默认密码123456
			appUser.put("USERNAME", "TQ-" + RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 12));
			//用户账号
			appUser.put("NAME", json.get("nickname"));//用户名称
			appUser.put("SFID", json.get("headimgurl"));//头像
			appUser.put("START_TIME", DatetimeUtil.getDatetime());//注册时间
			appUser.put("RIGHTS", "");//权限
			appUser.put("STATUS", "1");//用户状态
			appUser.put("ROLE_ID", "1b67fc82ce89457a8347ae53e43a347e"); //注册默认为初级会员
			appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
			appUser.put("IP", GetIp.getIp(this.getRequest()));//上次登录IP
			//添加上下级关系
			if(StringUtil.isNotEmpty(parentUnionid)){
				String parentUserID=appuserService.getUserIDByUnionID(parentUnionid);
				String parentUserInnerCode = appuserService.getAppUserInnerCode(parentUserID);
				appUser.put("INNER_CODE",parentUserInnerCode+appUser.getString("USER_ID"));
				appUser.put("PARENT_ID",parentUserID);
			}else{
				appUser.put("INNER_CODE",appUser.getString("USER_ID"));
			}

			appUser.put("OPEN_ID", openid);
			appUser.put("UNION_ID", unionid);
			appuserService.saveU(appUser);
		} else {
			logger.info("-----y--c--j---:" + unionid);
			appUser.put("IP", appUser.getString("LAST_LOGIN"));//上次登录IP
			appUser.put("LAST_LOGIN", GetIp.getIp(this.getRequest()));//最近登录IP
			appuserService.editU(appUser);
		}
	}
}

