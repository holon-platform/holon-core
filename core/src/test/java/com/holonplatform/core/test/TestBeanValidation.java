/*
 * Copyright 2016-2018 Axioma srl.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.test.data.TestBeanPropertyBean;
import com.holonplatform.test.TestUtils;

public class TestBeanValidation {

	@Test
	public void testValidationConstraints() {

		BeanPropertySet<TestBeanPropertyBean> set = BeanIntrospector.get().getPropertySet(TestBeanPropertyBean.class);

		assertTrue(set.getProperty("name").isPresent());
		assertTrue(set.getProperty("text").isPresent());
		assertTrue(set.getProperty("required").isPresent());
		assertTrue(set.getProperty("email").isPresent());
		assertTrue(set.getProperty("numbool").isPresent());
		assertTrue(set.getProperty("enm").isPresent());
		assertTrue(set.getProperty("enmOrdinal").isPresent());
		assertTrue(set.getProperty("date").isPresent());
		assertTrue(set.getProperty("lng").isPresent());
		assertTrue(set.getProperty("notneg").isPresent());
		assertTrue(set.getProperty("notnegzero").isPresent());
		assertTrue(set.getProperty("intval").isPresent());
		assertTrue(set.getProperty("legacyDate").isPresent());

		set.property("name").validate("xxx");

		TestUtils.expectedException(ValidationException.class, () -> set.property("name").validate(null));

		TestUtils.expectedException(ValidationException.class, () -> set.property("name").validate("   "));

		try {
			set.property("name").validate(null);
		} catch (ValidationException e) {
			assertEquals("Name is required;Name is empty", e.getMessage());
		}

		set.property("text").validate("x");

		TestUtils.expectedException(ValidationException.class, () -> set.property("text").validate(null));

		TestUtils.expectedException(ValidationException.class, () -> set.property("text").validate(""));

		set.property("required").validate("");

		TestUtils.expectedException(ValidationException.class, () -> set.property("required").validate(null));

		set.property("email").validate("abc@test.org");

		TestUtils.expectedException(ValidationException.class, () -> set.property("email").validate("abc"));

		set.property("numbool").validate(0);
		set.property("numbool").validate(1);

		TestUtils.expectedException(ValidationException.class, () -> set.property("intval").validate(-1));
		TestUtils.expectedException(ValidationException.class, () -> set.property("intval").validate(11));

		try {
			set.property("intval").validate(11);
		} catch (ValidationException e) {
			assertEquals("0-10 range", e.getMessage());
			assertEquals("test-mc", e.getMessageCode());
		}

		set.property("lng").validate(7L);

		TestUtils.expectedException(ValidationException.class, () -> set.property("lng").validate(3L));

		try {
			set.property("lng").validate(0L);
		} catch (ValidationException e) {
			assertEquals("Must be 7", e.getMessage());
		}

		TestUtils.expectedException(ValidationException.class, () -> set.property("notneg").validate(-1));

		TestUtils.expectedException(ValidationException.class, () -> set.property("notneg").validate(0));

		assertTrue(set.property("notneg").getConfiguration().hasNotNullParameter("k1"));
		assertEquals("v1", set.property("notneg").getConfiguration().getParameter("k1", String.class, null));

		assertTrue(set.property("notneg").getConfiguration().hasNotNullParameter("k2"));
		assertEquals("v2", set.property("notneg").getConfiguration().getParameter("k2", String.class, null));

		set.property("notnegzero").validate(0);

		TestUtils.expectedException(ValidationException.class, () -> set.property("notnegzero").validate(-1));

	}

}
