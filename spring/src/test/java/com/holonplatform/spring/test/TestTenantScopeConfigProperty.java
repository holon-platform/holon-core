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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.ScopeTenant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestTenantScopeConfigProperty.Config.class)
public class TestTenantScopeConfigProperty {

	private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

	@Configuration
	@PropertySource("tenant.properties")
	@EnableTenantScope
	protected static class Config {

		@Primary
		@Bean(name = "myTenantResolver")
		public TenantResolver tenantResolver() {
			return new TenantResolver() {

				@Override
				public Optional<String> getTenantId() {
					return Optional.ofNullable(CURRENT_TENANT_ID.get());
				}
			};
		}

		@Bean(name = "anotherTenantResolver")
		public TenantResolver anotherTenantResolver() {
			return () -> Optional.of("ANOTHER");
		}

		@Bean
		@ScopeTenant
		public TenantScopedServiceTest serviceTest() {
			return new TenantScopedServiceTest();
		}

	}

	@Autowired
	private ApplicationContext applicationContext;

	@Test(expected = BeanCreationException.class)
	public void testInvalidTenantScope() {
		applicationContext.getBean(TenantScopedServiceTest.class);
	}

	@Test
	public void testTenantScope() {
		TenantScopedServiceTest srv1;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv1 = applicationContext.getBean(TenantScopedServiceTest.class);
			Assert.assertNotNull(srv1);

			Assert.assertEquals("T1", srv1.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		TenantScopedServiceTest srv2;
		try {
			CURRENT_TENANT_ID.set("T2");
			srv2 = applicationContext.getBean(TenantScopedServiceTest.class);
			Assert.assertNotNull(srv2);

			Assert.assertEquals("T2", srv2.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertNotEquals(srv1, srv2);

		TenantScopedServiceTest srv3;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv3 = applicationContext.getBean(TenantScopedServiceTest.class);
			Assert.assertNotNull(srv3);

			Assert.assertEquals("T1", srv3.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertEquals(srv1, srv3);
	}

}
