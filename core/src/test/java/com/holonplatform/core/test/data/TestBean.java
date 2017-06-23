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

import com.holonplatform.core.i18n.Caption;

/*
 * Test bean for mocking purposes
 */
public class TestBean<T extends Number> extends TestBase<TestNestedGeneric> {

	@Caption(value = "nameCaption", messageCode = "nameCaptionMessageCode")
	private String name;

	private int sequence;

	private T generic;

	private TestNested nested;

	public TestBean() {
		super();
	}

	public TestBean(String name, int sequence) {
		super();
		this.name = name;
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public T getGeneric() {
		return generic;
	}

	public void setGeneric(T generic) {
		this.generic = generic;
	}

	public TestNested getNested() {
		return nested;
	}

	public void setNested(TestNested nested) {
		this.nested = nested;
	}

}
