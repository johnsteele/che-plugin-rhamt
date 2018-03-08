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
package org.jboss.tools.rhamt.ui.issues;

import java.util.Collections;
import java.util.List;

import org.eclipse.che.ide.api.parts.PartPresenter;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.eclipse.che.ide.api.selection.Selection;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.data.settings.NodeSettings;
import org.eclipse.che.ide.ui.smartTree.data.settings.SettingsProvider;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;
import org.jboss.tools.rhamt.ui.event.IssuesUpdatedEvent;
import org.jboss.tools.rhamt.ui.event.IssuesUpdatedEvent.IssuesUpdatedHandler;
import org.jboss.tools.rhamt.ui.issues.IssuesView.ActionDelegate;
import org.jboss.tools.rhamt.ui.issues.tree.IssueGroupNode.NodeFactory;

@Singleton
public class IssuesPresenter extends BasePresenter implements ActionDelegate, IssuesUpdatedHandler {

    private final IssuesView                    view;
    private final NodeFactory                   nodeFactory;
    private final SettingsProvider              settingsProvider;
    private final WorkspaceAgent                workspaceAgent;
    private final RhamtLocalizationConstant     locale;

    @Inject
    public IssuesPresenter(IssuesView view,
                           EventBus eventBus,
                           SettingsProvider settingsProvider,
                           NodeFactory nodeFactory,
                           WorkspaceAgent workspaceAgent,
                           RhamtLocalizationConstant locale) {
        this.view = view;
        this.nodeFactory = nodeFactory;
        this.settingsProvider = settingsProvider;
        this.workspaceAgent = workspaceAgent;
        this.locale = locale;
        this.view.setDelegate(this);

        workspaceAgent.getPartStack(PartStackType.TOOLING).addPart(this);

        eventBus.addHandler(IssuesUpdatedEvent.getType(), this);
    }

    public void show() {
        final PartPresenter activePart = partStack.getActivePart();

        if (activePart != null && activePart.equals(this)) {
            workspaceAgent.hidePart(this);
            return;
        }

        refreshView();
        workspaceAgent.setActivePart(this, PartStackType.TOOLING);
    }

    @Override
    public String getTitle() {
        return locale.issuesViewTitle();
    }

    @Override
    public IsWidget getView() {
        return view;
    }

    @Override
    public String getTitleToolTip() {
        return locale.issuesViewTooltip();
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void onSelectionChanged(List<Node> nodes) {
        setSelection(new Selection<>(nodes));
    }

    @Override
    public void onIssuesUpdated(IssuesUpdatedEvent event) {
        refreshView();
    }

    private void refreshView() {
        final NodeSettings settings = settingsProvider.getSettings();
        Path[] paths = new Path[] {};
        view.setIssues(Collections.<Node>singletonList(nodeFactory.newIssueGroupNode(paths, settings)));
    }
}

