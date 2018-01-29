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

import java.util.Collection;

import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityNotAvailableException;

/**
 * Represents a {@link DatastoreCommodity} handler, which provides available commodities and allow to create
 * {@link DatastoreCommodity} instances.
 *
 * @since 5.1.0
 */
public interface DatastoreCommodityHandler {

	/**
	 * Get the available {@link DatastoreCommodity} types. A registered {@link DatastoreCommodity} can be created using
	 * the {@link #create(Class)} method.
	 * @return Available {@link DatastoreCommodity} types collection, empty if none
	 */
	Collection<Class<? extends DatastoreCommodity>> getAvailableCommodities();

	/**
	 * Create a new {@link DatastoreCommodity} of given <code>commodityType</code> type.
	 * <p>
	 * Available commodity types can be obtained using {@link #getAvailableCommodities()}.
	 * </p>
	 * @param <C> Commodity type
	 * @param commodityType The commodity type to create (not null)
	 * @return The commodity instance
	 * @throws CommodityNotAvailableException If a commodity of the required type is not available
	 * @throws CommodityConfigurationException If a commodity configuration error occurred
	 */
	<C extends DatastoreCommodity> C create(Class<C> commodityType);

}
