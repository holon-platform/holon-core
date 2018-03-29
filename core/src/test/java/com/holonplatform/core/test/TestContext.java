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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.core.exceptions.TypeMismatchException;
import com.holonplatform.core.internal.ContextManager;
import com.holonplatform.core.internal.ContextResourceMap;

public class TestContext {

	@Test
	public void testDefaultScopes() {

		Optional<ContextScope> threadScope = Context.get().scope(Context.THREAD_SCOPE_NAME);
		assertTrue(threadScope.isPresent());

		final ContextScope ts1 = threadScope.get();

		threadScope = Context.get().threadScope();
		assertTrue(threadScope.isPresent());
		assertTrue(threadScope.get().equals(ts1));

		Optional<ContextScope> classLoaderScope = Context.get().scope(Context.CLASSLOADER_SCOPE_NAME);
		assertTrue(classLoaderScope.isPresent());

		final ContextScope cs1 = classLoaderScope.get();

		classLoaderScope = Context.get().scope(Context.CLASSLOADER_SCOPE_NAME);
		assertTrue(classLoaderScope.isPresent());
		assertTrue(classLoaderScope.get().equals(cs1));

	}

	@Test
	public void testScopeRegistry() {

		ContextManager.registerScope(ContextManager.getDefaultClassLoader(), new DummyScope());

		Optional<ContextScope> scope = Context.get().scope("dummy");
		assertTrue(scope.isPresent());

		final String TEST = "test";

		Optional<String> value = Context.get().resource(TEST, String.class);
		assertFalse(value.isPresent());

		final String tValue = "threadValue";
		final String clValue = "classLoaderValue";
		final String dValue = "dummyValue";

		Context.get().scope(Context.CLASSLOADER_SCOPE_NAME).ifPresent((s) -> s.put(TEST, clValue));
		value = Context.get().resource(TEST, String.class);
		assertTrue(value.isPresent());
		assertEquals(clValue, value.get());

		Context.get().scope("dummy").ifPresent((s) -> s.put(TEST, dValue));
		value = Context.get().resource(TEST, String.class);
		assertTrue(value.isPresent());
		assertEquals(dValue, value.get());

		Context.get().scope(Context.THREAD_SCOPE_NAME).ifPresent((s) -> s.put(TEST, tValue));
		value = Context.get().resource(TEST, String.class);
		assertTrue(value.isPresent());
		assertEquals(tValue, value.get());

		Context.get().scope(Context.THREAD_SCOPE_NAME).ifPresent((s) -> s.remove(TEST));
		value = Context.get().resource(TEST, String.class);
		assertTrue(value.isPresent());
		assertEquals(dValue, value.get());

		Context.get().scope("dummy").ifPresent((s) -> s.remove(TEST));
		value = Context.get().resource(TEST, String.class);
		assertTrue(value.isPresent());
		assertEquals(clValue, value.get());

		Context.get().scope(Context.CLASSLOADER_SCOPE_NAME).ifPresent((s) -> s.remove(TEST));
		value = Context.get().resource(TEST, String.class);
		assertFalse(value.isPresent());

		ContextManager.unregisterScope(ContextManager.getDefaultClassLoader(), "dummy");
		scope = Context.get().scope("dummy");
		assertFalse(scope.isPresent());

	}
	
	@Test
	public void testScopeMultiThread() {
		Optional<ContextScope> threadscope = Context.get().scope(Context.THREAD_SCOPE_NAME);
		Assert.assertNotNull(threadscope.get());

		Thread th1 = new Thread(() -> threadscope.get().put("key_1", "Value 1"));

		Thread th2 = new Thread(() -> {
			Optional<String> value1 = Context.get().resource("key_1", String.class);
			Assert.assertFalse(value1.isPresent());

			threadscope.get().put("key_2", "Value 2");
			Optional<String> value2 = Context.get().resource("key_2", String.class);
			Assert.assertTrue(value2.isPresent());
		});
		
		th1.start();
		th2.start();
	}

	@Test
	public void testScopeHiearchy() {

		final ClassLoader dft = ContextManager.getDefaultClassLoader();

		ContextManager.registerScope(dft, new DummyScope());

		Optional<ContextScope> scope = Context.get().scope("dummy", dft);
		assertTrue(scope.isPresent());

		ClassLoader myCl = new ClassLoader(dft) {
		};

		scope = Context.get().scope("dummy", myCl);
		assertTrue(scope.isPresent());

		ContextManager.setUseClassLoaderHierarchy(false);

		scope = Context.get().scope("dummy", myCl);
		assertFalse(scope.isPresent());

		ContextManager.setUseClassLoaderHierarchy(true);

	}

	public static final class DummyScope implements ContextScope {

		private final ContextResourceMap resources = new ContextResourceMap("dummy", true);

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#getName()
		 */
		@Override
		public String getName() {
			return "dummy";
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#getOrder()
		 */
		@Override
		public int getOrder() {
			return Integer.MIN_VALUE + 2000;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#get(java.lang.String, java.lang.Class)
		 */
		@Override
		public <T> Optional<T> get(String resourceKey, Class<T> resourceType) throws TypeMismatchException {
			return Optional.ofNullable(resources.get(resourceKey, resourceType));
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#put(java.lang.String, java.lang.Object)
		 */
		@Override
		public <T> Optional<T> put(String resourceKey, T value) throws UnsupportedOperationException {
			return Optional.ofNullable(resources.put(resourceKey, value));
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#putIfAbsent(java.lang.String, java.lang.Object)
		 */
		@Override
		public <T> Optional<T> putIfAbsent(String resourceKey, T value) throws UnsupportedOperationException {
			return Optional.ofNullable(resources.putIfAbsent(resourceKey, value));
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.context.ContextScope#remove(java.lang.String)
		 */
		@Override
		public boolean remove(String resourceKey) throws UnsupportedOperationException {
			return resources.remove(resourceKey);
		}

	}

}
