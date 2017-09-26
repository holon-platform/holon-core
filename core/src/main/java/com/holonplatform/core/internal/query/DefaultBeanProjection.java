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
package com.holonplatform.core.internal.query;

import java.util.Arrays;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.BeanProjection;

/**
 * Default {@link BeanProjection} implementation.
 * 
 * @param <T> Bean projection type
 *
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public class DefaultBeanProjection<T> implements BeanProjection<T>, VisitableQueryProjection<T> {

	/**
	 * Bean class
	 */
	private final Class<T> beanClass;

	/**
	 * Selection
	 */
	private final Path[] selection;

	/**
	 * Constructor
	 * @param beanClass Bean class (not null)
	 * @param selection Optional selection paths
	 */
	public DefaultBeanProjection(Class<T> beanClass, Path[] selection) {
		super();
		ObjectUtils.argumentNotNull(beanClass, "Bean class must be not null");
		this.beanClass = beanClass;
		this.selection = (selection != null && selection.length > 0) ? selection : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.BeanProjection#getBeanClass()
	 */
	@Override
	public Class<T> getBeanClass() {
		return beanClass;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.BeanProjection#getSelection()
	 */
	@Override
	public Optional<Path[]> getSelection() {
		return Optional.ofNullable(selection);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getBeanClass() == null) {
			throw new InvalidExpressionException("Null bean class");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection#accept(com.holonplatform.
	 * core.internal.query.QueryProjectionVisitor, java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QueryProjectionVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultBeanProjection [beanClass=" + beanClass + ", selection=" + Arrays.toString(selection) + "]";
	}

}
