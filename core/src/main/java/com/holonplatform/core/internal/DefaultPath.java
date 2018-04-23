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

import com.holonplatform.core.Path;
import com.holonplatform.core.Path.PathBuilder;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link Path} implementation.
 * 
 * @param <T> Path type
 *
 * @since 5.0.0
 */
public class DefaultPath<T> implements PathBuilder<T> {

	private static final long serialVersionUID = 7636700561875450996L;

	/**
	 * Path name
	 */
	private final String name;

	/**
	 * Path type
	 */
	private final Class<? extends T> type;

	/**
	 * Parent path
	 */
	private Path<?> parent;

	/**
	 * Optional data path
	 */
	private String dataPath;

	/**
	 * Construct a new DefaultPath
	 * @param name Path name (not null)
	 * @param type Path type (not null)
	 */
	public DefaultPath(String name, Class<? extends T> type) {
		super();

		ObjectUtils.argumentNotNull(name, "Name must be not null");
		ObjectUtils.argumentNotNull(type, "Type must be not null");

		this.name = name;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getParent()
	 */
	@Override
	public Optional<Path<?>> getParent() {
		return Optional.ofNullable(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path.Builder#parent(com.holonplatform.core.Path)
	 */
	@Override
	public PathBuilder<T> parent(Path<?> parent) {
		this.parent = parent;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.DataMappable.Builder#dataPath(java.lang.String)
	 */
	@Override
	public PathBuilder<T> dataPath(String dataPath) {
		this.dataPath = dataPath;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.DataMappable#getDataPath()
	 */
	@Override
	public Optional<String> getDataPath() {
		return Optional.ofNullable(dataPath);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPath [name=" + name + ", type=" + type + ", parent=" + parent + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getName() == null) {
			throw new InvalidExpressionException("Null path name");
		}
	}

}
