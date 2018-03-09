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
package org.jboss.tools.rhamt.ui.issues.explorer;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class IssueExplorerPlaceholder extends Composite {

	interface IssueExplorerPlaceholderUiBinder extends UiBinder<Widget, IssueExplorerPlaceholder> {
	}

	@Inject
	public IssueExplorerPlaceholder(IssueExplorerPlaceholderUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
