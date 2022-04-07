package com.z7z8.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import com.util.PropertiesLoader;
import com.util.web.HttpRenderUtil;
import com.z7z8.model.QrCode;
import com.z7z8.model.QrCodeResponse;
import com.z7z8.util.zxing.ZXingUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class QrCodeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeService.class);
	
	private static final String IMAGE_DIR = "images" + File.separatorChar + "matrix" + File.separatorChar;

    private static final List<Integer> WAYS = Arrays.asList(1, 2);
	
	private static String imageRootPath = null;
	
	static {
		Properties reader = PropertiesLoader.load("qr.properties");
		imageRootPath = reader.getProperty("root");
		if (StringUtils.isBlank(imageRootPath) || !new File(imageRootPath).isDirectory()) {
			LOGGER.error("二维码图片保存根目录配置有误!");
		} else {
			imageRootPath = imageRootPath + File.separatorChar;
		}
	}
	
	public void create(HttpServletResponse response, QrCode qrCode) {
		String content = qrCode.getContent();
		if (StringUtils.isNotBlank(content)) {
			filter(qrCode);
			boolean success = false;
			if (1 == qrCode.getWay()) {
				String fileName = fileNameMaker(qrCode.getExtension());
				String filePath = imageRootPath + fileName;
				String imageUrl = IMAGE_DIR + fileName;
				success = ZXingUtil.createMatrixToFile(content, null, qrCode.getWidth(), qrCode.getHeight(), filePath);
				QrCodeResponse result = success ? new QrCodeResponse(0, imageUrl) : new QrCodeResponse(1, "matrix to file error, check the logs for debugging!");
                HttpRenderUtil.renderJSON(JSON.toJSONString(result), response);
			} else {
				try {
					success = ZXingUtil.createMatrixToStream(content, null, qrCode.getWidth(), qrCode.getHeight(), qrCode.getExtension(), response.getOutputStream());
				} catch (Exception e) {
					LOGGER.error("将二维码图片写入HttpServletResponse时发生异常, 堆栈轨迹如下 : ", e);
				}
				if (!success) {
                    HttpRenderUtil.renderJSON(JSON.toJSONString(new QrCodeResponse(1, "matrix to stream error, check the logs for debugging!")), response);
				}
			}
		} else {
			HttpRenderUtil.renderJSON(JSON.toJSONString(new QrCodeResponse(1, "content is null")), response);
		}
	}

	
	private void filter(QrCode qrCode) {

		if (!WAYS.contains(qrCode.getWay())) {
			qrCode.setWay(WAYS.get(0));
		}
		if (StringUtils.isBlank(qrCode.getExtension())) {
			qrCode.setExtension("png");
		}
	}
	
	private String fileNameMaker(String extension) {

		return new StringBuilder(20).append(System.currentTimeMillis()).append('.').append(extension).toString();
	}
}
