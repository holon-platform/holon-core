package com.holonplatform.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;

import com.holonplatform.spring.internal.tenant.TenantScope;

/**
 * Stereotype annotation for Spring's <code>@Scope("tenant")</code>.
 */
@Scope(TenantScope.SCOPE_NAME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScopeTenant {

	/**
	 * Alias for {@link Scope#proxyMode}.
	 * <p>
	 * Defaults to {@link ScopedProxyMode#INTERFACES}.
	 * </p>
	 * @return The scoped proxy mode
	 * @see Scope#proxyMode()
	 */
	@AliasFor(annotation = Scope.class)
	ScopedProxyMode proxyMode() default ScopedProxyMode.INTERFACES;

}
