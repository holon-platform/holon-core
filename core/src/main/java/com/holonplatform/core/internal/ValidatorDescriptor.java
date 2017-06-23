/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.core.internal;

import java.io.Serializable;
import java.util.Set;

import com.holonplatform.core.Validator;

/**
 * Default {@link Validator}s descriptor.
 *
 * @since 5.0.0
 */
public interface ValidatorDescriptor extends Serializable {

	/**
	 * Get whether the value is required.
	 * @return whether the value is required
	 */
	boolean isRequired();

	/**
	 * Get whether the value is of email type.
	 * @return whether the value is of email type.
	 */
	boolean isEmail();

	/**
	 * Get whether the value must be in the past.
	 * @return whether the value must be in the past
	 */
	boolean isPast();

	/**
	 * Get whether the value must be in the future.
	 * @return whether the value must be in the future
	 */
	boolean isFuture();

	/**
	 * Get the minimum value.
	 * @return the minimum value
	 */
	Number getMin();

	/**
	 * Get the maximum value.
	 * @return the maximum value
	 */
	Number getMax();

	/**
	 * Get the minimum value is exclusive.
	 * @return Whether the minimum value is exclusive
	 */
	boolean isExclusiveMin();

	/**
	 * Get the maximum value is exclusive.
	 * @return Whether the maximum value is exclusive
	 */
	boolean isExclusiveMax();

	/**
	 * Get the value regexp pattern.
	 * @return the value regexp pattern
	 */
	String getPattern();

	/**
	 * Get the integer digits count.
	 * @return the integer digits count
	 */
	Integer getIntegerDigits();

	/**
	 * Get the fractional digits count.
	 * @return the fractional digits count
	 */
	Integer getFractionDigits();

	/**
	 * Get the admitted values
	 * @return Admitted values
	 */
	Set<?> getIn();

	/**
	 * Get the not admitted values
	 * @return Not admitted values
	 */
	Set<?> getNotIn();

	/**
	 * Get a builder to create a {@link ValidatorDescriptor}.
	 * @return the builder
	 */
	static Builder builder() {
		return new DefaultValidatorDescriptor.DefaultBuilder();
	}

	/**
	 * Builder to create {@link ValidatorDescriptor} instances.
	 */
	public interface Builder {

		/**
		 * Set the value as required.
		 * @return this
		 */
		Builder required();

		/**
		 * Set the value as email type.
		 * @return this
		 */
		Builder email();

		/**
		 * Set the value must be in the past.
		 * @return this
		 */
		Builder past();

		/**
		 * Set the value must be in the future.
		 * @return this
		 */
		Builder future();

		/**
		 * Set the minimum value.
		 * @param min Minimum value
		 * @return this
		 */
		Builder min(Number min);

		/**
		 * Set the maximum value.
		 * @param max Maximum value
		 * @return this
		 */
		Builder max(Number max);

		/**
		 * Set the minimun value as exclusive.
		 * @return this
		 */
		Builder exclusiveMin();

		/**
		 * Set the maximum value as exclusive.
		 * @return this
		 */
		Builder exclusiveMax();

		/**
		 * Set the value regexp pattern.
		 * @param pattern Regexp pattern
		 * @return this
		 */
		Builder pattern(String pattern);

		/**
		 * Set the integer digits count.
		 * @param digits Integer digits count
		 * @return this
		 */
		Builder integerDigits(int digits);

		/**
		 * Set the fractional digits count.
		 * @param digits Fractional digits count
		 * @return this
		 */
		Builder fractionDigits(int digits);

		/**
		 * Set the admitted values.
		 * @param values Admitted values
		 * @return this
		 */
		Builder in(Object... values);

		/**
		 * Set the not admitted values.
		 * @param values Not admitted values
		 * @return this
		 */
		Builder notIn(Object... values);

		/**
		 * Build the {@link ValidatorDescriptor} instance.
		 * @return the {@link ValidatorDescriptor} instance
		 */
		ValidatorDescriptor build();

	}

}