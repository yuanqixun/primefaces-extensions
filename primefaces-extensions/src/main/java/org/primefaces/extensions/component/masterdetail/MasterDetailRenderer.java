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
 * $Id: MasterDetailRenderer.java 555 2011-12-08 20:52:00Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.component.masterdetail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.extensions.component.reseteditablevalues.EditableValueHoldersVisitCallback;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link MasterDetail} component.
 *
 * @author  Oleg Varaksin / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 555 $
 */
public class MasterDetailRenderer extends CoreRenderer {

	private static final String FACET_HEADER = "header";
	private static final String FACET_FOOTER = "footer";

	@Override
	public void encodeEnd(final FacesContext fc, UIComponent component) throws IOException {
		MasterDetail masterDetail = (MasterDetail) component;
		MasterDetailLevel mdl;

		if (masterDetail.isSelectDetailRequest(fc)) {
			// component has been navigated via SelectDetailLevel
			MasterDetailLevel mdlToProcess = masterDetail.getDetailLevelToProcess(fc);

			if (fc.isValidationFailed()) {
				mdl = mdlToProcess;
			} else {
				mdl = masterDetail.getDetailLevelToGo(fc);

				boolean changeLevelAllowed = true;
				if (masterDetail.getFlowListener() != null) {
					FlowLevelEvent flowLevelEvent = new FlowLevelEvent(masterDetail, mdlToProcess.getLevel(), mdl.getLevel());
					changeLevelAllowed =
					    (Boolean) masterDetail.getFlowListener().invoke(fc.getELContext(), new Object[] {flowLevelEvent});
					if (!changeLevelAllowed) {
						mdl = mdlToProcess;
					}
				}

				if (changeLevelAllowed) {
					// reset last saved validation state and stored values of editable components
					EditableValueHoldersVisitCallback visitCallback = new EditableValueHoldersVisitCallback();
					mdlToProcess.visitTree(VisitContext.createVisitContext(fc), visitCallback);

					final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();
					for (EditableValueHolder editableValueHolder : editableValueHolders) {
						editableValueHolder.resetValue();
					}
				}
			}

			masterDetail.updateModel(fc, mdl);
		} else {
			// component has been navigated from outside, e.g. GET request or POST update from another component
			mdl = masterDetail.getDetailLevelByLevel(masterDetail.getLevel());
		}

		// render MasterDetailLevel
		encodeMarkup(fc, masterDetail, mdl);

		// reset calculated values
		masterDetail.resetCalculatedValues();
	}

	protected void encodeMarkup(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl)
	    throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = masterDetail.getClientId(fc);
		String styleClass =
		    masterDetail.getStyleClass() == null ? "ui-master-detail" : "ui-master-detail " + masterDetail.getStyleClass();

		writer.startElement("div", masterDetail);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (masterDetail.getStyle() != null) {
			writer.writeAttribute("style", masterDetail.getStyle(), "style");
		}

		// render header
		encodeFacet(fc, masterDetail, FACET_HEADER);

		if (masterDetail.isShowBreadcrumb()) {
			// get breadcrumb and its current model
			BreadCrumb breadcrumb = getBreadcrumb(masterDetail);
			if (breadcrumb == null) {
				throw new FacesException("BreadCrumb component was not found below MasterDetail.");
			}

			MenuModel model = buildBreadcrumbModel(fc, masterDetail, mdl);

			// remove not up-to-date children
			int kidsCount = breadcrumb.getChildCount();
			for (int i = 0; i < kidsCount; i++) {
				breadcrumb.getChildren().remove(0);
			}

			// add new children
			for (UIComponent kid : model.getMenuItems()) {
				breadcrumb.getChildren().add(kid);
			}

			// render breadcrumb
			breadcrumb.encodeAll(fc);
		}

		// render container for MasterDetailLevel
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_detaillevel", "id");
		writer.writeAttribute("class", "ui-master-detail-level", null);

		// try to get context value if contextVar exists
		Object contextValue = null;
		String contextVar = mdl.getContextVar();
		if (StringUtils.isNotBlank(contextVar)) {
			contextValue = masterDetail.getContextValueFromFlow(fc, mdl);
		}

		if (contextValue != null) {
			Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
			requestMap.put(contextVar, contextValue);
		}

		// render MasterDetailLevel
		mdl.encodeAll(fc);

		if (contextValue != null) {
			fc.getExternalContext().getRequestMap().remove(contextVar);
		}

		writer.endElement("div");

		// render footer
		encodeFacet(fc, masterDetail, FACET_FOOTER);
		writer.endElement("div");
	}

	protected void encodeFacet(final FacesContext fc, final UIComponent component, final String name) throws IOException {
		final UIComponent facet = component.getFacet(name);
		if (facet != null) {
			facet.encodeAll(fc);
		}
	}

	protected MenuModel buildBreadcrumbModel(final FacesContext fc, final MasterDetail masterDetail,
	                                         final MasterDetailLevel mdlToRender) {
		// create model from scratch
		MenuModel model = new DefaultMenuModel();

		for (UIComponent child : masterDetail.getChildren()) {
			if (child instanceof MasterDetailLevel) {
				MasterDetailLevel mdl = (MasterDetailLevel) child;

				// create a new menu item and add to the model
				if (child.isRendered()) {
					MenuItem menuItem =
					    createMenuItem(fc, masterDetail, mdl, masterDetail.getContextValueFromFlow(fc, mdl),
					                   mdlToRender.getLevel());
					model.addMenuItem(menuItem);
				}

				if (mdl.getLevel() == mdlToRender.getLevel()) {
					break;
				}
			}
		}

		return model;
	}

	protected MenuItem createMenuItem(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl,
	                                  final Object contextValue, final int currentLevel) {
		String clientId = masterDetail.getClientId(fc);
		MenuItem menuItem = new MenuItem();
		menuItem.setId(masterDetail.getId() + "_bcItem_" + mdl.getLevel());

		String contextVar = mdl.getContextVar();
		boolean putContext = (StringUtils.isNotBlank(contextVar) && contextValue != null);
		if (putContext) {
			Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
			requestMap.put(contextVar, contextValue);
		}

		menuItem.setValue(mdl.getLevelLabel());
		menuItem.setDisabled(mdl.isLevelDisabled());

		if (putContext) {
			fc.getExternalContext().getRequestMap().remove(contextVar);
		}

		menuItem.setAjax(true);
		menuItem.setImmediate(true);
		menuItem.setProcess("@none");
		menuItem.setUpdate(null);

		final String menuItemId = menuItem.getId();

		UIParameter uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sdr");
		uiParameter.setName(clientId + MasterDetail.SELECT_DETAIL_REQUEST);
		uiParameter.setValue(true);
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_cl");
		uiParameter.setName(clientId + MasterDetail.CURRENT_LEVEL);
		uiParameter.setValue(currentLevel);
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sl");
		uiParameter.setName(clientId + MasterDetail.SELECTED_LEVEL);
		uiParameter.setValue(mdl.getLevel());
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sp");
		uiParameter.setName(clientId + MasterDetail.SKIP_PROCESSING_REQUEST);
		uiParameter.setValue(true);
		menuItem.getChildren().add(uiParameter);

		return menuItem;
	}

	protected BreadCrumb getBreadcrumb(final MasterDetail masterDetail) {
		for (UIComponent child : masterDetail.getChildren()) {
			if (child instanceof BreadCrumb) {
				return (BreadCrumb) child;
			}
		}

		return null;
	}

	@Override
	public void encodeChildren(final FacesContext fc, UIComponent component) throws IOException {
		// rendering happens on encodeEnd
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
