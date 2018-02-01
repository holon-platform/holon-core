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

import java.util.Optional;

import com.holonplatform.core.Provider;

/**
 * Default {@link Provider} implementation.
 * 
 * @param <T> Provided object type
 *
 * @since 5.1.0
 */
public class DefaultProvider<T> implements Provider<T> {

	private final T value;

	public DefaultProvider(T value) {
		super();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Provider#get()
	 */
	@Override
	public Optional<T> get() {
		return Optional.ofNullable(value);
	}

}
