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

import com.holonplatform.core.internal.query.QueryProjectionVisitor;
import com.holonplatform.core.internal.query.QueryProjectionVisitor.VisitableQueryProjection;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PathProperty.PathPropertyBuilder;

/**
 * Default {@link PathProperty} implementation.
 * 
 * @param <T> Property (path) type
 *
 * @since 5.0.0
 */
public class DefaultPathProperty<T> extends AbstractPathProperty<T, PathPropertyBuilder<T>>
		implements PathPropertyBuilder<T>, VisitableQueryProjection<T> {

	private static final long serialVersionUID = 5796523880557314657L;

	/**
	 * Constructor
	 * @param name Property name, must be not <code>null</code>
	 * @param type Property value type
	 */
	public DefaultPathProperty(String name, Class<? extends T> type) {
		super(name, type);
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

}
