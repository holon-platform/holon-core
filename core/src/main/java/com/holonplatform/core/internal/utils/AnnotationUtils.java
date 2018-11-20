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
package com.holonplatform.core.internal.utils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for {@link Annotation} management.
 * 
 * @since 4.0.0
 */
public final class AnnotationUtils implements Serializable {

	private static final long serialVersionUID = 87654795323447752L;

	/**
	 * Empty value for string annotations
	 */
	public static final String EMPTY_STRING_ANNOTATION_VALUE = "";

	/*
	 * Empty private constructor: this class is intended only to provide constants ad utility methods.
	 */
	private AnnotationUtils() {
	}

	/**
	 * Check if given annotation String value is empty
	 * @param annotationValue Value
	 * @return <code>true</code> if empty
	 */
	public static boolean isEmpty(String annotationValue) {
		return EMPTY_STRING_ANNOTATION_VALUE.equals(annotationValue);
	}

	/**
	 * Read a string annotation value, treating empty strings as <code>null</code> values
	 * @param annotationValue Annotation string value
	 * @return String value, or <code>null</code> if <code>annotationValue</code> is an empty string
	 */
	public static String getStringValue(String annotationValue) {
		return getStringValue(annotationValue, null);
	}

	/**
	 * Read a string annotation value, treating empty strings as <code>null</code> values.
	 * @param annotationValue Annotation string value
	 * @param defaultValue Default value to return when annotation value is <code>null</code> or empty
	 * @return String value, or <code>defaultValue</code> if <code>annotationValue</code> is an empty string
	 */
	public static String getStringValue(String annotationValue, String defaultValue) {
		return (annotationValue != null && !annotationValue.equals(EMPTY_STRING_ANNOTATION_VALUE)) ? annotationValue
				: defaultValue;
	}

	/**
	 * Get the first {@link Annotation} of given <code>annotationClass</code> from given <code>annotations</code> set,
	 * if available.
	 * @param <A> Annotation type
	 * @param annotations Annotations set
	 * @param annotationClass Annotation type (not null)
	 * @return Optional annotation instance of given annotation class
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> Optional<A> getAnnotation(Annotation[] annotations, Class<A> annotationClass) {
		ObjectUtils.argumentNotNull(annotationClass, "Annotation class must be not null");
		if (annotations != null) {
			for (Annotation annotation : annotations) {
				if (annotation != null && annotationClass.isAssignableFrom(annotation.annotationType())) {
					return Optional.ofNullable((A) annotation);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Get the class which is annotated with given <code>annotation</code>, checking any superclass and implemented
	 * interface.
	 * @param cls Class to inspect (not null)
	 * @param annotation Annotation to look for (not null)
	 * @return The class (or implemented interface) on which the annotation is present, <code>null</code> if not found
	 */
	public static Class<?> getClassWithAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
		ObjectUtils.argumentNotNull(cls, "Class must be not null");
		ObjectUtils.argumentNotNull(annotation, "Annotation must be not null");

