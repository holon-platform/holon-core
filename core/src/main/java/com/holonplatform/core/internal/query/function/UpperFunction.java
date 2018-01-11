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
import com.holonplatform.core.query.StringFunction.Upper;

/**
 * {@link Upper} function implementation.
 *
 * @since 5.1.0
 */
public class UpperFunction extends AbstractPropertyQueryFunction<String, String> implements Upper {

	private static final long serialVersionUID = -1072443866437951740L;

	/**
	 * Constructor.
	 * @param argument Function argument (not null)
	 */
	public UpperFunction(QueryExpression<String> argument) {
		super(argument);
		setMinimumArguments(1);
		setMaximumArguments(1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.StringFunction#getType()
	 */
	@Override
	public Class<? extends String> getType() {
		return Upper.super.getType();
	}

}