package com.email.service.impl;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.mail.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.email.EmailMeta;
import com.email.Postman;
import com.email.service.EmailService;

public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	private String host;
	private boolean auth;
	private String username;
	private String password;

	private boolean debug;

	public void send(Map<?, ?> mailInfo) {

        logger.info("EmailService send start.............");
		long begin = System.currentTimeMillis();
		EmailMeta meta = new EmailMeta(mailInfo);
		Email mail = meta.toEmail();
		mail.setHostName(host);
		if (isAuth()) {
			mail.setAuthentication(username, password);
		}

		if (isDebug()) {
			mail.setDebug(debug);
			logger.debug("user info: " + username + "," + password);
		}
		new Thread(new Postman(mail, meta)).start();

		logger.info(MessageFormat.format("prepare mail[{0}] at {1}ms", mailInfo.get("to"), (System.currentTimeMillis() - begin)));
	}

    public String ping()
    {
        return "it's ok";
    }

	@Required
	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAuth() {
		return auth;
	}

	@Required
	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	@Required
	public void setUsername(String username) {
		this.username = username;
	}

	@Required
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
