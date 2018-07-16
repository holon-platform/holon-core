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

import com.holonplatform.core.property.BooleanProperty;
import com.holonplatform.core.property.BooleanProperty.BooleanPropertyBuilder;
import com.holonplatform.core.property.PathProperty;

/**
 * Default {@link BooleanProperty} implementation.
 *
 * @since 5.1.0
 */
public class DefaultBooleanProperty extends AbstractPathProperty<Boolean, BooleanProperty, BooleanPropertyBuilder>
		implements BooleanPropertyBuilder {

	private static final long serialVersionUID = -907186669849021056L;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 */
	public DefaultBooleanProperty(String name) {
		super(name, Boolean.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected BooleanProperty getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected BooleanPropertyBuilder getActualBuilder() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.BooleanProperty#clone(java.util.function.Consumer)
	 */
	@Override
	public BooleanProperty clone(Consumer<PathProperty.Builder<Boolean, PathProperty<Boolean>, ?>> builder) {
		return clonePathProperty(new DefaultBooleanProperty(getName()), builder);
	}

}
