package com.holonplatform.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.holonplatform.core.tenancy.TenantResolver;
import com.holonplatform.spring.internal.tenant.TenantScope;
import com.holonplatform.spring.internal.tenant.TenantScopeRegistrar;

/**
 * Setup and register a tenant scope with name {@link TenantScope#SCOPE_NAME}.
 * 
 * <p>
 * A {@link TenantResolver} bean must be available in context to use the tenant scope.
 * </p>
 *
 * @see ScopeTenant
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TenantScopeRegistrar.class)
public @interface EnableTenantScope {

}
