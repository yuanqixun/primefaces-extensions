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
 * $Id: MasterDetailTagHandler.java 469 2011-11-21 22:34:58Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.component.masterdetail;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;

import org.primefaces.component.breadcrumb.BreadCrumb;

/**
 * {@link javax.faces.view.facelets.TagHandler} for the <code>MasterDetail</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 469 $
 */
public class MasterDetailTagHandler extends ComponentHandler {

	public MasterDetailTagHandler(final ComponentConfig config) {
		super(config);
	}

	@Override
	public void onComponentPopulated(final FaceletContext ctx, final UIComponent c, final UIComponent parent) {
		if (!isNew(parent)) {
			return;
		}

		MasterDetail masterDetail = (MasterDetail) c;

		if (!isBreadcrumbAvailable(masterDetail)) {
			// create BreadCrumb programmatically
			BreadCrumb breadcrumb =
			    (BreadCrumb) ctx.getFacesContext().getApplication().createComponent(BreadCrumb.COMPONENT_TYPE);
			breadcrumb.setId(masterDetail.getId() + "_bc");

			// add it to the MasterDetail
			masterDetail.getChildren().add(breadcrumb);
		}
	}

	private static boolean isBreadcrumbAvailable(final MasterDetail masterDetail) {
		for (UIComponent child : masterDetail.getChildren()) {
			if (child instanceof BreadCrumb) {
				return true;
			}
		}

		return false;
	}
}
