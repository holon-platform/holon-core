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
package com.holonplatform.spring.internal;

import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.holonplatform.core.datastore.DataContextBound;

/**
 * A Spring {@link BeanDefinition} with {@link DataContextBound} support.
 *
 * @since 5.0.0
 */
public class GenericDataContextBoundBeanDefinition extends GenericBeanDefinition
		implements DataContextBoundBeanDefinition {

	private static final long serialVersionUID = 4030101451919228608L;

	private String dataContextId;

	public GenericDataContextBoundBeanDefinition() {
		super();
	}

	public GenericDataContextBoundBeanDefinition(BeanDefinition original) {
		super(original);
	}

	/**
	 * Set the data context id
	 * @param dataContextId the data context id to set
	 */
	public void setDataContextId(String dataContextId) {
		this.dataContextId = dataContextId;
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
