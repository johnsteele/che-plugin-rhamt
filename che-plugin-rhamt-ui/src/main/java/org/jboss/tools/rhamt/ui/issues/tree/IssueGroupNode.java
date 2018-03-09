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

import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.project.node.SyntheticNode;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.data.settings.NodeSettings;
import org.eclipse.che.ide.ui.smartTree.presentation.NodePresentation;
import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;
import org.jboss.tools.rhamt.ui.RhamtResources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class IssueGroupNode extends SyntheticNode<Void> {

    private final Path[]                        paths;
    private final PromiseProvider               promises;
    private final NodeFactory                   nodeFactory;
    private final RhamtLocalizationConstant     locale;
    private final RhamtResources                resources;
    private final AppContext                    appContext;

    @Inject
    public IssueGroupNode(@Assisted Path[] paths,
                          @Assisted NodeSettings nodeSettings,
                          PromiseProvider promises,
                          NodeFactory nodeFactory,
                          RhamtLocalizationConstant locale,
                          RhamtResources resources,
                          AppContext appContext) {
        super(null, nodeSettings);
        this.paths = paths;
        this.promises = promises;
        this.nodeFactory = nodeFactory;
        this.locale = locale;
        this.resources = resources;
        this.appContext = appContext;
    }

    @Override
    protected Promise<List<Node>> getChildrenImpl() {
        if (paths == null || paths.length == 0) {
            return promises.resolve(Collections.<Node>emptyList());
        }

        int maxDepth = paths[0].segmentCount();

        for (int i = 1; i < paths.length; i++) {
            if (maxDepth < paths[i].segmentCount()) {
                maxDepth = paths[i].segmentCount();
            }
        }

        return appContext.getWorkspaceRoot().getTree(maxDepth).then(new Function<Resource[], List<Node>>() {
            @Override
            public List<Node> apply(Resource[] resources) throws FunctionException {
                final List<Node> nodes = newArrayListWithCapacity(paths.length);
                for (Path path : paths) {
                    for (Resource resource : resources) {
                        if (resource.getLocation().equals(path)) {
                            nodes.add(nodeFactory.newIssueNode(resource, getSettings()));
                        }
                    }
                }

                return nodes;
            }
        });
    }

    @Override
    public void updatePresentation(@NotNull NodePresentation presentation) {
        presentation.setPresentableText(locale.issues(paths != null ? paths.length : 0));
        presentation.setPresentableIcon(resources.rhamt());
    }

    @Override
    public String getName() {
        return "Issue Group";
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public interface NodeFactory {
        IssueGroupNode newIssueGroupNode(Path[] paths, NodeSettings nodeSettings);
        IssueNode newIssueNode(Resource resource, NodeSettings nodeSettings);
    }
}
