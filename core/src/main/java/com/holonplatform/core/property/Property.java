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
package com.holonplatform.core.property;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import com.holonplatform.core.Context;
import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.Validatable;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.internal.utils.FormatUtils;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.property.PropertyRendererRegistry.NoSuitableRendererAvailableException;
import com.holonplatform.core.temporal.TemporalType;

/**
 * Property is the base interface to represent a generic data attribute. Declares its value type through
 * {@link #getType()} method and it is generalized on such type.
 * <p>
 * A property can be identified by a symbolic name, which can be obtained through the {@link #getName()} method.
 * </p>
 * <p>
 * Supports a {@link PropertyValueConverter} to be used within a {@link PropertyBox} abstraction when property is bound
 * to a data model, allowing property value conversion in both directions when property value type and data model value
 * type does not match.
 * </p>
 * <p>
 * A property brings with it a generic {@link PropertyConfiguration} structure, organized as a parameter set, which can
 * be used to set custom configuration attributes.
 * </p>
 * <p>
 * This property is {@link Validatable}, supporting property value {@link Validator}s.
 * </p>
 * <p>
 * Extends {@link Localizable} to optionally provide a localizable message which describes the property.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see PathProperty
 * @see VirtualProperty
 * @see PropertySet
 * @see PropertyBox
 */
public interface Property<T> extends Validatable<T>, Localizable, Serializable {

	/**
	 * Get the name which identifies this property.
	 * @return The property name (should be not null)
	 */
	String getName();

	/**
	 * Get the type of values supported by this property
	 * @return Property value type (not null)
	 */
	Class<? extends T> getType();

	/**
	 * Gets whether property is read-only, i.e. does not support value setting.
	 * <p>
	 * When a property is read-only, a {@link PropertyReadOnlyException} should be thrown by property value setting
	 * methods.
	 * </p>
	 * @return <code>true</code> if property is read-only
	 */
	boolean isReadOnly();

	/**
	 * Gets the configuration associated to this property.
	 * <p>
	 * This configuration is considered as immutable, and only provide methods to read configuration parameters. The
	 * configuration parameters setting is done at property creation time, using
	 * {@link Builder#configuration(String, Object)} and {@link Builder#configuration(ConfigProperty, Object)} methods
	 * provided by Property {@link Builder}.
	 * </p>
	 * @return The {@link PropertyConfiguration} of this property (immutable and not null)
	 */
	PropertyConfiguration getConfiguration();

	/**
	 * Get the converter to perform property value conversions between property value and the corresponding data model
	 * value. The {@link PropertyValueConverter} is supported out-of-the-box when property value is handled using a
	 * {@link PropertyBox}.
	 * @return Optional converter
	 */
	Optional<PropertyValueConverter<T, ?>> getConverter();

	/**
	 * Return the given <code>value</code> against this property, converting it to required model data type if a
	 * {@link PropertyValueConverter} is present.
	 * @param value Property value (can be null)
	 * @return Model value, possibly converted using the {@link PropertyValueConverter}
	 */
	default Object getConvertedValue(T value) {
		return getConverter().map((c) -> (Object) c.toModel(value, this)).orElse(value);
	}

	/**
	 * Present given property <code>value</code> as a {@link String}, using current
	 * {@link PropertyValuePresenterRegistry} if available as {@link Context} resource or the default instance
	 * otherwise. If a {@link PropertyValuePresenter} is not available from registry, simple {@link Object#toString()}
	 * representation of the value is returned.
	 * @param value Value to present as a String
	 * @return String presentation of the given <code>value</code>
	 */
	default String present(T value) {
		return PropertyValuePresenterRegistry.get().getPresenter(this).orElse((p, v) -> FormatUtils.toString(v))
				.present(this, value);
	}

	/**
	 * Render this property as given <code>renderType</code>.
	 * <p>
	 * To successfully render the property, a suitable {@link PropertyRenderer} for given render type must be available
	 * from the {@link PropertyRendererRegistry} obtained from current {@link Context} or from the default one for
	 * current ClassLoader.
	 * </p>
	 * @param <R> Rendered object type
	 * @param renderType Render type
	 * @return Rendered property as given render type
	 * @throws NoSuitableRendererAvailableException if no PropertyRenderer is available for this property and given
	 *         rendering type
	 */
	default <R> R render(Class<R> renderType) {
		return renderIfAvailable(renderType).orElseThrow(() -> new NoSuitableRendererAvailableException(
				"No PropertyRenderer available for rendering type " + renderType + " and property " + this));
	}

	/**
	 * Render this property as given <code>renderType</code> if a suitable {@link PropertyRenderer} for given rendering
	 * type is available from the {@link PropertyRendererRegistry} obtained from current {@link Context} or from the
	 * default one for current ClassLoader.
	 * @param <R> Rendered object type
	 * @param renderType Render type
	 * @return Rendered property as given render type, or an empty Optional if a suitable PropertyRenderer is not
	 *         available
	 */
	default <R> Optional<R> renderIfAvailable(Class<R> renderType) {
		return PropertyRendererRegistry.get().getRenderer(renderType, this).map(r -> r.render(this));
	}

	/**
	 * Base {@link Property} builder.
	 * @param <T> Property type
	 * @param <B> Concrete builder type
	 */
	public interface Builder<T, B extends Builder<T, B>> extends Localizable.Builder<B> {

		/**
		 * Set the property localization using given {@link Localizable} definition.
		 * @param localizable The localizable to use to obtain property localization attributes (not null)
		 * @return this
		 */
		B localization(Localizable localizable);

		/**
		 * Add a property configuration parameter
		 * @param parameterName Parameter name to add to property configuration (not null)
		 * @param value Configuration parameter value
		 * @return this
		 */
		B configuration(String parameterName, Object value);

