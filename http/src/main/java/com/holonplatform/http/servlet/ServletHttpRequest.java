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
package com.holonplatform.http.servlet;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.holonplatform.http.HttpRequest;
import com.holonplatform.http.internal.servlet.DefaultServletHttpRequest;

/**
 * A {@link HttpRequest} backed by a Servlet {@link HttpServletRequest}.
 * 
 * @since 5.0.6
 */
public interface ServletHttpRequest extends HttpRequest {

	/**
	 * Returns the portion of the request URI that indicates the context of the request. The context path always comes
	 * first in a request URI. The path starts with a "/" character but does not end with a "/" character. For servlets
	 * in the default (root) context, this method returns "".
	 * @return a <code>String</code> specifying the portion of the request URI that indicates the context of the request
	 */
	String getContextPath();

	/**
	 * Returns the part of this request's URL from the protocol name up to the query string in the first line of the
	 * HTTP request.
	 * @return a <code>String</code> containing the part of the URL from the protocol name up to the query string
	 */
	String getRequestURI();

	/**
	 * Returns the id of current <code>HttpSession</code> associated with this request, if any.
	 * @return the session id, or empty if no <code>HttpSession</code> is associated with this request
	 */
	Optional<String> getSessionId();

	/**
	 * Create a new {@link ServletHttpRequest} using given Servlet request.
	 * @param request Servlet request (not null)
	 * @return A new {@link ServletHttpRequest} backed by given Servlet request
	 */
	static ServletHttpRequest create(HttpServletRequest request) {
		return new DefaultServletHttpRequest(request);
	}

}
