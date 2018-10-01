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
package com.holonplatform.core.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.beans.BeanProperty;
import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.Config;
import com.holonplatform.core.beans.Sequence;
import com.holonplatform.core.beans.Temporal;
import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.internal.beans.BeanPropertyCaptionPostProcessor;
import com.holonplatform.core.internal.beans.BeanPropertyConfigPostProcessor;
import com.holonplatform.core.internal.beans.BeanPropertySequencePostProcessor;
import com.holonplatform.core.internal.beans.BeanPropertyTemporalPostProcessor;
import com.holonplatform.core.temporal.TemporalType;

public class TestBeanPostProcessor {

	@Test
	public void testCaption() {

		Builder<String> property = BeanProperty.builder("test", String.class);

		final Caption annotation = new Caption() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Caption.class;
			}

			@Override
			public String value() {
				return "theCaption";
			}

			@Override
			public String messageCode() {
				return "theCaptionMessageCode";
			}
		};

		property.annotations(new Annotation[] { annotation });

		BeanPropertyCaptionPostProcessor postProcessor = new BeanPropertyCaptionPostProcessor();
		postProcessor.processBeanProperty(property, Object.class);

		assertEquals("theCaption", property.getMessage());
		assertEquals("theCaptionMessageCode", property.getMessageCode());

	}

	@Test
	public void testConfig() {

		Builder<String> property = BeanProperty.builder("test", String.class);

		final Config annotation = new Config() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Config.class;
			}

			@Override
			public String value() {
				return "testValue";
			}

			@Override
			public String key() {
				return "test";
			}
		};

		property.annotations(new Annotation[] { annotation });

		BeanPropertyConfigPostProcessor postProcessor = new BeanPropertyConfigPostProcessor();
		postProcessor.processBeanProperty(property, Object.class);

		assertNotNull(property.getConfiguration());

		assertTrue(property.getConfiguration().hasNotNullParameter("test"));
		assertEquals("testValue", property.getConfiguration().getParameter("test", String.class).orElse(null));

	}

	@Test
	public void testSequence() {

		Builder<String> property = BeanProperty.builder("test", String.class);

		final Sequence annotation = new Sequence() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Sequence.class;
			}

			@Override
			public int value() {
				return 7;
			}
		};

		property.annotations(new Annotation[] { annotation });

		BeanPropertySequencePostProcessor postProcessor = new BeanPropertySequencePostProcessor();
		postProcessor.processBeanProperty(property, Object.class);

		assertTrue(property.getSequence().isPresent());
		assertEquals(Integer.valueOf(7), property.getSequence().get());

	}

	@Test
	public void testTemporal() {

		Builder<String> property = BeanProperty.builder("test", String.class);

		final Temporal annotation = new Temporal() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Temporal.class;
			}

			@Override
			public TemporalType value() {
				return TemporalType.DATE_TIME;
			}
		};

		property.annotations(new Annotation[] { annotation });

		BeanPropertyTemporalPostProcessor postProcessor = new BeanPropertyTemporalPostProcessor();
		postProcessor.processBeanProperty(property, Object.class);

		assertNotNull(property.getConfiguration());
		assertEquals(TemporalType.DATE_TIME, property.getConfiguration().getTemporalType().orElse(null));

	}

}
