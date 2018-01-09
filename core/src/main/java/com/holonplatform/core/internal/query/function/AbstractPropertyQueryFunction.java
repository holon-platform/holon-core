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
package com.holonplatform.core.internal.query.function;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.holonplatform.core.Validator;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.QueryFunction.PropertyQueryFunction;

/**
 * Abstract {@link PropertyQueryFunction} implementation.
 *
 * @param <T> Function result type
 * @param <A> Function arguments type
 * 
 * @since 5.1.0
 */
public abstract class AbstractPropertyQueryFunction<T, A> extends AbstractQueryFunction<T, A>
		implements PropertyQueryFunction<T, A> {

	private static final long serialVersionUID = 9044336386767727378L;

	private final PropertyConfiguration configuration;

	private PropertyValueConverter<T, ?> converter;

	/**
	 * Empty constructor
	 */
	public AbstractPropertyQueryFunction() {
		super();
		this.configuration = PropertyConfiguration.create();
	}

	/**
	 * Constructor with function argument.
	 * @param argument Function argument (not null)
	 */
	@SuppressWarnings("unchecked")
	public AbstractPropertyQueryFunction(QueryExpression<? extends A> argument) {
		super(argument);
		if (argument instanceof Property) {
			final Property<?> property = (Property<?>) argument;
			this.configuration = PropertyConfiguration.clone(property.getConfiguration());
			if (getType() == property.getType() && property.getConverter().isPresent()) {
				converter = (PropertyValueConverter<T, ?>) property.getConverter().get();
			}
		} else {
			this.configuration = PropertyConfiguration.create();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getConfiguration()
	 */
	@Override
	public PropertyConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.Property#getConverter()
	 */
	@Override
	public Optional<PropertyValueConverter<T, ?>> getConverter() {
		return Optional.ofNullable(converter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Validator.Validatable#getValidators()
	 */
	@Override
	public Collection<Validator<T>> getValidators() {
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable#getMessageCode()
	 */
	@Override
	public String getMessageCode() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.i18n.Localizable#getMessage()
	 */
	@Override
	public String getMessage() {
		return null;
	}

}
