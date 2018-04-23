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
package com.holonplatform.core.query;

import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;

/**
 * Represents a <em>query</em>, which can be used to configure and execute a query on the data managed by a
 * {@link Datastore}.
 * <p>
 * Query is configured through the {@link QueryBuilder} interface and executed using the {@link QueryResults} interface,
 * which provides method to execute the query and obtain a result using a {@link QueryProjection} to specify the
 * expected result type.
 * </p>
 * <p>
 * Extends {@link DatastoreCommodity} to allow query definition and registration using the {@link Datastore} commodities
 * paradigm.
 * </p>
 * 
 * @since 5.0.0
 */
public interface Query extends QueryBuilder<Query>, QueryResults, DatastoreCommodity {

}
