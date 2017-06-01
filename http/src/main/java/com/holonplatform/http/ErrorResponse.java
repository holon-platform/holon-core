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
 * A generic error response representation, providing conventional error informations including an error code, an
 * optional description and URI for more details, and the HTTP status code.
 * 
 * @since 5.0.0
 */
public interface ErrorResponse {

	/**
	 * Default <code>server_error</code> error code: Server encountered an unexpected condition that prevented it from
	 * fulfilling the request.
	 */
	public static final String SERVER_ERROR = "server_error";

	/**
	 * Default <code>temporarily_unavailable</code> error code: Server is currently unable to handle the request due to
	 * a temporary overloading or maintenance of the server.
	 */
	public static final String TEMPORARY_UNAVAILABLE = "temporarily_unavailable";

	/**
	 * Default <code>invalid_request</code> error code: The request is missing a required parameter, includes an invalid
	 * parameter value, includes a parameter more than once, or is otherwise malformed.
	 */
	public static final String INVALID_REQUEST = "invalid_request";

	/**
	 * Default <code>invalid_client</code> error code: Client authentication failed (e.g. unknown client, no client
	 * authentication included, or unsupported authentication method).
	 */
	public static final String INVALID_CLIENT = "invalid_client";

	/**
	 * Default <code>invalid_token</code> error code: The provided authorization token is not valid (expired, revoked,
	 * malformed, or invalid for other reasons)
	 */
	public static final String INVALID_TOKEN = "invalid_token";

	// Serialization

	/**
	 * Default serialization name for error code
	 */
	public static final String ERROR_CODE_SERIALIZATION_NAME = "error";
	/**
	 * Default serialization name for error description
	 */
	public static final String ERROR_DESCRIPTION_SERIALIZATION_NAME = "error_description";
	/**
	 * Default serialization name for error URI
	 */
	public static final String ERROR_URI_SERIALIZATION_NAME = "error_uri";

	/**
	 * Error code.
	 * @return Error code
	 */
	String getErrorCode();

	/**
	 * Error description, i.e. a human-readable explanation of this error
	 * @return Error description
	 */
	String getErrorDescription();

	/**
	 * Optional error URI that leads to further details about this error
	 * @return Error URI
	 */
	String getErrorURI();

	/**
	 * Optional HTTP status code to represent error as a HTTP response
	 * @return HTTP status code, <code>-1</code> means unknown
	 */
	int getHttpStatus();

	/**
	 * Base class to build a {@link RuntimeException} with {@link ErrorResponse} support.
	 * @since 5.0.0
	 */
	public abstract class ErrorResponseException extends RuntimeException implements ErrorResponse {

		private static final long serialVersionUID = -4231389067665295132L;

		/**
		 * Error code
		 */
		private String errorCode;

		/**
		 * Error description
		 */
		private String errorDescription;

		/**
		 * Error URI
		 */
		private String errorURI;

		/**
		 * HTTP status code, <code>-1</code> means unknown/unsetted
		 */
		private int httpStatus = -1;

		/**
		 * Constructor
		 * @param errorCode Error code
		 * @param errorDescription Error description, i.e. a human-readable explanation of this error
		 */
		public ErrorResponseException(String errorCode, String errorDescription) {
			this(errorCode, errorDescription, null, -1);
		}

		/**
		 * Constructor
		 * @param errorCode Error code
		 * @param errorDescription Error description, i.e. a human-readable explanation of this error
		 * @param httpStatus HTTP status code to represent error as a HTTP response
		 */
		public ErrorResponseException(String errorCode, String errorDescription, int httpStatus) {
			this(errorCode, errorDescription, null, httpStatus);
		}

		/**
		 * Constructor
		 * @param errorCode Error code
		 * @param errorDescription Error description, i.e. a human-readable explanation of this error
		 * @param errorURI Error URI that leads to further details about this error
		 */
		public ErrorResponseException(String errorCode, String errorDescription, String errorURI) {
			this(errorCode, errorDescription, errorURI, -1);
		}

		/**
		 * Constructor
		 * @param errorCode Error code
		 * @param errorDescription Error description, i.e. a human-readable explanation of this error
		 * @param errorURI Error URI that leads to further details about this error
		 * @param httpStatus HTTP status code to represent error as a HTTP response
		 */
		public ErrorResponseException(String errorCode, String errorDescription, String errorURI, int httpStatus) {
			super(buildExceptionMessage(errorCode, errorDescription));
			this.errorCode = errorCode;
			this.errorDescription = errorDescription;
			this.errorURI = errorURI;
			this.httpStatus = httpStatus;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.errors.ErrorResponse#getErrorCode()
		 */
		@Override
		public String getErrorCode() {
			return errorCode;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.errors.ErrorResponse#getErrorDescription()
		 */
		@Override
		public String getErrorDescription() {
			return errorDescription;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.errors.ErrorResponse#getErrorURI()
		 */
		@Override
		public String getErrorURI() {
			return errorURI;
		}

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.errors.ErrorResponse#getHttpStatus()
		 */
		@Override
		public int getHttpStatus() {
			return httpStatus;
		}

		/**
		 * Build exception message
		 * @param errorCode Error code
		 * @param errorDescription Error description
		 * @return Message with code and description
		 */
		protected static String buildExceptionMessage(String errorCode, String errorDescription) {
			StringBuilder sb = new StringBuilder();
			if (errorCode != null) {
				sb.append(errorCode);
			}
			if (errorDescription != null) {
				if (sb.length() > 0) {
					sb.append(" - ");
				}
				sb.append(errorDescription);
			}
			return sb.toString();
		}

	}

}
