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
 * Enumeration of common media types used by REST services.
 *
 * @since 5.0.0
 */
public enum MediaType {

	/**
	 * Wildcard media type
	 */
	WILDCARD("*", "*", null),

	/**
	 * text/plain media type
	 */
	TEXT_PLAIN("text", "plain", null),

	/**
	 * text/xml media type
	 */
	TEXT_XML("text", "xml", null),

	/**
	 * text/html media type
	 */
	TEXT_HTML("text", "html", null),

	/**
	 * text/markdown media type
	 */
	TEXT_MARKDOWN("text", "markdown", null),

	/**
	 * application/json media type
	 */
	APPLICATION_JSON("application", "json", null),

	/**
	 * application/json media type with UTF8 charset parameter
	 */
	APPLICATION_JSON_UTF8("application", "json", "charset=UTF-8"),

	/**
	 * application/x-www-form-urlencoded media type
	 */
	APPLICATION_FORM_URLENCODED("application", "x-www-form-urlencoded", null),

	/**
	 * multipart/form-data media type
	 */
	MULTIPART_FORM_DATA("multipart", "form-data", null),

	/**
	 * application/xml media type
	 */
	APPLICATION_XML("application", "xml", null),

	/**
	 * application/svg+xml media type
	 */
	APPLICATION_SVG_XML("application", "svg+xml", null),

	/**
	 * application/octet-stream media type
	 */
	APPLICATION_OCTET_STREAM("application", "octet-stream", null),

	/**
	 * application/pdf media type
	 */
	APPLICATION_PDF("application", "pdf", null),

	/**
	 * image/gif media type
	 */
	IMAGE_GIF("image", "gif", null),

	/**
	 * image/jpeg media type
	 */
	IMAGE_JPEG("image", "jpeg", null),

	/**
	 * image/png media type
	 */
	IMAGE_PNG("image", "png", null);

	private final String type;
	private final String subtype;
	private final String parameters;

	private MediaType(String type, String subtype, String parameters) {
		this.type = type;
		this.subtype = subtype;
		this.parameters = parameters;
	}

	/**
	 * Gets the primary type
	 * @return Primary type string
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the sub type
	 * @return Sub type string
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * Get media type parameters string
	 * @return Media type parameters, or <code>null</code> if none
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Returns the media type representation in the conventional HTTP header form, i.e. <code>type/subtype</code>.
	 * @return Media type representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getType()).append('/').append(getSubtype());
		if (getParameters() != null) {
			sb.append(';').append(getParameters());
		}
		return sb.toString();
	}

}
