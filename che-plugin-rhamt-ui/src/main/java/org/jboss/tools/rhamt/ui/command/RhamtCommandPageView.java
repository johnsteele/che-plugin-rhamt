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
package org.jboss.tools.rhamt.ui.command;

import org.eclipse.che.ide.api.mvp.View;

import com.google.inject.ImplementedBy;

@ImplementedBy(RhamtCommandPageViewImpl.class)
public interface RhamtCommandPageView extends View<RhamtCommandPageView.ActionDelegate> {

	String getWorkingDirectory();
	void setWorkingDirectory(String workingDirectory);
	String getArguments();
	void setArguments(String args);

	interface ActionDelegate {
		void onWorkingDirectoryChanged();
		void onArgumentsChanged();
	}
}

