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
@ContextConfiguration(classes = TestTenantScopeProxyMode.Config.class)
public class TestTenantScopeProxyMode {

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
		public TenantScopedServiceTest serviceTest() {
			return new TenantScopedServiceTest();
		}
		
		@Bean
		public SingletonComponent singletonComponent() {
			return new SingletonComponent();
		}

	}

	@Autowired
	private ApplicationContext applicationContext;

	@Test(expected = BeanCreationException.class)
	public void testInvalidTenantScope() {
		applicationContext.getBean(TenantScopedServiceTest.class);
	}

	@Test
	public void testTenantScopeProxy() {
		try {
			CURRENT_TENANT_ID.set("T1");
			SingletonComponent sc = applicationContext.getBean(SingletonComponent.class);
			Assert.assertNotNull(sc);
			
			Assert.assertEquals("T1", sc.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}
	}
	
	@Test(expected = BeanCreationException.class)
	public void testTenantScopeProxyFail() {
		SingletonComponent sc = applicationContext.getBean(SingletonComponent.class);
		sc.getTenantId();
	}

}
