package com.holonplatform.spring.internal.tenant;

import java.lang.ref.WeakReference;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.holonplatform.core.internal.Logger;
import com.holonplatform.spring.EnableTenantScope;
import com.holonplatform.spring.internal.SpringLogger;

/**
 * Registers tenant scope.
 */
public class TenantScopeRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

	/*
	 * Logger
	 */
	private static final Logger LOGGER = SpringLogger.create();

	private volatile BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		// check its not a call from subclasses
		if (importingClassMetadata.getAnnotationAttributes(EnableTenantScope.class.getName()) == null) {
			return;
		}

		if (beanFactory instanceof ConfigurableBeanFactory) {
			// register scope
			TenantScope scope = new TenantScope(beanFactory);

			((ConfigurableBeanFactory) beanFactory).registerScope(TenantScope.SCOPE_NAME, scope);

			LOGGER.info("Registered scope [" + TenantScope.SCOPE_NAME + "]");

			// register finalizer
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TenantScopeFinalizer.class)
					.addConstructorArgValue(scope).setDestroyMethodName("destroy")
					.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(TenantScopeFinalizer.class.getName(), builder.getBeanDefinition());

		} else {

			LOGGER.warn("Cannot register tenant scope: BeanFactory is not a ConfigurableBeanFactory");

		}

	}

	public static final class TenantScopeFinalizer {

		private final WeakReference<TenantScope> scopeRef;

		public TenantScopeFinalizer(TenantScope scope) {
			super();
			this.scopeRef = new WeakReference<>(scope);
		}

		public void destroy() {
			TenantScope scope = scopeRef.get();
			if (scope != null) {
				scope.destroy();
			}
		}

	}

}
