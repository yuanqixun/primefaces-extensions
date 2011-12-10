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
 * $Id: DocuPreparer.java 317 2011-10-22 20:44:57Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.showcase.controller.system;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.extensions.showcase.model.system.DocuAttribute;
import org.primefaces.extensions.showcase.model.system.DocuTag;
import org.primefaces.extensions.showcase.util.TagLibParser;

/**
 * Prepares the documentation from the tag library.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 317 $
 */
@ApplicationScoped
@ManagedBean(eager = true)
public class DocuPreparer {

	private Map<String, DocuTag> tags;

	@PostConstruct
	protected void initialize() {
		try {
			tags = TagLibParser.getTags();
		} catch (Exception e) {
			throw new FacesException("Taglib parsing failed!", e);
		}
	}

	public List<DocuAttribute> getDocuAttributes(final String tagName) {
		if (tagName == null || tags == null) {
			return null;
		}

		DocuTag docuTag = tags.get(tagName);
		if (docuTag == null) {
			return null;
		}

		return docuTag.getAttributes();
	}
}
