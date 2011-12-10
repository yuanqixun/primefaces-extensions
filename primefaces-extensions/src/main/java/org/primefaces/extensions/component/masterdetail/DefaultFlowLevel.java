/*
 * Copyright 2011 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: DefaultFlowLevel.java 460 2011-11-17 23:55:15Z ovaraksin@gmail.com $
 */

package org.primefaces.extensions.component.masterdetail;

import java.io.Serializable;

/**
 * Default implementation for {@link FlowLevel}.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@gmail.com $
 * @version $Revision: 460 $
 */
public class DefaultFlowLevel implements FlowLevel, Serializable {

	private static final long serialVersionUID = 20111116L;

	private int level;
	private Object contextValue;

	public DefaultFlowLevel() {
	}

	public DefaultFlowLevel(final int level, final Object contextValue) {
		this.level = level;
		this.contextValue = contextValue;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public Object getContextValue() {
		return contextValue;
	}

	public void setContextValue(final Object contextValue) {
		this.contextValue = contextValue;
	}
}
