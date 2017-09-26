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

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;

/**
 * Default {@link Context} implementation, using {@link ContextManager} to access context scopes and resources.
 * 
 * @since 5.0.0
 */
public enum DefaultContext implements Context {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.Context#getScope(java.lang.String, java.lang.ClassLoader)
	 */
	@Override
	public Optional<ContextScope> scope(String name, ClassLoader classLoader) {
		return Optional.ofNullable(ContextManager.getScope(name, classLoader));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.context.Context#resource(java.lang.String, java.lang.Class, java.lang.ClassLoader)
	 */
	@Override
	public <T> Optional<T> resource(String resourceKey, Class<T> resourceType, ClassLoader classLoader) {
		for (ContextScope scope : ContextManager.getScopes(classLoader)) {
			Optional<T> value = scope.get(resourceKey, resourceType);
			if (value.isPresent()) {
				return value;
			}
		}
		return Optional.empty();
	}

}
