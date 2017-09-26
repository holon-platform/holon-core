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
package com.holonplatform.core.messaging;

import java.util.Optional;

/**
 * Generic message representation with headers and payload.
 * 
 * @param <P> Message payload type
 * @param <H> Message headers value type
 * 
 * @since 5.0.0
 */
public interface Message<P, H> extends MessageHeaders<H> {

	/**
	 * Protocol name with which message was delivered
	 * @return Optional Protocol name
	 */
	Optional<String> getProtocol();

	/**
	 * Message payload
	 * @return Optional message payload
	 * @throws UnsupportedOperationException If this kind of message does not support a payload
	 */
	Optional<P> getPayload() throws UnsupportedOperationException;

	/**
	 * Message payload type
	 * @return Payload type
	 * @throws UnsupportedOperationException If this kind of message does not support a payload
	 */
	Class<? extends P> getPayloadType() throws UnsupportedOperationException;

}
