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

import jakarta.annotation.Priority;

/**
 * Presenter to obtain the value of a {@link Property} as a {@link String}.
 * <p>
 * PropertyValuePresenters are managed by a {@link PropertyValuePresenterRegistry}, which handles the presenters
 * registration and returns a suitable presenter for a {@link Property} relying on the conditions with which the
 * presenters were registered.
 * </p>
 *
 * @param <T> Property value type
 *
 * @since 5.0.0
 * 
 * @see PropertyValuePresenterRegistry
 * @see Property#present(Object)
 */
@FunctionalInterface
public interface PropertyValuePresenter<T> {

	/**
	 * Default {@link PropertyValuePresenter} priority if not specified using {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Get the <code>value</code> of the given <code>property</code> as {@link String}.
	 * @param property Property to which the value refers (not null)
	 * @param value Value to present (may be null)
	 * @return String representation of the value
	 */
	String present(Property<T> property, T value);

}
