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

import java.util.Date;

import com.holonplatform.core.i18n.Caption;

/*
 * Test nested bean class
 */
public class TestNested {

	@Caption(TestConstants.CAPTION_NESTED_ID)
	private long nestedId;

	@Caption(value = TestConstants.CAPTION_NESTED_DATE, messageCode = TestConstants.MESSAGE_CODE_NESTED_DATE)
	private Date nestedDate;

	public long getNestedId() {
		return nestedId;
	}

	public void setNestedId(long nestedId) {
		this.nestedId = nestedId;
	}

	public Date getNestedDate() {
		return nestedDate;
	}

	public void setNestedDate(Date nestedDate) {
		this.nestedDate = nestedDate;
	}

}
