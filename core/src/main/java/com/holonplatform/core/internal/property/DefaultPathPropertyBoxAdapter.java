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

import java.util.Optional;
import java.util.function.Consumer;

import com.holonplatform.core.Path;
import com.holonplatform.core.property.PathPropertyBoxAdapter;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyBox;

/**
 * Default {@link PathPropertyBoxAdapter} implementation.
 *
 * @since 5.1.0
 */
public class DefaultPathPropertyBoxAdapter extends DefaultPathPropertySetAdapter implements PathPropertyBoxAdapter {

	private final PropertyBox propertyBox;

	/**
	 * Constructor
	 * @param propertyBox The property box to adapt (not null)
	 */
	public DefaultPathPropertyBoxAdapter(PropertyBox propertyBox) {
		super(propertyBox);
		this.propertyBox = propertyBox;
	}

	/**
	 * Get the adapted {@link PropertyBox}.
	 * @return the adapted {@link PropertyBox}
	 */
	protected PropertyBox getPropertyBox() {
		return propertyBox;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertyBoxAdapter#containsValue(com.holonplatform.core.Path)
	 */
	@Override
	public <T> boolean containsValue(Path<T> path) {
		return getProperty(path).map(p -> getPropertyBox().containsValue(p)).orElse(false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertyBoxAdapter#getValue(com.holonplatform.core.Path)
	 */
	@Override
	public <T> Optional<T> getValue(Path<T> path) {
		return getProperty(path).map(p -> getPropertyBox().getValue(p));
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertyBoxAdapter#getValueOrElse(com.holonplatform.core.Path,
	 * java.util.function.Consumer)
	 */
	@Override
	public <T> Optional<T> getValueOrElse(Path<T> path, Consumer<Path<T>> valueNotPresent) {
		Optional<T> value = getValue(path);
		if (!value.isPresent() && valueNotPresent != null) {
			valueNotPresent.accept(path);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.property.PathPropertyBoxAdapter#setValue(com.holonplatform.core.Path,
	 * java.lang.Object)
	 */
	@Override
	public <T> Optional<Property<T>> setValue(Path<T> path, T value) {
		Optional<Property<T>> property = getProperty(path);
		property.ifPresent(p -> {
			getPropertyBox().setValue(p, value);
		});
		return property;
	}

	/**
	 * Default {@link PathPropertySetAdapterBuilder}.
	 */
	public static class DefaultPathPropertyBoxAdapterBuilder extends
			AbstractPathPropertySetAdapterBuilder<PathPropertyBoxAdapterBuilder, PathPropertyBoxAdapter, DefaultPathPropertyBoxAdapter>
			implements PathPropertyBoxAdapterBuilder {

		public DefaultPathPropertyBoxAdapterBuilder(PropertyBox propertyBox) {
			super(new DefaultPathPropertyBoxAdapter(propertyBox));
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.property.AbstractPathPropertySetAdapterBuilder#getBuilder()
		 */
		@Override
		protected PathPropertyBoxAdapterBuilder getBuilder() {
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.property.PathPropertySetAdapter.Builder#build()
		 */
		@Override
		public PathPropertyBoxAdapter build() {
			return instance;
		}
	}

}
