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
package com.holonplatform.core.internal.datastore.beans;

import java.util.Optional;

import com.holonplatform.core.datastore.Datastore.OperationType;
import com.holonplatform.core.datastore.beans.BeanDatastore.BeanOperationResult;

/**
 * Default {@link BeanOperationResult} implementation.
 * 
 * @param <T> Bean class
 *
 * @since 5.1.0
 */
public class DefaultBeanOperationResult<T> implements BeanOperationResult<T> {

	private static final long serialVersionUID = -5435669226183765696L;

	private OperationType operationType;
	private long affectedCount = 0;
	private transient T result;

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
	 * @see com.holonplatform.core.datastore.beans.BeanDatastore.BeanOperationResult#getResult()
	 */
	@Override
	public Optional<T> getResult() {
		return Optional.ofNullable(result);
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
	 * Set the operation result bean instance.
	 * @param result the result bean to set
	 */
	public void setResult(T result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultBeanOperationResult [operationType=" + operationType + ", affectedCount=" + affectedCount
				+ ", result=" + result + "]";
	}

	/**
	 * Default {@link Builder} implementation.
	 * 
	 * @param <T> Bean class
	 */
	public static class DefaultBuilder<T> implements Builder<T> {

		private final DefaultBeanOperationResult<T> instance = new DefaultBeanOperationResult<>();

		@Override
		public Builder<T> type(OperationType type) {
			instance.setOperationType(type);
			return this;
		}

		@Override
		public Builder<T> affectedCount(long count) {
			instance.setAffectedCount(count);
			return this;
		}

		@Override
		public Builder<T> result(T bean) {
			instance.setResult(bean);
			return this;
		}

		@Override
		public BeanOperationResult<T> build() {
			return instance;
		}

	}

}
