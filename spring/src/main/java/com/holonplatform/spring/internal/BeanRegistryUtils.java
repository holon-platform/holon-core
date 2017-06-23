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
package com.holonplatform.spring.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.StandardMethodMetadata;

import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Utility class for Spring bean registration related operations.
 *
 * @since 5.0.0
 */
public final class BeanRegistryUtils implements Serializable {

	private static final long serialVersionUID = 1786469857649327596L;

	private BeanRegistryUtils() {
	}

	/**
	 * Build Spring bean name using optional data context id. If the given data context id is not null/empty, the bean
	 * name is composed using default name, an underscore character and the data context id as suffix.
	 * <p>
	 * For example, if default name is <code>myname</code> and the data context id is <code>test</code>, this method
	 * will return <code>myname_test</code>.
	 * </p>
	 * @param dataContextId Data context id. May be null.
	 * @param name Default bean name
	 * @return Bean name with data context id suffix (prepended by an underscore), if data context id is not
	 *         <code>null</code>. Simple bean name otherwise.
	 */
	public static String buildBeanName(String dataContextId, String name) {
		if (dataContextId != null && !dataContextId.trim().equals("")) {
			return name + "_" + dataContextId;
		}
		return name;
	}

	/**
	 * Get all the bean names of given <code>type</code> in given bean factory.
	 * @param beanFactory Bean factory (not null)
	 * @param type Bean type (not null)
	 * @return The list of all bean names of given <code>type</code> in given bean factory, an empty list if none.
	 */
	public static List<String> getBeanNames(ListableBeanFactory beanFactory, Class<?> type) {
		ObjectUtils.argumentNotNull(beanFactory, "Bean factory must be not null");
		ObjectUtils.argumentNotNull(type, "Type must be not null");

		List<String> names = new LinkedList<>();
		String[] dsBeanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type, true, false);
		if (dsBeanNames != null) {
			for (String dsBeanName : dsBeanNames) {
				names.add((dsBeanName.startsWith("&")) ? dsBeanName.substring(1) : dsBeanName);
			}
		}
		return names;
	}

	/**
	 * Try to obtain the actual bean class of the bean with given name.
	 * @param beanName Bean name
	 * @param beanDefinition Optional bean definition
	 * @param beanFactory Bean factory (not null)
	 * @param classLoader ClassLoader to use
	 * @return The bean class, or <code>null</code> if cannot be detected
	 */
	public static Class<?> getBeanClass(String beanName, BeanDefinition beanDefinition,
			ConfigurableListableBeanFactory beanFactory, ClassLoader classLoader) {

		ObjectUtils.argumentNotNull(beanFactory, "Bean factory must be not null");

		BeanDefinition definition = beanDefinition;
		if (definition == null && beanName != null) {
			definition = beanFactory.getBeanDefinition(beanName);
		}

		if (definition == null) {
			throw new BeanDefinitionValidationException("Missing bean definition for bean name [" + beanName + "]");
		}

		// check Root definition
		if (definition instanceof RootBeanDefinition) {
			RootBeanDefinition rootBeanDef = (RootBeanDefinition) definition;
			try {
				if (rootBeanDef.getBeanClass() != null) {
					return rootBeanDef.getBeanClass();
				}
			} catch (@SuppressWarnings("unused") IllegalStateException e) {
				// factory bean: ignore
			}
		}

		// Check factory bean
		if (definition.getFactoryMethodName() == null) {
			// factory class
			if (definition.getBeanClassName() != null) {
				return getBeanClass(beanName, definition.getBeanClassName(), classLoader);
			}
		} else {
			// factory method
			String factoryClassName = getBeanFactoryClassName(definition, beanFactory);
			if (factoryClassName != null) {
				Class<?> factoryClass = getBeanClass(beanName, factoryClassName, classLoader);
				if (factoryClass != null) {
					for (Method method : factoryClass.getMethods()) {
						if (method.getName().equals(definition.getFactoryMethodName())) {
							return method.getReturnType();
						}
					}
				}
			}
		}

		// check beans defined using @Configuration
		Object source = definition.getSource();
		if (source instanceof StandardMethodMetadata) {
			StandardMethodMetadata metadata = (StandardMethodMetadata) source;
			return metadata.getIntrospectedMethod().getReturnType();
		}

		return null;

	}

	/**
	 * Get the Factory class name which corresponds to given bean definition.
	 * @param definition Bean definition
	 * @param beanFactory Bean factory
	 * @return Factory class name, or <code>null</code> if not found
	 */
	private static String getBeanFactoryClassName(BeanDefinition definition,
			ConfigurableListableBeanFactory beanFactory) {
		if (definition instanceof AnnotatedBeanDefinition) {
			return ((AnnotatedBeanDefinition) definition).getMetadata().getClassName();
		} else {
			if (definition.getFactoryBeanName() != null) {
				BeanDefinition fd = beanFactory.getBeanDefinition(definition.getFactoryBeanName());
				if (fd != null) {
					return fd.getBeanClassName();
				}
			} else {
				return definition.getBeanClassName();
			}
		}
		return null;
	}

	/**
	 * Get bean class from bean class name.
	 * @param beanName Bean name
	 * @param beanClassName Bean class naem (not null)
	 * @param classLoader ClassLoader to use (null for default class loader)
	 * @return Bean class
	 * @throws CannotLoadBeanClassException Class name not found in given ClassLoader
	 */
	private static Class<?> getBeanClass(final String beanName, final String beanClassName, ClassLoader classLoader)
			throws CannotLoadBeanClassException {
		ObjectUtils.argumentNotNull(beanClassName, "Bean class name must be not null");
		try {
			return ClassUtils.forName(beanClassName, classLoader);
		} catch (ClassNotFoundException e) {
			throw new CannotLoadBeanClassException(BeanRegistryUtils.class.getName(), beanName, beanClassName, e);
		}
	}

	/**
	 * Get the value of an annotation attribute
	 * @param <T> Default value type
	 * @param attributes Annotation attributes
	 * @param key Attribute name
	 * @param defaultValue Default value to return when attribute has no value
	 * @return Annotation attribute value
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAnnotationValue(Map<String, Object> attributes, String key, T defaultValue) {
		ObjectUtils.argumentNotNull(key, "Attribute name must be not null");
		if (attributes != null) {
			Object value = attributes.get(key);
			if (!isNullAnnotationValue(value)) {
				return (T) value;
			}
		}
		return defaultValue;
	}

	/**
	 * Checks whether the value of an annotation attribute is <code>null</code> or an empty String.
	 * @param value Annotation attribute value
	 * @return <code>true</code> if the value is <code>null</code> or an empty String
	 */
	private static boolean isNullAnnotationValue(Object value) {
		if (value == null || (String.class.isAssignableFrom(value.getClass()) && ((String) value).trim().equals(""))) {
			return true;
		}
		return false;
	}

}
