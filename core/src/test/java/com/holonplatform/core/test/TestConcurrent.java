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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.concurrent.Futures;

public class TestConcurrent {

	@Test
	public void testFutureUtils() throws InterruptedException, ExecutionException, TimeoutException {

		final CompletionStage<String> stage = CompletableFuture.completedFuture("test");

		Future<String> f = Futures.asFuture(stage);
		assertNotNull(f);
		String value = f.get();
		assertEquals("test", value);

		value = Futures.block(stage);
		assertEquals("test", value);

		value = Futures.block(stage, 1, TimeUnit.SECONDS);
		assertEquals("test", value);

	}

}
