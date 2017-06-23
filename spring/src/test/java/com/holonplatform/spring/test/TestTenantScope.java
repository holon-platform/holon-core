package com.holonplatform.spring.test;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.ScopeTenant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestTenantScope.Config.class)
public class TestTenantScope {

	private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

	@Configuration
	@EnableTenantScope
	protected static class Config {

		@Bean
		public TenantResolver tenantResolver() {
			return new TenantResolver() {

				@Override
				public Optional<String> getTenantId() {
					return Optional.ofNullable(CURRENT_TENANT_ID.get());
				}
			};
		}

		@Bean
		@ScopeTenant
		public ServiceTest serviceTest() {
			return new ServiceTest();
		}

	}

	@Autowired
	private ApplicationContext applicationContext;

	@Test(expected = BeanCreationException.class)
	public void testInvalidTenantScope() {
		applicationContext.getBean(ServiceTest.class);
	}

	@Test
	public void testTenantScope() {
		ServiceTest srv1;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv1 = applicationContext.getBean(ServiceTest.class);
			Assert.assertNotNull(srv1);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		ServiceTest srv2;
		try {
			CURRENT_TENANT_ID.set("T2");
			srv2 = applicationContext.getBean(ServiceTest.class);
			Assert.assertNotNull(srv2);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertNotEquals(srv1, srv2);

		ServiceTest srv3;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv3 = applicationContext.getBean(ServiceTest.class);
			Assert.assertNotNull(srv3);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertEquals(srv1, srv3);
	}

}
