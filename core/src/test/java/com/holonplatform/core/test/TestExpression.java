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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.Expression;
import com.holonplatform.core.Expression.InvalidExpressionException;
import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.ExpressionResolver.ResolutionContext;
import com.holonplatform.core.ExpressionResolverRegistry;
import com.holonplatform.core.NullExpression;
import com.holonplatform.core.TypedExpression;
import com.holonplatform.core.internal.DefaultExpressionResolverRegistry;
import com.holonplatform.core.property.ListPathProperty;
import com.holonplatform.core.property.StringProperty;
import com.holonplatform.core.query.ConstantExpression;
import com.holonplatform.core.temporal.TemporalType;

public class TestExpression {

	private interface ExpressionA extends Expression {

		int getId();

	}

	private interface ExpressionB extends ExpressionA {

	}

	private interface ExpressionC extends Expression {

		String getLabel();

	}

	private class ExpressionAImpl implements ExpressionA {

		private final int id;

		public ExpressionAImpl(int id) {
			super();
			this.id = id;
		}

		@Override
		public void validate() throws InvalidExpressionException {
		}

		@Override
		public int getId() {
			return id;
		}

	}

	private class ExpressionBImpl implements ExpressionB {

		private final int id;

		public ExpressionBImpl(int id) {
			super();
			this.id = id;
		}

		@Override
		public void validate() throws InvalidExpressionException {
		}

		@Override
		public int getId() {
			return id;
		}

	}

	private class ExpressionCImpl implements ExpressionC {

		private final String label;

		public ExpressionCImpl(String label) {
			super();
			this.label = label;
		}

		@Override
		public void validate() throws InvalidExpressionException {
		}

		@Override
		public String getLabel() {
			return label;
		}

	}

	@SuppressWarnings("serial")
	private class Resolver1 implements ExpressionResolver<ExpressionB, ExpressionA> {

		@Override
		public Class<? extends ExpressionB> getExpressionType() {
			return ExpressionB.class;
		}

		@Override
		public Class<? extends ExpressionA> getResolvedType() {
			return ExpressionA.class;
		}

		@Override
		public Optional<ExpressionA> resolve(ExpressionB expression, ResolutionContext context)
				throws InvalidExpressionException {
			return Optional.of(new ExpressionAImpl(expression.getId() + 1));
		}

	}

	@SuppressWarnings("serial")
	private class Resolver2 implements ExpressionResolver<ExpressionA, ExpressionC> {

		@Override
		public Class<? extends ExpressionA> getExpressionType() {
			return ExpressionA.class;
		}

		@Override
		public Class<? extends ExpressionC> getResolvedType() {
			return ExpressionC.class;
		}

		@Override
		public Optional<ExpressionC> resolve(ExpressionA expression, ResolutionContext context)
				throws InvalidExpressionException {
			return Optional.of(new ExpressionCImpl("" + expression.getId()));
		}

	}

	@Test
	public void testTemporalType() {

		TypedExpression<?> te = new TypedExpression<Date>() {

			@Override
			public void validate() throws InvalidExpressionException {
			}

			@Override
			public Class<? extends Date> getType() {
				return Date.class;
			}
		};

		assertTrue(te.getTemporalType().isPresent());
		assertEquals(TemporalType.DATE_TIME, te.getTemporalType().get());

		te = new TypedExpression<LocalDate>() {

			@Override
			public void validate() throws InvalidExpressionException {
			}

			@Override
			public Class<? extends LocalDate> getType() {
				return LocalDate.class;
			}
		};

		assertTrue(te.getTemporalType().isPresent());
		assertEquals(TemporalType.DATE, te.getTemporalType().get());

		te = new TypedExpression<LocalDateTime>() {

			@Override
			public void validate() throws InvalidExpressionException {
			}

			@Override
			public Class<? extends LocalDateTime> getType() {
				return LocalDateTime.class;
			}
		};

		assertTrue(te.getTemporalType().isPresent());
		assertEquals(TemporalType.DATE_TIME, te.getTemporalType().get());

		te = new TypedExpression<LocalTime>() {

			@Override
			public void validate() throws InvalidExpressionException {
			}

			@Override
			public Class<? extends LocalTime> getType() {
				return LocalTime.class;
			}
		};

		assertTrue(te.getTemporalType().isPresent());
		assertEquals(TemporalType.TIME, te.getTemporalType().get());

		te = new TypedExpression<String>() {

			@Override
			public void validate() throws InvalidExpressionException {
			}

			@Override
			public Class<? extends String> getType() {
				return String.class;
			}
		};

		assertFalse(te.getTemporalType().isPresent());

	}

	@Test
	public void testIsers() {

		StringProperty sp = StringProperty.create("test");
		assertTrue(sp.isConverterExpression().isPresent());
		assertFalse(sp.isConverterExpression().flatMap(ce -> ce.getExpressionValueConverter()).isPresent());

		sp = StringProperty.create("test").converter(Integer.class, v -> String.valueOf(v), v -> Integer.valueOf(v));
		assertTrue(sp.isConverterExpression().isPresent());
		assertTrue(sp.isConverterExpression().flatMap(ce -> ce.getExpressionValueConverter()).isPresent());

		assertFalse(sp.isCollectionExpression().isPresent());

		ListPathProperty<String> lp = ListPathProperty.create("test", String.class);
		assertTrue(lp.isConverterExpression().isPresent());
		assertTrue(lp.isCollectionExpression().isPresent());
		assertEquals(String.class, lp.isCollectionExpression().map(ce -> ce.getElementType()).orElse(null));

	}

	@Test
	public void testResolvers() {

		final Resolver1 R1 = new Resolver1();
		final Resolver2 R2 = new Resolver2();

		final ExpressionResolverRegistry registry = new DefaultExpressionResolverRegistry();

		final ResolutionContext ctx = new ResolutionContext() {

			@Override
			public <E extends Expression, R extends Expression> Optional<R> resolve(E expression,
					Class<R> resolutionType, ResolutionContext context) throws InvalidExpressionException {
				return registry.resolve(expression, resolutionType, context);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Iterable<ExpressionResolver> getExpressionResolvers() {
				return registry.getExpressionResolvers();
			}
		};

		registry.addExpressionResolver(R1);
		registry.addExpressionResolver(R2);

		ExpressionA expa = new ExpressionAImpl(0);
		Optional<ExpressionC> resolved1 = registry.resolve(expa, ExpressionC.class, ctx);
		assertTrue(resolved1.isPresent());
		assertEquals("0", resolved1.get().getLabel());

		ExpressionB expb = new ExpressionBImpl(0);
		Optional<ExpressionA> resolved2 = registry.resolve(expb, ExpressionA.class, ctx);
		assertTrue(resolved2.isPresent());
		assertEquals(1, resolved2.get().getId());

		ExpressionResolverRegistry registry2 = new DefaultExpressionResolverRegistry();

		registry2.addExpressionResolver(ExpressionResolver.create(ExpressionB.class, ExpressionA.class,
				(e, c) -> Optional.of(new ExpressionAImpl(e.getId() + 1))));

		expb = new ExpressionBImpl(0);
		resolved2 = registry2.resolve(expb, ExpressionA.class, ctx);
		assertTrue(resolved2.isPresent());
		assertEquals(1, resolved2.get().getId());

	}

	@Test
	public void testNullExpression() {

		NullExpression<String> ne = NullExpression.create(String.class);

		assertNotNull(ne);
		assertEquals(String.class, ne.getType());

		ne = NullExpression.create(ConstantExpression.create("A"));

		assertNotNull(ne);
		assertEquals(String.class, ne.getType());

	}

}
