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
package com.holonplatform.core.internal.query.sort;

import com.holonplatform.core.Path;
import com.holonplatform.core.internal.query.QuerySortVisitor;
import com.holonplatform.core.internal.query.QuerySortVisitor.VisitableQuerySort;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QuerySort.PathQuerySort;

/**
 * Default {@link PathQuerySort} implementation.
 * 
 * @param <T> Path type
 * 
 * @since 4.5.0
 */
public class Sort<T> implements PathQuerySort<T>, VisitableQuerySort {

	private static final long serialVersionUID = -6626959844031968290L;

	/**
	 * Sort path
	 */
	private Path<T> path;

	/**
	 * Sort direction
	 */
	private SortDirection direction = SortDirection.ASCENDING;

	/**
	 * Default constructor
	 */
	public Sort() {
		super();
	}

	/**
	 * Construct a new PropertySort using default {@link SortDirection#ASCENDING} sort direction.
	 * @param path Path by which to sort (not null)
	 */
	public Sort(Path<T> path) {
		this(path, SortDirection.ASCENDING);
	}

	/**
	 * Construct a new PropertySort
	 * @param path Path by which to sort (not null)
	 * @param direction Sort direction. If <code>null</code>, the default {@link SortDirection#ASCENDING} direction will
	 *        be used
	 */
	public Sort(Path<T> path, SortDirection direction) {
		super();
		ObjectUtils.argumentNotNull(path, "Path must be not null");
		this.path = path;
		this.direction = (direction != null) ? direction : SortDirection.ASCENDING;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QuerySort.PathQuerySort#getPath()
	 */
	@Override
	public Path<T> getPath() {
		return path;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.query.sort.PathQuerySort#getDirection()
	 */
	@Override
	public SortDirection getDirection() {
		return direction;
	}

	/**
	 * Set the sort direction
	 * @param direction Sort direction to set
	 */
	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (getPath() == null) {
			throw new InvalidExpressionException("Null sort path");
		}
		if (getDirection() == null) {
			throw new InvalidExpressionException("Null sort direction");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.VisitableQuerySort#accept(com.holonplatform.core.query.QuerySortVisitor,
	 * java.lang.Object)
	 */
	@Override
	public <R, C> R accept(QuerySortVisitor<R, C> visitor, C context) {
		return visitor.visit(this, context);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sort on path [");
		sb.append(getPath() != null ? getPath().fullName() : "<NULL>");
		sb.append("] with direction [");
		if (getDirection() == null) {
			sb.append(SortDirection.ASCENDING.name());
		} else {
			sb.append(getDirection().name());
		}
		sb.append("]");
		return sb.toString();
	}

}
