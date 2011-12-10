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
 * $Id: Person.java 483 2011-11-28 21:32:56Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.showcase.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Person model class.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 483 $
 */
public class Person implements Serializable {

	private static final long serialVersionUID = 20111128L;

	private String id;
	private String name;
	private int taxClass;
	private Date birthDate;

	public Person(String id, String name, int taxClass, Date birthDate) {
		this.id = id;
		this.name = name;
		this.taxClass = taxClass;
		this.birthDate = birthDate;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(int taxClass) {
		this.taxClass = taxClass;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}
