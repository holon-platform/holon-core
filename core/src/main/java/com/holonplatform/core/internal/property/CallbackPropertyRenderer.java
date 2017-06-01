/*
 * Copyright 2000-2016 Holon TDCN.
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

import java.util.function.Function;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyRenderer;

/**
 * A {@link PropertyRenderer} implementation which uses a {@link Function} to perform actual property rendering.
 *
 * @param <R> Rendering type
 * @param <T> Property type
 *
 * @since 5.0.0
 */
public class CallbackPropertyRenderer<R, T> implements PropertyRenderer<R, T> {

	private final Class<? extends R> renderingType;
	private final Function<Property<T>, R> renderer;

	/**
	 * Constructor
	 * @param renderingType Rendering type (not null)
	 * @param renderer Rendering function (not null)
	 */
	public CallbackPropertyRenderer(Class<? extends R> renderingType, Function<Property<T>, R> renderer) {
		super();
		ObjectUtils.argumentNotNull(renderingType, "Rendering type must be not null");
		ObjectUtils.argumentNotNull(renderer, "Renderer function must be not null");
		this.renderingType = renderingType;
		this.renderer = renderer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyRenderer#getRenderType()
	 */
	@Override
	public Class<? extends R> getRenderType() {
		return renderingType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PropertyRenderer#render(com.holonplatform.core.property.Property)
	 */
	@Override
	public R render(Property<T> property) {
		return renderer.apply(property);
	}

}
