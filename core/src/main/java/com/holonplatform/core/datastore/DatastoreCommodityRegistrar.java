/*
 * Copyright 2000-2016 Holon TDCN.
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

import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;

/**
 * Interface implemented by objects which provide direct {@link DatastoreCommodity}s registration.
 *
 * @param <X> Concrete commodity context type
 *
 * @since 5.0.0
 */
public interface DatastoreCommodityRegistrar<X extends DatastoreCommodityContext> {

	/**
	 * Get the {@link DatastoreCommodityFactory} base type to use with this Datastore.
	 * @return the DatastoreCommodityFactory base type
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends DatastoreCommodityFactory> getCommodityFactoryType();
	
	/**
	 * Register a {@link DatastoreCommodityFactory} to made available
	 * {@link DatastoreCommodityFactory#getCommodityType()} type commodities. If a {@link DatastoreCommodityFactory}
	 * which deals with the same commodity type is already registered, it will be replaced be the given one.
	 * @param <C> Commodity type
	 * @param commodityFactory The factory to register (not null)
	 * @throws CommodityConfigurationException If an error occurred
	 */
	<C extends DatastoreCommodity> void registerCommodity(DatastoreCommodityFactory<X, C> commodityFactory);

}
