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
 * $Id: ResetEditableValuesController.java 483 2011-11-28 21:32:56Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.showcase.controller.reseteditablevalues;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * ResetEditableValuesController
 *
 * @author  Thomas Andraschko / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 483 $
 */
@ManagedBean
@ViewScoped
public class ResetEditableValuesController {

	private String firstValue;
	private String secondValue;

	public final String getFirstValue() {
		return firstValue;
	}

	public final void setFirstValue(final String value) {
		this.firstValue = value;
	}

	public final String getSecondValue() {
		return secondValue;
	}

	public final void setSecondValue(final String secondValue) {
		this.secondValue = secondValue;
	}
}
