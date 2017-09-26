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
package com.holonplatform.auth.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;

import com.holonplatform.auth.Credentials;
import com.holonplatform.auth.CredentialsContainer;
import com.holonplatform.auth.CredentialsContainer.CredentialsMatcher;
import com.holonplatform.auth.exceptions.AuthenticationException;
import com.holonplatform.auth.exceptions.ExpiredCredentialsException;
import com.holonplatform.auth.exceptions.UnexpectedCredentialsException;
import com.holonplatform.core.internal.utils.ConversionUtils;
import com.holonplatform.core.internal.utils.Hash;

/**
 * Default {@link CredentialsMatcher} implementation that employs best-practices and common behaviours to perform
 * credentials validation and matching.
 * 
 * @since 5.0.0
 */
public class DefaultCredentialsMatcher implements CredentialsMatcher {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.auth.credentials.CredentialsMatcher#credentialsMatch(com.holonplatform.auth.
	 * CredentialsContainer, com.holonplatform.auth.CredentialsContainer)
	 */
	@Override
	public boolean credentialsMatch(CredentialsContainer provided, CredentialsContainer stored)
			throws AuthenticationException {
		Object providedCredentials = getProvidedCredentials(provided);
		Object storedCredentials = getStoredCredentials(stored);

		if (providedCredentials == null) {
			throw new UnexpectedCredentialsException("Null provided credentials");
		}
		if (storedCredentials == null) {
			throw new UnexpectedCredentialsException("Null stored credentials");
		}

		return match(providedCredentials, storedCredentials);
	}

	/**
	 * Get credentials data from container for provided credentials
	 * @param container Credentials container
	 * @return Credentials data, or <code>null</code> if container or credentials are null
	 * @throws UnexpectedCredentialsException Error retrieving credentials data
	 */
	protected Object getProvidedCredentials(CredentialsContainer container) throws UnexpectedCredentialsException {
		return (container != null) ? container.getCredentials() : null;
	}

	/**
	 * Get credentials data from container for stored credentials
	 * @param container Credentials container
	 * @return Credentials data, or <code>null</code> if container or credentials are null
	 * @throws UnexpectedCredentialsException Error retrieving credentials data
	 */
	protected Object getStoredCredentials(CredentialsContainer container) throws UnexpectedCredentialsException {
		return (container != null) ? container.getCredentials() : null;
	}

	/**
	 * Check if credentials data match
	 * @param providedCredentials Provided credentials data
	 * @param storedCredentials Stored credentials data
	 * @return <code>true</code> if credentials match
	 * @throws AuthenticationException Error performing match operations
	 */
	protected boolean match(Object providedCredentials, Object storedCredentials) throws AuthenticationException {
		if (isByteConvertible(providedCredentials) && isByteConvertible(storedCredentials)) {
			try {
				// convert provided to bytes
				byte[] provided = toBytes(providedCredentials);
				if (provided == null) {
					throw new UnexpectedCredentialsException(
							"Failed to convert provided credentials as bytes: null result");
				}
				// check stored credentials type
				if (storedCredentials instanceof Credentials) {
					return match(provided, (Credentials) storedCredentials);
				} else {
					byte[] stored = toBytes(storedCredentials);
					if (stored == null) {
						throw new UnexpectedCredentialsException(
								"Failed to convert stored credentials as bytes: null result");
					}
					return match(provided, stored);
				}
			} catch (Exception e) {
				throw new UnexpectedCredentialsException(e);
			}
		} else {
			// fallback to object equality
			return providedCredentials.equals(storedCredentials);
		}
	}

	/**
	 * Match provided credentials with stored {@link Credentials} data, performing hashing and encoding on provided
	 * credentials data if required
	 * @param providedCredentials Provided credentials
	 * @param storedCredentials Stored credentials
	 * @return <code>true</code> if credentials match
	 * @throws AuthenticationException Error during match operations
	 */
	protected boolean match(byte[] providedCredentials, Credentials storedCredentials) throws AuthenticationException {
		byte[] stored = storedCredentials.getSecret();
		if (stored == null) {
			throw new UnexpectedCredentialsException("Stored credentials secret is null");
		}

		// check expire date
		if (storedCredentials.getExpireDate() != null) {
			if (storedCredentials.getExpireDate().after(Calendar.getInstance().getTime())) {
				throw new ExpiredCredentialsException("Credentials are expired");
			}
		}

		byte[] provided = providedCredentials;

		if (storedCredentials.getHashAlgorithm() != null) {
			byte[] salt = storedCredentials.getSalt();
			if (storedCredentials.isBase64Encoded()) {
				// expect salt Base64 encoded too, if not null
				salt = (salt != null) ? Base64.getDecoder().decode(salt) : null;
			}
			try {
				provided = Hash.hash(storedCredentials.getHashAlgorithm(), provided, salt,
						storedCredentials.getHashIterations());
			} catch (NoSuchAlgorithmException e) {
				throw new UnexpectedCredentialsException(e);
			}
		}

		if (storedCredentials.isBase64Encoded()) {
			provided = Base64.getEncoder().encode(provided);
		}
		return match(provided, stored);
	}

	/**
	 * Perform a byte[] comparison
	 * @param providedCredentials Provided credentials as byte[]
	 * @param storedCredentials Stored credentials as byte[]
	 * @return <code>true</code> if match
	 */
	protected boolean match(byte[] providedCredentials, byte[] storedCredentials) {
		return Arrays.equals(providedCredentials, storedCredentials);
	}

	/**
	 * Check if given object can be converted to bytes using a supported input type
	 * @param o Object to convert
	 * @return <code>true</code> if object can be safely converted to bytes
	 */
	protected static boolean isByteConvertible(Object o) {
		return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof File
				|| o instanceof InputStream || o instanceof Credentials;
	}

	/**
	 * Try to convert the given Object into a byte array.
	 * <p>
	 * Supported inputs are <code>byte[]</code>, <code>char[]</code>, {@link String}, {@link InputStream} and
	 * {@link File}. Otherwise, an {@link IllegalArgumentException} will be thrown.
	 * </p>
	 * @param o the Object to convert into a byte array
	 * @return a byte array representation of the Object
	 */
	public static byte[] toBytes(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("Null object to convert");
		}
		if (o instanceof byte[]) {
			return (byte[]) o;
		}
		if (o instanceof char[]) {
			return ConversionUtils.toBytes((char[]) o);
		}
		if (o instanceof Credentials) {
			return ((Credentials) o).getSecret();
		}
		if (o instanceof String) {
			return ConversionUtils.toBytes((String) o);
		}
		if (o instanceof File) {
			try (FileInputStream fis = new FileInputStream((File) o)) {
				return ConversionUtils.convertInputStreamToBytes(fis);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (o instanceof InputStream) {
			try {
				return ConversionUtils.convertInputStreamToBytes((InputStream) o);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("Unsupported type for byte[] conversion: " + o.getClass().getName());
	}

}
