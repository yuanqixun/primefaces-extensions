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
 * $Id: SelectDetailLevelTagHandler.java 470 2011-11-22 19:54:13Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.component.masterdetail;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import org.primefaces.component.api.AjaxSource;

/**
 * {@link TagHandler} for the <code>SelectDetailLevel</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 470 $
 */
public class SelectDetailLevelTagHandler extends TagHandler {

	private final TagAttribute contextValue;
	private final TagAttribute level;
	private final TagAttribute step;

	public SelectDetailLevelTagHandler(final TagConfig config) {
		super(config);
		this.contextValue = getAttribute("contextValue");
		this.level = getAttribute("level");
		this.step = getAttribute("step");
	}

	@Override
	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		if (!(parent instanceof UICommand)) {
			throw new FacesException("SelectDetailLevel must be inside an UICommand.");
		}

		if (!ComponentHandler.isNew(parent)) {
			return;
		}

		// get value expression for contextValue attribute of this tag handler
		ValueExpression contextValueVE = null;
		if (contextValue != null) {
			contextValueVE = contextValue.getValueExpression(ctx, Object.class);
			if (contextValueVE != null) {
				// store valid value expression in the attributes map
				parent.getAttributes().put(MasterDetail.CONTEXT_VALUE_VALUE_EXPRESSION, contextValueVE);
			}
		}

		// get value expression for level attribute of this tag handler
		ValueExpression selectedLevelVE = null;
		if (level != null) {
			selectedLevelVE = level.getValueExpression(ctx, int.class);
			if (selectedLevelVE != null) {
				// store valid value expression in the attributes map
				parent.getAttributes().put(MasterDetail.SELECTED_LEVEL_VALUE_EXPRESSION, selectedLevelVE);
			}
		}

		// get value expression for step attribute of this tag handler
		ValueExpression selectedStepVE = null;
		if (step != null) {
			selectedStepVE = step.getValueExpression(ctx, int.class);
			if (selectedStepVE != null) {
				// store valid value expression in the attributes map
				parent.getAttributes().put(MasterDetail.SELECTED_STEP_VALUE_EXPRESSION, selectedStepVE);
			}
		}

		if (parent instanceof AjaxSource) {
			AjaxSource ajaxSource = (AjaxSource) parent;
			if (ajaxSource.getProcess() == null) {
				// set process to @none because PrimeFaces set it to @all
				try {
					Method setProcessMethod = ajaxSource.getClass().getMethod("setProcess", new Class[] {String.class});
					setProcessMethod.invoke(ajaxSource, "@none");
				} catch (Exception e) {
					// ignore
				}
			} else if ("@none".equals(ajaxSource.getProcess().trim())) {
				// mark navigation for skipping all JSF phases except rendering
				parent.getAttributes().put(MasterDetail.SKIP_PROCESSING, true);
			}
		}

		// register a ComponentSystemEventListener
		PreRenderCommandListener listener = new PreRenderCommandListener();
		parent.subscribeToEvent(PreRenderComponentEvent.class, listener);
	}
}
