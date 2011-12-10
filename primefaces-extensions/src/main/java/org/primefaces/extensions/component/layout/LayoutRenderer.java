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
 * $Id: LayoutRenderer.java 502 2011-11-29 16:18:19Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.component.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Layout} component.
 *
 * @author  Oleg Varaksin / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 502 $
 * @since   0.2
 */
public class LayoutRenderer extends CoreRenderer {

	private static final String POSITION_NORTH = "north";
	private static final String POSITION_SOUTH = "south";
	private static final String POSITION_CENTER = "center";
	private static final String POSITION_WEST = "west";
	private static final String POSITION_EAST = "east";
	private static final String POSITION_SEPARATOR = "_";
	private static final String MAIN_FORM = "form";
	private static final String STYLE_CLASS_PANE = "ui-widget-content ui-corner-top";
	private static final String STYLE_CLASS_PANE_HEADER = "ui-widget-header ui-layout-pane-header ui-corner-top";
	private static final String STYLE_CLASS_PANE_CONTENT = "ui-layout-pane-content";

	@Override
	public void decode(final FacesContext fc, final UIComponent component) {
		Layout layout = (Layout) component;
		layout.setDataModel(null);
	}

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		Layout layout = (Layout) component;

		Map layoutPanes = pickLayoutPanes(layout);
		if (layoutPanes.isEmpty() || layoutPanes.get(POSITION_CENTER) == null) {
			throw new FacesException("Full page layout must have at least one rendered layout pane with 'center' position");
		}

