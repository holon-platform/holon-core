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
package com.holonplatform.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

import org.junit.Test;

import com.holonplatform.core.datastore.DataTarget;
import com.holonplatform.core.datastore.relational.Join;
import com.holonplatform.core.datastore.relational.Join.JoinType;
import com.holonplatform.core.datastore.relational.RelationalTarget;
import com.holonplatform.core.property.PathProperty;

public class TestRelational {

	private static final DataTarget<String> TARGET1 = DataTarget.named("test1");
	private static final DataTarget<String> TARGET2 = DataTarget.named("test2");

	private static final PathProperty<Integer> P1 = PathProperty.create("p1", Integer.class);
	private static final PathProperty<Integer> P2 = PathProperty.create("p2", Integer.class);

	@Test
	public void testTarget() {

		RelationalTarget<String> rt = RelationalTarget.of(TARGET1);

		assertEquals("test1", rt.getName());
		assertEquals(0, rt.getJoins().size());
		assertFalse(rt.getAlias().isPresent());

		rt = rt.alias("test1Alias");

		assertTrue(rt.getAlias().isPresent());
		assertEquals("test1Alias", rt.getAlias().get());

		rt = rt.join(TARGET2, JoinType.LEFT).alias("test2Alias").on(P1.eq(P2)).add();

		Join<?> join = rt.getJoins().iterator().next();

		assertEquals("test2", join.getName());
		assertEquals("test2Alias", join.getAlias().get());
		assertTrue(join.getOn().isPresent());

		assertEquals(1, rt.getJoins().size());

	}

}
