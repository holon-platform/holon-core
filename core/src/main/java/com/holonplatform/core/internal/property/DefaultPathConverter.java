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

import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.property.PathPropertySetAdapter.PathConverter;
import com.holonplatform.core.property.Property;

/**
 * Default {@link PathConverter}.
 *
 * @since 5.1.0
 */
public enum DefaultPathConverter implements PathConverter {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.property.PathPropertySetAdapter.PathConverter#convert(com.holonplatform.core.property.
	 * Property)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<Path<T>> convert(Property<T> property) {
		if (property != null && property instanceof Path) {
			return Optional.of((Path<T>) property);
		}
		return Optional.empty();
	}

}
