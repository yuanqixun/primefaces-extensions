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
 * $Id: LocaleConverter.java 544 2011-12-06 11:18:58Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.LocaleUtils;

/**
 * {@link Converter} which converts a string to a {@link java.util.Locale} an vice-versa.
 *
 * @author Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 544 $
 * @since 0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.LocaleConverter")
public class LocaleConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		try {
			return LocaleUtils.toLocale(value);
		} catch (IllegalArgumentException e) {
			throw new ConverterException(e.getMessage(), e);
		}
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		return value.toString();
	}
}
