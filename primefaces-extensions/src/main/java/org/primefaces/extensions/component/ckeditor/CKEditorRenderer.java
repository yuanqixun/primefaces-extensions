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
 * $Id: CKEditorRenderer.java 545 2011-12-08 09:45:45Z zoigln $
 */

package org.primefaces.extensions.component.ckeditor;

import java.io.IOException;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.renderkit.InputRenderer;

/**
 * Renderer for the {@link CKEditor} component.
 *
 * @author Thomas Andraschko / last modified by $Author: zoigln $
 * @version $Revision: 545 $
 * @since 0.2
 */
public class CKEditorRenderer extends InputRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		final CKEditor ckEditor = (CKEditor) component;

		if (ckEditor.isReadOnly()) {
			return;
		}

		// set value
        final String clientId = ckEditor.getClientId(context);
        final Map<String, String> params =
        	context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
        	ckEditor.setSubmittedValue(params.get(clientId));
        }

        // decode behaviors
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final CKEditor ckEditor = (CKEditor) component;

		encodeMarkup(context, ckEditor);
		encodeScript(context, ckEditor);
	}

	protected void encodeMarkup(final FacesContext context, final CKEditor ckEditor) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = ckEditor.getClientId(context);

		writer.startElement("textarea", ckEditor);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
		if (ckEditor.getValue() != null) {
			writer.write(ckEditor.getValue().toString());
		}
		writer.endElement("textarea");
	}

	protected void encodeScript(final FacesContext context, final CKEditor ckEditor) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = ckEditor.getClientId(context);
		final String widgetVar = ckEditor.resolveWidgetVar();

		startScript(writer, clientId);

		writer.write("$(function() {");

		writer.write(widgetVar + " = new PrimeFacesExt.widget.CKEditor('" + clientId + "', {");

		// options
		writer.write("width:'" + ckEditor.getWidth() + "'");
		writer.write(",height:'" + ckEditor.getHeight() + "'");
		writer.write(",checkDirtyInterval:" + ckEditor.getCheckDirtyInterval());

		if (ckEditor.getSkin() != null) {
			writer.write(",skin:'" + ckEditor.getSkin() + "'");
		}

		if (ckEditor.getTheme() != null) {
			writer.write(",theme:'" + ckEditor.getTheme() + "'");
		}

		final String toolbar = ckEditor.getToolbar();
		if (toolbar != null) {
			if (toolbar.trim().startsWith("[")) {
				writer.write(",toolbar:" + ckEditor.getToolbar());
			} else {
				writer.write(",toolbar:\"" + ckEditor.getToolbar() + "\"");
			}
		}

		if (ckEditor.isReadOnly()) {
			writer.write(",readOnly:" + ckEditor.isReadOnly());
		}

		if (ckEditor.getInterfaceColor() != null) {
			writer.write(",interfaceColor:'" + ckEditor.getInterfaceColor() + "'");
		}

		if (ckEditor.getLanguage() != null) {
			writer.write(",language:" + ckEditor.getLanguage());
		}

		if (ckEditor.getDefaultLanguage() != null) {
			writer.write(",defaultLanguage:'" + ckEditor.getDefaultLanguage() + "'");
		}

		if (ckEditor.getContentsCss() != null) {
			writer.write(",contentsCss:'" + ckEditor.getContentsCss() + "'");
		}

		if (ckEditor.getCustomConfig() != null) {
			writer.write(",customConfig:'" + ckEditor.getCustomConfig() + "'");
		}

		encodeClientBehaviors(context, ckEditor);

		writer.write("});});");

		endScript(writer);
	}

    @Override
	public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
    	final CKEditor ckEditor = (CKEditor) component;
    	final String value = (String) submittedValue;
    	final Converter converter = ckEditor.getConverter();

		if (converter != null) {
			return converter.getAsObject(context, ckEditor, value);
		}

		final ValueExpression ve = ckEditor.getValueExpression("value");

		if (ve != null) {
		    final Class<?> valueType = ve.getType(context.getELContext());
		    final Converter converterForType = context.getApplication().createConverter(valueType);

		    if (converterForType != null) {
		        return converterForType.getAsObject(context, ckEditor, value);
		    }
		}

		return value;
	}
}
