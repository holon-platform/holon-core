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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.holonplatform.core.Path;
import com.holonplatform.core.Path.FinalPath;

public class TestPath {

	@Test
	public void testPath() {

		Path<String> path = Path.of("test", String.class);

		assertNotNull(path);
		assertEquals(String.class, path.getType());

		assertEquals("test", path.getName());
		assertEquals("test", path.relativeName());
		assertEquals("test", path.fullName());

	}

	@Test
	public void testPathHierarchy() {

		Path<String> ppath = Path.of("parent", String.class);
		Path<String> path = Path.of("test", String.class).parent(ppath);

		assertNotNull(path);
		assertEquals(String.class, path.getType());

		assertTrue(path.getParent().isPresent());
		assertEquals(ppath, path.getParent().get());

		assertEquals("test", path.getName());
		assertEquals("parent.test", path.relativeName());
		assertEquals("parent.test", path.fullName());
		
		assertFalse(path.isRootPath());
		assertTrue(ppath.isRootPath());

	}
	
	@Test
	public void testPathNameMapper() {
		
		Path<String> ppath = Path.of("parent", String.class);
		Path<String> path = Path.of("test", String.class).parent(ppath);
		
		assertEquals("test", path.getName());
		assertEquals("!parent.!test", path.relativeName(p -> "!" + p.getName()));
		assertEquals("$parent.$test", path.fullName(p -> "$" + p.getName()));
		
	}

	@Test
	public void testFinalPath() {

		FinalPath<String> fp = FinalPath.of("test", String.class);

		assertNotNull(fp);
		assertEquals(String.class, fp.getType());
		assertEquals("test", fp.getName());
		assertEquals("", fp.relativeName());
		assertEquals("test", fp.fullName());

		assertFalse(fp.getParent().isPresent());
		
		Path<String> path = Path.of("test", String.class).parent(fp);

		assertTrue(path.getParent().isPresent());

		assertEquals("test", path.getName());
		assertEquals("test", path.relativeName());
		assertEquals("test.test", path.fullName());

	}

}
