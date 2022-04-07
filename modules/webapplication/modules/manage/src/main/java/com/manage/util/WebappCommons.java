package com.manage.util;

public class WebappCommons {

	public static final String URL_SEPARATOR = "/";

	public static final String DLINE_SEPARATOR = "|";

	public static final String ULINE_SEPARATOR = "_";

	public static final String WEB_VIEW = "/web-view";

	public static final String WAP_VIEW = "/wap-view";

	public static final String REDIRECT_PRE = "redirect:";

	public static final String USER_AGENT = "User-Agent";

	/**
	 * request attribute key with browser's type is web or wap
	 */
	public static final String VIEW_TYPE_KEY = "viewtype";

	public static final String VIEW_TYPE_V_WEB = "web";

	public static final String VIEW_TYPE_V_WAP = "wap";

	/**
	 * GET请求类型
	 */
	public static final String REQ_METHOD_GET = "GET";

	/**
	 * POST请求类型
	 */
	public static final String REQ_METHOD_POST = "POST";

	/**
	 * 关于页面认证码的请求参数的KEY
	 */
	public static final String REQ_CHECKCODE_KEY = "checkcode";

	/**
	 * 关于返回跳转页面的链接地址的请求参数的KEY
	 */
	public static final String REQ_RETLINK_KEY = "retlink";

	/**
	 * 关于标准系统返回消息提示信息的请求参数的KEY
	 */
	public static final String REQ_MESSAGE_KEY = "message";

	/**
	 * 关于分页功能实现时起始页码的请求参数的KEY
	 */
	public static final String REQ_STARTPAGE_KEY = "sp";

	/**
	 * 关于用户设置每页记录行数时请求参数的KEY
	 */
	public static final String REQ_PAGEROWS_KEY = "pagerows";

	/**
	 * 关于页面布局行数请求参数的KEY
	 */
	public static final String REQ_PAGEROWNUM_KEY = "pagerownum";

	/**
	 * 关于页面布局列数请求参数的KEY
	 */
	public static final String REQ_PAGECOLNUM_KEY = "pagecolnum";

	/**
	 * 关于页面布局宽度请求参数的KEY
	 */
	public static final String REQ_WIDTH_KEY = "width";

	/**
	 * 关于页面布局高度请求参数的KEY
	 */
	public static final String REQ_HEIGHT_KEY = "height";

	/**
	 * 关于页面数据默认类型相关上传参数的KEY
	 */
	public static final String REQ_TYPE_KEY = "type";

	/**
	 * 关于页面数据终端ID相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_TERMID_VALUE = "termid";

	/**
	 * 关于页面数据终端设备ID相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_DEVID_VALUE = "devid";

	/**
	 * 关于页面数据终端号码相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_TERMPHONE_VALUE = "termphone";
	
	public static final String REQ_TYPE_USERPHONE_VALUE = "userphone";

	/**
	 * 关于页面数据终端绑定卡号相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_PHONE_VALUE = "phone";
	
	/**
	 * 关于页面数据终端绑定主用户号码相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_PRIPHONE_VALUE = "priphone";

	/**
	 * 关于页面数据终端绑定次用户号码相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_SLAPHONE_VALUE = "slaphone";

	/**
	 * 关于页面数据终端设备地址相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_TERMADDR_VALUE = "termaddr";

	/**
	 * 关于页面数据终端绑定地址ID相关上传参数的VALUE
	 */
	public static final String REQ_TYPE_AREAID_VALUE = "areaid";

	/**
	 * 关于页面数据编码相关的请求参数的KEY
	 */
	public static final String REQ_CODE_KEY = "code";

	/**
	 * 关于页面数据编码相关集合的请求参数的KEY
	 */
	public static final String REQ_CODELIST_KEY = "codelist";

	/**
	 * 关于页面数据默认动作相关上传参数的KEY
	 */
	public static final String REQ_ACT_KEY = "act";

	/**
	 * 关于页面数据创建动作相关上传参数的VALUE
	 */
	public static final String REQ_ACT_CREATE_VALUE = "create";

	/**
	 * 关于页面数据删除动作相关上传参数的VALUE
	 */
	public static final String REQ_ACT_DELETE_VALUE = "delete";

	/**
	 * 关于页面数据修改动作相关上传参数的VALUE
	 */
	public static final String REQ_ACT_UPDATE_VALUE = "update";

	/**
	 * 关于页面数据查询动作相关上传参数的VALUE
	 */
	public static final String REQ_ACT_SELECT_VALUE = "select";

	/**
	 * 关于页面数据发布动作相关上传参数的VALUE
	 */
	public static final String REQ_ACT_PUBLISH_VALUE = "publish";

	/**
	 * 关于页面数据默认内容相关上传参数的KEY
	 */
	public static final String REQ_CONTENT_KEY = "content";

	/**
	 * 关于页面数据默认起始时间相关上传参数的KEY
	 */
	public static final String REQ_STARTTIME_KEY = "st";

	/**
	 * 关于页面数据默认结束时间相关上传参数的KEY
	 */
	public static final String REQ_ENDTIME_KEY = "et";

	/**
	 * 关于页面数据终端ID相关上传参数的KEY
	 */
	public static final String REQ_TERMID_KEY = "termid";

	/**
	 * 关于页面数据终端ID集合相关上传参数的KEY
	 */
	public static final String REQ_TERMIDS_KEY = "termids[]";

	/**
	 * 关于页面数据终端所属区域ID相关上传参数的KEY
	 */
	public static final String REQ_AREAID_KEY = "areaid";
	
	/**
	 * 关于页面数据终端告警ID相关上传参数的KEY
	 */
	public static final String REQ_WARNID_KEY = "warnid";
	
	/**
	 * 关于页面数据终端告警状态相关上传参数的KEY
	 */
	public static final String REQ_WARNSTATUS_KEY = "warnstatus";

	/**
	 * 关于页面数据终端告警日志ID相关上传参数的KEY
	 */
	public static final String REQ_WARNLOGID_KEY = "warnlogid";

	/**
	 * 关于用户权限认证集合的请求参数的KEY
	 */
	public static final String REQ_AUTHLIST_KEY = "authlist";


}
