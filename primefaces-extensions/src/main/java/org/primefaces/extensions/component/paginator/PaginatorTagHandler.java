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
 * $Id: PaginatorTagHandler.java 556 2011-12-08 20:52:16Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.component.paginator;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import org.primefaces.component.api.UIData;

/**
 * {@link TagHandler} for the <code>Paginator</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 556 $
 * @since 0.2
 */
public class PaginatorTagHandler extends TagHandler {

	private final TagAttribute paginatorTemplate;
	private final TagAttribute rowsPerPageTemplate;
	private final TagAttribute currentPageReportTemplate;
	private final TagAttribute pageLinks;
	private final TagAttribute paginatorPosition;
	private final TagAttribute paginatorAlwaysVisible;
	private final TagAttribute rows;

	public PaginatorTagHandler(final TagConfig config) {
		super(config);

		this.paginatorTemplate = this.getAttribute("paginatorTemplate");
		this.rowsPerPageTemplate = this.getAttribute("rowsPerPageTemplate");
		this.currentPageReportTemplate = this.getAttribute("currentPageReportTemplate");
		this.pageLinks = this.getAttribute("pageLinks");
		this.paginatorPosition = this.getAttribute("paginatorPosition");
		this.paginatorAlwaysVisible = this.getAttribute("paginatorAlwaysVisible");
		this.rows = this.getAttribute("rows");
	}

	@Override
	public void apply(final FaceletContext context, final UIComponent parent) throws IOException {
		if (!(parent instanceof UIData)) {
			throw new FacesException("Paginator must be inside a component which extends UIData.");
		}

		if (ComponentHandler.isNew(parent)) {
			final UIData uiData = (UIData) parent;
			uiData.setPaginator(true);

			if (this.paginatorTemplate != null) {
				uiData.setPaginatorTemplate(this.paginatorTemplate.getValue(context));
			}

			if (this.rowsPerPageTemplate != null) {
				uiData.setRowsPerPageTemplate(this.rowsPerPageTemplate.getValue(context));
			}

			if (this.currentPageReportTemplate != null) {
				uiData.setCurrentPageReportTemplate(this.currentPageReportTemplate.getValue(context));
			}

			if (this.pageLinks != null) {
				uiData.setPageLinks(this.pageLinks.getInt(context));
			}

			if (this.paginatorPosition != null) {
				uiData.setPaginatorPosition(this.paginatorPosition.getValue(context));
			}

			if (this.paginatorAlwaysVisible != null) {
				uiData.setPaginatorAlwaysVisible(this.paginatorAlwaysVisible.getBoolean(context));
			}

			if (this.rows != null) {
				uiData.setRows(this.rows.getInt(context));
			}
		}
	}
}
