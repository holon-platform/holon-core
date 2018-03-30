package com.holonplatform.core.examples;

import java.util.Optional;

import com.holonplatform.core.Context;
import com.holonplatform.core.ContextScope;
import com.holonplatform.core.exceptions.TypeMismatchException;

@SuppressWarnings("unused")
public class ExampleContext {

	public void getContext() {

		// tag::getctx[]
		Context currentContext = Context.get(); // <1>
		// end::getctx[]

	}

	public void getContextScope() {
		ClassLoader aClassLoader = ClassLoader.getSystemClassLoader();

		// tag::getscope[]
		Optional<ContextScope> scope = Context.get().scope("scopeName"); // <1>

		scope = Context.get().scope("scopeName", aClassLoader); // <2>
		// end::getscope[]
	}

	public void defaultContextScopes() {
		ClassLoader aClassLoader = ClassLoader.getSystemClassLoader();
		Object resourceInstance = new String();

		// tag::dftscopes[]
		Optional<ContextScope> scope = Context.get().classLoaderScope(); // <1>
		scope = Context.get().classLoaderScope(aClassLoader); // <2>

		Optional<ContextScope> threadScope = Context.get().threadScope(); // <3>
		threadScope = Context.get().threadScope(aClassLoader); // <4>
		// end::dftscopes[]
	}

	public void threadScope() {
		Object resourceInstance = new String();

		// tag::threadscope[]
		Context.get().executeThreadBound("resourceKey", resourceInstance, () -> {
			// do something // <1>
		});

		Context.get().executeThreadBound("resourceKey", resourceInstance, () -> {
			// do something // <2>
			return null;
		});
		// end::threadscope[]
	}

	public void getResource() {
		ClassLoader aClassLoader = ClassLoader.getSystemClassLoader();

		// tag::get[]
		Optional<ResourceType> resource = Context.get().resource("resourceKey", ResourceType.class); // <1>

		resource = Context.get().resource("resourceKey", ResourceType.class, aClassLoader); // <2>

		resource = Context.get().resource(ResourceType.class); // <3>
		// end::get[]
	}

	public void registerResource() {
		// tag::register[]
		Context.get().scope("myscope") // <1>
				.ifPresent(scope -> scope.put("myResourceKey", new ResourceType())); // <2>
		// end::register[]
	}

	private final class ResourceType {
	}

	// tag::myscope[]
	public class MyContextScope implements ContextScope {

		@Override
		public String getName() {
			return "MY_SCOPE_NAME"; // <1>
		}

		@Override
		public int getOrder() {
			return 100; // <2>
		}

		@Override
		public <T> Optional<T> get(String resourceKey, Class<T> resourceType) throws TypeMismatchException {
			return Optional.empty(); // <3>
		}

		@Override
		public <T> Optional<T> put(String resourceKey, T value) throws UnsupportedOperationException { // <4>
			throw new UnsupportedOperationException(); // implement this method to allow resource registration
		}

		@Override
		public <T> Optional<T> putIfAbsent(String resourceKey, T value) throws UnsupportedOperationException { // <4>
			throw new UnsupportedOperationException(); // implement this method to allow resource registration
		}

		@Override
		public boolean remove(String resourceKey) throws UnsupportedOperationException { // <4>
			throw new UnsupportedOperationException(); // implement this method to allow resource removal
		}

	}
	// end::myscope[]

}
