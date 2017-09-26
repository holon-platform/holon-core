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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.UnsupportedValidationTypeException;
import com.holonplatform.core.Validator.ValidationException;
import com.holonplatform.core.internal.BuiltinValidator;
import com.holonplatform.core.internal.utils.TestUtils;

public class TestValidators {

	@Test
	public void testNull() {
		Validator.isNull().validate(null);
		TestUtils.expectedException(ValidationException.class, () -> Validator.isNull().validate(1));
	}

	@Test
	public void testNotNull() {
		Validator.notNull().validate(1);
		TestUtils.expectedException(ValidationException.class, () -> Validator.notNull().validate(null));
	}

	@Test
	public void testNotEmpty() {
		Validator.notEmpty().validate("a");
		TestUtils.expectedException(ValidationException.class, () -> Validator.notEmpty().validate(null));
		TestUtils.expectedException(ValidationException.class, () -> Validator.notEmpty().validate(""));

		Validator.notEmpty().validate(Collections.singleton(1));
		TestUtils.expectedException(ValidationException.class,
				() -> Validator.notEmpty().validate(Collections.emptySet()));

		Validator.notEmpty().validate(Collections.singletonMap("a", 1));
		TestUtils.expectedException(ValidationException.class,
				() -> Validator.notEmpty().validate(Collections.emptyMap()));

		Validator.notEmpty().validate(new String[] { "a" });
		TestUtils.expectedException(ValidationException.class, () -> Validator.notEmpty().validate(new String[0]));

		TestUtils.expectedException(UnsupportedValidationTypeException.class, () -> Validator.notEmpty().validate(1));
	}

	@Test
	public void testNotBlank() {
		Validator.notBlank().validate("a");

		TestUtils.expectedException(ValidationException.class, () -> Validator.notBlank().validate(null));
		TestUtils.expectedException(ValidationException.class, () -> Validator.notBlank().validate(""));
		TestUtils.expectedException(ValidationException.class, () -> Validator.notBlank().validate(" "));
	}

	@Test
	public void testMin() {
		Validator.min(1).validate(null);
		Validator.min(1).validate("a");
		Validator.min(1).validate("ab");
		Validator.min(1).validate(1);
		Validator.min(1).validate(2);
		Validator.min(1).validate(1d);
		Validator.min(1).validate(1.1d);
		Validator.min(1).validate(1L);
		Validator.min(1).validate(BigDecimal.valueOf(1));
		Validator.min(1).validate(BigInteger.valueOf(1));
		Validator.min(1).validate(Collections.singleton(1));
		Validator.min(1).validate(Collections.singletonMap("a", 1));
		Validator.min(1).validate(new String[] { "a" });

		TestUtils.expectedException(ValidationException.class, () -> Validator.min(1).validate(""));
		TestUtils.expectedException(ValidationException.class, () -> Validator.min(1).validate(0));
		TestUtils.expectedException(ValidationException.class, () -> Validator.min(2).validate(1.5d));
		TestUtils.expectedException(ValidationException.class, () -> Validator.min(1).validate(Collections.emptySet()));
		TestUtils.expectedException(ValidationException.class, () -> Validator.min(1).validate(Collections.emptyMap()));
		TestUtils.expectedException(ValidationException.class, () -> Validator.min(1).validate(new String[0]));

		TestUtils.expectedException(UnsupportedValidationTypeException.class,
				() -> Validator.min(1).validate(new Date()));
	}

	@Test
	public void testMax() {
		Validator.max(1).validate(null);
		Validator.max(2).validate("ab");
		Validator.max(1).validate(1);
		Validator.max(1).validate(0);
		Validator.max(1).validate(1d);
		Validator.max(5).validate(4.9d);
		Validator.max(3).validate(2L);
		Validator.max(2).validate(BigDecimal.valueOf(1));
		Validator.max(1).validate(BigInteger.valueOf(1));
		Validator.max(2).validate(Collections.singleton(1));
		Validator.max(1).validate(Collections.singletonMap("a", 1));
		Validator.max(3).validate(new String[] { "a", "b" });

		TestUtils.expectedException(ValidationException.class, () -> Validator.max(1).validate("ab"));
		TestUtils.expectedException(ValidationException.class, () -> Validator.max(1).validate(2));
		TestUtils.expectedException(ValidationException.class, () -> Validator.max(2).validate(2.1d));
		TestUtils.expectedException(ValidationException.class,
				() -> Validator.max(0).validate(Collections.singleton(1)));
		TestUtils.expectedException(ValidationException.class,
				() -> Validator.max(0).validate(Collections.singletonMap("a", 1)));
		TestUtils.expectedException(ValidationException.class,
				() -> Validator.max(1).validate(new String[] { "a", "b" }));

		TestUtils.expectedException(UnsupportedValidationTypeException.class,
				() -> Validator.max(1).validate(new Date()));
	}

	@Test
	public void testPattern() {
		Validator.pattern("a").validate(null);

		Validator.pattern("\\d+").validate("012");

		TestUtils.expectedException(ValidationException.class, () -> Validator.pattern("\\d+").validate("a1"));
	}

	@Test
	public void testIn() {
		Validator.in(1, 2).validate(2);

		TestUtils.expectedException(ValidationException.class, () -> Validator.in(1, 2).validate(null));
		TestUtils.expectedException(ValidationException.class, () -> Validator.in(1, 2).validate(3));
	}

