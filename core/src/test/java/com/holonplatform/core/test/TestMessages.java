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
package com.holonplatform.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.holonplatform.core.messaging.MessageHeaders;

public class TestMessages {

	@Test
	public void testMessageHeaders() {

		MessageHeaders<String> mh = new MessageHeaders<String>() {

			@Override
			public Map<String, String> getHeaders() {
				return Collections.singletonMap("a", "b");
			}
		};

		assertNotNull(mh.getHeaders());
		assertEquals(1, mh.getHeaders().size());
		assertTrue(mh.getHeader("a").isPresent());
		assertEquals("b", mh.getHeader("a").orElse(null));

	}

	@Test
	public void testMessageHeadersNullHeader() {

		final MessageHeaders<String> mh = new MessageHeaders<String>() {

			@Override
			public Map<String, String> getHeaders() {
				return Collections.singletonMap("a", "b");
			}
		};

		Assertions.assertThrows(IllegalArgumentException.class, () -> mh.getHeader(null));
	}

}
