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
package com.holonplatform.core.test.data;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import com.holonplatform.core.beans.Config;
import com.holonplatform.core.beans.Converter;
import com.holonplatform.core.beans.Converter.BUILTIN;
import com.holonplatform.core.beans.Identifier;
import com.holonplatform.core.beans.Ignore;
import com.holonplatform.core.beans.Temporal;
import com.holonplatform.core.beans.ValidationMessage;
import com.holonplatform.core.beans.Validator;
import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.temporal.TemporalType;

public class TestBeanPropertyBean {

	@SuppressWarnings("serial")
	public static final class TestValidator implements com.holonplatform.core.Validator<Long> {

		@Override
		public void validate(Long value) throws com.holonplatform.core.Validator.ValidationException {
			if (value != 7L)
				throw new ValidationException("Must be 7");
		}

	}

	@Identifier
	@NotNull(message = "Name is required")
	@NotBlank(message = "Name is empty")
	@Caption("TheName")
	private String name;

	@NotEmpty
	@Caption("Text")
	private String text;

	@NotNull
	private String required;

	@Ignore
	private String ignore;

	@Email
	private String email;

	@Min(0)
	@Max(10)
	@ValidationMessage(message = "0-10 range", messageCode = "test-mc")
	private Integer intval;

	@Converter(builtin = BUILTIN.NUMERIC_BOOLEAN)
	private boolean numbool;

	@Converter(TestValueConverter.class)
	private TestEnum2 enm;

	@Converter(builtin = BUILTIN.ENUM_BY_ORDINAL)
	private TestEnum enmOrdinal;

	@Converter(builtin = BUILTIN.LOCALDATE)
	private LocalDate date;

	@Temporal(TemporalType.DATE_TIME)
	private Date legacyDate;

	@Validator(TestValidator.class)
	private long lng;

	@Config(key = "k1", value = "v1")
	@Config(key = "k2", value = "v2")
	@Positive
	private Integer notneg;

	@PositiveOrZero
	private Integer notnegzero;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIntval() {
		return intval;
	}

	public void setIntval(Integer intval) {
		this.intval = intval;
	}

	public boolean isNumbool() {
		return numbool;
	}

	public void setNumbool(boolean numbool) {
		this.numbool = numbool;
	}

	public TestEnum2 getEnm() {
		return enm;
	}

	public void setEnm(TestEnum2 enm) {
		this.enm = enm;
	}

	public TestEnum getEnmOrdinal() {
		return enmOrdinal;
	}

	public void setEnmOrdinal(TestEnum enmOrdinal) {
		this.enmOrdinal = enmOrdinal;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Date getLegacyDate() {
		return legacyDate;
	}

	public void setLegacyDate(Date legacyDate) {
		this.legacyDate = legacyDate;
	}

	public long getLng() {
		return lng;
	}

	public void setLng(long lng) {
		this.lng = lng;
	}

	public Integer getNotneg() {
		return notneg;
	}

	public void setNotneg(Integer notneg) {
		this.notneg = notneg;
	}

	public Integer getNotnegzero() {
		return notnegzero;
	}

	public void setNotnegzero(Integer notnegzero) {
		this.notnegzero = notnegzero;
	}

}
