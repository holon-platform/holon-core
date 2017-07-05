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
package com.holonplatform.http.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.holonplatform.http.MediaType;
import com.holonplatform.http.internal.rest.DefaultRequestEntity;

/**
 * HTTP request message entity (payload) representation.
 *
 * @param <T> Entity type
 *
 * @see RestClient
 */
public interface RequestEntity<T> {

	/**
	 * Empty request entity (with no payload and media type)
	 */
	static final RequestEntity<Void> EMPTY = build(null, null);

	/**
	 * Get entity media type, if specified
	 * @return Media type
	 */
	Optional<String> getMediaType();

	/**
	 * Get entity payload, if available
	 * @return Entity payload
	 */
	Optional<T> getPayload();

	/**
	 * Build a RequestEntity.
	 * @param <T> Payload type
	 * @param mediaType Media type
	 * @param payload Entity payload
	 * @return RequestEntity
	 */
	static <T> RequestEntity<T> build(String mediaType, T payload) {
		return new DefaultRequestEntity<>(mediaType, payload);
	}

	/**
	 * Build a {@link MediaType#TEXT_PLAIN} entity.
	 * @param <T> Payload type
	 * @param payload Entity payload
	 * @return RequestEntity
	 */
	static <T> RequestEntity<T> text(T payload) {
		return new DefaultRequestEntity<>(MediaType.TEXT_PLAIN.toString(), payload);
	}

	/**
	 * Build a {@link MediaType#APPLICATION_XML} entity.
	 * @param <T> Payload type
	 * @param payload Entity payload
	 * @return RequestEntity
	 */
	static <T> RequestEntity<T> xml(T payload) {
		return new DefaultRequestEntity<>(MediaType.APPLICATION_XML.toString(), payload);
	}

	/**
	 * Build a {@link MediaType#APPLICATION_JSON} entity.
	 * @param <T> Payload type
	 * @param payload Entity payload
	 * @return RequestEntity
	 */
	static <T> RequestEntity<T> json(T payload) {
		return new DefaultRequestEntity<>(MediaType.APPLICATION_JSON.toString(), payload);
	}

	/**
	 * Build a {@link MediaType#APPLICATION_FORM_URLENCODED} entity
	 * @param formData Form data name-value(s) map
	 * @return RequestEntity
	 */
	static RequestEntity<Map<String, List<String>>> form(final Map<String, List<String>> formData) {
		return new DefaultRequestEntity<>(MediaType.APPLICATION_FORM_URLENCODED.toString(), formData);
	}

	/**
	 * Gets a {@link FormBuilder} to build form parameters name-value(s) map to be used with form data type entity.
	 * @return FormBuilder
	 * @see #form(Map)
	 */
	static FormBuilder formBuilder() {
		return new DefaultRequestEntity.DefaultFormBuilder();
	}

	/**
	 * Builder to create form type entity payloads.
	 */
	public interface FormBuilder {

		/**
		 * Set a form parameter value(s)
		 * @param name Form parameter name
		 * @param value Form parameter value(s)
		 * @return this
		 */
		FormBuilder set(String name, String... value);

		/**
		 * Build form data map
		 * @return Form data name-value(s) map
		 */
		Map<String, List<String>> build();

	}

}
