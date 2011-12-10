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
 * $Id: Constants.java 557 2011-12-08 20:59:07Z Zoigln@googlemail.com $
 */

package org.primefaces.extensions.util;

/**
 * Global constants for the project.
 *
 * @author Thomas Andraschko / last modified by $Author: Zoigln@googlemail.com $
 * @version $Revision: 557 $
 * @since 0.2
 */
public final class Constants {

	public static final String VERSION = "0.2.0-SNAPSHOT";
	public static final String LIBRARY = "primefaces-extensions";
	public static final String LIBRARY_UNCOMPRESSED = "primefaces-extensions-uncompressed";

	public static final String DELIVER_UNCOMPRESSED_RESOURCES_INIT_PARAM =
		"org.primefaces.extensions.DELIVER_UNCOMPRESSED_RESOURCES";

	public static final String EXTENSION_CSS = ".css";
	public static final String EXTENSION_JS = ".js";

	/**
	 * Avoid instantiation.
	 */
	private Constants() {

	}
}
