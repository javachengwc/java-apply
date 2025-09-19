package com.flower.ret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.ServletException;


public class Forward extends AbstractReturn {
	protected final Logger logger = LoggerFactory.getLogger(Forward.class);
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
