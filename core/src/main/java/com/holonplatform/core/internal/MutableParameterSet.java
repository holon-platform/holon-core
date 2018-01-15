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
package com.holonplatform.core.internal;

import com.holonplatform.core.ParameterSet;

/**
 * A mutable {@link ParameterSet}.
 *
 * @since 5.1.0
 */
public interface MutableParameterSet extends ParameterSet {

	/**
	 * Add a parameter. If a parameter with given <code>name</code> already exists, its value will be replaced by the
	 * new <code>value</code>.
	 * @param name Parameter name (not null)
	 * @param value Parameter value
	 */
	void addParameter(String name, Object value);

	/**
	 * Remove parameter named <code>name</code>, if exists.
	 * @param name Parameter name (not null)
	 */
	void removeParameter(String name);

}
