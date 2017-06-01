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
package com.holonplatform.http.internal;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.http.RequestEntity;

/**
 * Default {@link RequestEntity} implementation.
 * 
 * @param <T> Request payload type
 *
 * @since 5.0.0
 */
public class DefaultRequestEntity<T> implements RequestEntity<T> {

	/**
	 * Media type
	 */
	private final String mediaType;

	/**
	 * Payload
	 */
	private T payload;

	/**
	 * Constructor
	 * @param mediaType Request media type
	 */
	public DefaultRequestEntity(String mediaType) {
		this(mediaType, null);
	}

	/**
	 * Constructor
	 * @param mediaType Request media type
	 * @param payload Request payload
	 */
	public DefaultRequestEntity(String mediaType, T payload) {
		super();
		this.mediaType = mediaType;
		this.payload = payload;
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RequestEntity#getMediaType()
	 */
	@Override
	public Optional<String> getMediaType() {
		return Optional.ofNullable(mediaType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.http.RequestEntity#getPayload()
	 */
	@Override
	public Optional<T> getPayload() {
		return Optional.ofNullable(payload);
	}

	/**
	 * Set the entity payload
	 * @param payload the payload to set
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultRequestEntity [mediaType=" + mediaType + ", payload=" + payload + "]";
	}

	/**
	 * Default {@link FormBuilder}.
	 */
	public static class DefaultFormBuilder implements FormBuilder {

		/**
		 * Form parameters
		 */
		private final Map<String, List<String>> parameters = new LinkedHashMap<>();

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.RequestEntity.FormBuilder#set(java.lang.String, java.lang.String[])
		 */
		@Override
		public com.holonplatform.http.RequestEntity.FormBuilder set(String name, String... value) {
			ObjectUtils.argumentNotNull(name, "Parameter name must be not null");
			parameters.put(name, (value != null) ? Arrays.asList(value) : null);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.http.RequestEntity.FormBuilder#build()
		 */
		@Override
		public Map<String, List<String>> build() {
			return parameters;
		}

	}

}
