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
package com.holonplatform.core.temporal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Optional;

/**
 * Enumeration of temporal macro-types.
 * 
 * @since 5.0.0
 */
public enum TemporalType {

	/**
	 * Temporal representing only a date
	 */
	DATE,

	/**
	 * Temporal representing only a time
	 */
	TIME,

	/**
	 * Temporal representing a date and time
	 */
	DATE_TIME;

	/**
	 * Get the temporal macro-type of given <code>temporal</code> instance, if available.
	 * @param temporal Temporal object
	 * @return {@link TemporalType} of given {@link Temporal}, empty if given temporal is <code>null</code>
	 */
	public static Optional<TemporalType> getTemporalType(Temporal temporal) {
		if (temporal != null) {
			if (temporal.isSupported(ChronoUnit.HOURS)) {
				return Optional.of((temporal.isSupported(ChronoUnit.DAYS)) ? DATE_TIME : TIME);
			}
			return Optional.of(DATE);
		}
		return Optional.empty();
	}

	/**
	 * Get the temporal macro-type of given <code>type</code>, if it is well-known temporal type.
	 * <p>
	 * For {@link java.util.Date} and {@link java.util.Calendar} types, {@link #DATE_TIME} is returned.
	 * </p>
	 * @param type Type to inspect
	 * @return {@link TemporalType} of given type, empty if <code>null</code> or if it is not recognized as a known
	 *         temporal type
	 */
	public static Optional<TemporalType> getTemporalType(Class<?> type) {
		if (type != null) {
			if (LocalDate.class.isAssignableFrom(type)) {
				return Optional.of(DATE);
			}
			if (LocalTime.class.isAssignableFrom(type) || OffsetTime.class.isAssignableFrom(type)) {
				return Optional.of(TIME);
			}
			if (java.util.Date.class.isAssignableFrom(type) || java.util.Calendar.class.isAssignableFrom(type)
					|| LocalDateTime.class.isAssignableFrom(type) || OffsetDateTime.class.isAssignableFrom(type)
					|| ZonedDateTime.class.isAssignableFrom(type)) {
				return Optional.of(DATE_TIME);
			}
		}
		return Optional.empty();
	}

}
