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
package com.holonplatform.core.internal.query.function;

import java.util.LinkedList;
import java.util.List;

import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.query.QueryFunction;

/**
 * Abstract {@link QueryFunction} class.
 * 
 * @param <T> Function result type
 * @param <A> Function arguments type
 *
 * @since 5.1.0
 */
public abstract class AbstractQueryFunction<T, A> implements QueryFunction<T, A> {

	/**
	 * Function arguments
	 */
	private final List<TypedExpression<? extends A>> arguments = new LinkedList<>();

	private int minimumArguments = 0;
	private int maximumArguments = -1;

	/**
	 * Empty constructor with no arguments.
	 */
	public AbstractQueryFunction() {
		super();
	}

	/**
	 * Constructor with single argument.
	 * @param argument Function argument (not null)
	 */
	public AbstractQueryFunction(TypedExpression<? extends A> argument) {
		super();
		ObjectUtils.argumentNotNull(argument, "Function argument must be not null");
		this.arguments.add(argument);
	}

	/**
	 * Constructor.
	 * @param arguments Function arguments
	 */
	@SafeVarargs
	public AbstractQueryFunction(TypedExpression<? extends A>... arguments) {
		super();
		if (arguments != null) {
			for (TypedExpression<? extends A> argument : arguments) {
				this.arguments.add(argument);
			}
		}
	}

	/**
	 * Get the minimum required function arguments.
	 * @return the minimum required function arguments
	 */
	protected int getMinimumArguments() {
		return minimumArguments;
	}

	/**
	 * Set the minimum required function arguments.
	 * @param minimumArguments the minimum required function arguments
	 */
	protected void setMinimumArguments(int minimumArguments) {
		this.minimumArguments = minimumArguments;
	}

	/**
	 * Get the maximum required function arguments.
	 * @return the maximum required function arguments, <code>-1</code> if there is not an upper bound
	 */
	protected int getMaximumArguments() {
		return maximumArguments;
	}

	/**
	 * Set the maximum required function arguments.
	 * @param maximumArguments the maximum required function arguments
	 */
	protected void setMaximumArguments(int maximumArguments) {
		this.maximumArguments = maximumArguments;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.query.QueryFunction#getExpressionArguments()
	 */
	@Override
	public List<TypedExpression<? extends A>> getExpressionArguments() {
		return arguments;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.Expression#validate()
	 */
	@Override
	public void validate() throws InvalidExpressionException {
		if (minimumArguments > 0 && arguments.size() < minimumArguments) {
			throw new InvalidExpressionException(
					"[" + getClass().getName() + "] Invalid function arguments count: minimum is " + minimumArguments
							+ ", got " + arguments.size());
		}
		if (maximumArguments > -1 && arguments.size() > maximumArguments) {
			throw new InvalidExpressionException(
					"[" + getClass().getName() + "] Invalid function arguments count: maximum is " + maximumArguments
							+ ", got " + arguments.size());
		}
	}

}
