/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.rhamt.ui;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface RhamtResources extends ClientBundle {
	@Source("rhamt.svg")
	SVGResource rhamt();

	@Source("command/rhamt-command-type.svg")
	SVGResource rhamtCommandType();

	@Source({ "Rhamt.css", "org/eclipse/che/ide/api/ui/style.css" })
	RhamtCss css();

	interface RhamtCss extends CssResource {
		String editorInfoPanel();
		String downloadLink();
		String editorMessage();
	}
}
