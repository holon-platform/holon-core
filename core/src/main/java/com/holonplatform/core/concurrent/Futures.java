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
package com.holonplatform.core.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.holonplatform.core.internal.concurrent.BlockingCompletionStage;

/**
 * Covenience API to handle asynchronous operations results and perform {@link Future} type conversion and adaptation.
 * 
 * @since 5.2.0
 */
public interface Futures {

	/**
	 * Convert a {@link CompletionStage} in a {@link Future}, allowing to obtain the future stage result in a blocking
	 * fashion.
	 * @param <T> Result type
	 * @param stage The {@link CompletionStage} to convert (not null)
	 * @return A {@link Future} representing the given {@link CompletionStage}
	 */
	static <T> Future<T> asFuture(CompletionStage<T> stage) {
		return new BlockingCompletionStage<>(stage);
	}

	/**
	 * Obtain a {@link CompletionStage} result in <strong>blocking</strong> mode, waiting if necessary (indefinitely)
	 * for the computation to complete.
	 * @param <T> Result type
	 * @param stage The {@link CompletionStage} from which to obtain the result (not null)
	 * @return The computation result
	 * @throws CancellationException If the computation was cancelled
	 * @throws ExecutionException If the computation threw an exception
	 * @throws InterruptedException If the current thread was interrupted while waiting
	 */
	static <T> T block(CompletionStage<T> stage) throws InterruptedException, ExecutionException {
		return asFuture(stage).get();
	}

	/**
	 * Obtain a {@link CompletionStage} result in <strong>blocking</strong> mode, waiting if necessary for at most the
	 * given time for the computation to complete.
	 * @param <T> Result type
	 * @param stage The {@link CompletionStage} from which to obtain the result (not null)
	 * @param timeout The maximum time to wait
	 * @param unit The time unit of the timeout argument
	 * @return The computation result
	 * @throws CancellationException If the computation was cancelled
	 * @throws ExecutionException If the computation threw an exception
	 * @throws InterruptedException If the current thread was interrupted while waiting
	 * @throws TimeoutException if the wait timed out
	 */
	static <T> T block(CompletionStage<T> stage, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return asFuture(stage).get(timeout, unit);
	}

}
