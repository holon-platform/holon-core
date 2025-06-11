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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.holonplatform.core.Context;
import com.holonplatform.core.Path;
import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.config.ConfigProperty;
import com.holonplatform.core.i18n.Localizable;
import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.internal.beans.DefaultBeanIntrospector;
import com.holonplatform.core.internal.query.filter.OperationQueryFilter;
import com.holonplatform.core.internal.query.filter.OperationQueryFilter.FilterOperator;
import com.holonplatform.core.internal.utils.TypeUtils;
import com.holonplatform.core.objects.EqualsHandler;
import com.holonplatform.core.objects.HashCodeProvider;
import com.holonplatform.core.presentation.StringValuePresenter;
import com.holonplatform.core.property.BooleanProperty;
import com.holonplatform.core.property.NumericProperty;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.property.Property;
import com.holonplatform.core.property.Property.PropertyNotFoundException;
import com.holonplatform.core.property.Property.PropertyReadException;
import com.holonplatform.core.property.Property.PropertyReadOnlyException;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertyBoxProperty;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.property.PropertyValueConverter;
import com.holonplatform.core.property.PropertyValuePresenterRegistry;
import com.holonplatform.core.property.PropertyValueProvider;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.property.TemporalProperty;
import com.holonplatform.core.property.VirtualProperty;
import com.holonplatform.core.property.VirtualProperty.VirtualPropertyBuilder;
import com.holonplatform.core.query.QueryFilter;
import com.holonplatform.core.query.QuerySort;
import com.holonplatform.core.query.QuerySort.PathQuerySort;
import com.holonplatform.core.query.QuerySort.SortDirection;
import com.holonplatform.core.temporal.TemporalType;
import com.holonplatform.core.test.data.TestBean;
import com.holonplatform.core.test.data.TestBean2;
import com.holonplatform.core.test.data.TestBean3;
import com.holonplatform.core.test.data.TestIdentifiablePropertySet;
import com.holonplatform.core.test.data.TestNested;
import com.holonplatform.core.test.data.TestPropertySet;

/**
 * Property architecture test
 */
@SuppressWarnings("rawtypes")
public class TestProperty {

	final static Property<String> P1 = PathProperty.create("test1", String.class);
	final static Property<Integer> P2 = PathProperty.create("test2", Integer.class);
	final static Property<Boolean> P3 = PathProperty.create("test3", boolean.class)
			.converter(PropertyValueConverter.numericBoolean(Long.class));
	final static Property<Double> P4 = PathProperty.create("test4", Double.class)
			.withConfiguration(StringValuePresenter.DECIMAL_POSITIONS, 2);
	final static Property<TestEnum> P5 = PathProperty.create("test5", TestEnum.class);
	final static Property<TestEnum2> P6 = PathProperty.create("test6", TestEnum2.class);
	final static PathProperty<Date> P7 = PathProperty.create("test7", Date.class).temporalType(TemporalType.DATE_TIME);
	final static Property<LocalDate> P8 = PathProperty.create("test8", LocalDate.class);
	final static Property<LocalTime> P9 = PathProperty.create("test9", LocalTime.class);
	final static Property<LocalDateTime> P10 = PathProperty.create("test10", LocalDateTime.class);
	final static Property<String[]> P11 = PathProperty.create("test11", String[].class);
	final static Property<Double[]> P12 = PathProperty.create("test12", Double[].class);
	final static Property<String> P13 = VirtualProperty.create(String.class, p -> "VRT:" + p.getValue(P1));
	final static Property<TestCaptionable> P14 = PathProperty.create("test14", TestCaptionable.class);
	final static Property<Integer> P15 = PathProperty.create("test2", Integer.class)
			.withConfiguration(StringValuePresenter.DISABLE_GROUPING, true);

	@Test
	public void testPaths() {
		final PathProperty<String> PARENT1 = PathProperty.create("parent1", String.class);

		final PathProperty<Integer> PRP = PathProperty.create("prp", Integer.class).parent(PARENT1);

		assertEquals("parent1.prp", PRP.fullName());

		final PathProperty<String> PARENT2 = PathProperty.create("parent2", String.class).parent(PARENT1);

		final PathProperty<Integer> PRP2 = PathProperty.create("prp2", Integer.class).parent(PARENT2);

		assertEquals("parent1.parent2.prp2", PRP2.fullName());

	}

	@Test
	public void testVirtualProperty() {

		Property<String> gp = VirtualProperty.create(String.class);
		assertEquals(String.class, gp.getType());

		VirtualPropertyBuilder<String> vp = VirtualProperty.create(String.class).message("Test caption")
				.messageCode("test.message");
		assertEquals(String.class, vp.getType());
		assertEquals("Test caption", ((Localizable) vp).getMessage());
		assertEquals("test.message", ((Localizable) vp).getMessageCode());

		PropertyValueProvider<String> valueProvider = p -> "PROVIDED VALUE";

		vp.valueProvider(valueProvider);

		assertNotNull(vp.getValueProvider());

		VirtualProperty<String> vp2 = VirtualProperty.create(String.class).message(vp);

		assertEquals("Test caption", vp2.getMessage());
		assertEquals("test.message", vp2.getMessageCode());

		VirtualProperty vp3 = VirtualProperty.create(String.class).valueProvider(pb -> "TEST").name("virtualName");
		assertEquals("virtualName", vp3.getName());
	}

