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
 * Setup and register a tenant scope with the name {@link TenantScope#SCOPE_NAME}.
 * <p>
 * A {@link TenantResolver} bean must be available in context to use the tenant scope. The {@link TenantResolver} bean
 * definition name can be configured using the {@link #tenantResolver()} attribute or the
 * {@link #TENANT_RESOLVER_PROPERTY_NAME} configuration property when more than one {@link TenantResolver} type bean is
 * available in context.
 * </p>
 * <p>
 * The convenience {@link ScopeTenant} annotation can be used to declare tenant scoped beans.
 * <p>
 * <p>
 * If the {@link #enableTenantScopeManager()} attribute is <code>true</code>, a {@link TenantScopeManager} bean is
 * registered and made available in Spring context to manage the tenant scoped beans lifecycle.
 * </p>
 *
 * @see ScopeTenant
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TenantScopeRegistrar.class)
public @interface EnableTenantScope {

	/**
	 * Property which can be used to configure the {@link TenantResolver} type bean definition name to be used to obtain
	 * the current tenant id. It has the same meaning of the {@link #tenantResolver()} attribute, but higher precedence.
	 */
	public static final String TENANT_RESOLVER_PROPERTY_NAME = "holon.tenant-scope.tenant-resolver";

	/**
	 * Configures the name of the {@link TenantResolver} type bean definition to be used to obtain the current tenant
	 * id.
	 * <p>
	 * If not provided, the scope registrar will lookup for an unique {@link TenantResolver} type bean definition in the
	 * Spring application context. If not found, or more than one {@link TenantResolver} type bean definition is found,
	 * a scope configuration error is thrown.
	 * </p>
	 * @return The optional name of the {@link TenantResolver} type bean definition
	 */
	String tenantResolver() default "";

	/**
	 * Whether to enable and register a {@link TenantScopeManager} type bean definition, which can be obtained as any
	 * other Spring bean, for example through dependency injection.
	 * <p>
	 * The {@link TenantScopeManager} API allows to manage the tenant scoped beans lifecycle, for example discarding
	 * scoped bean instances for a specific tenant id, invoking any destruction callback accordingly.
	 * </p>
	 * <p>
	 * The attribute value is <code>true</code> by default.
	 * </p>
	 * @return Whether to enable and register a {@link TenantScopeManager} bean
	 * @see TenantScopeManager
	 */
	boolean enableTenantScopeManager() default true;

}
