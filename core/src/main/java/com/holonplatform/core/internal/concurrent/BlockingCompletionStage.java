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
package com.holonplatform.core.internal.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Delegating class to provide a <em>blocking</em> version of a {@link CompletionStage}, represented as a
 * {@link Future}.
 * 
 * @param <T> Result type
 *
 * @since 5.2.0
 */
public class BlockingCompletionStage<T> implements Future<T> {

	private CompletableFuture<T> delegate;

	public BlockingCompletionStage(final CompletionStage<T> stage) {
		super();
		ObjectUtils.argumentNotNull(stage, "CompletionStage must be not null");
		try {
			this.delegate = stage.toCompletableFuture();
		} catch (@SuppressWarnings("unused") UnsupportedOperationException e) {
			// handle the stage completion
			this.delegate = new CompletableFuture<>();
			stage.whenComplete((v, t) -> {
				if (t != null) {
					this.delegate.completeExceptionally(t);
				} else {
					this.delegate.complete(v);
				}
			});
		}
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return delegate.isCancelled();
	}

	@Override
	public boolean isDone() {
		return delegate.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		return delegate.get();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return delegate.get(timeout, unit);
	}

}
