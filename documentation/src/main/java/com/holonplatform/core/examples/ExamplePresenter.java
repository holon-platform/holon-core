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
package com.holonplatform.core.examples;

import com.holonplatform.core.ParameterSet;
import com.holonplatform.core.i18n.Caption;
import com.holonplatform.core.presentation.StringValuePresenter;

@SuppressWarnings("unused")
public class ExamplePresenter {

	// tag::presenter[]
	enum MyEnum {

		@Caption("The value 1")
		VALUE1,

		@Caption(value = "The value 2", messageCode = "message.value2")
		VALUE2

	}

	public void present() {
		String presented = StringValuePresenter.getDefault().present("stringValue"); // <1>
		presented = StringValuePresenter.getDefault().present("stringValue",
				ParameterSet.builder().parameter(StringValuePresenter.MAX_LENGTH, 6).build()); // <2>
		presented = StringValuePresenter.getDefault().present(MyEnum.VALUE1); // <3>
		presented = StringValuePresenter.getDefault().present(new MyEnum[] { MyEnum.VALUE1, MyEnum.VALUE2 }); // <4>
	}

	// end::presenter[]

}
