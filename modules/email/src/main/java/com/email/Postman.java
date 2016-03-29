package com.email;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Postman implements Runnable {

	private final Logger mailLog = LoggerFactory.getLogger("mail.log");

	private final Email mail;

	private final EmailMeta meta;

	public Postman(Email mail) {
		this.mail = mail;
		this.meta = null;
	}

	public Postman(Email mail, EmailMeta meta) {
		this.mail = mail;
		this.meta = meta;
	}

	public void run() {
		try {
			mail.send();
			logMail(1);
		} catch (EmailException e) {
			logMail(0);
		}
	}

	private void logMail(int success) {
		if (meta != null) {
			StringBuilder info = new StringBuilder();
			info.append(success).append('|');
			info.append(meta.getTo()).append('|');
			info.append(meta.getToName()).append('|');
			info.append(meta.getSubject());
			mailLog.info(info.toString());
		}
	}
}
