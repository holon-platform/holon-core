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
package com.holonplatform.core.internal.beans;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.Path;
import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.internal.property.AbstractPathProperty;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyConfiguration;
import com.holonplatform.core.property.PropertyConfiguration.PropertyConfigurationEditor;

/**
 * Default {@link BeanProperty} implementation.
 * 
 * @param <T> Property type
 * 
 * @since 5.0.0
 */
public class DefaultBeanProperty<T> extends AbstractPathProperty<T, Builder<T>> implements Builder<T> {

	private static final long serialVersionUID = -136356340742425752L;

	/**
	 * Getter method
	 */
	private transient WeakReference<Method> readMethod;

	/**
	 * Setter method
	 */
	private transient WeakReference<Method> writeMethod;

	/**
	 * Field
	 */
	private transient WeakReference<Field> field;

	/**
	 * Declared field annotations
	 */
	private transient Map<Class<? extends Annotation>, Annotation> annotations;

	/**
	 * Optional property sequence
	 */
	private Integer sequence;

	/**
	 * Identifier property
	 */
	private boolean identifier;

	/**
	 * Constructor.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 */
	public DefaultBeanProperty(String name, Class<T> type) {
		this(name, type, null);
	}

	/**
	 * Constructor with custom {@link PropertyConfiguration}.
	 * @param name Property name (not null)
	 * @param type Property value type (not null)
	 * @param configuration Optional property configuration instance
	 */
	public DefaultBeanProperty(String name, Class<? extends T> type, PropertyConfigurationEditor configuration) {
		super(name, type, configuration);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getParentProperty()
	 */
	@Override
	public Optional<BeanProperty<?>> getParentProperty() {
		Path<?> parent = getParent().orElse(null);
		if (parent != null && parent instanceof BeanProperty) {
			return Optional.of((BeanProperty<?>) parent);
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getReadMethod()
	 */
	@Override
	public Optional<Method> getReadMethod() {
		return Optional.ofNullable(readMethod != null ? readMethod.get() : null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getWriteMethod()
	 */
	@Override
	public Optional<Method> getWriteMethod() {
		return Optional.ofNullable(writeMethod != null ? writeMethod.get() : null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getField()
	 */
	@Override
	public Optional<Field> getField() {
		return Optional.ofNullable(field != null ? field.get() : null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getSequence()
	 */
	@Override
	public Optional<Integer> getSequence() {
		return Optional.ofNullable(sequence);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#isIdentifier()
	 */
	@Override
	public boolean isIdentifier() {
		return identifier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#getAnnotation(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass) {
		ObjectUtils.argumentNotNull(annotationClass, "Annotation class must be not null");
		if (annotations != null) {
			return Optional.ofNullable((A) annotations.get(annotationClass));
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#readMethod(java.lang.reflect.Method)
	 */
	@Override
	public BeanProperty.Builder<T> readMethod(Method method) {
		this.readMethod = (method != null) ? new WeakReference<>(method) : null;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#writeMethod(java.lang.reflect.Method)
	 */
	@Override
	public BeanProperty.Builder<T> writeMethod(Method method) {
		this.writeMethod = (method != null) ? new WeakReference<>(method) : null;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#field(java.lang.reflect.Field)
	 */
	@Override
	public BeanProperty.Builder<T> field(Field field) {
		this.field = (field != null) ? new WeakReference<>(field) : null;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#sequence(java.lang.Integer)
	 */
	@Override
	public BeanProperty.Builder<T> sequence(Integer sequence) {
		this.sequence = sequence;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#identifier(boolean)
	 */
	@Override
	public BeanProperty.Builder<T> identifier(boolean identifier) {
		this.identifier = identifier;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty.Builder#annotations(java.lang.annotation.Annotation[])
	 */
	@Override
	public BeanProperty.Builder<T> annotations(Annotation[] annotations) {
		this.annotations = null;
		if (annotations != null && annotations.length > 0) {
			this.annotations = new HashMap<>(annotations.length);
			for (Annotation annotation : annotations) {
				this.annotations.put(annotation.annotationType(), annotation);
			}
		}
		return this;
	}

	/**
	 * Set the property field annotations.
	 * @param annotations the annotations to set
	 */
	protected void setAnnotations(Map<Class<? extends Annotation>, Annotation> annotations) {
		this.annotations = annotations;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanProperty#clone(java.lang.Class)
	 */
	@Override
	public <NT> BeanProperty.Builder<NT> clone(Class<NT> type) {
		ObjectUtils.argumentNotNull(type, "New type must be not null");

		DefaultBeanProperty<NT> cloned = new DefaultBeanProperty<>(getName(), type,
				PropertyConfiguration.clone(getConfiguration()));
		// parent
		getParent().ifPresent(p -> cloned.parent(p));
		// localization
		cloned.message(getMessage());
		cloned.messageCode(getMessageCode());
		cloned.messageArguments(getMessageArguments());
		// bean configuration
		cloned.readMethod = readMethod;
		cloned.writeMethod = writeMethod;
		cloned.field = field;
		cloned.annotations = annotations;

		return cloned;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.property.AbstractPathProperty#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BeanProperty[name=");
		sb.append(fullName());
		sb.append(", type=");
		sb.append(getType());
		sb.append("]");
		return sb.toString();
	}

}
