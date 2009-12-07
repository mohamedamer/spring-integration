/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.transformer;

import org.springframework.integration.core.Message;
import org.springframework.integration.message.MessageBuilder;

/**
 * A base class for {@link Transformer} implementations that modify
 * the payload of a {@link Message}. If the return value is itself
 * a Message, it will be used as the result. Otherwise, the return
 * value will be used as the payload of the result Message.
 * 
 * @author Mark Fisher
 */
public abstract class AbstractPayloadTransformer<T, U> implements Transformer {

	@SuppressWarnings("unchecked")
	public final Message<?> transform(Message<?> message) {
		try {
	        U result = this.transformPayload((T) message.getPayload());
			return (result instanceof Message)
					? MessageBuilder.fromMessage((Message<?>) result).build()
					: MessageBuilder.withPayload(result).copyHeaders(message.getHeaders()).build();
        }
		catch (MessageTransformationException e) {
			throw e;
		}
		catch (Exception e) {
        	throw new MessageTransformationException(message, "failed to transform message payload", e);
        }
	}

	protected abstract U transformPayload(T payload) throws Exception;

}
