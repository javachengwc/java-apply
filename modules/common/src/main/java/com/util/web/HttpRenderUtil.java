package com.util.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class HttpRenderUtil {
	
	protected static final Logger logger = Logger.getLogger(HttpRenderUtil.class);

	public static void render(String text, String contentType, HttpServletResponse response)
		throws IOException
	{
		response.setContentType(contentType);
		response.getWriter().write(text);
		response.flushBuffer();
	}

	public static void renderText(String text,  HttpServletResponse response) {
		try {
			render(text, "text/plain;charset=UTF-8", response);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static void renderHtml(String html, HttpServletResponse response) {
		try {
			render(html, "text/html;charset=UTF-8", response);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static void renderXML(String xml, HttpServletResponse response) {
		try {
			render(xml, "text/xml;charset=UTF-8", response);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static void renderJSON(String json, HttpServletResponse response){
		try {
			render(json, "text/x-json;charset=UTF-8", response);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static void renderBinary(String mimetype, InputStream source, HttpServletResponse response)
		throws IOException
	{
		response.setContentType(mimetype);
		ServletOutputStream out = response.getOutputStream();
		IOUtils.copy(source, out);
		out.close();
		source.close();
	}
	
	public static void renderBinary(String mimetype, byte[] content, HttpServletResponse response)
		throws IOException
	{
		response.setContentType(mimetype);
		ServletOutputStream out = response.getOutputStream();
		BufferedOutputStream bo = new BufferedOutputStream(out, 1024);
		bo.write(content);
		bo.flush();
		bo.close();
		out.close();
	}
}
