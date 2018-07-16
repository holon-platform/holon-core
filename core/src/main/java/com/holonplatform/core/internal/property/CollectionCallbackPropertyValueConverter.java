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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.CollectionPropertyValueConverter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link PropertyValueConverter} implementation which uses two provided {@link Function}s to perform actual
 * conversions.
 *
 * @param <E> Collection element type
 * @param <C> Collection type
 * @param <MODEL> Model element type
 *
 * @since 5.2.0
 */
public class CollectionCallbackPropertyValueConverter<E, C extends Collection<E>, MODEL>
		implements CollectionPropertyValueConverter<E, C, MODEL> {

	private static final long serialVersionUID = -4226232700674616194L;

	private final Class<? extends C> propertyType;
	private final Class<MODEL> modelElementType;
	private final Function<MODEL, E> fromModel;
	private final Function<E, MODEL> toModel;
	private final Supplier<C> instanceProvider;

	public CollectionCallbackPropertyValueConverter(Class<? extends C> propertyType, Class<MODEL> modelElementType,
			Function<MODEL, E> fromModel, Function<E, MODEL> toModel, Supplier<C> instanceProvider) {
		super();
		ObjectUtils.argumentNotNull(propertyType, "Property type must be not null");
		ObjectUtils.argumentNotNull(modelElementType, "Model element type must be not null");
		ObjectUtils.argumentNotNull(fromModel, "Model to element type conversion function must be not null");
		ObjectUtils.argumentNotNull(toModel, "Element type to model conversion must be not null");
		ObjectUtils.argumentNotNull(instanceProvider, "Collection instance provider must be not null");
		this.propertyType = propertyType;
		this.modelElementType = modelElementType;
		this.fromModel = fromModel;
		this.toModel = toModel;
		this.instanceProvider = instanceProvider;
	}

	@Override
	public Class<MODEL> getModelElementType() {
		return modelElementType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#fromModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public C fromModel(Collection<MODEL> value, Property<C> property) throws PropertyConversionException {
		if (value != null) {
			if (!Collection.class.isAssignableFrom(value.getClass())) {
				throw new PropertyConversionException(property,
						"Cannot convert collection property value from model value [" + value
								+ "]: the model value type must be a Collection, got [" + value.getClass().getName()
								+ "]");
			}
			C instance = instanceProvider.get();
			for (MODEL element : value) {
				instance.add(fromModel.apply(element));
			}
			return instance;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#toModel(java.lang.Object,
	 * com.holonplatform.core.property.Property)
	 */
	@Override
	public Collection<MODEL> toModel(C value, Property<C> property) throws PropertyConversionException {
		if (value != null) {
			Collection<MODEL> model = createModelCollectionValue();
			for (E element : value) {
				model.add(toModel.apply(element));
			}
			return model;
		}
		return null;
	}

	protected Collection<MODEL> createModelCollectionValue() {
		if (Set.class.isAssignableFrom(propertyType)) {
			return new HashSet<>();
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getPropertyType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<C> getPropertyType() {
		return (Class<C>) propertyType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValueConverter#getModelType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<Collection<MODEL>> getModelType() {
		return (Class<Collection<MODEL>>) (Class<?>) Collection.class;
	}

}
