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
package com.holonplatform.core.internal.datastore;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.internal.DefaultPath;

/**
 * Default {@link DataTarget} implementation.
 *
 * @param <T> Target type
 *
 * @since 5.0.0
 */
public class DefaultDataTarget<T> extends DefaultPath<T> implements DataTarget<T> {

	private static final long serialVersionUID = 6821077978867212734L;

	/**
	 * Constructor with name and type
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 */
	public DefaultDataTarget(String name, Class<? extends T> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataTarget [name=" + getName() + ", type=" + getType().getName() + "]";
	}

}
