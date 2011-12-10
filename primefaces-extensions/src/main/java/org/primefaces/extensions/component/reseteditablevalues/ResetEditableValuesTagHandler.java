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
 * $Id: ResetEditableValuesTagHandler.java 556 2011-12-08 20:52:16Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.component.reseteditablevalues;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>ResetEditableValues</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 556 $
 * @since   0.2
 */
public class ResetEditableValuesTagHandler extends TagHandler {

	private final TagAttribute forValue;

	public ResetEditableValuesTagHandler(final TagConfig config) {
		super(config);
		this.forValue = super.getRequiredAttribute("for");
	}

	@Override
	public void apply(final FaceletContext context, final UIComponent parent) throws IOException {
		if (!(parent instanceof UICommand)) {
			throw new FacesException("ResetEditableValues must be inside a component which extends UICommand.");
		}

		if (ComponentHandler.isNew(parent)) {
			ActionSource actionSource = (ActionSource) parent;
			actionSource.addActionListener(new ResetEditableValuesListener(forValue.getValue(context)));
		}
	}
}
