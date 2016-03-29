package com.email;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailMeta implements Serializable {

	private static final long serialVersionUID = -9198182999l;

	/**
	 * 来自,默认service@cc.com
	 */
	private static String from = "service@cc.com";

	/**
	 * 来源名称
	 */
	private static String fromName = "cc-inc";

	/**
	 * 字符集
	 */
	private static String charset = "GBK";

	private final Map<String, String> headers = new HashMap<String, String>();

	static {
		try {
			Properties config = new Properties();
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
			config.load(input);
			input.close();
			from = config.getProperty("mail.from");
			fromName = config.getProperty("mail.fromName");
			charset = config.getProperty("mail.charset");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送到
	 */
	private String to;

	/**
	 * 接收者名称,例如用户昵称
	 */
	private String toName;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 是否html邮件
	 */
	private boolean html = true;

	/**
	 * 是否把html邮件信息中的非网页链接转为附件,例如图片
	 */
	private boolean link2Attachment = false;

	/**
	 * 是否把换行符转换成<br />,默认转换
	 */
	private boolean changeNewline = true;

	public EmailMeta() {
	}

	public EmailMeta(Map<?, ?> emailMap) {
		if (emailMap != null) {
			for (Object key : emailMap.keySet()) {
				if (key.toString().equals("html")) {
					html = Boolean.valueOf(emailMap.get(key).toString());
					continue;
				}
				if (key.toString().equals("link2Attachment")) {
					link2Attachment = Boolean.valueOf(emailMap.get(key).toString());
					continue;
				}
				if (key.toString().equals("changeNewline")) {
					changeNewline = Boolean.valueOf(emailMap.get(key).toString());
					continue;
				}
				try {
					BeanUtils.setProperty(this, key.toString(), emailMap.get(key));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param to
	 * @param toName
	 * @param from
	 * @param fromName
	 * @param charset
	 * @param subject
	 * @param message
	 * @param html
	 * @param link2Attachment
	 */
	public EmailMeta(String to, String toName, String from, String fromName, String charset, String subject,
			String message, boolean html, boolean link2Attachment) {
		this.to = to;
		this.toName = toName;
		EmailMeta.from = from;
		EmailMeta.fromName = fromName;
		EmailMeta.charset = charset;
		this.subject = subject;
		this.message = message;
		this.html = html;
		this.link2Attachment = link2Attachment;
	}

	/**
	 * 构造一个简单文件邮件
	 * 
	 * @param to
	 * @param toName
	 * @param subject
	 * @param message
	 */
	public EmailMeta(String to, String toName, String subject, String message) {
		this.to = to;
		this.toName = toName;
		this.subject = subject;
		this.message = message;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		EmailMeta.from = from;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		EmailMeta.charset = charset;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public boolean isLink2Attachment() {
		return link2Attachment;
	}

	public void setLink2Attachment(boolean link2Attachment) {
		this.link2Attachment = link2Attachment;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		EmailMeta.fromName = fromName;
	}

	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}

	/**
	 * 跟根据邮件内容转换成可发送的email
	 */
	public Email toEmail() {
		Email mail = null;
		if (this.isHtml()) {
			mail = new HtmlEmail();
		} else {
			mail = new SimpleEmail();
		}
		mail.setCharset(this.getCharset());
		try {
			mail.setFrom(this.getFrom(), this.getFromName());
			mail.addTo(this.getTo(), this.getToName());
			mail.setSubject(this.getSubject());
			if (this.headers.size() > 0) {
				for (String key : this.headers.keySet()) {
					mail.addHeader(key, this.headers.get(key));
				}
			}

			if (this.isHtml()) {
				HtmlEmail htmlEmail = (HtmlEmail) mail;
				if (this.isLink2Attachment()) {
					Set<String> linkSet = new TreeSet<String>();
					Pattern p = Pattern.compile("http://[^<>]+(jpg|gif|png|bmp)");
					Matcher m = p.matcher(this.getMessage());
					while (m.find()) {
						linkSet.add(this.getMessage().substring(m.start(), m.end()));
					}

					String msg = this.getMessage();
					for (String link : linkSet) {
						URL url = new URL(link);
						String cid = htmlEmail.embed(url, link.substring(link.lastIndexOf('/')));
						msg = msg.replaceAll(link, "cid:" + cid);
						break;
					}
				}
				if (changeNewline) {
					htmlEmail.setHtmlMsg(this.getMessage().replaceAll("\n", "<br />"));
				} else {
					htmlEmail.setHtmlMsg(this.getMessage());
				}
			} else {
				mail.setMsg(this.getMessage());
			}
		} catch (EmailException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return mail;
	}
}
