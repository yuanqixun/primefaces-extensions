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
 * $Id: MasterDetailLevelTagHandler.java 454 2011-11-14 21:06:40Z ovaraksin@gmail.com $
 */

package org.primefaces.extensions.component.masterdetail;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.TagAttribute;

/**
 * {@link javax.faces.view.facelets.TagHandler} for the <code>MasterDetailLevel</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@gmail.com $
 * @version $Revision: 454 $
 */
public class MasterDetailLevelTagHandler extends ComponentHandler {

	private final TagAttribute level;

	public MasterDetailLevelTagHandler(final ComponentConfig config) {
		super(config);
		this.level = getRequiredAttribute("level");
	}
}
