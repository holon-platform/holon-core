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
package com.holonplatform.http.internal.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.http.rest.ResponseType;

/**
 * {@link ResponseType} implementation.
 * 
 * @param <T> Actual type
 * 
 * @since 5.0.0
 */
public class ResponseTypeImpl<T> implements ResponseType<T> {

	private final Type type;
	private final boolean simpleType;

	protected ResponseTypeImpl() {
		type = TypeUtils.getTypeArgument(getClass(), ResponseType.class);
		this.simpleType = false;
	}

	/**
	 * Construct a ResponseType using a given simple type
	 * @param type Actual type
	 */
	public ResponseTypeImpl(Class<T> type) {
		super();

		ObjectUtils.argumentNotNull(type, "Type must be not null");

		this.type = type;
		this.simpleType = true;
	}

	/**
	 * Construct a ResponseType using specified parameterized type and raw class type
	 * @param type Type argument
	 * @param rawType Raw type
	 */
	public ResponseTypeImpl(Class<?> type, Class<?> rawType) {
		super();

		ObjectUtils.argumentNotNull(type, "Type must be not null");
		ObjectUtils.argumentNotNull(rawType, "Raw type must be not null");

		this.type = new ParameterizedType() {
			@Override
			public Type[] getActualTypeArguments() {
				return new Type[] { type };
			}

			@Override
			public Type getRawType() {
				return rawType;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}
		};
		this.simpleType = false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.ResponseType#getType()
	 */
	@Override
	public Type getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.ResponseType#isSimpleType()
	 */
	@Override
	public boolean isSimpleType() {
		return simpleType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseType [type=" + type + ", simpleType=" + simpleType + "]";
	}

}
