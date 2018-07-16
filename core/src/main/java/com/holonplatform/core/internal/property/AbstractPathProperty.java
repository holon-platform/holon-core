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
package com.holonplatform.core.internal.property;

import java.util.Optional;
import java.util.function.Consumer;

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.Path;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PathProperty.Builder;

/**
 * Abstract {@link PathProperty} implementation and builder.
 * 
 * @param <T> Property value type
 * @param <P> Property type
 * @param <B> Concrete property type
 * 
 * @since 5.0.0
 * 
 * @see DefaultPathProperty
 */
public abstract class AbstractPathProperty<T, P extends PathProperty<T>, B extends Builder<T, P, B>>
		extends AbstractProperty<T, P, B> implements Builder<T, P, B>, PathProperty<T> {

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
		super(type);
		ObjectUtils.argumentNotNull(name, "Property name must be not null");
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.DataMappable#getDataPath()
	 */
	@Override
	public Optional<String> getDataPath() {
		return getConfiguration().getParameter(DataMappable.PATH);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.DataMappable.Builder#dataPath(java.lang.String)
	 */
	@Override
	public B dataPath(String dataPath) {
		return configuration(DataMappable.PATH, dataPath);
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
	public B parent(Path<?> parent) {
		// check final paths
		if (parent != null && FinalPath.class.isAssignableFrom(this.getClass())) {
			throw new UnsupportedOperationException("Cannot declare a parent path for a final path: " + this);
		}
		this.parent = parent;
		return (B) this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getName() == null) {
			throw new InvalidExpressionException("Null path name");
		}
	}

	/**
	 * Clone a concrete path property.
	 * @param <I> Actual property type
	 * @param property The property instance to clone
	 * @param builder Property builder reference
	 * @return Cloned property
	 */
	@SuppressWarnings("unchecked")
	protected <I extends AbstractPathProperty<T, P, B>> I clonePathProperty(I property,
			Consumer<PathProperty.Builder<T, PathProperty<T>, ?>> builder) {
		// clone property
		cloneProperty(property);
		// parent
		getParent().ifPresent(p -> property.parent(p));
		// consumer
		if (builder != null) {
			builder.accept((PathProperty.Builder<T, PathProperty<T>, ?>) property);
		}
		return property;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PathProperty [name=" + name + ", type=" + getType() + "]";
	}

}