		/**
		 * Add a property configuration parameter using a {@link ConfigProperty}, with {@link ConfigProperty#getKey()}
		 * as parameter name.
		 * @param <C> Config property type
		 * @param configurationProperty ConfigProperty to add to property configuration (not null)
		 * @param value Configuration parameter value
		 * @return this
		 */
		default <C> B configuration(ConfigProperty<C> configurationProperty, C value) {
			ObjectUtils.argumentNotNull(configurationProperty, "Configuration property must be not null");
			return configuration(configurationProperty.getKey(), value);
		}

		/**
		 * Set the optional {@link TemporalType} specification for {@link Date} or {@link Calendar} type properties,
		 * which can be used to perform consistent operations on property value, such as presentation, rendering or
		 * persistence data manipulation.
		 * @param temporalType the property {@link TemporalType} to set
		 * @return this
		 */
		B temporalType(TemporalType temporalType);

		/**
		 * Sets the {@link PropertyValueConverter}.
		 * @param converter The converter to set
		 * @return this
		 */
		B converter(PropertyValueConverter<T, ?> converter);

		/**
		 * Create a {@link PropertyValueConverter} for given <code>modelType</code> using given conversion
		 * {@link Function}s: the <code>fromModel</code> function to convert a model type value into property value
		 * type, and the <code>toModel</code> function to perform the inverse operation, i.e. convert the property value
		 * type into model type. Then set this converter as {@link PropertyValueConverter} for the property.
		 * @param <MODEL> Model type
		 * @param modelType Model type class (not null)
		 * @param fromModel Function to convert a model type value into property value type (not null)
		 * @param toModel Function to convert a property value type value into model type (not null)
		 * @return this
		 */
		<MODEL> B converter(Class<MODEL> modelType, Function<MODEL, T> fromModel, Function<T, MODEL> toModel);

		/**
		 * Add a property value {@link Validator}
		 * @param validator Validator to add (not null)
		 * @return this
		 */
		B validator(Validator<T> validator);

	}

	// Exceptions

	/**
	 * Base exception class to all {@link Property} related exceptions.
	 */
	@SuppressWarnings("serial")
	public abstract class PropertyAccessException extends RuntimeException {

		/**
		 * Property to which exception is related
		 */
		private final Property<?> property;

		/**
		 * Default constructor
		 * @param property Property to which exception is related
		 */
		public PropertyAccessException(Property<?> property) {
			super();
			this.property = property;
		}

		/**
		 * Constructor with error message
		 * @param property Property to which exception is related
		 * @param message Error message
		 */
		public PropertyAccessException(Property<?> property, String message) {
			super(message);
			this.property = property;
		}

		/**
		 * Constructor with nested exception
		 * @param property Property to which exception is related
		 * @param cause Nested exception
		 */
		public PropertyAccessException(Property<?> property, Throwable cause) {
			super(cause);
			this.property = property;
		}

		/**
		 * Constructor with error message and nested exception
		 * @param property Property to which exception is related
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public PropertyAccessException(Property<?> property, String message, Throwable cause) {
			super(message, cause);
			this.property = property;
		}

		/**
		 * Get the property to which exception is related
		 * @return Optional property
		 */
		public Optional<Property<?>> getProperty() {
			return Optional.ofNullable(property);
		}

	}

	/**
	 * Exception thrown when trying to set a value for a read-only property.
	 */
	@SuppressWarnings("serial")
	public class PropertyReadOnlyException extends PropertyAccessException {

		/**
		 * Construct a new PropertyReadOnlyException
		 * @param property Property to which exception is related
		 */
		public PropertyReadOnlyException(Property<?> property) {
			super(property);
		}

	}

	/**
	 * Exception thrown when a property is not found in a property set.
	 */
	@SuppressWarnings("serial")
	public class PropertyNotFoundException extends PropertyAccessException {

		/**
		 * Constructor
		 * @param property Property to which exception is related
		 * @param message Error message
		 */
		public PropertyNotFoundException(Property<?> property, String message) {
			super(property, message);
		}

	}

	/**
	 * Generic exception thrown for property value reading failures.
	 */
	@SuppressWarnings("serial")
	public class PropertyReadException extends PropertyAccessException {

		/**
		 * Constructor with error message
		 * @param property Property to which exception is related
		 * @param message Error message
		 */
		public PropertyReadException(Property<?> property, String message) {
			super(property, message);
		}

		/**
		 * Constructor with nested exception
		 * @param property Property to which exception is related
		 * @param cause Nested exception
		 */
		public PropertyReadException(Property<?> property, Throwable cause) {
			super(property, cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param property Property to which exception is related
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public PropertyReadException(Property<?> property, String message, Throwable cause) {
			super(property, message, cause);
		}

	}

	/**
	 * Generic exception thrown for property value writing failures.
	 */
	@SuppressWarnings("serial")
	public class PropertyWriteException extends PropertyAccessException {

		/**
		 * Constructor with error message
		 * @param property Property to which exception is related
		 * @param message Error message
		 */
		public PropertyWriteException(Property<?> property, String message) {
			super(property, message);
		}

		/**
		 * Constructor with nested exception
		 * @param property Property to which exception is related
		 * @param cause Nested exception
		 */
		public PropertyWriteException(Property<?> property, Throwable cause) {
			super(property, cause);
		}

		/**
		 * Constructor with error message and nested exception
		 * @param property Property to which exception is related
		 * @param message Error message
		 * @param cause Nested exception
		 */
		public PropertyWriteException(Property<?> property, String message, Throwable cause) {
			super(property, message, cause);
		}

	}

}
