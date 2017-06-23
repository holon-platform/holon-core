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
package com.holonplatform.core.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Default {@link ValidatorDescriptor} implementation.
 * 
 * @since 5.0.0
 */
public class DefaultValidatorDescriptor implements ValidatorDescriptor {

	private static final long serialVersionUID = 1513949034218418341L;

	private boolean required;
	private Number min;
	private Number max;
	private boolean exclusiveMin;
	private boolean exclusiveMax;
	private String pattern;
	private Integer integerDigits;
	private Integer fractionDigits;
	private boolean past;
	private boolean future;
	private boolean email;
	private Set<?> in;
	private Set<?> notIn;

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isRequired()
	 */
	@Override
	public boolean isRequired() {
		return required;
	}

	/**
	 * Set whether the value is required.
	 * @param required whether the value is required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isEmail()
	 */
	@Override
	public boolean isEmail() {
		return email;
	}

	/**
	 * Set whether the value is of email type.
	 * @param email whether the value is of email type.
	 */
	public void setEmail(boolean email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isPast()
	 */
	@Override
	public boolean isPast() {
		return past;
	}

	/**
	 * Set whether the value must be in the past.
	 * @param past whether the value must be in the past
	 */
	public void setPast(boolean past) {
		this.past = past;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isFuture()
	 */
	@Override
	public boolean isFuture() {
		return future;
	}

	/**
	 * Set whether the value must be in the future.
	 * @param future whether the value must be in the future
	 */
	public void setFuture(boolean future) {
		this.future = future;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getMin()
	 */
	@Override
	public Number getMin() {
		return min;
	}

	/**
	 * Set the miminum value.
	 * @param min the miminum value
	 */
	public void setMin(Number min) {
		this.min = min;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getMax()
	 */
	@Override
	public Number getMax() {
		return max;
	}

	/**
	 * Set the maximum value.
	 * @param max the maximum value
	 */
	public void setMax(Number max) {
		this.max = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isExclusiveMin()
	 */
	@Override
	public boolean isExclusiveMin() {
		return exclusiveMin;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#isExclusiveMax()
	 */
	@Override
	public boolean isExclusiveMax() {
		return exclusiveMax;
	}

	/**
	 * Set whether the minimum value is exclusive.
	 * @param exclusiveMin whether the minimum value is exclusive
	 */
	public void setExclusiveMin(boolean exclusiveMin) {
		this.exclusiveMin = exclusiveMin;
	}

	/**
	 * Set whether the maximum value is exclusive.
	 * @param exclusiveMax whether the maximum value is exclusive
	 */
	public void setExclusiveMax(boolean exclusiveMax) {
		this.exclusiveMax = exclusiveMax;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getPattern()
	 */
	@Override
	public String getPattern() {
		return pattern;
	}

	/**
	 * Set the value regexp pattern.
	 * @param pattern the value regexp pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getIntegerDigits()
	 */
	@Override
	public Integer getIntegerDigits() {
		return integerDigits;
	}

	/**
	 * Set the integer digits count.
	 * @param integerDigits the integer digits count
	 */
	public void setIntegerDigits(Integer integerDigits) {
		this.integerDigits = integerDigits;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getFractionDigits()
	 */
	@Override
	public Integer getFractionDigits() {
		return fractionDigits;
	}

	/**
	 * Set the fractional digits count.
	 * @param fractionDigits the fractional digits count
	 */
	public void setFractionDigits(Integer fractionDigits) {
		this.fractionDigits = fractionDigits;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getIn()
	 */
	@Override
	public Set<?> getIn() {
		return (in != null) ? in : Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.internal.ValidatorDescriptor#getNotIn()
	 */
	@Override
	public Set<?> getNotIn() {
		return (notIn != null) ? notIn : Collections.emptySet();
	}

	/**
	 * Set the admitted values.
	 * @param in Values
	 */
	public void setIn(Set<?> in) {
		this.in = in;
	}

	/**
	 * Set the not admitted values.
	 * @param notIn Values
	 */
	public void setNotIn(Set<?> notIn) {
		this.notIn = notIn;
	}

	/**
	 * Default {@link Builder}.
	 */
	static class DefaultBuilder implements Builder {

		private final DefaultValidatorDescriptor instance = new DefaultValidatorDescriptor();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#required()
		 */
		@Override
		public Builder required() {
			instance.setRequired(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#email()
		 */
		@Override
		public Builder email() {
			instance.setEmail(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#past()
		 */
		@Override
		public Builder past() {
			instance.setPast(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#future()
		 */
		@Override
		public Builder future() {
			instance.setFuture(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#min(java.lang.Number)
		 */
		@Override
		public Builder min(Number min) {
			instance.setMin(min);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#max(java.lang.Number)
		 */
		@Override
		public Builder max(Number max) {
			instance.setMax(max);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#exclusiveMin()
		 */
		@Override
		public Builder exclusiveMin() {
			instance.setExclusiveMin(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#exclusiveMax()
		 */
		@Override
		public Builder exclusiveMax() {
			instance.setExclusiveMax(true);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#pattern(java.lang.String)
		 */
		@Override
		public Builder pattern(String pattern) {
			instance.setPattern(pattern);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#integerDigits(int)
		 */
		@Override
		public Builder integerDigits(int digits) {
			instance.setIntegerDigits(digits);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#fractionDigits(int)
		 */
		@Override
		public Builder fractionDigits(int digits) {
			instance.setFractionDigits(digits);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#in(java.lang.Object[])
		 */
		@Override
		public Builder in(Object... values) {
			Set<Object> set = new HashSet<>();
			if (values != null) {
				for (Object value : values) {
					set.add(value);
				}
			}
			instance.setIn(set);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#notIn(java.lang.Object[])
		 */
		@Override
		public Builder notIn(Object... values) {
			Set<Object> set = new HashSet<>();
			if (values != null) {
				for (Object value : values) {
					set.add(value);
				}
			}
			instance.setNotIn(set);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.internal.ValidatorDescriptor.Builder#build()
		 */
		@Override
		public ValidatorDescriptor build() {
			return instance;
		}

	}

}
