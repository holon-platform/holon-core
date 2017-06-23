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
package com.holonplatform.core.datastore;

import java.io.Serializable;

/**
 * Represents a <em>commodity</em> which can be provided by a {@link Datastore}.
 * <p>
 * A <em>commodity</em> can be anything which requires a {@link Datastore} for its setup and/or for its operation.
 * </p>
 * <p>
 * This interface its only a marker to identify a class as a {@link Datastore} <em>commodity</em>.
 * </p>
 * 
 * @since 5.0.0
 * 
 * @see Datastore
 */
public interface DatastoreCommodity extends Serializable {

}
