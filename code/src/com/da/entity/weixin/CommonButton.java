package com.da.entity.weixin;

/** 二级菜单
 * @author DA
 * 2016.11.1
 */
public class CommonButton extends Button{
	
	private String type;		//菜单类型
	private String key;			//key值
	private String url;			//网页链接，用户点击菜单可打开链接，不超过1024字节。type为miniprogram时，不支持小程序的老版本客户端将打开本url。
	private String appid;		//小程序的appid（仅认证公众号可配置）
	private String pagepath;	//小程序的页面路径

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppid() {
		return appid;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}
	public String getPagepath() {
		return pagepath;
	}
}
