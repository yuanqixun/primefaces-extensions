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
 * $Id: Sport.java 468 2011-11-20 22:48:32Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.showcase.model.masterdetail;

import java.io.Serializable;
import java.util.List;

/**
 * Sport model class.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 468 $
 */
public class Sport implements Serializable {

	private static final long serialVersionUID = 20111120L;

	private String name;
	private List<Country> countriesWithLeague;

	public Sport(String name, List<Country> countriesWithLeague) {
		this.name = name;
		this.countriesWithLeague = countriesWithLeague;
	}

	public String getName() {
		return name;
	}

	public List<Country> getCountriesWithLeague() {
		return countriesWithLeague;
	}
}
