package com.flower.ret;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

public class Forward extends AbstractReturn {
	protected final Logger logger = Logger.getLogger(Forward.class);
	public String path;
	public Forward(String path) {
		this.path = path;
	}
	
	public void setAttribute(String key, Object value){
		getRequest().setAttribute(key, value);
	}

	@Override
	public void process() throws ServletException, IOException {
		getRequest().getRequestDispatcher(path).forward(getRequest(), getResponse());
	}

}