		encodeScript(fc, layout, layoutPanes);
		encodeMarkup(fc, layout, layoutPanes);
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
		// nothing to do
	}

	protected Map pickLayoutPanes(final Layout layout) {
		Map<String, UIComponent> layoutPanes = new HashMap<String, UIComponent>();
		Iterator<UIComponent> iter = layout.getChildren().iterator();

		while (iter.hasNext()) {
			UIComponent child = (UIComponent) iter.next();
			if (child instanceof LayoutPane) {
				// layout pane on the first level
				pickLayoutPane(child, layoutPanes);
			} else if (child instanceof UIForm) {
				// a form is allowed here
				layoutPanes.put(MAIN_FORM, child);

				Iterator<UIComponent> iter2 = child.getChildren().iterator();
				while (iter2.hasNext()) {
					UIComponent child2 = (UIComponent) iter2.next();

					if (child2 instanceof LayoutPane) {
						// layout pane on the first level
						pickLayoutPane(child2, layoutPanes);
					}
				}
			}
		}

		return layoutPanes;
	}

	protected void encodeScript(final FacesContext fc, final Layout layout, final Map layoutPanes) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = layout.getClientId();
		String widgetVar = layout.resolveWidgetVar();

		writer.write("\n");
		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_script", null);
		writer.writeAttribute("type", "text/javascript", null);

		// write layout options ...
		writer.write("var tabLayoutOptions = {resizeWithWindow: false, south__spacing_open: 3");
		writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_NORTH));
		writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_SOUTH));
		writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER));
		writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST));
		writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST));
		writer.write("};\n");

		boolean hasCenterLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(POSITION_CENTER));
		if (hasCenterLayoutOptions) {
			// write layout options ...
			writer.write("var centerLayoutOptions = {resizeWhileDragging: false");
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER + POSITION_SEPARATOR + POSITION_NORTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER + POSITION_SEPARATOR + POSITION_SOUTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER + POSITION_SEPARATOR + POSITION_CENTER));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER + POSITION_SEPARATOR + POSITION_WEST));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_CENTER + POSITION_SEPARATOR + POSITION_EAST));
			writer.write("};\n");
		}

		boolean hasWestLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(POSITION_WEST));
		if (hasWestLayoutOptions) {
			// write layout options ...
			writer.write("var westLayoutOptions = {resizeWhileDragging: true");
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST + POSITION_SEPARATOR + POSITION_NORTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST + POSITION_SEPARATOR + POSITION_SOUTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST + POSITION_SEPARATOR + POSITION_CENTER));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST + POSITION_SEPARATOR + POSITION_WEST));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_WEST + POSITION_SEPARATOR + POSITION_EAST));
			writer.write("};\n");
		}

		boolean hasEastLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(POSITION_EAST));
		if (hasEastLayoutOptions) {
			// write layout options ...
			writer.write("var eastLayoutOptions = {resizeWhileDragging: true");
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST + POSITION_SEPARATOR + POSITION_NORTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST + POSITION_SEPARATOR + POSITION_SOUTH));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST + POSITION_SEPARATOR + POSITION_CENTER));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST + POSITION_SEPARATOR + POSITION_WEST));
			writeLayoutPaneOption(fc, writer, layoutPanes.get(POSITION_EAST + POSITION_SEPARATOR + POSITION_EAST));
			writer.write("};\n");
		}

		writer.write("$(document).ready(function(){");
		writer.write(widgetVar + " = new PrimeFacesExt.widget.Layout('" + clientId + "',");

		DataModel dataModel = layout.getDataModel();
		if (dataModel == null || dataModel.getRowCount() < 1) {
			writer.write("-1,");
		} else {
			String viewId = fc.getViewRoot().getViewId();
			viewId = viewId.substring(0, viewId.lastIndexOf('.'));
			if (viewId.startsWith("/")) {
				viewId = viewId.substring(1);
			}

			int indexTab = 0;
			Iterator<MenuItem> iter = dataModel.iterator();
			while (iter.hasNext()) {
				String url = iter.next().getUrl();
				url = url.substring(0, url.lastIndexOf('.'));
				if (url.startsWith("/")) {
					url = url.substring(1);
				}

				if (viewId.equals(url)) {
					break;
				}

				++indexTab;
			}

			writer.write(indexTab + ",");
		}

		if (layoutPanes.get(POSITION_NORTH) != null) {
			writer.write(((LayoutPane) layoutPanes.get(POSITION_NORTH)).getSize() + ",");
		} else {
			writer.write("0,");
		}

		writer.write("tabLayoutOptions,");
		writer.write(hasCenterLayoutOptions ? "centerLayoutOptions," : "null,");
		writer.write(hasWestLayoutOptions ? "westLayoutOptions," : "null,");
		writer.write(hasEastLayoutOptions ? "eastLayoutOptions," : "null,");

		if (layout.getTogglerTipClose() != null) {
			writer.write("\"" + layout.getTogglerTipClose() + "\",");
		} else {
			writer.write("'Close',");
		}

		if (layout.getTogglerTipOpen() != null) {
			writer.write("\"" + layout.getTogglerTipOpen() + "\",");
		} else {
			writer.write("'Open',");
		}

		if (layout.getResizerTip() != null) {
			writer.write("\"" + layout.getResizerTip() + "\"");
		} else {
			writer.write("'Resize'");
		}

		writer.write(");");
		writer.write(widgetVar + ".buildOuterTabsLayout();");
		if (dataModel != null && dataModel.getRowCount() > 0) {
			writer.write("$('#" + getEscapedClientId(clientId)
			             + "-layout-tabbuttons').find('.ui-tab').corner('top 6px');");
		}

		writer.write("});");
		writer.endElement("script");
		writer.write("\n");
	}

	protected void encodeMarkup(final FacesContext fc, final Layout layout, final Map layoutPanes) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = layout.getClientId();

		LayoutPane layoutPane = (LayoutPane) layoutPanes.get(POSITION_NORTH);
		if (layoutPane != null && layoutPane.isRendered()) {
			writer.startElement("div", null);
			writer.writeAttribute("id", clientId + "-layout-outer-north", null);
			writer.writeAttribute("class", "layout-outer-north", null);

			renderChildren(fc, layoutPane);

			writer.endElement("div");
		}

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "-layout-outer-center", null);
		writer.writeAttribute("class", "layout-outer-center", null);

		DataModel dataModel = layout.getDataModel();
		if (dataModel != null && dataModel.getRowCount() > 0) {
			// render tabs
			writer.startElement("ul", null);
			writer.writeAttribute("id", clientId + "-layout-tabbuttons", null);
			writer.writeAttribute("class", "layout-tabbuttons", null);
			writer.writeAttribute("style", "display: none;", null);

			Iterator<MenuItem> iter = dataModel.iterator();
			while (iter.hasNext()) {
				MenuItem mi = iter.next();
				writer.startElement("li", null);
				if (!iter.hasNext()) {
					writer.writeAttribute("class", "ui-tab last-tab", null);
				} else {
					writer.writeAttribute("class", "ui-tab", null);
				}

				writer.startElement("a", null);

				// build complete URL
				ExternalContext ec = fc.getExternalContext();
				StringBuilder url = new StringBuilder();
				String scheme = ec.getRequestScheme();
				int port = ec.getRequestServerPort();

				url.append(scheme); // http, https
				url.append("://");
				url.append(ec.getRequestServerName());
				if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
					url.append(':');
					url.append(port);
				}

				String actionURL = fc.getApplication().getViewHandler().getActionURL(fc, mi.getUrl());
				url.append(ec.encodeActionURL(actionURL));

				// write URL
				writer.writeURIAttribute("href", "#" + url.toString(), null);

				// render inner table = tab icon + label
				writer.startElement("table", null);
				writer.startElement("tr", null);
				writer.startElement("td", null);
				writer.writeAttribute("class", "ui-icon " + mi.getIcon(), null);
				writer.endElement("td");
				writer.startElement("td", null);
				writer.write((String) mi.getValue());
				writer.endElement("td");
				writer.endElement("tr");
				writer.endElement("table");

				writer.endElement("a");
				writer.endElement("li");
			}

			writer.endElement("ul");
		}

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "-layout-tabpanels", null);
		writer.writeAttribute("class", "layout-tabpanels", null);

		writer.startElement("div", null);
		writer.writeAttribute("class", "ui-layout-tab", null);

		UIForm form = (UIForm) layoutPanes.get(MAIN_FORM);
		if (form != null) {
			form.encodeBegin(fc);
		}

		// render current tab panel pane by pane
		encodePane(fc, writer, layoutPanes, POSITION_SOUTH);
		encodePane(fc, writer, layoutPanes, POSITION_CENTER);
		encodePane(fc, writer, layoutPanes, POSITION_WEST);
		encodePane(fc, writer, layoutPanes, POSITION_EAST);

		if (form != null) {
			form.encodeEnd(fc);
		}

		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
	}

	protected void encodePane(final FacesContext fc, final ResponseWriter writer, final Map layoutPanes,
			final String position) throws IOException {

		LayoutPane layoutPane = (LayoutPane) layoutPanes.get(position);
		if (layoutPane == null) {
			return;
		}

		writer.startElement("div", null);

		// render class attribute
		if (layoutPane.isExistNestedPanes() || layoutPane.isStatusbar()) {
			writer.writeAttribute("class", "ui-layout-" + layoutPane.getPosition(), null);
		} else {
			writer.writeAttribute("class", "ui-layout-" + layoutPane.getPosition() + " " + STYLE_CLASS_PANE, null);
		}

		// render stuff inside pane(s)
		if (layoutPane.isExistNestedPanes()) {
			encodePane(fc, writer, layoutPanes, position + POSITION_SEPARATOR + POSITION_NORTH);
			encodePane(fc, writer, layoutPanes, position + POSITION_SEPARATOR + POSITION_CENTER);
			encodePane(fc, writer, layoutPanes, position + POSITION_SEPARATOR + POSITION_SOUTH);
			encodePane(fc, writer, layoutPanes, position + POSITION_SEPARATOR + POSITION_EAST);
			encodePane(fc, writer, layoutPanes, position + POSITION_SEPARATOR + POSITION_WEST);
		} else {
			encodePaneHeader(fc, writer, layoutPane);
			encodePaneContent(fc, writer, layoutPane);
		}

		writer.endElement("div");
	}

	protected void encodePaneHeader(final FacesContext fc, final ResponseWriter writer, final LayoutPane layoutPane)
			throws IOException {

		UIComponent header = layoutPane.getFacet("header");
		if (header != null) {
			writer.startElement("div", null);
			if (layoutPane.getStyleClassHeader() != null) {
				writer.writeAttribute("class", STYLE_CLASS_PANE_HEADER + " " + layoutPane.getStyleClassHeader(), null);
			} else {
				writer.writeAttribute("class", STYLE_CLASS_PANE_HEADER, null);
			}

			if (layoutPane.getStyleHeader() != null) {
				writer.writeAttribute("style", layoutPane.getStyleHeader(), null);
			}

			header.encodeAll(fc);
			writer.endElement("div");
		}
	}

	protected void encodePaneContent(final FacesContext fc, final ResponseWriter writer, final LayoutPane layoutPane)
			throws IOException {

		writer.startElement("div", null);

		String styleClass = STYLE_CLASS_PANE_CONTENT;
		if (layoutPane.isStatusbar()) {
			styleClass = styleClass + " ui-state-default statusbar";
		}

		if (layoutPane.getStyleClassContent() != null) {
			writer.writeAttribute("class", styleClass + " " + layoutPane.getStyleClassContent(), null);
		} else {
			writer.writeAttribute("class", styleClass, null);
		}

		if (layoutPane.getStyleContent() != null) {
			writer.writeAttribute("style", layoutPane.getStyleContent(), null);
		}

		renderChildren(fc, layoutPane);
		writer.endElement("div");
	}

	private void pickLayoutPane(final UIComponent child, final Map<String, UIComponent> layoutPanes) {
		if (!child.isRendered()) {
			return;
		}

		String position = ((LayoutPane) child).getPosition();
		layoutPanes.put(position, child);

		boolean hasSubPanes = false;
		Iterator<UIComponent> iter = child.getChildren().iterator();

		while (iter.hasNext()) {
			UIComponent subChild = (UIComponent) iter.next();
			if (subChild instanceof LayoutPane) {
				if (!subChild.isRendered()) {
					continue;
				}

				// layout pane on the second level
				layoutPanes.put(position + POSITION_SEPARATOR + ((LayoutPane) subChild).getPosition(), subChild);
				hasSubPanes = true;
			}
		}

		if (hasSubPanes && layoutPanes.get(position + POSITION_SEPARATOR + POSITION_CENTER) == null) {
			throw new FacesException("Rendered 'center' layout pane inside of '" + position
			                         + "' layout pane is missing");
		}

		if (hasSubPanes) {
			((LayoutPane) child).setExistNestedPanes(true);
		}
	}

	private void writeLayoutPaneOption(final FacesContext fc, final ResponseWriter writer, final Object objPane)
			throws IOException {

		if (objPane == null) {
			return;
		}

		LayoutPane pane = (LayoutPane) objPane;

		writer.write(", " + pane.getPosition() + "__resizable: " + pane.isResizable());
		writer.write(", " + pane.getPosition() + "__closable: " + pane.isClosable());
		writer.write(", " + pane.getPosition() + "__initClosed: " + pane.isInitClosed());

		if (pane.getSize() != null) {
			writer.write(", " + pane.getPosition() + "__size: " + pane.getSize());
		}

		if (pane.getMinSize() != null) {
			writer.write(", " + pane.getPosition() + "__minSize: " + pane.getMinSize());
		}

		if (pane.getMaxSize() != null) {
			writer.write(", " + pane.getPosition() + "__maxSize: " + pane.getMaxSize());
		}

		if (pane.getMinWidth() != null) {
			writer.write(", " + pane.getPosition() + "__minWidth: " + pane.getMinWidth());
		}

		if (pane.getMaxWidth() != null) {
			writer.write(", " + pane.getPosition() + "__maxWidth: " + pane.getMaxWidth());
		}

		if (pane.getMinHeight() != null) {
			writer.write(", " + pane.getPosition() + "__minHeight: " + pane.getMinHeight());
		}

		if (pane.getMaxHeight() != null) {
			writer.write(", " + pane.getPosition() + "__maxHeight: " + pane.getMaxHeight());
		}
	}

	private boolean hasNestedLayoutOptions(final LayoutPane layoutPane) {
		if (layoutPane == null || !layoutPane.isExistNestedPanes()) {
			return false;
		}

		return true;
	}
}
