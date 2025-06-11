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
package com.holonplatform.core.property;

import java.util.function.Function;

import jakarta.annotation.Priority;

import com.holonplatform.core.internal.property.CallbackPropertyRenderer;

/**
 * Renderer to render a {@link Property} as a the target {@link #getRenderType()} object type.
 * <p>
 * PropertyRenderers are managed by a {@link PropertyRendererRegistry}, which handles the renderers registration and
 * returns a suitable renderer for a {@link Property} and a given rendering type relying on the conditions with which
 * the renderers were registered.
 * </p>
 *
 * @param <R> Rendering type
 * @param <T> Property type
 *
 * @since 5.0.0
 * 
 * @see PropertyRendererRegistry
 */
public interface PropertyRenderer<R, T> {

	/**
	 * Default {@link PropertyRenderer} priority if not specified using {@link Priority} annotation.
	 */
	public static final int DEFAULT_PRIORITY = 10000;

	/**
	 * Return the object type handled by this renderer and returned by the {@link #render(Property)} method.
	 * @return Render type
	 */
	Class<? extends R> getRenderType();

	/**
	 * Renders the given <code>property</code> as a {@link #getRenderType()} object
	 * @param property Property to render
	 * @return Rendered property object according to render type
	 */
	R render(Property<? extends T> property);

	/**
	 * Create a {@link PropertyRenderer} for given <code>renderingType</code> using given <code>renderer</code>
	 * {@link Function} to perform actual property rendering.
	 * @param <R> Rendering type
	 * @param <T> Property type
	 * @param renderingType Rendering type (not null)
	 * @param renderer Rendering function (not null)
	 * @return The {@link PropertyRenderer} instance
	 */
	static <R, T> PropertyRenderer<R, T> create(Class<? extends R> renderingType, Function<Property<? extends T>, R> renderer) {
		return new CallbackPropertyRenderer<>(renderingType, renderer);
	}

}
