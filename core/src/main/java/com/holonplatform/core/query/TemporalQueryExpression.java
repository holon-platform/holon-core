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
package com.holonplatform.core.query;

import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.query.QueryFunction.PropertyQueryFunction;
import com.holonplatform.core.query.TemporalFunction.Day;
import com.holonplatform.core.query.TemporalFunction.Hour;
import com.holonplatform.core.query.TemporalFunction.Month;
import com.holonplatform.core.query.TemporalFunction.Year;

/**
 * A {@link QueryExpression} of a temporal type.
 * 
 * @param <T> Expression type
 *
 * @since 5.1.0
 */
public interface TemporalQueryExpression<T> extends QueryExpression<T> {

	// ------- Function builders

	/**
	 * Creates a function to extract the <em>year</em> part of a temporal data type.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Year} function
	 */
	default Year year() {
		return Year.create(this);
	}

	/**
	 * Creates a function to extract the <em>month</em> part of a temporal data type.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Month} function
	 */
	default Month month() {
		return Month.create(this);
	}

	/**
	 * Creates a function to extract the <em>day</em> part of a temporal data type.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Day} function
	 */
	default Day day() {
		return Day.create(this);
	}

	/**
	 * Creates a function to extract the <em>hour</em> part of a temporal data type.
	 * <p>
	 * The returned function is a {@link PropertyQueryFunction}, to allow inclusion in property sets and handling within
	 * a {@link PropertyBox}.
	 * </p>
	 * @return A new {@link Hour} function
	 */
	default Hour hour() {
		return Hour.create(this);
	}

}
