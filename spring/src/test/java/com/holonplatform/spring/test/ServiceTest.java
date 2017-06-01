package com.holonplatform.spring.test;

import java.io.Serializable;

import org.springframework.beans.factory.DisposableBean;

public class ServiceTest implements Serializable, DisposableBean {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() throws Exception {
		// destruction callback
	}

}
