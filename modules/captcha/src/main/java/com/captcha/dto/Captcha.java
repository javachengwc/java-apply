package com.captcha.dto;

import java.awt.image.BufferedImage;

public class Captcha {
	
	private String code;
	
	private BufferedImage image;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public Captcha()
	{
		
	}

	public Captcha(String code,BufferedImage image)
	{
		this.code=code;
		this.image=image;
	}
}
