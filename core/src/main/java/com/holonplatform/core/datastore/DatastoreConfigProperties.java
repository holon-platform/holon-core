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
package com.holonplatform.core.datastore;

import java.util.Optional;

import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.config.ConfigPropertySet;
import com.holonplatform.core.internal.config.DefaultConfigPropertySet;

/**
 * A {@link ConfigPropertySet} for {@link Datastore} configuration, using {@link #DEFAULT_NAME} as property prefix.
 *
 * @since 5.0.0
 */
public interface DatastoreConfigProperties extends ConfigPropertySet, DataContextBound {

	/**
	 * Configuration property set default name
	 */
	static final String DEFAULT_NAME = "holon.datastore";

	/**
	 * Enable datastore operations tracing (for example, logging the concrete query definitions)
	 */
	static final ConfigProperty<Boolean> TRACE = ConfigProperty.create("trace", Boolean.class);

	/**
	 * The fully qualified class name of the <em>dialect</em> to use, if the concrete {@link Datastore} supports
	 * dialects.
	 */
	static final ConfigProperty<String> DIALECT = ConfigProperty.create("dialect", String.class);

	/**
	 * Gets whether datastore operations tracing is enabled.
	 * @return <code>true</code> if datastore operations tracing is enabled, <code>false</code> otherwise
	 */
	default boolean isTrace() {
		return getConfigPropertyValue(TRACE, Boolean.FALSE);
	}

	/**
	 * Get the dialect class name.
	 * @return Dialect class name, or <code>null</code> if not available
	 */
	default String getDialect() {
		return getConfigPropertyValue(DIALECT, null);
	}

	/**
	 * Builder to create property set instances bound to a property data source.
	 * @param dataContextId Optional data context id to which {@link Datastore} is bound
	 * @return ConfigPropertySet builder
	 */
	static Builder<DatastoreConfigProperties> builder(String dataContextId) {
		return new DefaultConfigPropertySet.DefaultBuilder<>(new DatastoreConfigPropertiesImpl(dataContextId));
	}

	/**
	 * Builder to create property set instances bound to a property data source, without data context id specification.
	 * @return ConfigPropertySet builder
	 */
	static Builder<DatastoreConfigProperties> builder() {
		return new DefaultConfigPropertySet.DefaultBuilder<>(new DatastoreConfigPropertiesImpl(null));
	}

	/**
	 * Default implementation
	 */
	static class DatastoreConfigPropertiesImpl extends DefaultConfigPropertySet implements DatastoreConfigProperties {

		private final String dataContextId;

		public DatastoreConfigPropertiesImpl(String dataContextId) {
			super((dataContextId != null && !dataContextId.trim().equals("")) ? (DEFAULT_NAME + "." + dataContextId)
					: DEFAULT_NAME);
			this.dataContextId = (dataContextId != null && !dataContextId.trim().equals("")) ? dataContextId : null;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.DataContextBound#getDataContextId()
		 */
		@Override
		public Optional<String> getDataContextId() {
			return Optional.ofNullable(dataContextId);
		}

	}

}
