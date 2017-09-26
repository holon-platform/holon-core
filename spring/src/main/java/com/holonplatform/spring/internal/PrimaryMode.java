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
package com.holonplatform.spring.internal;

/**
 * Enumerates the bean registration <code>primary</code> setting modalities to be used in automatic beans registration
 * classes.
 *
 * @since 5.0.0
 */
public enum PrimaryMode {

	/**
	 * Automatic primary setting based on current context and logical bean references. For example, if a bean depends
	 * from another bean and that bean is marked as primary, the bean is registered as primary too.
	 */
	AUTO,

	/**
	 * Always register the bean as primary.
	 */
	TRUE,

	/**
	 * Do not register the bean as primary.
	 */
	FALSE

}
