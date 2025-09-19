package com.flower.ret;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Redirect  extends AbstractReturn  {
	protected final Logger logger = LoggerFactory.getLogger(Redirect.class);
	public String path;
	
	public Redirect(String path) {
		this.path = path;
	}

	@Override
	public void process() throws IOException {
		getResponse().sendRedirect(path);
	}

}
