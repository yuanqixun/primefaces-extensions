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
 * $Id: ResizeEvent.java 491 2011-11-29 13:41:23Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesListener;

/**
 * Event which is triggered by the
 * {@link org.primefaces.extensions.component.imagerotateandresize.ImageRotateAndResize} component.
 *
 * @author Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 491 $
 * @since 0.1
 */
@SuppressWarnings("serial")
public class ResizeEvent extends AjaxBehaviorEvent {

	private double width;
	private double height;

	public ResizeEvent(final UIComponent component,
			final Behavior behavior,
			final double width,
			final double height) {
		super(component, behavior);
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isAppropriateListener(final FacesListener faceslistener) {
		return false;
	}

	@Override
	public void processListener(final FacesListener faceslistener) {
		throw new UnsupportedOperationException();
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}
}
