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
 * $Id: DataTableController.java 407 2011-11-02 20:19:29Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * DataTableController
 *
 * @author  Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 407 $
 */
@ManagedBean
@ViewScoped
public class DataTableController implements Serializable {

	private static final long serialVersionUID = 20111020L;

	private List<Message> messages;

	public DataTableController() {
		if (messages == null) {
			messages = new ArrayList<Message>();

			for (int i = 0; i < 100; i++) {
				Message message = new Message();
				message.setMessage("message " + i);
				message.setSubject("subject " + i);
				messages.add(message);
			}
		}
	}

	public final List<Message> getMessages() {
		return messages;
	}

	public final void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	public class Message {

		private String subject;
		private String message;

		public final String getSubject() {
			return subject;
		}

		public final void setSubject(String subject) {
			this.subject = subject;
		}

		public final String getMessage() {
			return message;
		}

		public final void setMessage(String message) {
			this.message = message;
		}
	}
}
