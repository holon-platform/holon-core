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
package com.holonplatform.core.examples;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.Localization;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.MessageProvider;
import com.holonplatform.core.i18n.NumberFormatFeature;
import com.holonplatform.core.i18n.TemporalFormat;
import com.holonplatform.core.temporal.TemporalType;

@SuppressWarnings("unused")
public class ExampleI18n {

	public void localizable() {
		// tag::localizable[]
		Localizable localizable = Localizable.builder().message("defaultMessage").messageCode("message.code").build(); // <1>

		localizable = Localizable.builder().message("message &").messageCode("message.code").messageArguments("test") // <2>
				.build();
		// end::localizable[]
	}

	public void contextbuild() {
		// tag::ctxbuild[]
		LocalizationContext localizationContext = LocalizationContext.builder()
				.messageProvider(MessageProvider.fromProperties("messages").build()) // <1>
				.messageProvider(MessageProvider.fromProperties("messages2").build()) // <2>
				.messageArgumentsPlaceholder("$") // <3>
				.withDefaultDateTemporalFormat(TemporalFormat.MEDIUM) // <4>
				.withDefaultTimeTemporalFormat(TemporalFormat.FULL) // <5>
				.withDefaultBooleanLocalization(Boolean.TRUE, Localizable.builder().messageCode("boolean.true").build()) // <6>
				.withDefaultBooleanLocalization(Boolean.FALSE,
						Localizable.builder().messageCode("boolean.false").build()) // <7>
				.withInitialSystemLocale() // <8>
				.withInitialLocale(Locale.US) // <9>
				.build();
		// end::ctxbuild[]
	}

	public void localize() {
		// tag::localize[]
		LocalizationContext localizationContext = LocalizationContext.getCurrent()
				.orElseThrow(() -> new IllegalStateException("Missing LocalizationContext")); // <1>

		localizationContext.localize(Locale.US); // <2>
		boolean localized = localizationContext.isLocalized(); // <3>

		localizationContext.localize(Localization.builder(Locale.JAPAN).defaultDecimalPositions(2)
				.defaultDateTemporalFormat(TemporalFormat.FULL).build()); // <4>
		// end::localize[]
	}

	public void localization() {
		// tag::localization[]
		LocalizationContext ctx = LocalizationContext.builder()
				.messageProvider(MessageProvider.fromProperties("messages").build()).withInitialLocale(Locale.US)
				.build();

		ctx.getLocale().ifPresent(l -> System.out.println(l)); // <1>

		String localizedMessage = ctx.getMessage("test.message", "defaultMessage"); // <2>
		localizedMessage = ctx
				.getMessage(Localizable.builder().message("defaultMessage").messageCode("test.message").build()); // <3>

		ctx.format(2.56); // <4>
		ctx.format(0.5, NumberFormatFeature.PERCENT_STYLE); // <5>
		ctx.format(5600.678, 2); // <6>

		NumberFormat nf = ctx.getNumberFormat(Integer.class); // <7>

		ctx.format(new Date(), TemporalType.DATE); // <8>
		ctx.format(new Date(), TemporalType.DATE_TIME, TemporalFormat.LONG, TemporalFormat.LONG); // <9>

		ctx.format(LocalDate.of(2017, Month.MARCH, 15)); // <10>
		ctx.format(LocalDateTime.of(2017, Month.MARCH, 15, 16, 48), TemporalFormat.FULL, TemporalFormat.SHORT); // <11>

		DateFormat df = ctx.getDateFormat(TemporalType.DATE); // <12>
		DateTimeFormatter dtf = ctx.getDateTimeFormatter(TemporalType.DATE_TIME); // <13>
		// end::localization[]
	}

}
