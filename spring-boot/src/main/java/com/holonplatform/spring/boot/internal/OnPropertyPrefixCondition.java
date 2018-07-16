/*
 * Copyright 2000-2017 Holon TDCN.
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
package com.holonplatform.spring.boot.internal;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.spring.EnvironmentConfigPropertyProvider;

/**
 * Condition bound to the {@link ConditionalOnPropertyPrefix} annotation.
 * 
 * @since 5.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 30)
public class OnPropertyPrefixCondition extends SpringBootCondition {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.boot.autoconfigure.condition.SpringBootCondition#getMatchOutcome(org.springframework.context.
	 * annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)
	 */
	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnPropertyPrefix.class.getName());
		if (attributes != null && attributes.containsKey("value")) {
			String prefix = (String) attributes.get("value");
			if (prefix != null && !prefix.trim().equals("")) {
				final String propertyPrefix = !prefix.endsWith(".") ? prefix + "." : prefix;
				ConfigPropertyProvider configPropertyProvider = EnvironmentConfigPropertyProvider
						.create(context.getEnvironment());
				Set<String> names = configPropertyProvider.getPropertyNames()
						.filter((n) -> n.startsWith(propertyPrefix)).collect(Collectors.toSet());
				if (!names.isEmpty()) {
					return ConditionOutcome.match();
				}
				return ConditionOutcome.noMatch(
						ConditionMessage.forCondition(ConditionalOnPropertyPrefix.class).notAvailable(propertyPrefix));
			}
		}
		return ConditionOutcome.match();
	}

}
