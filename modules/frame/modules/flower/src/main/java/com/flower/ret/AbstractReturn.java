package com.flower.ret;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flower.DataFilter;
import com.flower.Return;

public abstract class AbstractReturn implements Return {

	protected HttpServletRequest getRequest() {
		return DataFilter.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return DataFilter.getResponse();
	}
}
