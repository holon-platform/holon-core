/*
 * Copyright 2016-2018 Axioma srl.
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

import java.util.Collection;

/**
 * A {@link PropertyValueConverter} to handle {@link Collection} property and model type values.
 *
 * @param <TYPE> Collection property element type
 * @param <C> Collection type
 * @param <MODEL> Model collection element type
 * 
 * @since 5.2.0
 */
public interface CollectionPropertyValueConverter<TYPE, C extends Collection<TYPE>, MODEL>
		extends PropertyValueConverter<C, Collection<MODEL>> {

	/**
	 * Get the model collection element type.
	 * @return model collection element type
	 */
	Class<MODEL> getModelElementType();

}
