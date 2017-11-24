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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.Localization;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.MessageProvider;
import com.holonplatform.core.i18n.NumberFormatFeature;
import com.holonplatform.core.i18n.TemporalFormat;
import com.holonplatform.core.i18n.Localizable.LocalizationException;
import com.holonplatform.core.internal.i18n.DefaultLocalization;
import com.holonplatform.core.internal.i18n.DefaultLocalizationContext;
import com.holonplatform.core.internal.utils.TestUtils;
import com.holonplatform.core.temporal.TemporalType;

public class TestI18n {

	@Test
	public void testBase() {

		TestUtils.checkEnum(TemporalType.class);
		TestUtils.checkEnum(TemporalFormat.class);
		TestUtils.checkEnum(NumberFormatFeature.class);

		Localization lc = Localization.builder(Locale.ITALY).defaultDecimalPositions(2)
				.defaultDateTemporalFormat(TemporalFormat.MEDIUM).defaultTimeTemporalFormat(TemporalFormat.SHORT)
				.build();

		assertEquals(Locale.ITALY, lc.getLocale());
		assertEquals(new Integer(2), lc.getDefaultDecimalPositions().get());
		assertEquals(TemporalFormat.MEDIUM, lc.getDefaultDateTemporalFormat().get());
		assertEquals(TemporalFormat.SHORT, lc.getDefaultTimeTemporalFormat().get());

		DefaultLocalization l2 = new DefaultLocalization(Locale.US);

		Localization lp = Localization.builder(Locale.ITALY).parent(l2).build();
		assertEquals(l2, lp.getParent().get());

		assertNotEquals(lc, l2);
		assertNotEquals(lc, null);
		assertEquals(lc, lc);
	}

	@Test
	public void testContext() throws Exception {

		final LocalizationContext ctx = LocalizationContext.builder().build();
		assertFalse(ctx.isLocalized());
		assertFalse(ctx.getLocale().isPresent());

		TestUtils.expectedException(LocalizationException.class, new Runnable() {

			@Override
			public void run() {
				ctx.format(1);
			}
		});
		TestUtils.expectedException(LocalizationException.class, new Runnable() {

			@Override
			public void run() {
				ctx.format(new Date(), TemporalType.DATE);
			}
		});
		TestUtils.expectedException(LocalizationException.class, new Runnable() {

			@Override
			public void run() {
				ctx.format(LocalDate.now());
			}
		});
		TestUtils.expectedException(LocalizationException.class, new Runnable() {

			@Override
			public void run() {
				ctx.getMessage("xxx", null);
			}
		});

		LocalizationContext ctx2 = LocalizationContext.builder().withInitialLocale(Locale.US).build();
		assertTrue(ctx2.isLocalized());
		assertEquals(Locale.US, ctx2.getLocale().get());

		ctx2 = LocalizationContext.builder().withInitialLocale(Locale.FRANCE).build();
		assertTrue(ctx2.isLocalized());

		ctx2 = LocalizationContext.builder().build();
		assertFalse(ctx2.isLocalized());

		ctx2.localize((Locale) null);
		assertFalse(ctx2.isLocalized());
		ctx2.localize((Localization) null);
		assertFalse(ctx2.isLocalized());

		Localization lc = Localization.builder(Locale.ITALIAN).build();
		ctx2.localize(lc);
		assertTrue(ctx2.isLocalized());

		final DefaultLocalizationContext ctx3 = new DefaultLocalizationContext(Locale.US);
		ctx3.setUseDateTimeFormatsCache(false);
		ctx3.setMessageArgumentsPlaceholder("*");

		TestUtils.expectedException(IllegalArgumentException.class, new Runnable() {

			@Override
			public void run() {
				ctx3.localize(new DefaultLocalization(null));
			}
		});

		Context.get().threadScope().map((s) -> s.put(LocalizationContext.CONTEXT_KEY, ctx));

		assertTrue(LocalizationContext.getCurrent().isPresent());
		assertEquals(ctx, LocalizationContext.getCurrent().get());

		Context.get().threadScope().map((s) -> s.remove(LocalizationContext.CONTEXT_KEY));

		assertFalse(LocalizationContext.getCurrent().isPresent());

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY, ctx, () -> {
			assertTrue(LocalizationContext.getCurrent().isPresent());
			assertEquals(ctx, LocalizationContext.getCurrent().get());
		});

