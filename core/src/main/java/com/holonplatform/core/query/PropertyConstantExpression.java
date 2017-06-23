package com.holonplatform.core.query;

import java.util.Collection;

import com.holonplatform.core.internal.query.DefaultPropertyCollectionExpression;
import com.holonplatform.core.internal.query.DefaultPropertyConstantExpression;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link QueryExpression} which represents a {@link Property} value.
 * 
 * @param <T> Expression type
 * @param <E> Concrete value type
 * 
 * @since 5.0.0
 */
public interface PropertyConstantExpression<T, E> extends ConstantExpression<T, E> {

	/**
	 * Get the property to which the value is bound.
	 * @return the property
	 */
	Property<E> getProperty();

	/**
	 * Get the model value.
	 * @return The constant expression value, converted to model type if a {@link PropertyValueConverter} is available.
	 */
	Object getModelValue();

	/**
	 * Get the model type.
	 * @return The constant expresion type, using the model type if a {@link PropertyValueConverter} is available.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default Class<?> getModelType() {
		return getProperty().getConverter().map(c -> c.getModelType()).orElse((Class) getProperty().getType());
	}

	/**
	 * Create a {@link PropertyConstantExpression} which represents a constant value bound to a {@link Property}.
	 * @param <T> Expression type
	 * @param property Property to which the value is bound (not null)
	 * @param value Constant value (not null)
	 * @return A new constant expression
	 */
	static <T> PropertyConstantExpression<T, T> create(Property<T> property, T value) {
		return new DefaultPropertyConstantExpression<>(property, value);
	}

	/**
	 * Create a {@link PropertyConstantExpression} which represents a collection of constant values bound to a
	 * {@link Property}.
	 * @param <T> Expression type
	 * @param property Property to which the value is bound (not null)
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	static <T> PropertyConstantExpression<Collection<T>, T> create(Property<T> property,
			Collection<? extends T> values) {
		return new DefaultPropertyCollectionExpression<>(property, values);
	}

	/**
	 * Create a {@link PropertyConstantExpression} which represents a collection of constant values bound to a
	 * {@link Property}.
	 * @param <T> Expression type
	 * @param property Property to which the value is bound (not null)
	 * @param values Expression values (not null)
	 * @return A new collection expression
	 */
	@SafeVarargs
	static <T> PropertyConstantExpression<Collection<T>, T> create(Property<T> property, T... values) {
		return new DefaultPropertyCollectionExpression<>(property, values);
	}

}
