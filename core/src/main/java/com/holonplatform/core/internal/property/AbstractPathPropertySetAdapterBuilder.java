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
package com.holonplatform.core.internal.property;

import com.holonplatform.core.property.PathPropertySetAdapter;
import com.holonplatform.core.property.PathPropertySetAdapter.PathConverter;
import com.holonplatform.core.property.PathPropertySetAdapter.PathMatcher;

/**
 * Abstract {@link PathPropertySetAdapter} builder.
 * 
 * @param <B> Concrete builder type
 * @param <A> PathPropertySetAdapter type
 * @param <I> Implementation type
 *
 * @since 5.1.0
 */
public abstract class AbstractPathPropertySetAdapterBuilder<B extends PathPropertySetAdapter.Builder<B, A>, A extends PathPropertySetAdapter, I extends DefaultPathPropertySetAdapter>
		implements PathPropertySetAdapter.Builder<B, A> {

	protected final I instance;

	public AbstractPathPropertySetAdapterBuilder(I instance) {
		super();
		this.instance = instance;
	}

	protected abstract B getBuilder();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.property.PathPropertySetAdapter.Builder#pathConverter(com.holonplatform.core.property.
	 * PathPropertySetAdapter.PathConverter)
	 */
	@Override
	public B pathConverter(PathConverter pathConverter) {
		instance.setPathConverter(pathConverter);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertySetAdapter.Builder#pathMatcher(com.holonplatform.core.property.
	 * PathPropertySetAdapter.PathMatcher)
	 */
	@Override
	public B pathMatcher(PathMatcher pathMatcher) {
		instance.setPathMatcher(pathMatcher);
		return getBuilder();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertySetAdapter.Builder#withCaseInsensitivePathMatcher()
	 */
	@Override
	public B withCaseInsensitivePathMatcher() {
		instance.setPathMatcher(DefaultCaseInsensitivePathMatcher.INSTANCE);
		return getBuilder();
	}

}
