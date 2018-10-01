package com.holonplatform.spring.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.ScopeTenant;

@ExtendWith(SpringExtension.class)
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

	@Test
	public void testInvalidTenantScope() {
		Assertions.assertThrows(BeanCreationException.class,
				() -> applicationContext.getBean(TenantScopedServiceTest.class));
	}

	@Test
	public void testTenantScopeProxy() {
		try {
			CURRENT_TENANT_ID.set("T1");
			SingletonComponent sc = applicationContext.getBean(SingletonComponent.class);
			assertNotNull(sc);

			assertEquals("T1", sc.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}
	}

	@Test
	public void testTenantScopeProxyFail() {
		Assertions.assertThrows(BeanCreationException.class,
				() -> applicationContext.getBean(SingletonComponent.class).getTenantId());
	}

}
