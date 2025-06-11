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
package com.holonplatform.core.datastore;

import java.io.Serializable;

import jakarta.annotation.Priority;

import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;

/**
 * Factory to create a specific {@link DatastoreCommodity} type instances, using a {@link DatastoreCommodityContext}.
 * 
 * @param <X> Concrete commodity context type
 * @param <C> Actual commodity type
 *
 * @since 5.0.0
 */
public interface DatastoreCommodityFactory<X extends DatastoreCommodityContext, C extends DatastoreCommodity>
		extends Serializable {

	/**
	 * Default {@link DatastoreCommodityFactory} priority if not specified using {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Get the {@link DatastoreCommodity} type which this factory deals with.
	 * @return Concrete commodity type (not null)
	 */
	Class<? extends C> getCommodityType();

	/**
	 * Create a {@link DatastoreCommodity} instance.
	 * @param context Commodity configuration context
	 * @return The new commodity instance
	 * @throws CommodityConfigurationException If a commodity configuration error occurred
	 */
	C createCommodity(X context) throws CommodityConfigurationException;

}