	@Test
	public void testPropertyEqualsHashCode() {

		PathProperty<String> p1 = PathProperty.create("p1", String.class);
		PathProperty<String> p2 = PathProperty.create("p1", String.class);

		assertFalse(p1.equals(p2));
		assertNotEquals(p1.hashCode(), p2.hashCode());

		EqualsHandler<PathProperty<?>> eh = (p, o) -> {
			if (p != null && o != null) {
				if (o instanceof PathProperty) {
					return p.getName().equals(((PathProperty<?>) o).getName());
				}
			}
			return false;
		};

		HashCodeProvider<PathProperty<?>> hcp = p -> {
			return Optional.of(p.getName().hashCode());
		};

		p1 = PathProperty.create("p1", String.class).equalsHandler(eh).hashCodeProvider(hcp);
		p2 = PathProperty.create("p1", String.class).equalsHandler(eh).hashCodeProvider(hcp);

		assertTrue(p1.equals(p2));
		assertEquals(p1.hashCode(), p2.hashCode());

		PropertySet<?> set1 = PropertySet.builderOf(p1, p2).build();
		assertEquals(1, set1.size());

		PropertySet<?> set2 = PropertySet.of(p1, p2);
		assertEquals(1, set2.size());

		PropertySet<?> set3 = PropertySet.builder().add(p1).add(p2).build();
		assertEquals(1, set3.size());

	}

	@Test
	public void testTemporalType() {
		assertEquals(TemporalType.DATE_TIME, P7.getTemporalType().orElse(null));
	}

