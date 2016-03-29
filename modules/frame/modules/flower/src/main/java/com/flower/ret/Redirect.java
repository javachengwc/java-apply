package com.flower.ret;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Redirect  extends AbstractReturn  {
	protected final Logger logger = Logger.getLogger(Redirect.class);
	public String path;
	
	public Redirect(String path) {
		this.path = path;
	}

	@Override
	public void process() throws IOException {
		getResponse().sendRedirect(path);
	}

}