	@Test
	public void testNotIn() {
		Validator.notIn(1, 2).validate(3);
		Validator.notIn(1, 2).validate(null);

		TestUtils.expectedException(ValidationException.class, () -> Validator.notIn(1, 2).validate(1));
		TestUtils.expectedException(ValidationException.class, () -> Validator.notIn(1, 2).validate(2));
	}

	@Test
	public void testNotNegative() {
		Validator.notNegative().validate(null);
		Validator.notNegative().validate(0);
		Validator.notNegative().validate(2);
		Validator.notNegative().validate(2.7d);
		Validator.notNegative().validate(BigDecimal.valueOf(0.1));

		TestUtils.expectedException(ValidationException.class, () -> Validator.notNegative().validate(-1));
		TestUtils.expectedException(ValidationException.class, () -> Validator.notNegative().validate(-0.67d));
	}

	@Test
	public void testDigits() {
		Validator.digits(3, 2).validate(123.56);
		Validator.digits(3, 2).validate(23.56);
		Validator.digits(3, 2).validate(1);
		Validator.digits(3, 2).validate(0);
		Validator.digits(3, 0).validate(200);

		TestUtils.expectedException(ValidationException.class, () -> Validator.digits(3, 2).validate(2.567d));
		TestUtils.expectedException(ValidationException.class, () -> Validator.digits(3, 2).validate(4000.6));
		TestUtils.expectedException(ValidationException.class, () -> Validator.digits(3, 2).validate(4000));
		TestUtils.expectedException(ValidationException.class, () -> Validator.digits(3, 0).validate(0.4));
	}

	@Test
	public void testEmail() {
		Validator.email().validate("test@mail.com");

		TestUtils.expectedException(ValidationException.class, () -> Validator.email().validate("xxx"));
	}

	@Test
	public void testLess() {
		Validator.lessThan(3).validate(2);
		TestUtils.expectedException(ValidationException.class, () -> Validator.lessThan(3).validate(3));
		TestUtils.expectedException(ValidationException.class, () -> Validator.lessThan(3).validate(4));

		Validator.lessOrEqual(3).validate(2);
		Validator.lessOrEqual(3).validate(3);
		TestUtils.expectedException(ValidationException.class, () -> Validator.lessOrEqual(3).validate(4));
	}

	@Test
	public void testGreater() {
		Validator.greaterThan(3).validate(4);
		TestUtils.expectedException(ValidationException.class, () -> Validator.greaterThan(3).validate(3));
		TestUtils.expectedException(ValidationException.class, () -> Validator.greaterThan(3).validate(2));

		Validator.greaterOrEqual(3).validate(4);
		Validator.greaterOrEqual(3).validate(3);
		TestUtils.expectedException(ValidationException.class, () -> Validator.greaterOrEqual(3).validate(2));
	}

	@Test
	public void testDate() {

		Calendar c = Calendar.getInstance();

		Calendar c1 = (Calendar) c.clone();
		c1.add(Calendar.DATE, -1);

		Validator.past(false).validate(c1.getTime());

		Calendar c2 = (Calendar) c.clone();
		c2.add(Calendar.HOUR_OF_DAY, -1);

		Validator.past(true).validate(c1.getTime());

		TestUtils.expectedException(ValidationException.class, () -> Validator.past(false).validate(c.getTime()));

		Calendar c3 = Calendar.getInstance();
		c3.add(Calendar.DATE, 1);
		Calendar c4 = Calendar.getInstance();
		c4.add(Calendar.HOUR_OF_DAY, 1);

		TestUtils.expectedException(ValidationException.class, () -> Validator.past(false).validate(c3.getTime()));
		TestUtils.expectedException(ValidationException.class, () -> Validator.past(true).validate(c4.getTime()));

	}

	@Test
	public void testValidatorDefinition() {
		Validator<?> v = Validator.notNull();
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isRequired());

		v = Validator.notEmpty();
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isRequired());

		v = Validator.notBlank();
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isRequired());

		v = Validator.min(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMin().intValue());
		assertFalse(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMin());

		v = Validator.max(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMax().intValue());
		assertFalse(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMax());

		v = Validator.greaterOrEqual(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMin().intValue());
		assertFalse(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMin());

		v = Validator.lessOrEqual(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMax().intValue());
		assertFalse(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMax());

		v = Validator.greaterThan(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMin().intValue());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMin());

		v = Validator.lessThan(1);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(1, ((BuiltinValidator<?>) v).getDescriptor().get().getMax().intValue());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMax());

		v = Validator.notNegative();
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(0, ((BuiltinValidator<?>) v).getDescriptor().get().getMin().intValue());
		assertFalse(((BuiltinValidator<?>) v).getDescriptor().get().isExclusiveMin());

		v = Validator.pattern("^[0-9]");
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals("^[0-9]", ((BuiltinValidator<?>) v).getDescriptor().get().getPattern());

		v = Validator.in(1, 2);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(2, ((BuiltinValidator<?>) v).getDescriptor().get().getIn().size());

		v = Validator.notIn(1, 2, 3);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(3, ((BuiltinValidator<?>) v).getDescriptor().get().getNotIn().size());

		v = Validator.digits(1, 2);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertEquals(Integer.valueOf(1), ((BuiltinValidator<?>) v).getDescriptor().get().getIntegerDigits());
		assertEquals(Integer.valueOf(2), ((BuiltinValidator<?>) v).getDescriptor().get().getFractionDigits());

		v = Validator.past(false);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isPast());

		v = Validator.future(false);
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isFuture());

		v = Validator.email();
		assertTrue(v instanceof BuiltinValidator);
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().isPresent());
		assertTrue(((BuiltinValidator<?>) v).getDescriptor().get().isEmail());
	}

}
