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
 * $Id: UserSettings.java 294 2011-10-18 19:03:57Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.extensions.showcase.model.Circle;

/**
 * RemoteCommandController
 *
 * @author  Thomas Andraschko / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@ManagedBean
@ViewScoped
public class RemoteCommandController implements Serializable {

	private static final long serialVersionUID = 20111020L;

	private String subject;
	private Date date;
	private Circle circle;

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(final Circle circle) {
		this.circle = circle;
	}
}
