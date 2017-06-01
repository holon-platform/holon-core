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
package com.holonplatform.core.test.data;

import java.math.BigDecimal;

import com.holonplatform.core.beans.Ignore;

/*
 * Test bean class
 */
public class TestBean2 implements TestQueryData {

	private long id;

	private BigDecimal someDecimal;

	@Ignore
	private TestNested nested;

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getSomeDecimal() {
		return someDecimal;
	}

	public void setSomeDecimal(BigDecimal someDecimal) {
		this.someDecimal = someDecimal;
	}

	public TestNested getNested() {
		return nested;
	}

	public void setNested(TestNested nested) {
		this.nested = nested;
	}

}
