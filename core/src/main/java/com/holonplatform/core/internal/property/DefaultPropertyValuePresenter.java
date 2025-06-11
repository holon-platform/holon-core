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

import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.annotation.Priority;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.presentation.StringValuePresenter;
import com.holonplatform.core.property.CollectionProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyValuePresenter;

/**
 * Default {@link PropertyValuePresenter}, using default {@link StringValuePresenter#getDefault()} and
 * {@link PropertyConfiguration} as presentation parameters source.
 *
 * @param <T> Property value type
 *
 * @since 5.0.0
 */
@Priority(Integer.MAX_VALUE)
public class DefaultPropertyValuePresenter<T> implements PropertyValuePresenter<T> {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyValuePresenter#present(com.holonplatform.core.property.Property,
	 * java.lang.Object)
	 */
	@Override
	public String present(Property<T> property, T value) {
		ObjectUtils.argumentNotNull(property, "Property must be not null");

		// check TemporalType
		ParameterSet.Builder<?> builder = ParameterSet.builder().withParameters(property.getConfiguration());
		property.getConfiguration().getTemporalType()
				.ifPresent(t -> builder.withParameter(StringValuePresenter.TEMPORAL_TYPE, t));
		final ParameterSet parameters = builder.build();

		// check collection property
		if (value != null && CollectionProperty.class.isAssignableFrom(property.getClass())
				&& Collection.class.isAssignableFrom(value.getClass())) {
			final Class<?> elementType = ((CollectionProperty<?, ?>) property).getElementType();
			return ((Collection<?>) value).stream().map(v -> presentValue(elementType, v, parameters))
					.collect(Collectors.joining(","));
		}
		// default presentation
		return presentValue(property.getType(), value, parameters);
	}

	/**
	 * Present a value as a {@link String} using the default {@link StringValuePresenter}.
	 * @param <V> Value type
	 * @param type Value type
	 * @param value Value to present
	 * @param parameters Presentation parameters
	 * @return Value as {@link String}
	 */
	private static <V> String presentValue(Class<? extends V> type, V value, ParameterSet parameters) {
		return StringValuePresenter.getDefault().present(type, value, parameters);
	}

}
