package com.email.service;

import java.util.Map;

public interface EmailService {

	/**
	 * 邮件发送
	 */
	void send(Map<?, ?> mailInfo);
}
