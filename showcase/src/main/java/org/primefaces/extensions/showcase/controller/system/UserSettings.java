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
 * $Id: UserSettings.java 317 2011-10-22 20:44:57Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.showcase.controller.system;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.extensions.showcase.model.system.AvailableThemes;
import org.primefaces.extensions.showcase.model.system.Theme;

/**
 * User settings.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 317 $
 */
@ManagedBean
@SessionScoped
public class UserSettings implements Serializable {

	private static final long serialVersionUID = 20111020L;

	private List<Theme> availableThemes;
	private Theme currentTheme;

	public UserSettings() {
		currentTheme = AvailableThemes.getInstance().getThemeForName("aristo");
		availableThemes = AvailableThemes.getInstance().getThemes();
	}

	public final List<Theme> getAvailableThemes() {
		return availableThemes;
	}

	public final void setAvailableThemes(List<Theme> availableThemes) {
		this.availableThemes = availableThemes;
	}

	public final Theme getCurrentTheme() {
		return currentTheme;
	}

	public final void setCurrentTheme(Theme currentTheme) {
		this.currentTheme = currentTheme;
	}
}
