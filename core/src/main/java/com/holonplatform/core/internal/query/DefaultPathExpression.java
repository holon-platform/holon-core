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
package com.holonplatform.core.internal.query;

import com.holonplatform.core.internal.DefaultPath;
import com.holonplatform.core.query.PathExpression;

/**
 * Default {@link PathExpression} implementation.
 *
 * @param <T> Path and expression type
 *
 * @since 5.0.0
 */
public class DefaultPathExpression<T> extends DefaultPath<T> implements PathExpression<T> {

	private static final long serialVersionUID = 5990464618958961015L;

	/**
	 * Constructor
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 */
	public DefaultPathExpression(String name, Class<? extends T> type) {
		super(name, type);
	}

}
