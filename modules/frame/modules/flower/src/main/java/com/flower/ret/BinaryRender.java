package com.flower.ret;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.util.web.HttpRenderUtil;

/**
 * 二进制类型的输出
 */
public class BinaryRender extends AbstractReturn {
	protected static final Logger logger = Logger.getLogger(BinaryRender.class);

	private InputType type;
	private String mimetype;
	private InputStream source;
	private byte[] content;
	
	public BinaryRender(String mimetype, InputStream source){
		type = InputType.STREAM;
		this.mimetype = mimetype;
		this.source = source;
	}
	public BinaryRender(String mimetype, byte[] content){
		type = InputType.BYTES;
		this.mimetype = mimetype;
		this.content = content;
	}
	
	@Override
	public void process() throws Exception {
		HttpServletResponse response = getResponse();
		switch(type){
		case BYTES:
			HttpRenderUtil.renderBinary(mimetype, content, response);
			break;
		case STREAM:
			HttpRenderUtil.renderBinary(mimetype, source, response);
		default:
			logger.error("UNKOWN RENDER TYPE");
		}
	}

	public enum InputType {
		STREAM, BYTES
	}
}
