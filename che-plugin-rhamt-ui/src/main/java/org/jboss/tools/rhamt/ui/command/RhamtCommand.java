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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.command.CommandPage;
import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.jboss.tools.rhamt.ui.RhamtResources;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtCommand implements CommandType {

	private static final String ID = "rhamt";
	private static final String COMMAND_TEMPLATE = "rhamt-cli --input ${current.project.path} --output ${current.project.path}/rhamt --target eap:7";

	private final List<CommandPage> pages;
	
	private final AppContext context;
	
	@Inject
	public RhamtCommand(RhamtResources resources, RhamtCommandPagePresenter page, IconRegistry iconRegistry, AppContext context) {
		pages = new LinkedList<>();
		pages.add(page);
		iconRegistry.registerIcon(new Icon("command.type." + ID, resources.rhamt()));
		this.context = context;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getDisplayName() {
		return "Rhamt";
	}

	@Override
	public String getDescription() {
		return "Command for executing Rhamt";
	}

	@Override
	public List<CommandPage> getPages() {
		return pages;
	}

	@Override
	public String getCommandLineTemplate() {
		return COMMAND_TEMPLATE;
	}

	@Override
	public String getPreviewUrlTemplate() {
//		PreivewHTMLAction 
//		final String contentUrl = ((File) selectedResource).getContentUrl();
//	      BrowserUtils.openInNewTab(agentURLModifier.modify(contentUrl));
//		context.getWorkspace().getConfig();
		String endpoint = context.getWsAgentServerApiEndpoint();
		return endpoint+"/${current.project.path}/rhamt/index.html";
	}
}
