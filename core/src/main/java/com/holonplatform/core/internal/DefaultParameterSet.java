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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;

/**
 * Default {@link ParameterSet} implementation. A {@link HashMap} is used to store parameters values.
 * 
 * @since 4.5.0
 */
public class DefaultParameterSet implements MutableParameterSet {

	private static final long serialVersionUID = 5385317218930611729L;

	/**
	 * Parameters name-value map.
	 */
	protected Map<String, Object> parameters;

	/**
	 * Construct a new ParameterSet
	 */
	public DefaultParameterSet() {
		this(null);
	}

	/**
	 * Construct a new ParameterSet using given <code>parameters</code> as initial value.
	 * @param parameters Initial parameters
	 */
	public DefaultParameterSet(Map<String, Object> parameters) {
		super();
		this.parameters = parameters;
	}

	/**
	 * Gets the value associated to given parameter name
	 * @param name Parameter name
	 * @return Parameter value
	 */
	protected Object getParameterValue(String name) {
		return (parameters != null) ? parameters.get(name) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasParameters()
	 */
	@Override
	public boolean hasParameters() {
		return parameters != null && !parameters.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasParameter(java.lang.String)
	 */
	@Override
	public boolean hasParameter(String name) {
		return parameters != null && parameters.containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#hasNotNullParameter(java.lang.String)
	 */
	@Override
	public boolean hasNotNullParameter(String name) {
		return parameters != null && parameters.get(name) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameter(java.lang.String)
	 */
	@Override
	public Optional<Object> getParameter(String name) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		return Optional.ofNullable(getParameterValue(name));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameter(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getParameter(String name, Class<T> type) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		ObjectUtils.argumentNotNull(type, "Parameter type must be not null");
		Object value = getParameterValue(name);

		if (value != null && !TypeUtils.isAssignable(value.getClass(), type)) {
			throw new TypeMismatchException("Value type " + value.getClass().getName()
					+ " is not compatible with required type " + type.getName());
		}

		return Optional.ofNullable((T) value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.parameters.ParameterSet#getParameterIf(java.lang.String, java.lang.Class,
	 * java.util.function.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getParameterIf(String name, Class<T> type, Predicate<T> condition) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		ObjectUtils.argumentNotNull(type, "Parameter type must be not null");
		ObjectUtils.argumentNotNull(condition, "Condition must be not null");

		Object value = getParameterValue(name);

		if (value != null && !TypeUtils.isAssignable(value.getClass(), type)) {
			throw new TypeMismatchException("Value type " + value.getClass().getName()
					+ " is not compatible with required type " + type.getName());
		}

		final T typedValue = (T) value;

		if (value != null && condition.test(typedValue)) {
			return Optional.of(typedValue);
		}

		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.ParameterSet#forEach(java.util.function.BiConsumer)
	 */
	@Override
	public void forEachParameter(BiConsumer<String, Object> action) {
		ObjectUtils.argumentNotNull(action, "Action to perform must be not null");
		if (parameters != null) {
			parameters.forEach((n, v) -> action.accept(n, v));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.MutableParameterSet#addParameter(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addParameter(String name, Object value) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		if (parameters == null) {
			parameters = new HashMap<>(4);
		}
		parameters.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.MutableParameterSet#removeParameter(java.lang.String)
	 */
	@Override
	public void removeParameter(String name) {
		ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
		if (parameters != null) {
			parameters.remove(name);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ParameterSet with ");
		sb.append((parameters != null) ? parameters.size() : 0);
		sb.append(" parameters");
		if (parameters != null && !parameters.isEmpty()) {
			sb.append(":");
			for (String name : parameters.keySet()) {
				sb.append("\n");
				sb.append(name);
				sb.append(":");
				sb.append(parameters.get(name));
			}
		}
		return sb.toString();
	}

	/**
	 * Builder for fluent-style ParameterSet construction.
	 */
	public static class DefaultBuilder extends AbstractBuilder<ParameterSet, DefaultParameterSet, DefaultBuilder> {

		public DefaultBuilder() {
			super(new DefaultParameterSet());
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.parameters.DefaultParameterSet.AbstractBuilder#builder()
		 */
		@Override
		protected DefaultBuilder builder() {
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.parameters.ParameterSetBuilder#build()
		 */
		@Override
		public ParameterSet build() {
			return getInstance();
		}

	}

	/**
	 * Abstract ParameterSet builder.
	 */
	public abstract static class AbstractBuilder<S extends ParameterSet, I extends DefaultParameterSet, B extends Builder<S>>
			implements Builder<S> {

		private final I instance;

		/**
		 * Constructor
		 * @param instance Instance to build
		 */
		public AbstractBuilder(I instance) {
			super();
			this.instance = instance;
		}

		protected I getInstance() {
			return instance;
		}

		protected abstract B builder();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.parameters.ParameterSetBuilder#addParameter(java.lang.String, java.lang.Object)
		 */
		@Override
		public B parameter(String name, Object value) {
			instance.addParameter(name, value);
			return builder();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.parameters.ParameterSet.Builder#parameter(com.holonplatform.core.config.
		 * ConfigProperty, java.lang.Object)
		 */
		@Override
		public <T> B parameter(ConfigProperty<T> property, T value) {
			ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
			instance.addParameter(property.getKey(), value);
			return builder();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.parameters.ParameterSet.Builder#parameters(java.util.Map)
		 */
		@Override
		public B parameters(Map<String, Object> parameters) {
			if (parameters != null) {
				parameters.forEach((n, v) -> instance.addParameter(n, v));
			}
			return builder();
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.parameters.ParameterSet.Builder#parameters(com.holonplatform.core.parameters.
		 * ParameterSet)
		 */
		@Override
		public B parameters(ParameterSet parameters) {
			if (parameters != null) {
				parameters.forEachParameter((n, v) -> instance.addParameter(n, v));
			}
			return builder();
		}

	}

}
