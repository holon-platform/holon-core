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
import com.holonplatform.core.query.QueryFunction.Max;

/**
 * {@link Max} function implementation.
 * 
 * @param <T> Function result type
 *
 * @since 5.0.0
 */
public class MaxFunction<T> extends AbstractPropertyQueryFunction<T, T> implements Max<T> {

	private static final long serialVersionUID = 2713208705061349621L;

	private final Class<? extends T> resultType;

	/**
	 * Constructor.
	 * @param argument Function argument (not null)
	 */
	public MaxFunction(QueryExpression<T> argument) {
		super(argument);
		this.resultType = argument.getType();
		setMinimumArguments(1);
		setMaximumArguments(1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return resultType;
	}

}
