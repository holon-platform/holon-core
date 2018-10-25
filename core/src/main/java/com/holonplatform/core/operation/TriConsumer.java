/*
 * Copyright 2016-2018 Axioma srl.
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
package com.holonplatform.core.operation;

/**
 * Operation that accepts three input arguments and returns no result.
 *
 * @param <F> type of the first argument
 * @param <S> type of the second argument
 * @param <T> type of the third argument
 * 
 * @since 5.2.0
 */
public interface TriConsumer<F, S, T> {

	/**
	 * Performs this operation on the given arguments.
	 * @param f the first input argument
	 * @param s the second input argument
	 * @param t the third input argument
	 */
	void accept(F f, S s, T t);

}
