/*
 * Copyright 2000-2016 Holon TDCN.
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
package com.holonplatform.core.internal.property;

import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PathProperty.Builder;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;

/**
 * Abstract {@link PathProperty} implementation which acts as base class for concrete implementations.
 * 
 * <p>
 * Extends {@link AbstractProperty} inheriting {@link Localizable} support.
 * </p>
 * 
 * @param <T> Property value type
 * @param <P> Concrete property type
 * 
 * @since 5.0.0
 * 
 * @see DefaultPathProperty
 */
public abstract class AbstractPathProperty<T, P extends Builder<T, P>> extends AbstractProperty<T, P>
		implements Builder<T, P>, PathProperty<T> {

	private static final long serialVersionUID = 2939113810465856718L;

	/**
	 * Property (path) name
	 */
	private final String name;

	/**
	 * Optional parent path
	 */
	private Path<?> parent;

	/**
	 * Constructor with default PropertyConfiguration.
	 * @param name Property name, must be not <code>null</code>
	 * @param type Property value type (not null)
	 */
	public AbstractPathProperty(String name, Class<? extends T> type) {
		this(name, type, null);
	}

	/**
	 * Constructor.
	 * @param name Property path name (not null)
	 * @param type Property value type (not null)
	 * @param configuration Optional {@link PropertyConfiguration}
	 */
	public AbstractPathProperty(String name, Class<? extends T> type, PropertyConfigurationEditor configuration) {
		super(type, configuration);
		ObjectUtils.argumentNotNull(name, "Property name must be not null");
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryProperty#getQueryPath()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getParent()
	 */
	@Override
	public Optional<Path<?>> getParent() {
		return Optional.ofNullable(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path.Builder#parent(com.holonplatform.core.Path)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public P parent(Path<?> parent) {
		// check final paths
		if (parent != null && FinalPath.class.isAssignableFrom(this.getClass())) {
			throw new UnsupportedOperationException("Cannot declare a parent path for a final path: " + this);
		}
		this.parent = parent;
		return (P) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getName() == null) {
			throw new InvalidExpressionException("Null path name");
		}
	}

	@Override
	public PathPropertyBuilder<T> clone() {
		PathPropertyBuilder<T> builder = PathProperty.create(getName(), getType());
		// parent
		getParent().ifPresent(p -> builder.parent(p));
		// localizable
		builder.localization(this);
		// converter
		getConverter().ifPresent(c -> builder.converter(c));
		// validators
		getValidators().forEach(v -> builder.validator(v));
		// configuration
		getConfiguration().getTemporalType().ifPresent(t -> builder.temporalType(t));
		getConfiguration().forEachParameter((n, v) -> builder.configuration(n, v));
		return builder;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PathProperty [name=");
		sb.append(fullName());
		sb.append(", type=");
		sb.append(((getType() != null) ? getType().getName() : "null"));
		sb.append("]");
		return sb.toString();
	}

}
