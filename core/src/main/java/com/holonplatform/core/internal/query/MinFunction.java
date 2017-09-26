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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryFunction.Min;

/**
 * {@link Min} function implementation.
 * 
 * @param <T> Function result type
 *
 * @since 5.0.0
 */
public class MinFunction<T> implements Min<T> {

	private final Class<? extends T> resultType;

	/**
	 * Constructor
	 * @param resultType Result type (not null)
	 */
	public MinFunction(Class<? extends T> resultType) {
		super();
		ObjectUtils.argumentNotNull(resultType, "Result type must be not null");
		this.resultType = resultType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFunction#getResultType()
	 */
	@Override
	public Class<? extends T> getResultType() {
		return resultType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getResultType() == null) {
			throw new InvalidExpressionException("Null function result type");
		}
	}

}
