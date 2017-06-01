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
package com.holonplatform.core.i18n;

/**
 * Enumeration of features which can be used to configure number format behaviour.
 * 
 * @since 5.0.0
 * 
 * @see LocalizationContext
 */
public enum NumberFormatFeature {

	/**
	 * Disable grouping (don't add grouping character in number format)
	 */
	DISABLE_GROUPING,

	/**
	 * Use percent-style format. Given numbers are expected to be in decimal <code>0.xx</code> form
	 */
	PERCENT_STYLE,

	/**
	 * Hide number decimals when all decimal positions (if any) are equal to zero
	 */
	HIDE_DECIMALS_WHEN_ALL_ZERO;

	/**
	 * Check if given <code>feature</code> is present among given <code>features</code> array
	 * @param feature Feature to check
	 * @param features Features set
	 * @return <code>true</code> if given <code>feature</code> is present in features set
	 */
	public static boolean hasFeature(NumberFormatFeature feature, NumberFormatFeature[] features) {
		if (feature != null && features != null) {
			for (NumberFormatFeature nff : features) {
				if (feature == nff) {
					return true;
				}
			}
		}
		return false;
	}

}
