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
package com.holonplatform.core.internal.datastore.operation;

import com.holonplatform.core.datastore.operation.PropertyBoxOperation;
import com.holonplatform.core.datastore.operation.PropertyBoxOperationConfiguration;
import com.holonplatform.core.property.PropertyBox;

/**
 * Abstract {@link PropertyBoxOperation}.
 * 
 * @param <O> Actual operation type
 * @param <C> Actual operation configuration type
 * @param <D> Actual operation definition type
 *
 * @since 5.1.0
 */
public abstract class AbstractPropertyBoxOperation<O extends PropertyBoxOperation<O, C>, C extends PropertyBoxOperationConfiguration, D extends PropertyBoxOperationDefinition>
		extends AbstractDatastoreOperation<O, C, D> implements PropertyBoxOperation<O, C> {

	/**
	 * Constructor.
	 * @param definition Operation definition (not null)
	 */
	public AbstractPropertyBoxOperation(D definition) {
		super(definition);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.PropertyBoxOperation#value(com.holonplatform.core.property.
	 * PropertyBox)
	 */
	@Override
	public O value(PropertyBox value) {
		getDefinition().setValue(value);
		return getActualOperation();
	}

}
