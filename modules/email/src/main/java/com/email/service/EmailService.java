package com.email.service;

import java.util.Map;

public interface EmailService {

	/**
	 * 邮件发送
	 */
	public void send(Map<?, ?> mailInfo);

    public String ping();
}
