/*
 * Copyright 2000-2016 Holon TDCN.
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

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

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
	 * Get temporal macro-type of given <code>temporal</code> instance
	 * @param temporal Temporal to process
	 * @return TemporalType of given {@link Temporal}, or <code>null</code> if given temporal was null
	 */
	public static TemporalType getTemporalType(Temporal temporal) {
		if (temporal != null) {
			if (temporal.isSupported(ChronoUnit.HOURS)) {
				return (temporal.isSupported(ChronoUnit.DAYS)) ? DATE_TIME : TIME;
			}
			return DATE;
		}
		return null;
	}

}
