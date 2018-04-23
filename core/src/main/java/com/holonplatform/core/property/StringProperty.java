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
package com.holonplatform.core.property;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.property.DefaultStringProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.StringQueryExpression;

/**
 * A String type {@link PathProperty}.
 * 
 * <p>
 * Extends {@link StringQueryExpression} to provide String type related query expression builder methods.
 * </p>
 *
 * @since 5.1.0
 */
public interface StringProperty extends PathProperty<String>, StringQueryExpression {

	/**
	 * Create a new {@link StringProperty} with given <code>name</code>.
	 * @param name Property name (not null)
	 * @return {@link StringProperty} builder
	 */
	static StringPropertyBuilder create(String name) {
		return new DefaultStringProperty(name);
	}

	/**
	 * Create a new {@link StringProperty} from given <code>path</code>, using given {@link Path} <code>name</code>.
	 * @param path Path from which to obtain the property path name (not null)
	 * @return {@link StringProperty} builder
	 */
	static StringPropertyBuilder create(Path<String> path) {
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		StringPropertyBuilder builder = create(path.getName());
		path.getParent().ifPresent(p -> builder.parent(p));
		return builder;
	}

	/**
	 * {@link StringProperty} builder.
	 */
	public interface StringPropertyBuilder
			extends Builder<String, StringProperty, StringPropertyBuilder>, StringProperty {

	}

}
