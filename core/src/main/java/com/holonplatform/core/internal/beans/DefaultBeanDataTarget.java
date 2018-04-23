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
package com.holonplatform.core.internal.beans;

import java.util.Optional;

import com.holonplatform.core.beans.BeanDataTarget;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link BeanDataTarget} implementation.
 * 
 * @param <T> Bean type
 *
 * @since 5.1.0
 */
public class DefaultBeanDataTarget<T> implements BeanDataTarget<T> {

	private static final long serialVersionUID = -5872448163704220092L;

	private final Class<? extends T> beanClass;
	private final String name;
	private final String dataPath;

	/**
	 * Constructor.
	 * @param beanClass Bean class (not null)
	 */
	public DefaultBeanDataTarget(Class<? extends T> beanClass) {
		super();
		ObjectUtils.argumentNotNull(beanClass, "Bean class must be not null");
		this.beanClass = beanClass;
		this.name = beanClass.getSimpleName().toLowerCase();
		this.dataPath = BeanPropertySet.create(beanClass).getDataPath().orElse(null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Path#getDataPath()
	 */
	@Override
	public Optional<String> getDataPath() {
		return Optional.ofNullable(dataPath);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.TypedExpression#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return beanClass;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getType() == null) {
			throw new InvalidExpressionException("Null bean type");
		}
		if (getName() == null) {
			throw new InvalidExpressionException("Null path name");
		}
	}

}
