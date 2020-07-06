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
package com.holonplatform.core.internal.i18n;

import com.holonplatform.core.i18n.LocalizationContext;
import com.holonplatform.core.i18n.LocalizationContext.LocalizationChangeEvent;
import com.holonplatform.core.internal.utils.ObjectUtils;

/**
 * Default {@link LocalizationChangeEvent} implementation.
 *
 * @since 5.2.0
 */
public class DefaultLocalizationChangeEvent implements LocalizationChangeEvent {

	private static final long serialVersionUID = 8959321114274963919L;

	private final transient LocalizationContext source;

	/**
	 * Constructor.
	 * @param source Source context (not null)
	 */
	public DefaultLocalizationChangeEvent(LocalizationContext source) {
		super();
		ObjectUtils.argumentNotNull(source, "LocalizationContext must be not null");
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.holonplatform.core.i18n.LocalizationContext.LocalizationChangeEvent#
	 * getSource()
	 */
	@Override
	public LocalizationContext getSource() {
		return source;
	}

}
