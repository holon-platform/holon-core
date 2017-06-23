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
package com.holonplatform.core.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import com.holonplatform.core.Validator;
import com.holonplatform.core.internal.beans.DefaultBeanProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * Represents a Java Bean property as a {@link PathProperty}, providing additional configuration informations and
 * methods.
 * 
 * @param <T> Property type
 * 
 * @since 5.0.0
 * 
 * @see BeanIntrospector
 */
public interface BeanProperty<T> extends PathProperty<T> {

	/**
	 * Get the parent bean property, if any
	 * @return Optional parent bean property
	 */
	Optional<BeanProperty<?>> getParentProperty();

	/**
	 * Get the bean method to be used to read property value, if available.
	 * @return Optional read method
	 */
	Optional<Method> getReadMethod();

	/**
	 * Get the bean method to be used to write property value, if available.
	 * @return Optional write method
	 */
	Optional<Method> getWriteMethod();

	/**
	 * Get the field to which the bean property is bound
	 * @return Optional bean property field
	 */
	Optional<Field> getField();

	/**
	 * Get the property sequence within a property set, if configured.
	 * @return Optional property sequence
	 */
	Optional<Integer> getSequence();

	/**
	 * Gets the annotation of given <code>annotationClass</code> type declared on this property, if available.
	 * <p>
	 * Only annotations declared on the {@link Field} which corresponds to this property are taken into account, any
	 * annotation on read/write methods is ignored.
	 * </p>
	 * @param <A> Annotation type
	 * @param annotationClass Annotation class to obtain
	 * @return Optional Annotation instance
	 */
	<A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass);

	/**
	 * Checks whether an annotation of given <code>annotationClass</code> is present on this property.
	 * @param <A> Annotation type
	 * @param annotationClass Annotation class to check
	 * @return <code>true</code> if an annotation of given type is present on this property, <code>false</code>
	 *         otherwise
	 */
	default <A extends Annotation> boolean hasAnnotation(Class<A> annotationClass) {
		return getAnnotation(annotationClass).isPresent();
	}

	// Clone

	/**
	 * Clone this property to obtain a property with same configuration but different type. Because of the potential
	 * type change, any {@link PropertyValueConverter} or {@link Validator} is not inherited from the cloned property.
	 * @param <NT> Type of the cloned property
	 * @param type New property type (not null)
	 * @return {@link BeanProperty} builder for the newly created property
	 */
	<NT> Builder<NT> clone(Class<NT> type);

	// Builder

	/**
	 * Get a builder to create a {@link BeanProperty}.
	 * @param <T> Property type
	 * @param name Property name (not null)
	 * @param type Property type (not null)
	 * @return BeanProperty builder
	 */
	static <T> Builder<T> builder(String name, Class<T> type) {
		return new DefaultBeanProperty<>(name, type);
	}

	/**
	 * BeanProperty builder.
	 * @param <T> Property type
	 */
	public interface Builder<T> extends PathProperty.Builder<T, Builder<T>>, BeanProperty<T> {

		/**
		 * Set the bean property read (get) method
		 * @param method Method to set
		 * @return this
		 */
		Builder<T> readMethod(Method method);

		/**
		 * Set the bean property write (set) method
		 * @param method Method to set
		 * @return this
		 */
		Builder<T> writeMethod(Method method);

		/**
		 * Set the bean property field
		 * @param field Field to set
		 * @return this
		 */
		Builder<T> field(Field field);

		/**
		 * Set the bean property sequence
		 * @param sequence Sequence to set
		 * @return this
		 */
		Builder<T> sequence(Integer sequence);

		/**
		 * Set the property annotations
		 * @param annotations Annotations to set
		 * @return this
		 */
		Builder<T> annotations(Annotation[] annotations);

	}

}
