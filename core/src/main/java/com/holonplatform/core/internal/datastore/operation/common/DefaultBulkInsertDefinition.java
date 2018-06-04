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
package com.holonplatform.core.internal.datastore.operation.common;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;

/**
 * Default {@link BulkInsertDefinition}.
 *
 * @since 5.1.0
 */
public class DefaultBulkInsertDefinition extends AbstractDatastoreOperationDefinition implements BulkInsertDefinition {

	/*
	 * Operation values
	 */
	private final List<PropertyBox> values = new LinkedList<>();

	/*
	 * Operation property set
	 */
	private PropertySet<?> propertySet;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.commons.BulkInsertOperationConfiguration#getValues()
	 */
	@Override
	public List<PropertyBox> getValues() {
		return Collections.unmodifiableList(values);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.operation.commons.BulkInsertOperationConfiguration#getPropertySet()
	 */
	@Override
	public Optional<PropertySet<?>> getPropertySet() {
		return Optional.ofNullable(propertySet);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.operation.common.BulkInsertDefinition#addValue(com.holonplatform.core.
	 * property.PropertyBox)
	 */
	@Override
	public void addValue(PropertyBox value) {
		ObjectUtils.argumentNotNull(value, "Value must be not null");
		values.add(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.datastore.operation.common.BulkInsertDefinition#setPropertySet(com.holonplatform.
	 * core.property.PropertySet)
	 */
	@Override
	public void setPropertySet(PropertySet<?> propertySet) {
		this.propertySet = propertySet;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.datastore.bulk.AbstractBulkOperationDefinition#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		super.validate();
		if (getValues().isEmpty()) {
			throw new InvalidExpressionException("No values to insert");
		}
	}
}
