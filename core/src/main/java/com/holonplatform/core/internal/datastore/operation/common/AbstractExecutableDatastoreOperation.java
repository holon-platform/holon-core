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
package com.holonplatform.core.internal.datastore.operation.common;

import com.holonplatform.core.datastore.operation.commons.ExecutablePropertyBoxOperation;
import com.holonplatform.core.datastore.operation.commons.PropertyBoxOperationConfiguration;

/**
 * Abstract {@link ExecutablePropertyBoxOperation} implementation.
 * 
 * @param <R> Operation result type
 * @param <O> Concrete operation type
 *
 * @since 5.2.0
 */
public abstract class AbstractExecutableDatastoreOperation<R, O extends ExecutablePropertyBoxOperation<R, O>>
		extends AbstractPropertyBoxOperation<O, PropertyBoxOperationConfiguration, PropertyBoxOperationDefinition>
		implements ExecutablePropertyBoxOperation<R, O> {

	private static final long serialVersionUID = 8131544081190818395L;

	public AbstractExecutableDatastoreOperation() {
		super(new DefaultPropertyBoxOperationDefinition());
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.DatastoreOperation#getConfiguration()
	 */
	@Override
	public PropertyBoxOperationConfiguration getConfiguration() {
		return getDefinition();
	}

}
