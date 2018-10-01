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

import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.ExpressionResolver;
import com.holonplatform.core.config.ConfigPropertyProvider;
import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.Datastore;
import com.holonplatform.core.datastore.DatastoreCommodityContext;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
import com.holonplatform.core.datastore.DatastoreConfigProperties;
import com.holonplatform.core.datastore.bulk.BulkDelete;
import com.holonplatform.core.datastore.bulk.BulkInsert;
import com.holonplatform.core.datastore.bulk.BulkUpdate;
import com.holonplatform.core.internal.datastore.AbstractDatastore;
import com.holonplatform.core.internal.query.DefaultQueryDefinition;
import com.holonplatform.core.property.PropertyBox;
import com.holonplatform.core.property.PropertySet;
import com.holonplatform.core.query.Query;
import com.holonplatform.core.test.data.DummyQuery;
import com.holonplatform.core.test.data.TestPropertySet;

public class TestDatastore {

	@Test
	public void testDatastore() {

		DummyDatastore ds = new DummyDatastore();

		ds.registerCommodity(new DummyQueryFactory());

		PropertyBox pb = PropertyBox.builder(TestPropertySet.PROPERTIES).set(TestPropertySet.NAME, "test").build();

		final DataTarget<String> qt = DataTarget.named("tt");

		assertEquals(pb, ds.refresh(qt, pb));

		ds.delete(qt, pb);

		String id = "testId";
		ds.setDataContextId(id);
		assertEquals(id, ds.getDataContextId().get());

		Query q = ds.query();
		assertNotNull(q);
		assertTrue(q instanceof DummyQuery);

	}

	@Test
	public void testDatastoreConfig() {

		final Properties ps = new Properties();
		ps.setProperty("holon.datastore.trace", "true");

		final DatastoreConfigProperties cfg = DatastoreConfigProperties.builder()
				.withPropertySource(ConfigPropertyProvider.using(ps)).build();

		assertEquals(DatastoreConfigProperties.DEFAULT_NAME, cfg.getName());

		assertEquals(DatastoreConfigProperties.DEFAULT_NAME + "." + DatastoreConfigProperties.DIALECT.getKey(),
				cfg.getConfigPropertyName(DatastoreConfigProperties.DIALECT));

		assertTrue(cfg.getConfigPropertyValue(DatastoreConfigProperties.TRACE).orElse(false));
		assertTrue(cfg.getConfigPropertyValue(DatastoreConfigProperties.TRACE, Boolean.FALSE));

		assertFalse(cfg.getConfigPropertyValue(DatastoreConfigProperties.DIALECT).isPresent());
		Optional<String> dlt = cfg.getConfigPropertyValueOrElse(DatastoreConfigProperties.DIALECT,
				() -> Optional.of("TestDialect"));
		assertTrue(dlt.isPresent());
		assertEquals("TestDialect", dlt.get());

		final Properties ps2 = new Properties();
		ps2.setProperty("holon.datastore.ctx1.dialect", "MyDialect");

		final DatastoreConfigProperties cfg2 = DatastoreConfigProperties.builder("ctx1")
				.withPropertySource(ConfigPropertyProvider.using(ps2)).build();

		assertEquals("ctx1", cfg2.getDataContextId().orElse(null));
		assertFalse(cfg2.getConfigPropertyValue(DatastoreConfigProperties.TRACE, Boolean.FALSE));
		assertEquals("MyDialect",
				cfg2.getConfigPropertyValueOrElse(DatastoreConfigProperties.DIALECT, () -> Optional.of("TestDialect"))
						.orElse(null));
	}

	@SuppressWarnings("serial")
	private static class DummyDatastore extends AbstractDatastore<DatastoreCommodityContext> implements Datastore {

		public DummyDatastore() {
			super(DatastoreCommodityFactory.class, ExpressionResolver.class);
		}

		@Override
		public OperationResult insert(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
			assertNotNull(propertyBox);
			return OperationResult.builder().type(OperationType.INSERT).affectedCount(1).build();
		}

		@Override
		public OperationResult update(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
			assertNotNull(propertyBox);
			return OperationResult.builder().type(OperationType.UPDATE).affectedCount(1).build();
		}

		@Override
		public OperationResult save(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
			assertNotNull(propertyBox);
			return OperationResult.builder().type(OperationType.INSERT).affectedCount(1).build();
		}

		@Override
		public PropertyBox refresh(DataTarget<?> target, PropertyBox propertyBox) {
			assertNotNull(propertyBox);
			return propertyBox;
		}

		@Override
		public OperationResult delete(DataTarget<?> target, PropertyBox propertyBox, WriteOption... options) {
			assertNotNull(propertyBox);
			return OperationResult.builder().type(OperationType.DELETE).affectedCount(1).build();
		}

		@Override
		public BulkInsert bulkInsert(DataTarget<?> target, PropertySet<?> propertySet, WriteOption... options) {
			return null;
		}

		@Override
		public BulkUpdate bulkUpdate(DataTarget<?> target, WriteOption... options) {
			return null;
		}

		@Override
		public BulkDelete bulkDelete(DataTarget<?> target, WriteOption... options) {
			return null;
		}

		@Override
		protected DatastoreCommodityContext getCommodityContext() throws CommodityConfigurationException {
			return null;
		}

	}

	@SuppressWarnings("serial")
	private static class DummyQueryFactory implements DatastoreCommodityFactory<DatastoreCommodityContext, Query> {

		/*
		 * (non-Javadoc)
		 * @see com.holonplatform.core.datastore.DatastoreCommodityFactory#getCommodityType()
		 */
		@Override
		public Class<? extends Query> getCommodityType() {
			return Query.class;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.holonplatform.core.datastore.DatastoreCommodityFactory#createCommodity(com.holonplatform.core.datastore.
		 * DatastoreCommodityContext)
		 */
		@Override
		public Query createCommodity(DatastoreCommodityContext context) throws CommodityConfigurationException {
			return new DummyQuery(new DefaultQueryDefinition());
		}

	}

}
