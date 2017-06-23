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
package com.holonplatform.core.utils;

import java.util.Stack;

/**
 * {@link Stack} extension which supports a max stack size.
 * <p>
 * If max size is valid (<code>&gt;0</code>), when stack size exceeds max size, the eldest element is removed before
 * adding a new one on the top of the stack.
 * </p>
 * 
 * @param <T> Stack elements type
 * 
 * @since 5.0.0
 */
public class SizedStack<T> extends Stack<T> {

	private static final long serialVersionUID = 5108382753384242948L;

	/**
	 * Max stack size
	 */
	private final int maxSize;

	/**
	 * Constructor
	 * @param maxSize Max stack size. If <code>$lt;=0</code>, max size is ignored and a standard unbound stack is
	 *        created.
	 */
	public SizedStack(int maxSize) {
		super();
		this.maxSize = maxSize;
	}

	/**
	 * Stack max size
	 * @return the max size
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Stack#push(java.lang.Object)
	 */
	@Override
	public T push(T item) {
		if (getMaxSize() > 0) {
			while (this.size() >= getMaxSize()) {
				this.remove(0);
			}
		}
		return super.push(item);
	}

}
