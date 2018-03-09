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

import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.extension.Extension;
import org.jboss.tools.rhamt.ui.action.AnalyzeAction;
import org.jboss.tools.rhamt.ui.action.NewRunConfigurationAction;
import org.jboss.tools.rhamt.ui.action.ShowIssueExplorerAction;
import org.jboss.tools.rhamt.ui.action.StartRhamtAction;
import org.jboss.tools.rhamt.ui.action.StopRhamtAction;

import static org.eclipse.che.ide.api.action.IdeActions.GROUP_MAIN_MENU;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_PROFILE;
import static org.eclipse.che.ide.api.constraints.Anchor.BEFORE;
import org.eclipse.che.ide.api.constraints.Constraints;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Extension(title = "RHAMT Extension", version = "1.0.0")
public class RhamtExtension {
	
	private static final String COMMAND_GROUP_MAIN_MENU = "MigrationCommandGroupMenu";
	
	@Inject
	public RhamtExtension(RhamtProgressSubscriber subscriber, RhamtEventHandler handler, ActionManager actionManager, StartRhamtAction startRhamtAction,
			StopRhamtAction stopRhamtAction, AnalyzeAction analyzeAction, ShowIssueExplorerAction showIssueExplorerAction, 
			NewRunConfigurationAction newRunConfigAction) {
		
		actionManager.registerAction(AnalyzeAction.ID, analyzeAction);
		actionManager.registerAction(ShowIssueExplorerAction.ID, showIssueExplorerAction);
		actionManager.registerAction(NewRunConfigurationAction.ID, newRunConfigAction);
//		actionManager.registerAction(StartRhamtAction.ID, startRhamtAction);
//		actionManager.registerAction(StopRhamtAction.ID, stopRhamtAction);
		
		
	    DefaultActionGroup mainMenu = (DefaultActionGroup) actionManager.getAction(GROUP_MAIN_MENU);
		DefaultActionGroup migration = new DefaultActionGroup("Migration", true, actionManager);
		actionManager.registerAction("migration", migration);
		mainMenu.add(migration, new Constraints(BEFORE, GROUP_PROFILE));
		
		DefaultActionGroup commandGroup =
		        new DefaultActionGroup(COMMAND_GROUP_MAIN_MENU, false, actionManager);
		actionManager.registerAction("migrationCommandGroup", commandGroup);
		migration.add(commandGroup);
		migration.addSeparator();
		commandGroup.add(showIssueExplorerAction);
		commandGroup.addSeparator();
		commandGroup.add(startRhamtAction);
	    commandGroup.add(stopRhamtAction);
	    commandGroup.addSeparator();
	    commandGroup.add(newRunConfigAction);
		
		DefaultActionGroup migrationContextMenuGroup = new DefaultActionGroup("Migration", true, actionManager);
	    actionManager.registerAction("migrationContextMenu", migrationContextMenuGroup);
	    migrationContextMenuGroup.add(analyzeAction);
	    migrationContextMenuGroup.addSeparator();
//	    migrationContextMenuGroup.add(startRhamtAction);
//	    migrationContextMenuGroup.add(stopRhamtAction);
	    migrationContextMenuGroup.addSeparator();
	    migrationContextMenuGroup.add(newRunConfigAction);
	}
}
