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
package com.holonplatform.core.internal.property;

import java.util.function.Consumer;

import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.StringProperty.StringPropertyBuilder;

/**
 * Default {@link StringProperty} implementation.
 *
 * @since 5.1.0
 */
public class DefaultStringProperty extends AbstractPathProperty<String, StringProperty, StringPropertyBuilder>
		implements StringPropertyBuilder {

	private static final long serialVersionUID = -3647255339530687803L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 */
	public DefaultStringProperty(String name) {
		super(name, String.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected StringProperty getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected StringPropertyBuilder getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.StringProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public StringProperty clone(Consumer<PathProperty.Builder<String, PathProperty<String>, ?>> builder) {
		return clonePathProperty(new DefaultStringProperty(getName()), builder);
	}

}
