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
package com.holonplatform.http;

/**
 * HTTP request methods enumeration.
 * 
 * @since 5.0.0
 */
public enum HttpMethod {

	GET("GET"),

	POST("POST"),

	PUT("PUT"),

	DELETE("DELETE"),

	HEAD("HEAD"),

	PATCH("PATCH"),

	OPTIONS("OPTIONS"),

	TRACE("TRACE");

	/**
	 * Method name
	 */
	private final String methodName;

	/**
	 * Constructor
	 * @param methodName Method name
	 */
	private HttpMethod(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Method name
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Get HttpMethod enum constant matching given <code>methodName</code> name
	 * @param methodName Method name
	 * @return Matching HttpMethod, or <code>null</code> if none match
	 */
	public static HttpMethod from(String methodName) {
		if (methodName != null) {
			for (HttpMethod value : values()) {
				if (methodName.equalsIgnoreCase(value.getMethodName())) {
					return value;
				}
			}
		}
		return null;
	}

}
