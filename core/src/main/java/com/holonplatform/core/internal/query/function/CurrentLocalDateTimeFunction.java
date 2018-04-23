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

import java.time.LocalDateTime;

import com.holonplatform.core.query.TemporalFunction;
import com.holonplatform.core.query.TemporalFunction.CurrentLocalDateTime;

/**
 * A {@link TemporalFunction} to obtain the current date and time as a {@link LocalDateTime}.
 *
 * @since 5.1.0
 */
public class CurrentLocalDateTimeFunction extends AbstractQueryFunction<LocalDateTime, Object>
		implements CurrentLocalDateTime {

	public CurrentLocalDateTimeFunction() {
		super();
		setMinimumArguments(0);
		setMaximumArguments(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression#getType()
	 */
	@Override
	public Class<? extends LocalDateTime> getType() {
		return LocalDateTime.class;
	}

}
