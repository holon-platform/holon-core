package com.holonplatform.spring.internal.tenant;

import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.internal.BeanRegistryUtils;

/**
 * Registers tenant scope.
 */
public class TenantScopeRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private static final String TENANT_SCOPE_POST_PROCESSOR_NAME = TenantScopePostProcessor.class.getName();

	private volatile Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		// check its not a call from subclasses
		if (importingClassMetadata.getAnnotationAttributes(EnableTenantScope.class.getName()) == null) {
			return;
		}

		// register post processor
		if (!registry.containsBeanDefinition(TENANT_SCOPE_POST_PROCESSOR_NAME)) {

			Map<String, Object> attributes = importingClassMetadata
					.getAnnotationAttributes(EnableTenantScope.class.getName());

			// tenant resolver bean definition name
			String tenantResolver = null;

			// check environment
			if (environment.containsProperty(EnableTenantScope.TENANT_RESOLVER_PROPERTY_NAME)) {
				tenantResolver = environment.getProperty(EnableTenantScope.TENANT_RESOLVER_PROPERTY_NAME, String.class);
			}

			if (tenantResolver == null || tenantResolver.trim().length() == 0) {
				// check annotation
				tenantResolver = BeanRegistryUtils.getAnnotationValue(attributes, "tenantResolver", null);
			}

			if (tenantResolver != null && tenantResolver.trim().length() == 0) {
				tenantResolver = null;
			}

			final BeanDefinitionBuilder postProcessorBuilder = BeanDefinitionBuilder
					.genericBeanDefinition(TenantScopePostProcessor.class).setDestroyMethodName("unregister")
					.addPropertyValue("tenantResolver", tenantResolver).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(TENANT_SCOPE_POST_PROCESSOR_NAME, postProcessorBuilder.getBeanDefinition());

		}

	}

}