		assertFalse(LocalizationContext.getCurrent().isPresent());

		LocalizationContext ctx4 = LocalizationContext.builder().withInitialLocale(Locale.US).build();

		Locale lcl = Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY, ctx4,
				() -> LocalizationContext.getCurrent().flatMap(l -> l.getLocale()).orElse(null));
		assertEquals(Locale.US, lcl);

	}

	@Test
	public void testContextNumbers() {

		assertFalse(NumberFormatFeature.hasFeature(null, new NumberFormatFeature[0]));
		assertFalse(NumberFormatFeature.hasFeature(NumberFormatFeature.DISABLE_GROUPING, null));
		assertTrue(NumberFormatFeature.hasFeature(NumberFormatFeature.DISABLE_GROUPING,
				new NumberFormatFeature[] { NumberFormatFeature.DISABLE_GROUPING, NumberFormatFeature.PERCENT_STYLE }));

		LocalizationContext ctx = LocalizationContext.builder().withInitialLocale(Locale.ITALIAN).build();

		String fv = ctx.format(235000.656d, 2);
		assertEquals("235.000,66", fv);

		fv = ctx.format(235000);
		assertEquals("235.000", fv);

		fv = ctx.format(235000, NumberFormatFeature.DISABLE_GROUPING);
		assertEquals("235000", fv);

		fv = ctx.format(235000.000d, NumberFormatFeature.DISABLE_GROUPING,
				NumberFormatFeature.HIDE_DECIMALS_WHEN_ALL_ZERO);
		assertEquals("235000", fv);

		fv = ctx.format(0.34, NumberFormatFeature.PERCENT_STYLE);
		assertEquals("34%", fv);
	}

	@Test
	public void testDateFormats() {

		Calendar c = Calendar.getInstance(Locale.ITALIAN);
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.YEAR, 1979);
		c.set(Calendar.HOUR_OF_DAY, 18);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 15);
		c.set(Calendar.MILLISECOND, 0);

		final Date date = c.getTime();

		LocalizationContext ctx = LocalizationContext.builder().build();
		ctx.localize(Locale.ITALIAN);

		assertEquals("09/03/79", ctx.format(date, TemporalType.DATE));
		assertEquals("09/03/79", ctx.format(date, TemporalType.DATE, TemporalFormat.SHORT, null));
		assertEquals("9-mar-1979", ctx.format(date, TemporalType.DATE, TemporalFormat.MEDIUM, null));
		assertEquals("9 marzo 1979", ctx.format(date, TemporalType.DATE, TemporalFormat.LONG, null));
		assertEquals("venerdì 9 marzo 1979", ctx.format(date, TemporalType.DATE, TemporalFormat.FULL, null));

		ctx.localize(Localization.builder(Locale.ITALY).defaultDateTemporalFormat(TemporalFormat.MEDIUM).build());

		assertEquals("9-mar-1979", ctx.format(date, TemporalType.DATE));
		assertEquals("9-mar-1979", ctx.format(date, TemporalType.DATE, TemporalFormat.DEFAULT, null));

		assertEquals("18.30", ctx.format(date, TemporalType.TIME));
		assertEquals("18.30.15", ctx.format(date, TemporalType.TIME, null, TemporalFormat.MEDIUM));
		assertEquals("18.30.15 CET", ctx.format(date, TemporalType.TIME, null, TemporalFormat.LONG));
		assertEquals("18.30.15 CET", ctx.format(date, TemporalType.TIME, null, TemporalFormat.FULL));

		assertEquals("09/03/79 18.30", ctx.format(date, TemporalType.DATE_TIME, TemporalFormat.SHORT, null));
		assertEquals("9-mar-1979 18.30.15",
				ctx.format(date, TemporalType.DATE_TIME, TemporalFormat.MEDIUM, TemporalFormat.MEDIUM));
		assertEquals("9 marzo 1979 18.30.15",
				ctx.format(date, TemporalType.DATE_TIME, TemporalFormat.LONG, TemporalFormat.MEDIUM));
		assertEquals("9 marzo 1979 18.30.15 CET",
				ctx.format(date, TemporalType.DATE_TIME, TemporalFormat.LONG, TemporalFormat.LONG));
		assertEquals("venerdì 9 marzo 1979 18.30.15",
				ctx.format(date, TemporalType.DATE_TIME, TemporalFormat.FULL, TemporalFormat.MEDIUM));
	}

	@Test
	public void testTemporalFormats() {

		LocalizationContext ctx = LocalizationContext.builder().build();
		ctx.localize(Locale.ITALIAN);

		LocalDate date = LocalDate.of(1979, Month.MARCH, 9);
		LocalTime time = LocalTime.of(18, 30, 15);
		LocalDateTime dt = LocalDateTime.of(1979, Month.MARCH, 9, 18, 30, 15);

		assertEquals("09/03/79", ctx.format(date));
		assertEquals("09/03/79", ctx.format(date, TemporalFormat.SHORT, null));
		assertEquals("9-mar-1979", ctx.format(date, TemporalFormat.MEDIUM, null));
		assertEquals("9 marzo 1979", ctx.format(date, TemporalFormat.LONG, null));
		assertEquals("venerdì 9 marzo 1979", ctx.format(date, TemporalFormat.FULL, null));

		assertEquals("18.30", ctx.format(time));
		assertEquals("18.30.15", ctx.format(time, null, TemporalFormat.MEDIUM));
		assertEquals("18.30.15", ctx.format(time, null, TemporalFormat.LONG));
		assertEquals("18.30.15", ctx.format(time, null, TemporalFormat.FULL));

		assertEquals("09/03/79 18.30", ctx.format(dt));
		assertEquals("09/03/79 18.30", ctx.format(dt, TemporalFormat.SHORT, null));
		assertEquals("9-mar-1979 18.30.15", ctx.format(dt, TemporalFormat.MEDIUM, TemporalFormat.MEDIUM));
		assertEquals("9 marzo 1979 18.30.15", ctx.format(dt, TemporalFormat.LONG, TemporalFormat.MEDIUM));
		assertEquals("9 marzo 1979 18.30.15", ctx.format(dt, TemporalFormat.LONG, TemporalFormat.LONG));
		assertEquals("venerdì 9 marzo 1979 18.30.15", ctx.format(dt, TemporalFormat.FULL, TemporalFormat.MEDIUM));

		ctx.localize(Localization.builder(Locale.ITALY).defaultDateTemporalFormat(TemporalFormat.MEDIUM).build());

		assertEquals("9-mar-1979", ctx.format(date));
		assertEquals("9-mar-1979", ctx.format(date, TemporalFormat.DEFAULT, null));

		ctx.localize(Localization.builder(Locale.ITALY).defaultDateTemporalFormat(TemporalFormat.SHORT)
				.defaultTimeTemporalFormat(TemporalFormat.MEDIUM).build());

		assertEquals("18.30.15", ctx.format(time));

	}

	@Test
	public void testMessages() {

		final MessageProvider mp = new MessageProvider() {

			@Override
			public Optional<String> getMessage(Locale locale, String code) throws LocalizationException {
				if ("test".equals(code)) {
					if ("en".equals(locale.getLanguage())) {
						return Optional.of("testEN");
					}
					if ("it".equals(locale.getLanguage())) {
						return ("var".equals(locale.getVariant())) ? Optional.of("testVAR") : Optional.of("testIT");
					}
				} else if ("testarg".equals(code)) {
					return Optional.of("arg is &");
				} else if ("test2".equals(code) && !"var".equals(locale.getVariant())) {
					if ("it".equals(locale.getLanguage())) {
						return Optional.of("t2");
					}
				}
				return Optional.empty();
			}
		};

		DefaultLocalizationContext ctx = new DefaultLocalizationContext();
		ctx.localize(Locale.US);

		String m = ctx.getMessage("notex", null);
		assertNull(m);

		ctx.addMessageProvider(mp);

		m = ctx.getMessage("notex", null);
		assertNull(m);
		m = ctx.getMessage("notex", "dft");
		assertEquals("dft", m);

		m = ctx.getMessage("test", "dft");
		assertEquals("testEN", m);

		ctx.localize(Locale.ITALIAN);
		m = ctx.getMessage("test", "dft");
		assertEquals("testIT", m);

		m = ctx.getMessage("testarg", "dft", "ARG");
		assertEquals("arg is ARG", m);

		final MessageProvider mp2 = new MessageProvider() {

			@Override
			public Optional<String> getMessage(Locale locale, String code) throws LocalizationException {
				if ("testpar".equals(code)) {
					return Optional.of("resolved");
				}
				return Optional.empty();
			}
		};

		ctx.addMessageProvider(mp2);

		m = ctx.getMessage("testpar", "dft");
		assertEquals("resolved", m);

		DefaultLocalization l1 = new DefaultLocalization(new Locale("it", "IT"));
		DefaultLocalization l2 = new DefaultLocalization(new Locale("it", "IT", "var"));
		l2.setParent(l1);

		ctx.localize(l2);

		m = ctx.getMessage("test", "dft");
		assertEquals("testVAR", m);

		m = ctx.getMessage("test2", "dft");
		assertEquals("t2", m);

		Localizable cp = Localizable.builder().message("dft").messageCode("test2").messageArguments("A", 3).build();

		assertEquals("dft", cp.getMessage());
		assertEquals("test2", cp.getMessageCode());
		assertTrue(Arrays.equals(new Object[] { "A", 3 }, cp.getMessageArguments()));

		assertEquals("t2", ctx.getMessage(cp));

		cp = Localizable.builder().message("dft").build();

		assertEquals("dft", ctx.getMessage(cp));

		cp = Localizable.builder().message("dft").messageCode("xxx").build();

		assertEquals("dft", ctx.getMessage(cp));

		final Localizable msg = Localizable.builder().message("dft").messageCode("test-mc").build();

		TestUtils.expectedException(LocalizationException.class, new Runnable() {

			@Override
			public void run() {
				LocalizationContext.builder().build().getMessage(msg, false);
			}
		});

		assertEquals("dft", LocalizationContext.builder().build().getMessage(msg, true));

	}
	
	@Test
	public void testMissingMessages() {
		
		final AtomicInteger counter = new AtomicInteger();
		
		final LocalizationContext ctx = LocalizationContext.builder().withInitialLocale(Locale.US)
				.messageProvider((locale, code) -> {
					if ("test".equals(code)) {
						return Optional.of("testCode");
					}
					return Optional.empty();
				}).withMissingMessageLocalizationListener((locale, code, dft) -> {
					counter.incrementAndGet();
					assertEquals(Locale.US, locale);
					assertEquals("theDefault", dft);
				})
				.build();
		
		String msg = ctx.getMessage("test", "theDefault");
		
		assertEquals("testCode", msg);
		assertEquals(0, counter.get());
		
		msg = ctx.getMessage("xxx", "theDefault");
		
		assertEquals("theDefault", msg);
		assertEquals(1, counter.get());
		
		msg = ctx.getMessage("xxx2", "theDefault");
		
		assertEquals("theDefault", msg);
		assertEquals(2, counter.get());
	}

	@Test
	public void testProperties() {
		MessageProvider mp = MessageProvider.fromProperties().basename("messages/messages").build();

		Optional<String> v = mp.getMessage(Locale.ENGLISH, "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test_en", v.get());
		v = mp.getMessage(Locale.US, "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test_en_US", v.get());
		v = mp.getMessage(new Locale("en", "US", "var"), "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test_en_US_var", v.get());
		v = mp.getMessage(new Locale("en", "US", "x"), "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test_en_US", v.get());
		v = mp.getMessage(new Locale("fr"), "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test", v.get());
		v = mp.getMessage(Locale.ITALIAN, "test.msg");
		assertTrue(v.isPresent());
		assertEquals("Test_it", v.get());
	}

	@Test
	public void testBuilder() {

		LocalizationContext ctx = LocalizationContext.builder().withInitialLocale(Locale.FRANCE)
				.disableDateTimeFormatsCache()
				.messageProvider(MessageProvider.fromProperties("messages/messages").build()).build();

		assertTrue(ctx.getLocale().isPresent());
		assertEquals(Locale.FRANCE, ctx.getLocale().get());

		String v = ctx.getMessage("test.msg", "dft");
		assertEquals("Test", v);

		ctx = LocalizationContext.builder().withInitialSystemLocale().messageArgumentsPlaceholder("*")
				.messageProvider(MessageProvider.fromProperties("messages/messages").build()).build();

		assertTrue(ctx.getLocale().isPresent());
		assertEquals(Locale.getDefault(), ctx.getLocale().get());

	}

}
