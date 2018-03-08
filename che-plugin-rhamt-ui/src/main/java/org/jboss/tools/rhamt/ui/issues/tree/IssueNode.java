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
package org.jboss.tools.rhamt.ui.issues.tree;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.ide.api.editor.events.FileEvent;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.project.node.icon.NodeIconProvider;
import org.eclipse.che.ide.project.shared.NodesResources;
import org.eclipse.che.ide.resources.reveal.RevealResourceEvent;
import org.eclipse.che.ide.resources.tree.ResourceNode;
import org.eclipse.che.ide.ui.smartTree.data.HasAction;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.data.settings.NodeSettings;
import org.eclipse.che.ide.ui.smartTree.presentation.NodePresentation;
import org.eclipse.che.ide.util.Pair;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;


public class IssueNode extends ResourceNode<Resource> implements HasAction {

    private final EventBus eventBus;
    private final PromiseProvider promises;

    @Inject
    protected IssueNode(@Assisted Resource resource,
                           @Assisted NodeSettings nodeSettings,
                           NodesResources nodesResources,
                           NodeFactory nodeFactory,
                           EventBus eventBus,
                           Set<NodeIconProvider> nodeIconProviders,
                           PromiseProvider promises) {
        super(resource, nodeSettings, nodesResources, nodeFactory, eventBus, nodeIconProviders);
        this.eventBus = eventBus;
        this.promises = promises;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    protected Promise<List<Node>> getChildrenImpl() {
        return promises.resolve(Collections.<Node>emptyList());
    }

    @Override
    public void updatePresentation(@NotNull NodePresentation presentation) {
        super.updatePresentation(presentation);
        presentation.setInfoText(getData().getLocation().parent().toString());
        presentation.setInfoTextWrapper(Pair.of("(", ")"));
    }

    @Override
    public void actionPerformed() {
        if (getData().getResourceType() == Resource.FILE) {
            eventBus.fireEvent(FileEvent.createOpenFileEvent((File)getData()));
        } else {
            eventBus.fireEvent(new RevealResourceEvent(getData()));
        }
    }
}

