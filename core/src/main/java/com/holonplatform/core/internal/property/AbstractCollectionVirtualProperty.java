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
import com.holonplatform.core.property.CollectionProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.VirtualProperty;

/**
 * Base Collection type {@link VirtualProperty} implementation.
 *
 * @param <E> Collection element type
 * @param <C> Collection type
 * @param <P> Actual property type
 * @param <B> Concrete property builder type
 *
 * @since 5.2.0
 */
public abstract class AbstractCollectionVirtualProperty<E, C extends Collection<E>, P extends CollectionProperty<E, C> & VirtualProperty<C>, B extends VirtualProperty.Builder<C, P, B> & CollectionProperty.Builder<E, C, P, B>>
		extends AbstractVirtualProperty<C, P, B>
		implements CollectionProperty<E, C>, CollectionProperty.Builder<E, C, P, B> {

	private static final long serialVersionUID = 5459973618033959757L;

	/**
	 * Collection element type
	 */
	private final Class<? extends E> elementType;

	/**
	 * Constructor.
	 * @param type Property type (not null)
	 * @param elementType Collection element type (not null)
	 */
	public AbstractCollectionVirtualProperty(Class<? extends C> type, Class<? extends E> elementType) {
		super(type);
		ObjectUtils.argumentNotNull(elementType, "Collection element type must be not null");
		this.elementType = elementType;
	}

	/**
	 * Get the default collection instances provider to be used with value converters.
	 * @return The default collection instances provider
	 */
	protected abstract Supplier<C> getDefaultInstanceProvider();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return true;
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
	 * @see com.holonplatform.core.property.CollectionProperty.Builder#elementConverter(java.lang.Class,
	 * java.util.function.Function, java.util.function.Function)
	 */
	@Override
	public <MODEL> B elementConverter(Class<MODEL> modelElementType, Function<MODEL, E> fromModel,
			Function<E, MODEL> toModel) {
		return converter(new CollectionCallbackPropertyValueConverter<>(getType(), modelElementType, fromModel, toModel,
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

}
