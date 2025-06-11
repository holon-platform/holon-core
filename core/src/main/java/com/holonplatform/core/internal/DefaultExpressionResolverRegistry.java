/*
 * Copyright 2016-2017 Axioma srl.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.holonplatform.core.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import jakarta.annotation.Priority;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.ExpressionResolverRegistry;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link ExpressionResolverRegistry} implementation.
 *
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public class DefaultExpressionResolverRegistry implements ExpressionResolverRegistry {

	private static final long serialVersionUID = 2157560322522794698L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = CoreLogger.create();

	/**
	 * Expression resolvers cache
	 */
	private final WeakHashMap<Class<?>, WeakHashMap<Class<?>, List<ExpressionResolver>>> cache;

	/**
	 * {@link ExpressionResolver} comparator using {@link Priority} annotation.
	 */
	private static final Comparator<ExpressionResolver> PRIORITY_COMPARATOR = Comparator.comparingInt(
			p -> p.getClass().isAnnotationPresent(Priority.class) ? p.getClass().getAnnotation(Priority.class).value()
					: ExpressionResolver.DEFAULT_PRIORITY);

	/**
	 * Registered resolvers.
	 */
	private final List<ExpressionResolver> resolvers = new ArrayList<>();

	/**
	 * Contructor with cache enabled by default.
	 */
	public DefaultExpressionResolverRegistry() {
		this(true);
	}

	/**
	 * Contructor with cache enabling flag.
	 * @param cacheEnabled Whether to enable the expression resolvers cache
	 */
	public DefaultExpressionResolverRegistry(boolean cacheEnabled) {
		super();
		cache = cacheEnabled ? new WeakHashMap<>() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolverRegistry#addExpressionResolver(com.holonplatform.core.
	 * ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void addExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		ObjectUtils.argumentNotNull(expressionResolver, "ExpressionResolver to add must be not null");
		resolvers.add(expressionResolver);
		LOGGER.debug(() -> "Added ExpressionResolver [" + expressionResolver + "] to registry [" + this + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolverRegistry#removeExpressionResolver(com.holonplatform.core.
	 * ExpressionResolver)
	 */
	@Override
	public <E extends Expression, R extends Expression> void removeExpressionResolver(
			ExpressionResolver<E, R> expressionResolver) {
		ObjectUtils.argumentNotNull(expressionResolver, "ExpressionResolver to remove must be not null");
		resolvers.remove(expressionResolver);
		LOGGER.debug(() -> "Removed ExpressionResolver [" + expressionResolver + "] from registry [" + this + "]");
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ExpressionResolverRegistry#getExpressionResolvers()
	 */
	@Override
	public Iterable<ExpressionResolver> getExpressionResolvers() {
		return Collections.unmodifiableList(resolvers);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.ExpressionResolver.ExpressionResolverHandler#resolve(com.holonplatform.core.Expression,
	 * java.lang.Class, com.holonplatform.core.ExpressionResolver.ResolutionContext)
	 */
	@Override
	public <E extends Expression, R extends Expression> Optional<R> resolve(final E expression,
			final Class<R> resolutionType, ResolutionContext context) throws InvalidExpressionException {
		ObjectUtils.argumentNotNull(expression, "Expression to resolve must be not null");
		ObjectUtils.argumentNotNull(resolutionType, "Resolution type must be not null");

		LOGGER.debug(() -> "Resolving expression [" + expression + "] for type [" + resolutionType + "] using context ["
				+ context + "]...");

		final Optional<R> resolved = resolveExpression(
				getResolversForExpressionType(expression.getClass(), resolutionType), expression, context);

		LOGGER.debug(() -> "Expression [" + expression + "] for type [" + resolutionType + "] "
				+ (resolved.isPresent() ? " was resolved into [" + resolved.get() + "]" : "was not resolved"));

		return resolved;
	}

	/**
	 * Get a priority-ordered list of the suitable {@link ExpressionResolver} for given expression and resolution type.
	 * @param expressionType Expression type
	 * @param resolvedType Resolution type
	 * @return Expression resolvers list, empty if none
	 */
	@SuppressWarnings("unchecked")
	private List<ExpressionResolver> getResolversForExpressionType(Class<?> expressionType, Class<?> resolvedType) {

		// check cache
		if (cache != null && cache.containsKey(expressionType) && cache.get(expressionType).containsKey(resolvedType)) {
			return cache.get(expressionType).get(resolvedType);
		}

		List<ExpressionResolver> expressionResolvers = resolvers.stream().filter(
				r -> r.getExpressionType().isAssignableFrom(expressionType) && r.getResolvedType() == resolvedType)
				.sorted(PRIORITY_COMPARATOR).collect(Collectors.toList());

		// cache resolvers
		if (cache != null) {
			cache.computeIfAbsent(expressionType, k -> new WeakHashMap<>()).put(resolvedType, expressionResolvers);
		}

		return expressionResolvers;
	}

	/**
	 * Resolve given expression using provided expression resolvers.
	 * @param <R> Expresion result type
	 * @param typeConsistentResolvers Expression resolvers to use
	 * @param expression Expression to resolve
	 * @param context Resolution context
	 * @return Optional resolution result
	 * @throws InvalidExpressionException If expression validation failed
	 */
	@SuppressWarnings("unchecked")
	private static <R extends Expression> Optional<R> resolveExpression(
			List<ExpressionResolver> typeConsistentResolvers, final Expression expression, ResolutionContext context)
			throws InvalidExpressionException {

		// validate
		expression.validate();

		Optional<R> resolved = Optional.empty();

		for (ExpressionResolver resolver : typeConsistentResolvers) {
			resolved = resolver.resolve(expression, context);
			if (resolved.isPresent()) {
				LOGGER.debug(() -> "Expression [" + expression + "] was resolved by [" + resolver + "]");
				break;
			}
		}

		return resolved;
	}

}
