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
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.temporal.TemporalType;

public class TestTemporalType {

	@Test
	public void testTemporalType() {

		TestUtils.checkEnum(TemporalType.class);

		assertFalse(TemporalType.getTemporalType((Temporal) null).isPresent());

		assertEquals(TemporalType.DATE, TemporalType.getTemporalType(LocalDate.now()).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(LocalDateTime.now()).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(OffsetDateTime.now()).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(ZonedDateTime.now()).orElse(null));
		assertEquals(TemporalType.TIME, TemporalType.getTemporalType(LocalTime.now()).orElse(null));
		assertEquals(TemporalType.TIME, TemporalType.getTemporalType(OffsetTime.now()).orElse(null));

		assertEquals(TemporalType.DATE, TemporalType.getTemporalType(LocalDate.class).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(LocalDateTime.class).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(OffsetDateTime.class).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(ZonedDateTime.class).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(Date.class).orElse(null));
		assertEquals(TemporalType.DATE_TIME, TemporalType.getTemporalType(Calendar.class).orElse(null));
		assertEquals(TemporalType.TIME, TemporalType.getTemporalType(LocalTime.class).orElse(null));
		assertEquals(TemporalType.TIME, TemporalType.getTemporalType(OffsetTime.class).orElse(null));

	}

}
