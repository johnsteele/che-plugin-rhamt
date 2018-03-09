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

import com.google.gwt.i18n.client.Messages;

public interface RhamtLocalizationConstant extends Messages {
	  @Key("view.rhamtCommandPage.arguments.text")
	  String rhamtCommandPageViewArgumentsText();
	  @Key("action.show.issues.title")
	  String showIssuesActionTittle();
	  @Key("action.show.issues.description")
	  String showIssuesActionDescription();
	  @Key("view.issues.title")
	  String issuesViewTitle();
	  @Key("view.issues.tooltip")
	  String issuesViewTooltip();
	  @Key("view.issues.group.count")
	  String issues(int count);
	  @Key("view.issues.project.removed")
	  String projectRemoved(String projectName);
	  @Key("view.issues.project.remove.error")
	  String projectRemoveError(String projectName);
	  @Key("view.issues.project.update.error")
	  String projectUpdateError(String projectName);
	  @Key("view.issues.new.run.configuration")
	  String newRunConfigurationLabel();
	  @Key("view.issues.no.issues.title")
	  String noIssuesTitle();
}

