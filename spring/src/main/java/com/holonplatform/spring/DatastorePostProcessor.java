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
package com.holonplatform.spring;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.datastore.ConfigurableDatastore;

/**
 * A post processor to configure a {@link ConfigurableDatastore} bean right after its initialization. Can be used, for
 * example, to register {@link ExpressionResolver}s.
 * <p>
 * A class implementing this interface declared as Spring bean is automatically detected by Datastores and the method
 * {@link #postProcessDatastore(ConfigurableDatastore, String)} is called at Datastore bean initialization. The
 * DatastorePostProcessor class must be registered with singleton scope in context.
 * </p>
 * 
 * @since 5.0.0
 */
public interface DatastorePostProcessor {

	/**
	 * Configure given <code>datastore</code>.
	 * @param datastore Datastore instance
	 * @param datastoreBeanName Datastore bean name
	 */
	void postProcessDatastore(ConfigurableDatastore datastore, String datastoreBeanName);

}
