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
package com.holonplatform.core.internal;

import com.holonplatform.core.Path;
import com.holonplatform.core.Path.FinalPath.FinalPathBuilder;

/**
 * Default {@link FinalPath} implementation.
 *
 * @param <T> Path type
 *
 * @since 5.1.0
 */
public class DefaultFinalPath<T> extends DefaultPath<T> implements FinalPathBuilder<T> {

	private static final long serialVersionUID = 1436121213156928030L;

	/**
	 * Constructor.
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 */
	public DefaultFinalPath(String name, Class<? extends T> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.DefaultPath#parent(com.holonplatform.core.Path)
	 */
	@Override
	public PathBuilder<T> parent(Path<?> parent) {
		throw new UnsupportedOperationException("Cannot set a parent for a FinalPath");
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultFinalPath [getName()=" + getName() + "]";
	}

}
