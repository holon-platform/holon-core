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

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;

/**
 * Default handler to provide <code>equals</code> and <code>hashCode</code> logic for a {@link PropertyBox}, using the
 * property set identifier properties, if available.
 *
 * @since 5.1.0
 */
public enum DefaultPropertyBoxEqualsHashCodeHandler
		implements BiFunction<PropertyBox, Integer, Integer>, BiPredicate<PropertyBox, Object> {

	/**
	 * Singleton istance
	 */
	INSTANCE;

	@Override
	public boolean test(final PropertyBox pb, final Object other) {
		if (pb == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof PropertyBox))
			return false;
		// compare identifier values, if available form both PropertyBox instances
		return getIdentifierValues(pb)
				.map(values -> getIdentifierValues((PropertyBox) other)
						.map(otherValues -> Arrays.equals(values, otherValues)).orElse(false))
				// defaults to Object equals
				.orElse(pb == other);
	}

	@Override
	public Integer apply(PropertyBox pb, Integer defaultHashCode) {
		// check identifier
		return getIdentifierValues(pb).map(values -> {
			int result = (defaultHashCode != null) ? (31 * defaultHashCode.intValue()) : 1;
			return 31 * result + Arrays.hashCode(values);
		}).orElse(pb.hashCode()); // defaults to Object hashCode
	}

	/**
	 * Get the identifier property values, if any.
	 * @param pb Property value from which to obtain the identifier property values
	 * @return An array of the identifier property values, or an empty Optional is no identifier property available
	 */
	@SuppressWarnings("rawtypes")
	private static Optional<Object[]> getIdentifierValues(PropertyBox pb) {
		final Set<Property> identifiers = pb.getIdentifiers();
		if (!identifiers.isEmpty()) {
			return Optional.of(identifiers.stream().map(p -> pb.getValue((Property<?>) p)).toArray());
		}
		return Optional.empty();
	}

}
