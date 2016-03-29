package com.flower.ret;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.flower.DataFilter;
import com.util.web.HttpRenderUtil;

/**
 * 
 * 字符型的输出
 */
public class Render  extends AbstractReturn  {
	protected static final Logger logger = Logger.getLogger(Render.class);
	
	public RenderType renderType;
	public Render(RenderType renderType, String renderData) {
		this.renderType = renderType;
		this.renderData = renderData;
	}
	
	public String renderData;
	
	
	@Override
	public void process() throws Exception {
		HttpServletResponse response = DataFilter.getResponse();
		switch(renderType){
		case HTML:
			HttpRenderUtil.renderHtml(renderData, response);
			break;
		case JSON:
			HttpRenderUtil.renderJSON(renderData, response);
			break;
		case TEXT:
			HttpRenderUtil.renderText(renderData, response);
			break;
		case XML:
			HttpRenderUtil.renderXML(renderData, response);
			break;
		default:
			logger.error("UNKOWN RENDER TYPE");
		}
	}
	
	

}
