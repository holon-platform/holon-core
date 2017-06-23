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

import com.holonplatform.core.Validator;
import com.holonplatform.core.Validator.ValidationException;

@SuppressWarnings("unused")
public class ExampleValidator {

	public void validator() {
		// tag::validator[]
		Validator<String> validator = v -> { // <1>
			if (v.length() < 10)
				throw new ValidationException("Value must be at least 10 characters");
		};

		validator = Validator.create(v -> v.length() >= 10, "Value must be at least 10 characters"); // <2>

		validator = Validator.create(v -> v.length() >= 10, "Value must be at least 10 characters",
				"messageLocalizationCode"); // <3>
		// end::validator[]
	}

	public void bultin() {
		// tag::builtin[]
		try {
			Validator.notEmpty().validate("mustBeNotEmpty"); // <1>
			Validator.notEmpty("Value must be not empty", "myLocalizationMessageCode").validate("mustBeNotEmpty"); // <2>
		} catch (ValidationException e) {
			// invalid value
			System.out.println(e.getLocalizedMessage());
		}
		// end::builtin[]
	}

}
