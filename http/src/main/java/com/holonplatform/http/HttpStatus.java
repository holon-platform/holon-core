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
 * Enumeration of HTTP status codes.
 * 
 * @since 5.0.0
 */
public enum HttpStatus {

	// 1xx: Informational

	/**
	 * 100 - Continue
	 */
	CONTINUE(100, "Continue"),
	/**
	 * 101 - Switching Protocols
	 */
	SWITCHING_PROTOCOLS(101, "Switching Protocols"),
	/**
	 * 102 - Processing
	 */
	PROCESSING(102, "Processing"),
	/**
	 * 103 - Checkpoint
	 */
	CHECKPOINT(103, "Checkpoint"),

	// 2xx: Success

	/**
	 * 200 - OK
	 */
	OK(200, "OK"),
	/**
	 * 201 - Created
	 */
	CREATED(201, "Created"),
	/**
	 * 202 - Accepted
	 */
	ACCEPTED(202, "Accepted"),
	/**
	 * 203 - Non-Authoritative Information
	 */
	NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
	/**
	 * 204 - No content
	 */
	NO_CONTENT(204, "No Content"),
	/**
	 * 205 - Reset content
	 */
	RESET_CONTENT(205, "Reset Content"),
	/**
	 * 206 - Partial content
	 */
	PARTIAL_CONTENT(206, "Partial Content"),
	/**
	 * 207 - Multi-status
	 */
	MULTI_STATUS(207, "Multi-Status"),
	/**
	 * 208 - Already reported
	 */
	ALREADY_REPORTED(208, "Already Reported"),
	/**
	 * 226 - IM used
	 */
	IM_USED(226, "IM Used"),

	// 3xx: Redirection

	/**
	 * 300 - Multiple Choices
	 */
	MULTIPLE_CHOICES(300, "Multiple Choices"),
	/**
	 * 301 - Moved Permanently
	 */
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	/**
	 * 302 - Found
	 */
	FOUND(302, "Found"),
	/**
	 * 303 - See other
	 */
	SEE_OTHER(303, "See Other"),
	/**
	 * 304 - Not modified
	 */
	NOT_MODIFIED(304, "Not Modified"),
	/**
	 * 307 - Temporary Redirect
	 */
	TEMPORARY_REDIRECT(307, "Temporary Redirect"),
	/**
	 * 308 - Permanent Redirect
	 */
	PERMANENT_REDIRECT(308, "Permanent Redirect"),

	// 4xx: Client Error

	/**
	 * 400 - Bad request
	 */
	BAD_REQUEST(400, "Bad Request"),
	/**
	 * 401 - Unauthorized
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	/**
	 * 402 - Payment Required
	 */
	PAYMENT_REQUIRED(402, "Payment Required"),
	/**
	 * 403 - Forbidden
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * 404 - Not found
	 */
	NOT_FOUND(404, "Not Found"),
	/**
	 * 405 - Method not allowed
	 */
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	/**
	 * 406 - Not acceptable
	 */
	NOT_ACCEPTABLE(406, "Not Acceptable"),
	/**
	 * 407 - Proxy Authentication Required
	 */
	PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
	/**
	 * 408 - Request timeout
	 */
	REQUEST_TIMEOUT(408, "Request Timeout"),
	/**
	 * 409 - Conflict
	 */
	CONFLICT(409, "Conflict"),
	/**
	 * 410 - Gone
	 */
	GONE(410, "Gone"),
	/**
	 * 411 - Length Required
	 */
	LENGTH_REQUIRED(411, "Length Required"),
	/**
	 * 412 - Precondition Failed
	 */
	PRECONDITION_FAILED(412, "Precondition Failed"),
	/**
	 * 413 - Payload Too Large
	 */
	PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
	/**
	 * 414 - URI too long
	 */
	URI_TOO_LONG(414, "URI Too Long"),
	/**
	 * 415 - Unsupported Media Type
	 */
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	/**
	 * 416 - Requested range not satisfiable
	 */
	REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
	/**
	 * 417 - Expectation Failed
	 */
	EXPECTATION_FAILED(417, "Expectation Failed"),
	/**
	 * 418 - I'm a teapot
	 */
	I_AM_A_TEAPOT(418, "I'm a teapot"),
	/**
	 * 419 - Insufficient Space On Resource
	 */
	INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
	/**
	 * 420 - Method Failure
	 */
	METHOD_FAILURE(420, "Method Failure"),
	/**
	 * 421 - Destination Locked
	 */
	DESTINATION_LOCKED(421, "Destination Locked"),
	/**
	 * 422 - Unprocessable Entity
	 */
	UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
	/**
	 * 423 - Locked
	 */
	LOCKED(423, "Locked"),
	/**
	 * 424 - Failed Dependency
	 */
	FAILED_DEPENDENCY(424, "Failed Dependency"),
	/**
	 * 426 - Upgrade Required
	 */
	UPGRADE_REQUIRED(426, "Upgrade Required"),
	/**
	 * 428 - Precondition Required
	 */
	PRECONDITION_REQUIRED(428, "Precondition Required"),
	/**
	 * 429 - Too Many Requests
	 */
	TOO_MANY_REQUESTS(429, "Too Many Requests"),
	/**
	 * 431 - Request Header Fields Too Large
	 */
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
	/**
	 * 451 - Unavailable For Legal Reasons
	 */
	UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

	// 5xx: Server Error

	/**
	 * 500 - Internal server error
	 */
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	/**
	 * 501 - Not Implemented
	 */
	NOT_IMPLEMENTED(501, "Not Implemented"),
	/**
	 * 502 - Bad Gateway
	 */
	BAD_GATEWAY(502, "Bad Gateway"),
	/**
	 * 503 - Service Unavailable
	 */
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	/**
	 * 504 - Gateway Timeout
	 */
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	/**
	 * 505 - HTTP Version not supported
	 */
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
	/**
	 * 506 - Variant Also Negotiates
	 */
	VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
	/**
	 * 507 - Insufficient Storage
	 */
	INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
	/**
	 * 508 - Loop Detected
	 */
	LOOP_DETECTED(508, "Loop Detected"),
	/**
	 * 509 - Bandwidth Limit Exceeded
	 */
	BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
	/**
	 * 510 - Not Extended
	 */
	NOT_EXTENDED(510, "Not Extended"),
	/**
	 * 511 - Network Authentication Required
	 */
	NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

	/**
	 * Status code
	 */
	private final int code;
	/**
	 * Status description
	 */
	private final String description;

	/**
	 * Constructor
	 * @param code Status code
	 * @param description Status description
	 */
	private HttpStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Get the status numeric code
	 * @return the status code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Get the status description
	 * @return the status description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the {@link HttpStatus} which corresponds to given status code, if any
	 * @param code Status code
	 * @return HttpStatus which corresponds to given code, or <code>null</code>
	 */
	public static HttpStatus of(int code) {
		for (HttpStatus status : values()) {
			if (code == status.code) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gets whether given <code>statusCode</code> is a <em>success</em>, i.e. a <code>2xx</code> status code
	 * @param statusCode Status code
	 * @return <code>true</code> if given <code>statusCode</code> is a <em>success</em> status code
	 */
	public static boolean isSuccessStatusCode(int statusCode) {
		return statusCode >= 200 && statusCode < 300;
	}

}
