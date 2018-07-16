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
package com.holonplatform.core;

import java.util.Optional;

import com.holonplatform.core.internal.query.DefaultPropertyExpressionValueConverter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * The expression value converter associated to a {@link ConverterExpression}.
 *
 * @param <TYPE> Expression type
 * @param <MODEL> Model type
 *
 * @since 5.1.0
 */
public interface ExpressionValueConverter<TYPE, MODEL> {

	/**
	 * Convert given value from model data type to expression value type.
	 * @param value Value to convert
	 * @return Value converted to expression value type
	 */
	TYPE fromModel(MODEL value);

	/**
	 * Convert given value from expression value type to model data type.
	 * @param value Value to convert
	 * @return Value converted to model data type
	 */
	MODEL toModel(TYPE value);

	/**
	 * Get the model data type.
	 * @return Model data type
	 */
	Class<MODEL> getModelType();

	/**
	 * Create a new {@link ExpressionValueConverter} using given <code>property</code>.
	 * @param <TYPE> Expression type
	 * @param property Property (not null)
	 * @return If the property provides a {@link PropertyValueConverter}, return an {@link ExpressionValueConverter}
	 *         which uses the property value converter to perform conversions. Otherwise, an empty Optional is returned.
	 */
	static <TYPE> Optional<ExpressionValueConverter<TYPE, ?>> fromProperty(Property<TYPE> property) {
		return property.getConverter().map(c -> new DefaultPropertyExpressionValueConverter<>(property, c));
	}

}
