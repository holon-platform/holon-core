package com.holonplatform.spring.test;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.holonplatform.core.tenancy.TenantResolver;

public class TenantScopedServiceTest implements ITenantScopedTest, Serializable, DisposableBean {

	private static final long serialVersionUID = 1L;

	@Autowired
	private TenantResolver tenantResolver;

	private String tenantId;

	@PostConstruct
	public void init() {
		this.tenantId = tenantResolver.getTenantId().orElse(null);
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void destroy() throws Exception {
		// destruction callback
	}

}
