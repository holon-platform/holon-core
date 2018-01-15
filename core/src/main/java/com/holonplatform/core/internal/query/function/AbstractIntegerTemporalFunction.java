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

import com.holonplatform.core.query.QueryExpression;
import com.holonplatform.core.query.TemporalFunction;

/**
 * Abstract {@link TemporalFunction} with an {@link Integer} result type.
 *
 * @since 5.1.0
 */
public abstract class AbstractIntegerTemporalFunction extends AbstractPropertyQueryFunction<Integer, Object>
		implements TemporalFunction<Integer> {

	private static final long serialVersionUID = 7911584185529712160L;

	public AbstractIntegerTemporalFunction(QueryExpression<?> argument) {
		super(argument, Integer.class);
	}

}
