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

import static com.google.common.base.Preconditions.checkState;
import static org.eclipse.che.ide.api.resources.Resource.FOLDER;
import static org.eclipse.che.ide.api.resources.Resource.PROJECT;

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.api.resources.Resource;
import org.jboss.tools.rhamt.ui.RhamtProgressSubscriber;
import org.jboss.tools.rhamt.ui.RhamtServiceClient;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AnalyzeAction extends AbstractPerspectiveAction {

	private final RhamtServiceClient rhamtClient;
    private final AppContext appContext;
    private final WorkspaceAgent workspaceAgent;
	private final RhamtProgressSubscriber progressSubscriber;
    
	@Inject
	public AnalyzeAction(
			AppContext appContext, 
			WorkspaceAgent workspaceAgent, 
			RhamtServiceClient serviceClient,
			RhamtProgressSubscriber progressSubscriber) {
		super(null, "Analyze", "Analyze using RHAMT");
		this.appContext = appContext;
		this.workspaceAgent = workspaceAgent;
		this.rhamtClient = serviceClient;
		this.progressSubscriber = progressSubscriber;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Resource resource = appContext.getResource();
	    checkState(resource != null, "AnalyzeProjectAction :: Null resource occurred");
	    analyze(resource);
	}
	
	private void analyze(Resource resource) {
		progressSubscriber.subscribe(resource);
		rhamtClient.analyze(Lists.newArrayList(resource.getLocation().toString())).then(new Operation<String>() {
			@Override
			public void apply(String response) throws OperationException {
				progressSubscriber.done();
			}
		}).catchError(new Operation<PromiseError>() {
			@Override
			public void apply(PromiseError error) throws OperationException {
				progressSubscriber.fail(error.getMessage());
			}
		});
	}

	@Override
	public void updateInPerspective(ActionEvent e) {
		if (workspaceAgent.getActivePart() == null || workspaceAgent.getActivePart() instanceof EditorPartPresenter) {
			e.getPresentation().setEnabledAndVisible(false);
			return;
		}

		final Resource[] resources = appContext.getResources();
		e.getPresentation().setVisible(true);

		if (resources == null || resources.length != 1) {
			e.getPresentation().setEnabled(false);
			return;
		}

		if (!isValidContainer(resources[0])) {
			e.getPresentation().setEnabled(false);
			return;
		}

		e.getPresentation().setEnabled(true);
	}
	
	private boolean isValidContainer(Resource resource) {
		return resource.getResourceType() == FOLDER || 
			resource.getResourceType() == PROJECT;
	}
}

