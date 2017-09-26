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

import java.util.Optional;

/**
 * Interface to distinguish a specific data context (such as a database connection) between multiple avaialable contexts
 * using a String id.
 * 
 * @since 5.0.0
 */
public interface DataContextBound {

	/**
	 * Get data context id, if configured.
	 * @return Optional data context id
	 */
	Optional<String> getDataContextId();

}
