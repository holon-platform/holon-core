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
package com.holonplatform.core.query.async;

import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodity;
import com.holonplatform.core.query.QueryBuilder;
import com.holonplatform.core.query.QueryProjection;

/**
 * Represents an asynchronous <em>query</em>, which can be used to configure and execute a query on the data managed by
 * a {@link Datastore} and obtain the results asynchronously.
 * <p>
 * The query is configured through the {@link QueryBuilder} API and executed using the {@link AsyncQueryResults} API,
 * which provides methods to execute the query and obtain the results asynchronously, using a {@link QueryProjection} to
 * specify the expected result type.
 * </p>
 * <p>
 * Extends {@link DatastoreCommodity} to allow query definition and registration using the {@link Datastore} commodities
 * paradigm.
 * </p>
 * 
 * @since 5.2.0
 */
public interface AsyncQuery extends QueryBuilder<AsyncQuery>, AsyncQueryResults, DatastoreCommodity {

}