	@Test
	public void testPropertyValuePresenter() {

		assertEquals("str", P1.present("str"));

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY)
						.withDefaultBooleanLocalization(Boolean.TRUE, Localizable.builder().message("isTrue").build())
						.withDefaultBooleanLocalization(Boolean.FALSE, Localizable.builder().message("isFalse").build())
						.build(),
				() -> {
					assertEquals("7", P2.present(7));
					assertEquals("1.300", P2.present(1300));

					assertEquals("3.500,00", P4.present(3500d));
					assertEquals("3.500,42", P4.present(3500.42d));
					assertEquals("3.500,01", P4.present(3500.007d));

					assertEquals("isTrue", P3.present(Boolean.TRUE));
					assertEquals("isFalse", P3.present(Boolean.FALSE));
					assertEquals("isFalse", P3.present(null));
				});

		assertEquals("B", P5.present(TestEnum.B));

		assertEquals("valueA", P6.present(TestEnum2.A));

		final Calendar c = Calendar.getInstance(Locale.ITALIAN);
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.YEAR, 1979);
		c.set(Calendar.HOUR_OF_DAY, 18);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 15);
		c.set(Calendar.MILLISECOND, 0);

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY).build(), () -> {

					assertEquals("09/03/79, 18:30", P7.present(c.getTime()));

					final LocalDate date = LocalDate.of(1979, Month.MARCH, 9);
					assertEquals("09/03/79", P8.present(date));

					final LocalTime time = LocalTime.of(18, 30, 15);
					assertEquals("18:30", P9.present(time));

					final LocalDateTime dt = LocalDateTime.of(1979, Month.MARCH, 9, 18, 30, 15);
					assertEquals("09/03/79, 18:30", P10.present(dt));

				});

		String[] sa = new String[] { "a", "b", "c" };

		assertEquals("a;b;c", P11.present(sa));

		assertEquals("TestCaptionableCaption", P14.present(new TestCaptionable()));

		PropertyBox box = PropertyBox.builder(P1, P2, P13).set(P1, "p1").set(P2, 2).build();

		assertEquals("VRT:p1", P13.present(box.getValue(P13)));

		assertEquals("1300", P15.present(1300));

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.US).build(), () -> {
					assertEquals("35;57.8", P12.present(new Double[] { 35d, 57.8d }));
				});

		Context.get().executeThreadBound(LocalizationContext.CONTEXT_KEY,
				LocalizationContext.builder().withInitialLocale(Locale.ITALY).build(), () -> {
					assertEquals("35;57,8", P12.present(new Double[] { 35d, 57.8d }));
				});

		assertEquals("p1", box.present(P1));
	}

	@Test
	public void testPropertyValuePresenterRegistry() {

		PropertyValuePresenterRegistry registry = PropertyValuePresenterRegistry.create(true);

		registry.register(p -> p.getConfiguration().hasNotNullParameter("testpar"), (p, v) -> "TEST_PRS");

		final PathProperty<Integer> prp = PathProperty.create("test", Integer.class).withConfiguration("testpar", "x");

		assertEquals("1", prp.present(1));

		assertEquals("TEST_PRS", Context.get().executeThreadBound(PropertyValuePresenterRegistry.CONTEXT_KEY, registry,
				() -> prp.present(1)));
	}

	@Test
	public void testPropertyMessages() {
		PathProperty<String> qp = PathProperty.create("test", String.class);
		assertEquals(String.class, qp.getType());
		assertEquals("test", qp.getName());

		qp = PathProperty.create("test", String.class).message("Test caption").messageCode("test.message");
		assertEquals(String.class, qp.getType());
		assertEquals("test", qp.getName());

		assertEquals("Test caption", qp.getMessage());
		assertEquals("test.message", qp.getMessageCode());
	}

	@SuppressWarnings("serial")
	@Test
	public void testPropertyBox() {
		TestBean test = new TestBean();
		test.setName("Test");
		test.setSequence(1);

		PropertyBox box = BeanIntrospector.get()
				.read(PropertyBox.builder(TestPropertySet.NAME, TestPropertySet.SEQUENCE).build(), test);

		Object value = box.getValue(TestPropertySet.NAME);
		assertEquals("Test", value);
		assertEquals(TestPropertySet.NAME.getType(), value.getClass());

		Integer sv = box.getValue(TestPropertySet.SEQUENCE);
		assertEquals(Integer.valueOf(1), sv);
		assertEquals(TestPropertySet.SEQUENCE.getType(), sv.getClass());

		PropertyBox pb = PropertyBox
				.builder(TestPropertySet.NAME, TestPropertySet.SEQUENCE, TestPropertySet.NESTED_DATE).build();

		assertTrue(pb.contains(TestPropertySet.NAME));

		pb.setValue(TestPropertySet.NAME, "test");
		assertTrue(pb.containsValue(TestPropertySet.NAME));
		assertFalse(pb.containsValue(TestPropertySet.SEQUENCE));
		assertFalse(pb.containsValue(TestPropertySet.NESTED_DATE));
		assertEquals("test", pb.getValue(TestPropertySet.NAME));

		assertThrows(IllegalArgumentException.class, () -> pb.containsValue(null));

		assertThrows(IllegalArgumentException.class, () -> pb.getValue(null));

		final PathProperty<Integer> cp = PathProperty.create("testConv", Integer.class)
				.converter(new PropertyValueConverter<Integer, String>() {

					@Override
					public Integer fromModel(String value, Property<Integer> property)
							throws PropertyConversionException {
						if (value != null) {
							return Integer.parseInt(value);
						}
						return null;
					}

					@Override
					public String toModel(Integer value, Property<Integer> property)
							throws PropertyConversionException {
						if (value != null) {
							return value.toString();
						}
						return null;
					}

					@Override
					public Class<Integer> getPropertyType() {
						return Integer.class;
					}

					@Override
					public Class<String> getModelType() {
						return String.class;
					}
				});

		final VirtualProperty<String> vp = VirtualProperty.create(String.class)
				.valueProvider(p -> "PROVIDED:" + p.getValue(TestPropertySet.NAME));

		final VirtualProperty<String> evp = VirtualProperty.create(String.class).valueProvider(p -> {
			throw new PropertyReadException(null, "test error");
		});

		final PropertyBox pb2 = PropertyBox.builder(TestPropertySet.NAME, cp, vp, evp).set(TestPropertySet.NAME, "test")
				.set(cp, Integer.valueOf(1)).build();

		testSetValueUsingConverter(pb2, cp, "1");

		assertEquals("test", pb2.getValue(TestPropertySet.NAME));
		assertEquals(Integer.valueOf(1), pb2.getValue(cp));
		assertEquals("1", cp.getConvertedValue(pb2.getValue(cp)));
		assertEquals("PROVIDED:test", pb2.getValue(vp));
		assertTrue(pb2.containsValue(TestPropertySet.NAME));
		assertTrue(pb2.containsValue(vp));

		assertNotNull(pb2.toString());

		pb2.getValue(vp);

		assertThrows(IllegalArgumentException.class, () -> pb2.setValue(null, ""));
		assertThrows(PropertyNotFoundException.class, () -> pb2.setValue(TestPropertySet.NESTED_ID, Long.valueOf(1L)));

		Property<String> vrtChain = VirtualProperty.create(String.class).valueProvider(b -> chain(b));

		PropertyBox pb3 = PropertyBox
				.builder(TestPropertySet.NAME, TestPropertySet.SEQUENCE, TestPropertySet.NESTED_DATE, vrtChain).build();

		pb3.setValue(TestPropertySet.NAME, "test");

		String vv = pb3.getValue(vrtChain);
		assertNotNull(vv);
		assertEquals("test-1", vv);

		pb3.setValue(TestPropertySet.SEQUENCE, Integer.valueOf(3));
		vv = pb3.getValue(vrtChain);
		assertNotNull(vv);
		assertEquals("test 3", vv);

		PropertyBox pb4 = PropertyBox.builder(TestPropertySet.PROPERTIES).set(TestPropertySet.NAME, "test")
				.set(TestPropertySet.SEQUENCE, Integer.valueOf(1)).set(TestPropertySet.GENERIC, Double.valueOf(3d))
				.set(TestPropertySet.NESTED_ID, 5L).build();

		TestBean<Double> tb = BeanIntrospector.get().write(pb4, new TestBean<>());

		assertEquals("test", tb.getName());
		assertEquals(1, tb.getSequence());
		assertEquals(Double.valueOf(3d), tb.getGeneric());
		assertEquals(5L, tb.getNested().getNestedId());

		PropertyBox cloned = pb4.cloneBox();

		assertNotNull(cloned);
		assertEquals("test", cloned.getValue(TestPropertySet.NAME));
		assertEquals(Integer.valueOf(1), cloned.getValue(TestPropertySet.SEQUENCE));
		assertEquals(Double.valueOf(3d), cloned.getValue(TestPropertySet.GENERIC));
		assertEquals(Long.valueOf(5), cloned.getValue(TestPropertySet.NESTED_ID));

		cloned = pb4.cloneBox(PropertySet.of(TestPropertySet.NAME));
		assertNotNull(cloned);
		assertEquals("test", cloned.getValue(TestPropertySet.NAME));
		assertFalse(cloned.containsValue(TestPropertySet.SEQUENCE));

		cloned = pb4.cloneBox(TestPropertySet.NAME, TestPropertySet.SEQUENCE);
		assertNotNull(cloned);
		assertEquals("test", cloned.getValue(TestPropertySet.NAME));
		assertTrue(cloned.containsValue(TestPropertySet.SEQUENCE));

		PropertyBox pb5 = PropertyBox.builder(TestPropertySet.NAME).set(TestPropertySet.NAME, "tn").build();
		assertNotNull(pb5);
		assertEquals("tn", pb5.getValue(TestPropertySet.NAME));

		pb5 = PropertyBox.builder(TestPropertySet.PROPERTIES).set(TestPropertySet.NAME, "tn")
				.set(TestPropertySet.SEQUENCE, 7).build();
		assertNotNull(pb5);
		assertEquals("tn", pb5.getValue(TestPropertySet.NAME));
		assertEquals(Integer.valueOf(7), pb5.getValue(TestPropertySet.SEQUENCE));

		final PropertyBox pbro = PropertyBox.builder(TestPropertySet.PROPERTIES).build();

		assertThrows(PropertyReadOnlyException.class, () -> pbro.setValue(TestPropertySet.VIRTUAL, "readonly"));

		PropertyBox pb6 = PropertyBox.create(TestPropertySet.PROPERTIES);
		pb6.setValue(TestPropertySet.NAME, "theName");

		long cnt = pb6.propertyValues().filter(pv -> TestPropertySet.NAME.equals(pv.getProperty()))
				.filter(pv -> "theName".equals(pv.getValue())).count();
		assertEquals(1, cnt);

		cnt = pb6.propertyValues().filter(pv -> TestPropertySet.SEQUENCE.equals(pv.getProperty()))
				.filter(pv -> pv.hasValue()).count();

		assertEquals(0, cnt);

	}

	private static String chain(PropertyBox propertyBox) {
		StringBuilder sb = new StringBuilder();
		sb.append(propertyBox.getValueIfPresent(TestPropertySet.NAME).orElse(""));
		Optional<Integer> sequence = propertyBox.getValueIfPresent(TestPropertySet.SEQUENCE);
		if (sequence.isPresent() && sb.length() > 0)
			sb.append(" ");
		sb.append(sequence.orElse(Integer.valueOf(-1)));
		return sb.length() > 0 ? sb.toString() : null;
	}

	@SuppressWarnings("unchecked")
	private static void testSetValueUsingConverter(PropertyBox box, Property property, Object value) {
		box.setValue(property, value);
	}

	@Test
	public void testBeanProperty() {

		BeanIntrospector.get().clearCache();
		int cs = ((DefaultBeanIntrospector) BeanIntrospector.get()).getCacheSize();
		assertEquals(0, cs);

		assertThrows(IllegalArgumentException.class, () -> BeanIntrospector.get().getPropertySet(null));

		BeanPropertySet<TestBean> beanPropertySet = BeanIntrospector.get().getPropertySet(TestBean.class);

		beanPropertySet = BeanIntrospector.get().getPropertySet(TestBean.class);

		cs = ((DefaultBeanIntrospector) BeanIntrospector.get()).getCacheSize();
		assertEquals(1, cs);

		beanPropertySet = BeanIntrospector.get().getPropertySet(TestBean.class);

		cs = ((DefaultBeanIntrospector) BeanIntrospector.get()).getCacheSize();
		assertEquals(1, cs);

		assertFalse(beanPropertySet.getProperty("none").isPresent());

		assertTrue(beanPropertySet.getProperty("name").isPresent());
		assertTrue(beanPropertySet.getProperty("sequence").isPresent());
		assertTrue(beanPropertySet.getProperty("superText").isPresent());
		assertTrue(beanPropertySet.getProperty("nested").isPresent());
		assertTrue(beanPropertySet.getProperty("nested.nestedId").isPresent());
		assertTrue(beanPropertySet.getProperty("nested.nestedDate").isPresent());

		assertEquals(String.class, beanPropertySet.getProperty("name").get().getType());

		Optional<PathProperty<String>> nbp = beanPropertySet.getProperty("name");
		assertTrue(nbp.isPresent());

		assertEquals(String.class, nbp.get().getType());
		assertEquals("name", nbp.get().getName());

		assertEquals("nameCaption", beanPropertySet.getProperty("name").get().getMessage());
		assertEquals("nameCaptionMessageCode", beanPropertySet.getProperty("name").get().getMessageCode());

		// methods

		Optional<PathProperty<String>> np = beanPropertySet.getProperty("name");
		assertTrue(np.isPresent());
		assertEquals("name", np.get().getName());

		np.get().toString();

		Optional<PathProperty<Long>> np2 = beanPropertySet.getProperty("sequence");
		assertTrue(np2.isPresent());
		assertFalse(np2.get().equals(np.get()));
		assertFalse(np.get().hashCode() == np2.get().hashCode());

		final TestBean testMock = mock(TestBean.class);
		when(testMock.getName()).thenReturn("Test");
		when(testMock.getSequence()).thenReturn(Integer.valueOf(1));

		// read

		Object value = beanPropertySet.read("name", testMock);
		assertEquals("Test", value);
		value = beanPropertySet.read("sequence", testMock);
		assertEquals(1, value);

		value = beanPropertySet.read("nested.nestedId", testMock);
		assertNull(value);

		PathProperty<String> pName = PathProperty.create("name", String.class);
		PathProperty<Integer> pSequence = PathProperty.create("sequence", Integer.class);

		value = beanPropertySet.read(pName.getName(), testMock);
		assertEquals("Test", value);
		value = beanPropertySet.read(pSequence.getName(), testMock);
		assertEquals(1, value);

		TestNested testNested = mock(TestNested.class);
		when(testNested.getNestedId()).thenReturn(2L);
		when(testNested.getNestedDate()).thenReturn(new Date());

		when(testMock.getNested()).thenReturn(testNested);

		Optional<PathProperty<Object>> bp = beanPropertySet.getProperty("nested.nestedId");
		assertTrue(bp.isPresent());
		assertTrue(bp.get().getParent().isPresent());
		assertEquals("nestedId", bp.get().getName());
		assertNotNull(bp.get().getParent());

		bp = beanPropertySet.getProperty("nested.nestedDate");
		assertTrue(bp.isPresent());
		bp.get().toString();

		value = beanPropertySet.read("nested.nestedId", testMock);
		assertNotNull(value);
		assertEquals(2L, value);

		value = beanPropertySet.read("nested.nestedDate", testMock);
		assertNotNull(value);

		bp = beanPropertySet.getProperty("name");
		assertTrue(bp.isPresent());
		value = beanPropertySet.read("name", testMock);
		assertEquals("Test", value);

		// write

		final TestBean testWrite = new TestBean();

		beanPropertySet.write("sequence", 2, testWrite);
		value = beanPropertySet.read("sequence", testWrite);
		assertEquals(2, value);

		beanPropertySet.write("name", "Test write", testWrite);
		value = beanPropertySet.read("name", testWrite);
		assertEquals("Test write", value);

		testWrite.setNested(new TestNested());

		beanPropertySet.write("nested.nestedId", 7L, testWrite);
		value = beanPropertySet.read("nested.nestedId", testWrite);
		assertEquals(7L, value);

		testWrite.setNested(null);

		beanPropertySet.write("nested.nestedId", 9L, testWrite);
		value = beanPropertySet.read("nested.nestedId", testWrite);
		assertEquals(9L, value);

		TestBean tb = new TestBean();

		PropertyBox pb = PropertyBox.builder(PropertySet.of(TestPropertySet.NAME)).set(TestPropertySet.NAME, "tn")
				.build();

		beanPropertySet.write(pb, tb);

		assertEquals("tn", tb.getName());

	}

	@Test
	public void testBeanPropertiesNone() {

		BeanPropertySet<Object> ctx = BeanIntrospector.get().getPropertySet(Object.class);
		assertNotNull(ctx);
		assertEquals(0, ctx.size());

	}

	@Test
	public void testIgnoreProperty() {

		BeanPropertySet<TestBean> testBeanContext = BeanIntrospector.get().getPropertySet(TestBean.class);
		BeanPropertySet<TestBean2> testBean2Context = BeanIntrospector.get().getPropertySet(TestBean2.class);

		assertFalse(testBeanContext.getProperty("internalToIgnore").isPresent());

		assertFalse(testBean2Context.getProperty("nested").isPresent());
		assertFalse(testBean2Context.getProperty("nested.nestedId").isPresent());
		assertFalse(testBean2Context.getProperty("nested.nestedDate").isPresent());

		Date date = new Date();

		TestBean2 testMock = mock(TestBean2.class);
		when(testMock.getSomeDecimal()).thenReturn(BigDecimal.valueOf(2.7));

		TestNested testNested = mock(TestNested.class);
		when(testNested.getNestedId()).thenReturn(2L);
		when(testNested.getNestedDate()).thenReturn(date);

		when(testMock.getNested()).thenReturn(testNested);

		Object value = testBean2Context.read("someDecimal", testMock);
		assertEquals(BigDecimal.valueOf(2.7), value);
	}

	@Test
	public void testIgnorePropertyNotNested() {

		BeanPropertySet<TestBean3> testBean3Context = BeanIntrospector.get().getPropertySet(TestBean3.class);

		assertFalse(testBean3Context.getProperty("nested").isPresent());
		assertTrue(testBean3Context.getProperty("nested.nestedId").isPresent());
		assertTrue(testBean3Context.getProperty("nested.nestedDate").isPresent());
	}

	@Test
	public void testBeanPropertyBox() {

		Date date = new Date();

		TestBean test = new TestBean();
		test.setName("Test");
		test.setSequence(1);
		TestNested testNested = new TestNested();
		testNested.setNestedId(2L);
		testNested.setNestedDate(date);
		test.setNested(testNested);

		final PropertyBox box = PropertyBox.builder(TestPropertySet.PROPERTIES).build();
		BeanIntrospector.get().read(box, test);

		assertTrue(box.size() > 0);

		Object value = box.getValue(TestPropertySet.NAME);
		assertEquals("Test", value);

		value = box.getValue(TestPropertySet.NESTED_ID);
		assertNotNull(value);
		assertEquals(Long.valueOf(2L), value);

		value = box.getValue(TestPropertySet.NESTED_DATE);
		assertNotNull(value);

		assertThrows(PropertyNotFoundException.class, () -> {
			PathProperty<String> px = PathProperty.create("px", String.class);
			box.getValue(px);
		});

	}

	@Test
	public void testPropertySet() {
		assertTrue(TestPropertySet.PROPERTIES.contains(TestPropertySet.NAME));

		assertFalse(TestPropertySet.PROPERTIES.contains(null));

		PropertySet<?> ps = PropertySet.builder().add(TestPropertySet.NAME).build();
		assertTrue(ps.contains(TestPropertySet.NAME));

		ps = PropertySet.of(TestPropertySet.NAME);
		assertTrue(ps.contains(TestPropertySet.NAME));

		@SuppressWarnings("unchecked")
		List<Property> lst = (List<Property>) ps.asList();
		assertNotNull(lst);
		assertEquals(1, lst.size());
		assertEquals(TestPropertySet.NAME, lst.get(0));

		List<Property> col = new ArrayList<>();
		col.add(TestPropertySet.NAME);
		ps = PropertySet.of(col);
		assertTrue(ps.contains(TestPropertySet.NAME));

		final Collection<Property<?>> props = new ArrayList<>();
		props.add(TestPropertySet.NAME);

		ps = PropertySet.of(props);
		assertTrue(ps.contains(TestPropertySet.NAME));

		Iterable<Property<?>> pi = new Iterable<Property<?>>() {

			@Override
			public Iterator<Property<?>> iterator() {
				return props.iterator();
			}
		};

		ps = PropertySet.of(pi);
		assertTrue(ps.contains(TestPropertySet.NAME));

		PropertySet<Property<?>> p1 = PropertySet.of(TestPropertySet.NAME);

		PropertySet<Property<?>> p3 = PropertySet.builder().add(p1).add(TestPropertySet.GENERIC).build();
		assertTrue(p3.contains(TestPropertySet.NAME));
		assertTrue(p3.contains(TestPropertySet.GENERIC));

		p3 = PropertySet.builder().add(p1).add(TestPropertySet.GENERIC).build();
		assertTrue(p3.contains(TestPropertySet.NAME));
		assertTrue(p3.contains(TestPropertySet.GENERIC));

		PropertySet<Property<?>> p4 = PropertySet.builder().add(p3).remove(TestPropertySet.GENERIC).build();
		assertTrue(p4.contains(TestPropertySet.NAME));
		assertFalse(p4.contains(TestPropertySet.GENERIC));

		Object value = p4.execute(() -> Context.get().resource(PropertySet.CONTEXT_KEY, PropertySet.class)
				.map(p -> p.iterator().next()).orElse(null));
		assertEquals(TestPropertySet.NAME, value);

		PropertySet<?> source = PropertySet.of(TestPropertySet.NAME, TestPropertySet.SEQUENCE);
		PropertySet<?> joined = PropertySet.of(source, TestPropertySet.GENERIC, TestPropertySet.VIRTUAL);

		assertTrue(joined.contains(TestPropertySet.NAME));
		assertTrue(joined.contains(TestPropertySet.SEQUENCE));
		assertTrue(joined.contains(TestPropertySet.GENERIC));
		assertTrue(joined.contains(TestPropertySet.VIRTUAL));

		PropertySet<Property<?>> ps2 = PropertySet.builder().add(TestPropertySet.NAME).add(TestPropertySet.NAME)
				.build();
		assertEquals(1, ps2.size());

		PropertySet<Property<?>> pbo = PropertySet.builderOf(TestPropertySet.NAME, TestPropertySet.SEQUENCE)
				.withIdentifier(TestPropertySet.SEQUENCE).build();
		assertTrue(pbo.contains(TestPropertySet.NAME));
		assertTrue(pbo.contains(TestPropertySet.SEQUENCE));

		PropertySet<PathProperty> pps = PropertySet.builder(PathProperty.class).add(TestPropertySet.NAME).build();
		assertTrue(pps.contains(TestPropertySet.NAME));
	}

	@Test
	public void testPropertySetIdentifier() {

		assertFalse(TestIdentifiablePropertySet.PROPERTIES.getIdentifiers().isEmpty());
		assertTrue(TestIdentifiablePropertySet.PROPERTIES.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(TestIdentifiablePropertySet.PROPERTIES.getFirstIdentifier().isPresent());
		assertEquals(TestIdentifiablePropertySet.ID, TestIdentifiablePropertySet.PROPERTIES.getFirstIdentifier().get());
		assertTrue(TestIdentifiablePropertySet.ID == TestIdentifiablePropertySet.PROPERTIES.identifiers().findFirst()
				.get());

		PropertySet<?> set = PropertySet.builder().add(TestIdentifiablePropertySet.ID)
				.withIdentifier(TestIdentifiablePropertySet.ID).build();

		assertTrue(set.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(set.getFirstIdentifier().isPresent());
		assertEquals(TestIdentifiablePropertySet.ID, set.getFirstIdentifier().get());

		PropertySet<?> set2 = PropertySet.of(set, TestIdentifiablePropertySet.TEXT);

		assertTrue(set2.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(set2.getFirstIdentifier().isPresent());
		assertEquals(TestIdentifiablePropertySet.ID, set2.getFirstIdentifier().get());

		PropertySet<?> set3 = PropertySet.builder().add(P1).add(P2).add(P3).identifiers(P1, P2).build();
		assertEquals(2, set3.getIdentifiers().size());
		assertTrue(set3.getIdentifiers().contains(P1));
		assertTrue(set3.getIdentifiers().contains(P2));
		assertTrue(set3.getFirstIdentifier().isPresent());
		assertEquals(P1, set3.getFirstIdentifier().get());
	}

	@Test
	public void testPropertySetConfiguration() {

		assertTrue(TestIdentifiablePropertySet.PROPERTIES.getConfiguration().hasNotNullParameter("test"));
		assertEquals("TEST",
				TestIdentifiablePropertySet.PROPERTIES.getConfiguration().getParameter("test").orElse(null));

		ConfigProperty<Long> cp = ConfigProperty.create("tcfg", Long.class);

		PropertySet<?> set = PropertySet.builder().add(TestIdentifiablePropertySet.ID).withConfiguration(cp, 7L)
				.build();
		assertEquals(Long.valueOf(7), set.getConfiguration().getParameter(cp, 1L));

		PropertySet<?> set2 = PropertySet.of(set, TestIdentifiablePropertySet.TEXT);
		assertEquals(Long.valueOf(7), set2.getConfiguration().getParameter(cp, 1L));

	}

	@Test
	public void testPropertySetBuilder() {

		PropertySet set = PropertySet.builderOf(TestIdentifiablePropertySet.PROPERTIES).build();
		assertNotNull(set);
		assertTrue(set.contains(TestIdentifiablePropertySet.ID));
		assertTrue(set.contains(TestIdentifiablePropertySet.TEXT));
		assertTrue(set.contains(TestIdentifiablePropertySet.ENM));
		assertEquals(1, set.getIdentifiers().size());
		assertTrue(set.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(set.getConfiguration().hasNotNullParameter("test"));
		assertEquals("TEST", set.getConfiguration().getParameter("test").orElse(null));

		final StringProperty TEXT2 = StringProperty.create("text2");

		set = PropertySet.builderOf(TestIdentifiablePropertySet.PROPERTIES).add(TEXT2)
				.remove(TestIdentifiablePropertySet.TEXT).withConfiguration("test2", "TEST2").build();
		assertNotNull(set);
		assertTrue(set.contains(TestIdentifiablePropertySet.ID));
		assertFalse(set.contains(TestIdentifiablePropertySet.TEXT));
		assertTrue(set.contains(TEXT2));
		assertTrue(set.contains(TestIdentifiablePropertySet.ENM));
		assertEquals(1, set.getIdentifiers().size());
		assertTrue(set.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(set.getConfiguration().hasNotNullParameter("test"));
		assertEquals("TEST", set.getConfiguration().getParameter("test").orElse(null));
		assertTrue(set.getConfiguration().hasNotNullParameter("test2"));
		assertEquals("TEST2", set.getConfiguration().getParameter("test2").orElse(null));
	}

	@Test
	public void testPropertyBoxIdentifier() {

		PropertyBox box = PropertyBox.create(TestIdentifiablePropertySet.PROPERTIES);

		assertTrue(box.getIdentifiers().contains(TestIdentifiablePropertySet.ID));
		assertTrue(box.getFirstIdentifier().isPresent());
		assertEquals(TestIdentifiablePropertySet.ID, box.getFirstIdentifier().get());
		assertTrue(TestIdentifiablePropertySet.ID == box.identifiers().findFirst().get());

		box = PropertyBox.create(PropertySet.builderOf(TestIdentifiablePropertySet.ID, TestIdentifiablePropertySet.ENM)
				.withIdentifier(TestIdentifiablePropertySet.ENM).build());
		assertTrue(box.getFirstIdentifier().isPresent());
		assertEquals(TestIdentifiablePropertySet.ENM, box.getFirstIdentifier().get());

		PropertyBox box1 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.ID, 1L).set(TestIdentifiablePropertySet.TEXT, "test").build();
		PropertyBox box2 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.ID, 2L).set(TestIdentifiablePropertySet.TEXT, "test").build();
		PropertyBox box3 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.ID, 1L).set(TestIdentifiablePropertySet.TEXT, "test").build();
		PropertyBox box4 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.TEXT, "test").build();

		assertTrue(box1.equals(box3));
		assertFalse(box1.equals(box2));
		assertFalse(box1.equals(null));
		assertFalse(box1.equals(box4));
		assertFalse(box4.equals(box2));
		assertTrue(box1.equals(box1));
		assertTrue(box4.equals(box4));

		PropertyBox box5 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES).equalsHandler((a, b) -> true)
				.hashCodeProvider(pb -> Optional.of(1)).build();
		assertTrue(box5.equals(box1));
		assertTrue(box5.equals(null));
		assertEquals(1, box5.hashCode());

		Map<Object, Object> map = new HashMap<>();

		PropertyBox m1 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.ID, 1L).set(TestIdentifiablePropertySet.TEXT, "m1").build();

		map.put(m1, m1);

		Object value = map.get(m1);
		assertNotNull(value);

		PropertyBox m2 = PropertyBox.builder(TestIdentifiablePropertySet.PROPERTIES)
				.set(TestIdentifiablePropertySet.ID, 1L).set(TestIdentifiablePropertySet.TEXT, "m1").build();

		value = map.get(m2);
		assertNotNull(value);
	}

	@Test
	public void testPropertyBoxConfiguration() {

		PropertyBox box = PropertyBox.create(TestIdentifiablePropertySet.PROPERTIES);
		box.setValue(TestIdentifiablePropertySet.ID, 3L);

		assertEquals("TEST", box.getConfiguration().getParameter("test").orElse(null));

		PropertyBox box2 = box.cloneBox();

		assertEquals("TEST", box2.getConfiguration().getParameter("test").orElse(null));
		assertEquals(Long.valueOf(3), box2.getValue(TestIdentifiablePropertySet.ID));
	}

	@Test
	public void testPathProperty() {
		StringProperty property = StringProperty.create("test");

		assertNotNull(property.toString());

		assertEquals("test", property.getName());

		PathProperty<String> property2 = PathProperty.create("test", String.class);

		PathProperty<Integer> ap = PathProperty.create("test", int.class).message("Test").messageCode("mc");
		assertTrue(TypeUtils.isInteger(ap.getType()));

		ap.toString();

		final Path<String> sp = Path.of("testp", String.class);

		property = StringProperty.create(sp);
		assertEquals("testp", property.getName());

		assertThrows(IllegalArgumentException.class, () -> StringProperty.create((Path<String>) null));

		assertThrows(IllegalArgumentException.class, () -> PathProperty.create(null));

		property2 = PathProperty.create(sp);
		assertEquals("testp", property.getName());

		// sorts

		QuerySort sort = property.asc();
		assertInstanceOf(sort, PathQuerySort.class);
		assertNotNull(((PathQuerySort) sort).getPath());
		assertEquals(SortDirection.ASCENDING, ((PathQuerySort) sort).getDirection());

		sort = property.desc();
		assertInstanceOf(sort, PathQuerySort.class);
		assertNotNull(((PathQuerySort) sort).getPath());
		assertEquals(SortDirection.DESCENDING, ((PathQuerySort) sort).getDirection());

		// filters

		QueryFilter flt = property.isNull();
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NULL, ((OperationQueryFilter) flt).getOperator());

		flt = property.isNotNull();
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NOT_NULL, ((OperationQueryFilter) flt).getOperator());

		flt = property.eq("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.EQUAL, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.neq("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NOT_EQUAL, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.gt("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.GREATER_THAN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.goe("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.GREATER_OR_EQUAL, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.lt("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.LESS_THAN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.loe("x");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.LESS_OR_EQUAL, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.contains("x", false);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.MATCH, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.contains("x", true);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.MATCH, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.in("x", "y");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.IN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.nin("x", "y");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NOT_IN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.between("x", "y");
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.BETWEEN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		Collection<String> col = new ArrayList<>();
		col.add("z");

		flt = property.in(col);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.IN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.nin(col);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NOT_IN, ((OperationQueryFilter) flt).getOperator());
		assertTrue(((OperationQueryFilter) flt).getRightOperand().isPresent());

		flt = property.eq(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.EQUAL, ((OperationQueryFilter) flt).getOperator());

		flt = property.neq(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.NOT_EQUAL, ((OperationQueryFilter) flt).getOperator());

		flt = property.gt(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.GREATER_THAN, ((OperationQueryFilter) flt).getOperator());

		flt = property.goe(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.GREATER_OR_EQUAL, ((OperationQueryFilter) flt).getOperator());

		flt = property.lt(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.LESS_THAN, ((OperationQueryFilter) flt).getOperator());

		flt = property.loe(property2);
		assertNotNull(flt);
		assertInstanceOf(flt, OperationQueryFilter.class);
		assertNotNull(((OperationQueryFilter) flt).getLeftOperand());
		assertEquals(FilterOperator.LESS_OR_EQUAL, ((OperationQueryFilter) flt).getOperator());

	}

	@Test
	public void testBooleanProperty() {

		BooleanProperty bp = BooleanProperty.create("test");
		assertEquals(Boolean.class, bp.getType());

		bp = BooleanProperty.create(Path.of("test", boolean.class));
		assertEquals(Boolean.class, bp.getType());

		assertThrows(IllegalArgumentException.class, () -> BooleanProperty.create((Path<Boolean>) null));

		bp = BooleanProperty.create("test", Integer.class);
		assertTrue(bp.getConverter().isPresent());
		Object model = bp.getConvertedValue(Boolean.TRUE);
		assertNotNull(model);
		assertTrue(model instanceof Number);
		assertEquals(1, ((Number) model).intValue());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNumericProperty() {

		NumericProperty<Integer> ip = NumericProperty.create(Path.of("test", Integer.class));
		assertEquals(Integer.class, ip.getType());

		assertThrows(IllegalArgumentException.class, () -> NumericProperty.create((Path) null));

		NumericProperty<?> np = NumericProperty.integerType("test");
		assertEquals(Integer.class, np.getType());
		np = NumericProperty.longType("test");
		assertEquals(Long.class, np.getType());
		np = NumericProperty.floatType("test");
		assertEquals(Float.class, np.getType());
		np = NumericProperty.doubleType("test");
		assertEquals(Double.class, np.getType());
		np = NumericProperty.shortType("test");
		assertEquals(Short.class, np.getType());
		np = NumericProperty.floatType("test");
		assertEquals(Float.class, np.getType());
		np = NumericProperty.bigIntegerType("test");
		assertEquals(BigInteger.class, np.getType());
		np = NumericProperty.bigDecimalType("test");
		assertEquals(BigDecimal.class, np.getType());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTemporalProperty() {

		TemporalProperty<Date> dp = TemporalProperty.create(Path.of("test", Date.class));
		assertEquals(Date.class, dp.getType());

		assertThrows(IllegalArgumentException.class, () -> TemporalProperty.create((Path) null));

		TemporalProperty<?> tp = TemporalProperty.date("test");
		assertEquals(Date.class, tp.getType());
		tp = TemporalProperty.localDate("test");
		assertEquals(LocalDate.class, tp.getType());
		tp = TemporalProperty.localTime("test");
		assertEquals(LocalTime.class, tp.getType());
		tp = TemporalProperty.localDateTime("test");
		assertEquals(LocalDateTime.class, tp.getType());
		tp = TemporalProperty.offsetDateTime("test");
		assertEquals(OffsetDateTime.class, tp.getType());

	}

	@Test
	public void testPropertyBoxProperty() {

		final PropertyBoxProperty p1 = PropertyBoxProperty.create("test", TestPropertySet.PROPERTIES);
		assertNotNull(p1);
		assertEquals(PropertyBox.class, p1.getType());
		assertEquals(TestPropertySet.PROPERTIES,
				p1.getConfiguration().getParameter(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE).orElse(null));

		final PropertyBoxProperty p2 = PropertyBoxProperty.create("test", TestPropertySet.NAME,
				TestPropertySet.SEQUENCE);
		assertNotNull(p2);
		assertEquals(PropertyBox.class, p2.getType());
		assertNotNull(p2.getConfiguration().getParameter(PropertySet.PROPERTY_CONFIGURATION_ATTRIBUTE).orElse(null));

	}

	private static void assertInstanceOf(Object object, Class<?> type) {
		if (!type.isInstance(object)) {
			Assertions.fail("Expected object type [" + type + "] but got type ["
					+ ((object == null) ? "NULL" : object.getClass().getName() + "]"));
		}
	}

	static enum TestEnum {
		A, B, C;
	}

	static enum TestEnum2 implements Localizable {
		A("valueA"), B("valueB"), C("valueC");

		private final String caption;

		private TestEnum2(String caption) {
			this.caption = caption;
		}

		@Override
		public String getMessage() {
			return caption;
		}

		@Override
		public String getMessageCode() {
			return null;
		}
	}

	static class TestCaptionable implements Localizable {

		@Override
		public String getMessage() {
			return "TestCaptionableCaption";
		}

		@Override
		public String getMessageCode() {
			return null;
		}

	}

}
