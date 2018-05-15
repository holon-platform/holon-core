/*
 * Copyright 2016-2018 Axioma srl.
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

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.CollectionPathProperty;
import com.holonplatform.core.property.CollectionPathProperty.Builder;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * Base {@link CollectionPathProperty} implementation.
 * 
 * @param <E> Collection element type
 * @param <C> Collection type
 * @param <P> Actual property type
 * @param <B> Concrete property builder type
 *
 * @since 5.2.0
 */
public abstract class AbstractCollectionPathProperty<E, C extends Collection<E>, P extends CollectionPathProperty<E, C>, B extends Builder<E, C, P, B>>
		extends AbstractPathProperty<C, P, B> implements CollectionPathProperty<E, C>, Builder<E, C, P, B> {

	private static final long serialVersionUID = -3782502750069180604L;

	/**
	 * Collection element type
	 */
	private final Class<? extends E> elementType;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @param elementType Collection element type (not null)
	 */
	public AbstractCollectionPathProperty(String name, Class<? extends C> type, Class<? extends E> elementType) {
		super(name, type);
		ObjectUtils.argumentNotNull(elementType, "Collection element type must be not null");
		this.elementType = elementType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.CollectionProperty#getElementType()
	 */
	@Override
	public Class<? extends E> getElementType() {
		return elementType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return false;
	}

	/**
	 * Get the default collection instances provider to be used with value converters.
	 * @return The default collection instances provider
	 */
	protected abstract Supplier<C> getDefaultInstanceProvider();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.CollectionProperty.Builder#elementConverter(java.lang.Class,
	 * java.util.function.Function, java.util.function.Function)
	 */
	@Override
	public <MODEL> B elementConverter(Class<MODEL> modelType, Function<MODEL, E> fromModel,
			Function<E, MODEL> toModel) {
		return converter(new CollectionCallbackPropertyValueConverter<>(getType(), modelType, fromModel, toModel,
				getDefaultInstanceProvider()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.CollectionProperty.Builder#elementConverter(com.holonplatform.core.property.
	 * PropertyValueConverter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <MODEL> B elementConverter(PropertyValueConverter<E, MODEL> elementConverter) {
		ObjectUtils.argumentNotNull(elementConverter, "Element value converter must be not null");
		return converter(new CollectionCallbackPropertyValueConverter<>(getType(), elementConverter.getModelType(),
				v -> elementConverter.fromModel(v, (Property<E>) this),
				v -> elementConverter.toModel(v, (Property<E>) this), getDefaultInstanceProvider()));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectionPathProperty [name=" + getName() + ", type=" + getType() + ", elements type="
				+ getElementType() + "]";
	}

}
