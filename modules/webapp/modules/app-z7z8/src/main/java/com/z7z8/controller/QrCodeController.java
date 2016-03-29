package com.z7z8.controller;

import javax.servlet.http.HttpServletResponse;

import com.z7z8.model.QrCode;
import com.z7z8.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 二维码生成入口
 */
@Controller
@RequestMapping("qrCode")
public class QrCodeController {

	@Autowired
	private QrCodeService qrCodeService;
	
	@RequestMapping("create")
	public void create(HttpServletResponse response, QrCode qrCode) {
		qrCodeService.create(response, qrCode);
	}
}
