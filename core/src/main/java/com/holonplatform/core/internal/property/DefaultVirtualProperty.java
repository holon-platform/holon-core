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

import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.property.VirtualProperty;
import com.holonplatform.core.property.VirtualProperty.VirtualPropertyBuilder;

/**
 * Default {@link VirtualProperty} implementation with {@link Localizable} support.
 * 
 * @param <T> Property value type
 * 
 * @since 5.0.0
 */
public class DefaultVirtualProperty<T> extends AbstractVirtualProperty<T, VirtualProperty<T>, VirtualPropertyBuilder<T>>
		implements VirtualPropertyBuilder<T> {

	private static final long serialVersionUID = -7091967623813118367L;

	/**
	 * Constructor
	 * @param type Property value type
	 */
	public DefaultVirtualProperty(Class<? extends T> type) {
		super(type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualProperty()
	 */
	@Override
	protected VirtualProperty<T> getActualProperty() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractProperty#getActualBuilder()
	 */
	@Override
	protected DefaultVirtualProperty<T> getActualBuilder() {
		return this;
	}

}
