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
package com.holonplatform.core.internal.datastore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.datastore.Datastore.OperationResult;
import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link OperationResult} implementation.
 *
 * @since 5.0.0
 */
public class DefaultOperationResult implements OperationResult {

	private static final long serialVersionUID = -5435669226183765696L;

	private OperationType operationType;
	private long affectedCount = 0;
	private Map<Path<?>, Object> insertedKeys;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.Datastore.OperationResult#getOperationType()
	 */
	@Override
	public Optional<OperationType> getOperationType() {
		return Optional.ofNullable(operationType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.Datastore.OperationResult#getAffectedCount()
	 */
	@Override
	public long getAffectedCount() {
		return affectedCount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.datastore.Datastore.OperationResult#getInsertedKeys()
	 */
	@Override
	public Map<Path<?>, Object> getInsertedKeys() {
		return (insertedKeys != null) ? insertedKeys : Collections.emptyMap();
	}

	/**
	 * Set the operation type
	 * @param operationType the operation type to set
	 */
	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	/**
	 * Set the affected elements count
	 * @param affectedCount the affected elements count to set
	 */
	public void setAffectedCount(long affectedCount) {
		this.affectedCount = affectedCount;
	}

	/**
	 * Set the inserted keys map
	 * @param insertedKeys the inserted keys map to set
	 */
	public void setInsertedKeys(Map<Path<?>, Object> insertedKeys) {
		this.insertedKeys = insertedKeys;
	}

	/**
	 * Add an inserted key
	 * @param key Key path (not null)
	 * @param value Key value
	 */
	public void addInsertedKey(Path<?> key, Object value) {
		ObjectUtils.argumentNotNull(key, "Key path must be not null");
		if (insertedKeys == null) {
			insertedKeys = new HashMap<>(4);
		}
		insertedKeys.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultOperationResult [operationType=" + operationType + ", affectedCount=" + affectedCount
				+ ", insertedKeys=" + insertedKeys + "]";
	}

	/**
	 * Default {@link Builder} implementation.
	 */
	public static class DefaultBuilder implements Builder {

		private final DefaultOperationResult instance = new DefaultOperationResult();

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.Datastore.OperationResult.Builder#type(com.holonplatform.core.datastore.
		 * Datastore.OperationType)
		 */
		@Override
		public Builder type(OperationType type) {
			instance.setOperationType(type);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.Datastore.OperationResult.Builder#affectedCount(int)
		 */
		@Override
		public Builder affectedCount(long count) {
			instance.setAffectedCount(count);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.Datastore.OperationResult.Builder#withInsertedKey(com.holonplatform.core.
		 * Path, java.lang.Object)
		 */
		@Override
		public Builder withInsertedKey(Path<?> key, Object value) {
			instance.addInsertedKey(key, value);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.Datastore.OperationResult.Builder#build()
		 */
		@Override
		public OperationResult build() {
			return instance;
		}

	}

}
