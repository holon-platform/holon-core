package com.holonplatform.spring.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.ScopeTenant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestTenantScopeConfig.Config.class)
public class TestTenantScopeConfig {

	private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

	@Configuration
	@EnableTenantScope(tenantResolver = "myTenantResolver")
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

	@Test
	public void testInvalidTenantScope() {
		Assertions.assertThrows(BeanCreationException.class,
				() -> applicationContext.getBean(TenantScopedServiceTest.class));
	}

	@Test
	public void testTenantScope() {
		TenantScopedServiceTest srv1;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv1 = applicationContext.getBean(TenantScopedServiceTest.class);
			assertNotNull(srv1);

			assertEquals("T1", srv1.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		TenantScopedServiceTest srv2;
		try {
			CURRENT_TENANT_ID.set("T2");
			srv2 = applicationContext.getBean(TenantScopedServiceTest.class);
			assertNotNull(srv2);

			assertEquals("T2", srv2.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		assertNotEquals(srv1, srv2);

		TenantScopedServiceTest srv3;
		try {
			CURRENT_TENANT_ID.set("T1");
			srv3 = applicationContext.getBean(TenantScopedServiceTest.class);
			assertNotNull(srv3);

			assertEquals("T1", srv3.getTenantId());
		} finally {
			CURRENT_TENANT_ID.remove();
		}

		assertEquals(srv1, srv3);
	}

}
