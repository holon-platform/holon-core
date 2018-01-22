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
package com.holonplatform.core.property;

import java.util.Optional;
import java.util.function.Consumer;

import com.holonplatform.core.Path;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.internal.property.DefaultPathPropertyBoxAdapter.DefaultPathPropertyBoxAdapterBuilder;
import com.holonplatform.core.property.Property.PropertyAccessException;
import com.holonplatform.core.property.Property.PropertyReadOnlyException;

/**
 * Adapter to use {@link Path} expressions to inspect and set {@link PropertyBox} values.
 *
 * @since 5.1.0
 */
public interface PathPropertyBoxAdapter extends PathPropertySetAdapter {

	/**
	 * Check if the {@link PropertyBox} contains a {@link Property} which corresponds to given <code>path</code> with a
	 * not <code>null</code> value.
	 * @param property Path to check (not null)
	 * @param <T> Property type
	 * @return <code>true</code> if a property which corresponds to given <code>path</code> is present and the property
	 *         value is not <code>null</code>
	 */
	<T> boolean containsValue(Path<T> path);

	/**
	 * Get the value of the {@link PropertyBox} property which corresponds to given <code>path</code>, if the the
	 * property is present and its value is not <code>null</code>.
	 * @param <T> Path and value type
	 * @param property Path for which obtain the property value (not null)
	 * @return The value of the property which corresponds to given <code>path</code>, or an empty Optional if path does
	 *         not corresponds to any property or the property has no value
	 */
	<T> Optional<T> getValue(Path<T> path);

	/**
	 * Get the value of the {@link PropertyBox} property which corresponds to given <code>path</code>, if the the
	 * property is present and its value is not <code>null</code>. When no property or value available, invokes the
	 * given {@link Consumer}.
	 * @param <T> Path and value type
	 * @param path Path for which obtain the property value (not null)
	 * @param valueNotPresent Consumer to be invoked when a value is not available for given path
	 * @return The value of the property which corresponds to given <code>path</code>, or an empty Optional if path does
	 *         not corresponds to any property or the property has no value
	 */
	<T> Optional<T> getValueOrElse(Path<T> path, Consumer<Path<T>> valueNotPresent);

	/**
	 * Set the value of the {@link PropertyBox} property which corresponds to given <code>path</code>, if such property
	 * is available.
	 * @param <T> Path and value type
	 * @param property Path for which to set the value (not null)
	 * @param value The value to set
	 * @return The property which corresponds to given <code>path</code> and for which the value was setted, or an empty
	 *         Optional if no property corresponds to given <code>path</code>
	 * @throws PropertyAccessException If an error occurred setting the property value
	 * @throws PropertyReadOnlyException If the property is read-only
	 * @throws ValidationException If property value validation fails
	 */
	<T> Optional<Property<T>> setValue(Path<T> path, T value);

	/**
	 * Create a new {@link PathPropertyBoxAdapter}.
	 * @param propertyBox The {@link PropertyBox} set to use (not null)
	 * @return A new {@link PathPropertyBoxAdapter}
	 */
	static PathPropertyBoxAdapter create(PropertyBox propertyBox) {
		return builder(propertyBox).build();
	}

	/**
	 * Create a new {@link PathPropertyBoxAdapter} builder.
	 * @param propertyBox The {@link PropertyBox} set to use (not null)
	 * @return A new {@link PathPropertyBoxAdapter} builder
	 */
	static PathPropertyBoxAdapterBuilder builder(PropertyBox propertyBox) {
		return new DefaultPathPropertyBoxAdapterBuilder(propertyBox);
	}

	/**
	 * Default builder.
	 */
	public interface PathPropertyBoxAdapterBuilder
			extends Builder<PathPropertyBoxAdapterBuilder, PathPropertyBoxAdapter> {

	}

}
