package com.holonplatform.spring.test;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.ScopeTenant;
import com.holonplatform.spring.TenantScopeManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestTenantScopeManager.Config.class)
public class TestTenantScopeManager {

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
		public TenantManagedBeanTest tenantManagedBeanTest() {
			return new TenantManagedBeanTest();
		}

	}

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TenantScopeManager manager;

	@Test
	public void testConfig() {
		assertNotNull(manager);
	}

	@Test
	public void testDiscardBeanStore() {

		TenantManagedBeanTest i1 = null;
		TenantManagedBeanTest i2 = null;
		try {
			CURRENT_TENANT_ID.set("T1");
			i1 = applicationContext.getBean(TenantManagedBeanTest.class);
			Assert.assertNotNull(i1);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		try {
			CURRENT_TENANT_ID.set("T2");
			i2 = applicationContext.getBean(TenantManagedBeanTest.class);
			Assert.assertNotNull(i2);
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		Assert.assertFalse(i1 == i2);
		
		TenantManagedBeanTest i3 = null;
		
		try {
			CURRENT_TENANT_ID.set("T1");
			i3 = applicationContext.getBean(TenantManagedBeanTest.class);
			Assert.assertNotNull(i3);
		} finally {
			CURRENT_TENANT_ID.remove();
		}
		
		Assert.assertTrue(i1 == i3);
		
		manager.discardTenantBeanStore("T1");
		
		try {
			CURRENT_TENANT_ID.set("T1");
			i3 = applicationContext.getBean(TenantManagedBeanTest.class);
			Assert.assertNotNull(i3);
		} finally {
			CURRENT_TENANT_ID.remove();
		}
		
		Assert.assertFalse(i1 == i3);
		
		Assert.assertTrue(i1.isDestroyed());
	}

}
