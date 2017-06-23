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
package com.holonplatform.core.datastore;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.Path;
import com.holonplatform.core.Path.FinalPath;
import com.holonplatform.core.internal.CallbackExpressionResolver;
import com.holonplatform.core.internal.datastore.DefaultDataTarget;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PathProperty.PathPropertyBuilder;

/**
 * Representation of the target element of a data model persistence/query operation.
 * <p>
 * A DataTarget is a {@link Path}, so the target data model <em>entity</em> is represented by a String path name. The
 * path is <em>final</em>, i.e. does not support a parent.
 * </p>
 * <p>
 * A {@link DataTargetResolver} can be used to resolve a symbolic named data target into a persistence model specific
 * data target representation.
 * </p>
 * 
 * @param <T> Target type
 * 
 * @since 5.0.0
 * 
 * @see Datastore
 */
public interface DataTarget<T> extends FinalPath<T> {

	/**
	 * Create a new {@link PathProperty} with given <code>name</code> and <code>type</code>, setting this
	 * {@link DataTarget} as property parent path.
	 * @param <P> Property type
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @return Property builder
	 */
	default <P> PathPropertyBuilder<P> property(String name, Class<? extends P> type) {
		return PathProperty.<P>create(name, type).parent(this);
	}

	/**
	 * Create a new {@link PathProperty} cloning from given <code>fromProperty</code>, setting this {@link DataTarget}
	 * as property parent path. Property name, type and configuration of the the property will be cloned from given
	 * property.
	 * @param <P> Property type
	 * @param fromProperty Property from which to clone (not null)
	 * @return The new property
	 */
	default <P> PathProperty<P> property(PathProperty<P> fromProperty) {
		ObjectUtils.argumentNotNull(fromProperty, "Property to clone must be not null");
		return fromProperty.clone().parent(this);
	}

	/**
	 * Build a {@link String} type data target using root path <code>name</code>.
	 * @param name Path name (not null)
	 * @return DataTarget instance
	 */
	static DataTarget<String> named(String name) {
		return new DefaultDataTarget<>(name, String.class);
	}

	/**
	 * Build a typed data target using root path <code>name</code>.
	 * @param <T> Target type
	 * @param name Path name (not null)
	 * @param type Data target type (not null)
	 * @return DataTarget instance
	 */
	static <T> DataTarget<T> of(String name, Class<T> type) {
		return new DefaultDataTarget<>(name, type);
	}

	/**
	 * Interface implemented by classes which support {@link DataTarget} setting.
	 * @param <C> Concrete type
	 */
	public interface DataTargetSupport<C extends DataTargetSupport<C>> {

		/**
		 * Set the data target.
		 * @param target Data target to set
		 * @return the modified DataTargetSupport
		 */
		C target(DataTarget<?> target);

		/**
		 * Set the data target using a {@link String} as root path name.
		 * @param targetName Target name
		 * @return the modified DataTargetSupport
		 */
		default C target(String targetName) {
			return target(DataTarget.named(targetName));
		}

	}

	/**
	 * Convenience interface to create an {@link ExpressionResolver} to resolve a custom {@link DataTarget} class into a
	 * standard {@link DataTarget}.
	 * @param <T> DataTarget type to be resolved
	 */
	@SuppressWarnings("rawtypes")
	public interface DataTargetResolver<T extends DataTarget> extends ExpressionResolver<T, DataTarget> {

		@Override
		default Class<? extends DataTarget> getResolvedType() {
			return DataTarget.class;
		}

		/**
		 * Create an {@link ExpressionResolver} to resolve a custom {@link DataTarget} class into a standard
		 * {@link DataTarget} using the given resolver function.
		 * @param <T> DataTarget type to resolve
		 * @param type DataTarget type to be resolved
		 * @param function Resolver function
		 * @return A new {@link ExpressionResolver}
		 */
		static <T extends DataTarget> ExpressionResolver<T, DataTarget> create(Class<? extends T> type,
				ExpressionResolverFunction<T, DataTarget> function) {
			return new CallbackExpressionResolver<>(type, DataTarget.class, function);
		}

	}

}
