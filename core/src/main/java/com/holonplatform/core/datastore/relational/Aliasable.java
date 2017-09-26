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
package com.holonplatform.core.datastore.relational;

import java.util.Optional;

import com.holonplatform.core.Path;

/**
 * Implemented by types which support an <code>alias</code> name, allows to set and retrieve the alias name.
 * 
 * @param <C> Concrete type
 * 
 * @since 5.0.0
 */
public interface Aliasable<C extends Aliasable<C>> {

	/**
	 * Get the optional alias name.
	 * @return Optional alias name
	 */
	Optional<String> getAlias();

	/**
	 * Set the alias name.
	 * @param alias The alias name to set
	 * @return A new object with the given <code>alias</code> setted
	 */
	C alias(String alias);

	/**
	 * Represents an {@link Aliasable} {@link Path}.
	 * @param <T> Path type
	 * @param <C> Concrete aliasable type
	 */
	public interface AliasablePath<T, C extends AliasablePath<T, C>> extends Aliasable<C>, Path<T> {

	}

}
