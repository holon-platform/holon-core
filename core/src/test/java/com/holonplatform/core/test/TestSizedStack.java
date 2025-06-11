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
package com.holonplatform.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.utils.SizedStack;

public class TestSizedStack {

	@Test
	public void testStack() {
		SizedStack<Integer> stack = new SizedStack<>(3);

		assertEquals(3, stack.getMaxSize());

		stack.push(1);
		assertEquals(1, stack.size());
		stack.push(2);
		assertEquals(2, stack.size());
		stack.push(3);
		assertEquals(3, stack.size());

		stack.push(4);
		assertEquals(3, stack.size());

		assertEquals(Integer.valueOf(2), stack.firstElement());
	}

}
