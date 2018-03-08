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
package org.jboss.tools.rhamt.ui.action;

import static java.util.Collections.singletonList;
import static org.eclipse.che.ide.part.perspectives.project.ProjectPerspective.PROJECT_PERSPECTIVE_ID;

import javax.validation.constraints.NotNull;

import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ShowIssuesAction extends AbstractPerspectiveAction {

	public static final String ID = "showIssues";

	private final IssuesPresenter presenter;

	@Inject
	public ShowIssuesAction(IssuesPresenter presenter, RhamtLocalizationConstant locale) {
		super(singletonList(PROJECT_PERSPECTIVE_ID), locale.showIssuesActionTittle(),
				locale.showIssuesActionDescription(), null, null);
		this.presenter = presenter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		presenter.show();
	}

	@Override
	public void updateInPerspective(@NotNull ActionEvent event) {
		event.getPresentation().setEnabledAndVisible(true);
	}
}
