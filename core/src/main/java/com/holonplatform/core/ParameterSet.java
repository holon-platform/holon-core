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

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.internal.DefaultParameterSet;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Interface for name-value parameter sets management.
 * <p>
 * A ParameterSet is considered immutable, so only read methods are exposed.
 * </p>
 * <p>
 * Each parameter must me identified by a <code>String</code> name, unique within the set, and associated to a value of
 * any type. <code>null</code> values are allowed.
 * </p>
 * 
 * @since 4.5.0
 */
public interface ParameterSet extends Serializable {

	/**
	 * Check if some parameter is present
	 * @return <code>true</code> if some parameter is present, <code>false</code> if set is empty.
	 */
	boolean hasParameters();

	/**
	 * Check if parameter is present using parameter <code>name</code>. <code>null</code> parameter values are allowed,
	 * so if a parameter is present it doesn't means that it has a value.
	 * @param name Parameter name
	 * @return <code>true</code> if parameter is present, <code>false</code> otherwise
	 */
	boolean hasParameter(String name);

	/**
	 * Just like {@link #hasParameter(String)}, check if parameter with given <code>name</code> is present, but returns
	 * <code>true</code> only if parameter has a not <code>null</code> value.
	 * @param name Parameter name
	 * @return <code>true</code> if parameter is present and its value is not null, <code>false</code> otherwise
	 */
	boolean hasNotNullParameter(String name);

	/**
	 * Get value of parameter named <code>name</code>, if found.
	 * @param name Parameter name (not null)
	 * @return Parameter value, or an empty Optional if parameter name is not present
	 */
	Optional<Object> getParameter(String name);

	/**
	 * Get typed parameter value. An unchecked cast to required type will be performed.
	 * @param <T> Parameter value type
	 * @param name Parameter name (not null)
	 * @param type Expected value type
	 * @return Parameter value, or an empty Optional if parameter is not present
	 */
	<T> Optional<T> getParameter(String name, Class<T> type);

	/**
	 * Get typed parameter value with default-fallback support. An unchecked cast to required type will be performed. If
	 * parameter value was not found, <code>defaultValue</code> is returned.
	 * @param <T> Parameter value type
	 * @param name Parameter name (not null)
	 * @param type Expected value type
	 * @param defaultValue Default value to return when parameter was not found
	 * @return Parameter value, or <code>defaultValue</code> if parameter is not present
	 */
	default <T> T getParameter(String name, Class<T> type, T defaultValue) {
		return getParameter(name, type).orElse(defaultValue);
	}

	/**
	 * Get a (typed) parameter value only if parameter is present, has a not <code>null</code> value, and its value
	 * satisfies given <code>condition</code>.
	 * @param <T> Parameter value type
	 * @param name Parameter name (not null)
	 * @param type Expected value type
	 * @param condition Condition to check (not null)
	 * @return Parameter value, or an empty Optional if parameter is not present, or has not a value or its value does
	 *         not satisfy given <code>condition</code>
	 */
	<T> Optional<T> getParameterIf(String name, Class<T> type, Predicate<T> condition);

	/**
	 * Check if a parameter is present using given {@link ConfigProperty} property key. <code>null</code> parameter
	 * values are allowed, so if a parameter is present it doesn't means that it has a value.
	 * @param <T> Parameter value type
	 * @param property ConfigProperty to check (not null)
	 * @return <code>true</code> if parameter is present, <code>false</code> otherwise
	 */
	default <T> boolean hasParameter(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		return hasParameter(property.getKey());
	}

	/**
	 * Just like {@link #hasParameter(ConfigProperty)}, check if parameter with given key is present, but returns
	 * <code>true</code> only if parameter has a not <code>null</code> value.
	 * @param <T> Parameter value type
	 * @param property ConfigProperty to check (not null)
	 * @return <code>true</code> if parameter is present and its value is not null, <code>false</code> otherwise
	 */
	default <T> boolean hasNotNullParameter(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		return hasNotNullParameter(property.getKey());
	}

	/**
	 * Get the value of the parameter which name matches given {@link ConfigProperty} key, if found.
	 * @param <T> Parameter value type
	 * @param property ConfigProperty to get (not null)
	 * @return Parameter value, or an empty Optional if parameter name is not present
	 */
	default <T> Optional<T> getParameter(ConfigProperty<T> property) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		return getParameter(property.getKey(), property.getType());
	}

	/**
	 * Get the value of the parameter which name matches given {@link ConfigProperty} key with default-fallback support.
	 * @param <T> Parameter value type
	 * @param property ConfigProperty to get (not null)
	 * @param defaultValue Default value to return when parameter was not found
	 * @return Parameter value, or <code>defaultValue</code> if parameter is not present
	 */
	default <T> T getParameter(ConfigProperty<T> property, T defaultValue) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		return getParameter(property.getKey(), property.getType(), defaultValue);
	}

	/**
	 * Get a (typed) parameter value only if parameter is present, has a not <code>null</code> value, and its value
	 * satisfies given <code>condition</code>. This method uses a {@link ConfigProperty} to represent parameter name and
	 * type.
	 * @param <T> Parameter value type
	 * @param property ConfigProperty to get (not null)
	 * @param condition Condition to check (not null)
	 * @return Parameter value, or an empty Optional if parameter is not present, or has not a value or its value does
	 *         not satisfy given <code>condition</code>
	 */
	default <T> Optional<T> getParameterIf(ConfigProperty<T> property, Predicate<T> condition) {
		ObjectUtils.argumentNotNull(property, "ConfigProperty must be not null");
		return getParameterIf(property.getKey(), property.getType(), condition);
	}

	/**
	 * Performs the given action for each entry (name-value) of this parameter set.
	 * @param action Action to perform (not null)
	 */
	void forEachParameter(BiConsumer<String, Object> action);

	// Builders

	/**
	 * Create an empty {@link ParameterSet}.
	 * @return A new empty {@link ParameterSet}
	 */
	static ParameterSet empty() {
		return new DefaultParameterSet(Collections.emptyMap());
	}

	/**
	 * Builder to create a ParameterSet
	 * @return ParameterSetBuilder
	 */
	static Builder<ParameterSet> builder() {
		return new DefaultParameterSet.DefaultBuilder();
	}

	// Builder

	/**
	 * {@link ParameterSet} builder.
	 * @param <S> Concrete ParameterSet type to build
	 */
	public interface Builder<S extends ParameterSet> {

		/**
		 * Add a parameter
		 * @param name Parameter name
		 * @param value Parameter value
		 * @return this
		 */
		Builder<S> parameter(String name, Object value);

		/**
		 * Add a parameter using a {@link ConfigProperty} and {@link ConfigProperty#getKey()} as parameter name
		 * @param <T> Property type
		 * @param property ConfigProperty (not null) to obtain parameter name
		 * @param value Parameter value
		 * @return this
		 */
		<T> Builder<S> parameter(ConfigProperty<T> property, T value);

		/**
		 * Add all given parameters to the set
		 * @param parameters Parameters to add
		 * @return this
		 */
		Builder<S> parameters(Map<String, Object> parameters);

		/**
		 * Add all parameters of the given <code>parameters</code> set
		 * @param parameters Parameters to add
		 * @return this
		 */
		Builder<S> parameters(ParameterSet parameters);

		/**
		 * Build ParameterSet
		 * @return ParameterSet instance
		 */
		S build();

	}

}