		if (cls.isAnnotationPresent(annotation)) {
			return cls;
		}
		for (Class<?> intf : cls.getInterfaces()) {
			if (intf.isAnnotationPresent(annotation)) {
				return intf;
			}
		}
		Class<?> superClass = cls.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			return getClassWithAnnotation(superClass, annotation);
		}
		return null;
	}

	/**
	 * Get whether the class is annotated with given <code>annotation</code>, checking any superclass and implemented
	 * interface.
	 * @param cls Class to inspect (not null)
	 * @param annotation Annotation to look for (not null)
	 * @return whether the class is annotated with given <code>annotation</code>
	 */
	public static boolean hasAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
		ObjectUtils.argumentNotNull(cls, "Class must be not null");
		ObjectUtils.argumentNotNull(annotation, "Annotation must be not null");

		if (cls.isAnnotationPresent(annotation)) {
			return true;
		}
		for (Class<?> intf : cls.getInterfaces()) {
			if (intf.isAnnotationPresent(annotation)) {
				return true;
			}
		}
		Class<?> superClass = cls.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			return hasAnnotation(superClass, annotation);
		}
		return false;
	}

	/**
	 * Get a single annotation of given <code>annotationType</code> present in given <code>element</code>, including any
	 * meta-annotation and supporting repeatable annotations.
	 * @param <A> Annotation type
	 * @param element Annotated element to inspect (not null)
	 * @param annotationType Annotation type to lookup (not null)
	 * @return Optional annotation value, if found. If more than one annotation is found, the first one is returned
	 */
	public static <A extends Annotation> Optional<A> getAnnotation(AnnotatedElement element, Class<A> annotationType) {
		List<A> annotations = getAnnotations(element, annotationType);
		if (!annotations.isEmpty()) {
			return Optional.ofNullable(annotations.get(0));
		}
		return Optional.empty();
	}

	/**
	 * Get all the annotations of given <code>annotationType</code> present in given <code>element</code>, including any
	 * meta-annotation and supporting repeatable annotations.
	 * @param <A> Annotation type
	 * @param element Annotated element to inspect (not null)
	 * @param annotationType Annotation type to lookup (not null)
	 * @return List of detected annotation of given <code>annotationType</code>, an empty List if none found
	 */
	public static <A extends Annotation> List<A> getAnnotations(AnnotatedElement element, Class<A> annotationType) {
		ObjectUtils.argumentNotNull(element, "AnnotatedElement must be not null");
		ObjectUtils.argumentNotNull(annotationType, "Annotation type must be not null");

		Class<? extends Annotation> repeatableContainerType = null;
		if (annotationType.isAnnotationPresent(Repeatable.class)) {
			repeatableContainerType = annotationType.getAnnotation(Repeatable.class).value();
		}

		List<A> annotations = new LinkedList<>();
		findAnnotations(annotations, element, annotationType, repeatableContainerType);
		return annotations;
	}

	/**
	 * Get all the annotations of given <code>annotationType</code> present in given annotations list, including any
	 * meta-annotation and supporting repeatable annotations.
	 * @param <A> Annotation type
	 * @param annotations Annotation list element to inspect
	 * @param annotationType Annotation type to lookup
	 * @return List of detected annotation of given <code>annotationType</code>, an empty List if none found
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> List<A> getAnnotations(List<Annotation> annotations, Class<A> annotationType) {
		ObjectUtils.argumentNotNull(annotationType, "Annotation type must be not null");

		if (annotations == null || annotations.isEmpty()) {
			return Collections.emptyList();
		}

		Class<? extends Annotation> repeatableContainerType = null;
		if (annotationType.isAnnotationPresent(Repeatable.class)) {
			repeatableContainerType = annotationType.getAnnotation(Repeatable.class).value();
		}

		List<A> ans = new LinkedList<>();
		for (Annotation annotation : annotations) {
			if (annotationType.equals(annotation.annotationType())) {
				ans.add((A) annotation);
			}
			if (!isInJavaLangAnnotationPackage(annotation) && !annotation.annotationType().equals(annotationType)
					&& (repeatableContainerType == null
							|| !annotation.annotationType().equals(repeatableContainerType))) {
				findAnnotations(ans, annotation.annotationType(), annotationType, repeatableContainerType);
			}
		}
		return ans;
	}

	/**
	 * Find the annotations of given <code>annotationType</code> on given element and stores them in given
	 * <code>accumulator</code>.
	 * @param accumulator Accumulator
	 * @param element Annotated element
	 * @param annotationType Annotation type to lookup
	 * @param repeatableContainerType Optional repeteable annotation type
	 */
	private static <A extends Annotation> void findAnnotations(List<A> accumulator, AnnotatedElement element,
			Class<A> annotationType, Class<? extends Annotation> repeatableContainerType) {

		// direct lookup
		A[] as = element.getAnnotationsByType(annotationType);
		if (as.length > 0) {
			for (A a : as) {
				accumulator.add(a);
			}
		}

		// check meta-annotations
		Annotation[] all = element.getAnnotations();
		if (all.length > 0) {
			for (Annotation annotation : all) {
				if (!isInJavaLangAnnotationPackage(annotation) && !annotation.annotationType().equals(annotationType)
						&& (repeatableContainerType == null
								|| !annotation.annotationType().equals(repeatableContainerType))) {
					findAnnotations(accumulator, annotation.annotationType(), annotationType, repeatableContainerType);
				}
			}
		}

	}

	/**
	 * Check if the specified {@link Annotation} is defined in the core JDK <code>java.lang.annotation</code> package.
	 * @param annotation the annotation to check
	 * @return <code>true</code> if the annotation is in the <code>java.lang.annotation</code> package
	 */
	private static boolean isInJavaLangAnnotationPackage(Annotation annotation) {
		return (annotation != null && annotation.annotationType().getName().startsWith("java.lang.annotation"));
	}

}
