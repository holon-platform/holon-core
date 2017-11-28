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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.DatastoreCommodityContext;
import com.holonplatform.core.datastore.DatastoreCommodityContext.CommodityConfigurationException;
import com.holonplatform.core.datastore.DatastoreCommodityFactory;
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

	@SuppressWarnings("serial")
	@Test
	public void testDatastore() {

		AbstractDatastore<DatastoreCommodityContext> ds = new AbstractDatastore<DatastoreCommodityContext>(null, null) {

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
		};

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
