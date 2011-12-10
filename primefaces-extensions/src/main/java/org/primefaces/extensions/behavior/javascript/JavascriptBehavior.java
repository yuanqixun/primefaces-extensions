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
 * $Id: JavascriptBehavior.java 335 2011-10-24 21:12:25Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.behavior.javascript;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;

/**
 * Client Behavior class for the <code>Javascript</code> behavior.
 *
 * @author Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 335 $
 * @since  0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class JavascriptBehavior extends ClientBehaviorBase {

	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.JavascriptBehaviorRenderer";

	private String execute;
	private boolean disabled = false;

	@Override
	public String getRendererType() {
		return DEFAULT_RENDERER;
	}

	public final String getExecute() {
		return execute;
	}

	public void setExecute(final String execute) {
		this.execute = execute;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(final boolean disabled) {
		this.disabled = disabled;
	}
}
