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
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.jboss.tools.rhamt.ui.action.AnalyzeAction;
import org.jboss.tools.rhamt.ui.action.StartRhamtAction;
import org.jboss.tools.rhamt.ui.action.StopRhamtAction;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Extension(title = "RHAMT Extension", version = "1.0.0")
public class RhamtExtension {

	@Inject
	public RhamtExtension(RhamtProgressSubscriber subscriber, RhamtEventHandler handler, ActionManager actionManager, StartRhamtAction startRhamtAction,
			StopRhamtAction stopRhamtAction, AnalyzeAction analyzeAction) {
		actionManager.registerAction("analyzeAction", analyzeAction);
		DefaultActionGroup mainContextMenuGroup = (DefaultActionGroup) actionManager
				.getAction(IdeActions.GROUP_MAIN_CONTEXT_MENU);
//		mainContextMenuGroup.add(startRhamtAction);
//		mainContextMenuGroup.add(stopRhamtAction);
		mainContextMenuGroup.add(analyzeAction);
		
		
		
	}
}
