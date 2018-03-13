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
package com.holonplatform.core.query;

import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.internal.query.DefaultBeanProjection;

/**
 * A {@link QueryProjection} which uses a {@link BeanPropertySet} to define the projection selection and returns
 * {@link BeanPropertySet#getBeanClass()} type results.
 * 
 * @param <T> Bean result type
 * 
 * @since 5.0.0
 */
@SuppressWarnings("rawtypes")
public interface BeanProjection<T> extends QueryProjection<T> {

	/**
	 * Get the projection bean class.
	 * @return Projection bean class (not null)
	 */
	Class<? extends T> getBeanClass();

	/**
	 * Get the optional projection selection bean {@link Path}s. If not provided, all valid bean property paths will be
	 * used as selection.
	 * @return Optional projection selection paths
	 */
	Optional<Path[]> getSelection();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryProjection#getType()
	 */
	@Override
	default Class<? extends T> getType() {
		return getBeanClass();
	}

	/**
	 * Create a {@link BeanProjection} using given bean class.
	 * @param <T> Bean and result type
	 * @param beanClass Bean class (not null)
	 * @param selection Optional selection paths. If not provided, all valid bean property paths will be used.
	 * @return BeanProjection using given bean class
	 */
	static <T> BeanProjection<T> of(Class<? extends T> beanClass, Path... selection) {
		return new DefaultBeanProjection<>(beanClass, selection);
	}

}
