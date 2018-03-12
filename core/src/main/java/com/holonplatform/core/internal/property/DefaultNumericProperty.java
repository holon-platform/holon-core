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

import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.NumericProperty.NumericPropertyBuilder;

/**
 * Default {@link NumericProperty} implementation.
 * 
 * @param <N> Numeric property type
 *
 * @since 5.1.0
 */
public class DefaultNumericProperty<N extends Number> extends
		AbstractPathProperty<N, NumericProperty<N>, NumericPropertyBuilder<N>> implements NumericPropertyBuilder<N> {

	private static final long serialVersionUID = -2278164109480762076L;

	/**
	 * Constructor
	 * @param name Property name (not null)
	 * @param type Property type (not null)
	 */
	public DefaultNumericProperty(String name, Class<? extends N> type) {
		super(name, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected NumericProperty<N> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected NumericPropertyBuilder<N> getActualBuilder() {
		return this;
	}

}
