/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.core.internal.property;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.holonplatform.core.Context;
import com.holonplatform.core.internal.utils.AnnotationUtils;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertySetRef;

/**
 * Introspector to obtain a {@link PropertySet} from {@link PropertySetRef} annotation.
 * 
 * @since 5.0.0
 */
public interface PropertySetRefIntrospector extends Serializable {

	/**
	 * Default {@link Context} resource key
	 */
	static final String CONTEXT_KEY = PropertySetRefIntrospector.class.getName();

	/**
	 * Obtain the {@link PropertySet} declared through given <code>annotation</code>.
	 * @param annotation {@link PropertySetRef} annotation from which to obtain the {@link PropertySet}
	 * @return PropertySet which corresponds to given annotation attributes
	 * @throws PropertySetIntrospectionException In an error occurred
	 */
	PropertySet<?> getPropertySet(PropertySetRef annotation) throws PropertySetIntrospectionException;

	// Accessors

	/**
	 * Gets the current {@link PropertySetRefIntrospector} instance.
	 * @return The {@link Context}-bound PropertySetRefIntrospector instance, if available using {@link #CONTEXT_KEY} as
	 *         context key, or the default instance for the default ClassLoader obtained through {@link #getDefault()}.
	 */
	static PropertySetRefIntrospector get() {
		return Context.get().resource(CONTEXT_KEY, PropertySetRefIntrospector.class).orElse(getDefault());
	}

	/**
	 * Return the default {@link PropertySetRefIntrospector}.
	 * @return Default PropertySetRefIntrospector
	 */
	static PropertySetRefIntrospector getDefault() {
		return DefaultPropertySetRefIntrospector.INSTANCE;
	}

	/**
	 * Check whether the {@link PropertySetRef} annotation is present in given annotations list, checking
	 * meta-annotations too.
	 * @param annotations Annotations to scan
	 * @return The {@link PropertySetRef} annotation if present in given annotations list, an empty Optional otherwise
	 */
	static Optional<PropertySetRef> getPropertySetRef(List<Annotation> annotations) {
		if (annotations != null && !annotations.isEmpty()) {
			List<PropertySetRef> as = AnnotationUtils.getAnnotations(annotations, PropertySetRef.class);
			if (!as.isEmpty()) {
				return Optional.ofNullable(as.get(0));
			}
		}
		return Optional.empty();
	}

	/**
	 * Check whether the {@link PropertySetRef} annotation is present in given annotations list, checking
	 * meta-annotations too.
	 * @param annotations Annotations to scan
	 * @return The {@link PropertySetRef} annotation if present in given annotations list, an empty Optional otherwise
	 */
	static Optional<PropertySetRef> getPropertySetRef(Annotation... annotations) {
		if (annotations != null && annotations.length > 0) {
			List<PropertySetRef> as = AnnotationUtils.getAnnotations(Arrays.asList(annotations), PropertySetRef.class);
			if (!as.isEmpty()) {
				return Optional.ofNullable(as.get(0));
			}
		}
		return Optional.empty();
	}

	/**
	 * Exception thrown for PropertySet introspection errors.
	 */
	@SuppressWarnings("serial")
	public class PropertySetIntrospectionException extends RuntimeException {

		/**
		 * Constructor with error message
		 * @param message Error message
		 */
		public PropertySetIntrospectionException(String message) {
			super(message);
		}

		/**
		 * Constructor with nested exception
		 * @param cause Nested exception
		 */
		public PropertySetIntrospectionException(Throwable cause) {
			super(cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public PropertySetIntrospectionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
