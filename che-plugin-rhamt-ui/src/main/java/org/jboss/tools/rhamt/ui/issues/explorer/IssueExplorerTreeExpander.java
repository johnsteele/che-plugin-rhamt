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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.ui.smartTree.Tree;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.data.TreeExpander;

import com.google.common.base.Predicate;

final class IssueExplorerTreeExpander implements TreeExpander {

	private Tree tree;
	private AppContext appContext;

	public IssueExplorerTreeExpander(Tree tree, AppContext appContext) {
		this.tree = tree;
		this.appContext = appContext;
	}

	private final boolean[] everExpanded = new boolean[] { false };

	@Override
	public void expandTree() {
		if (everExpanded[0]) {
			tree.expandAll();
			return;
		}

		appContext.getWorkspaceRoot().getTree(-1).then(new Operation<Resource[]>() {
			@Override
			public void apply(Resource[] ignored) throws OperationException {
				everExpanded[0] = true;
				tree.expandAll();
			}
		});
	}

	@Override
	public boolean isExpandEnabled() {
		return tree.getNodeStorage().getAllItemsCount() != 0;
	}

	@Override
	public void collapseTree() {
		tree.collapseAll();
	}

	@Override
	public boolean isCollapseEnabled() {
		return any(tree.getRootNodes(), isExpanded());
	}

	private Predicate<Node> isExpanded() {
		return new Predicate<Node>() {
			@Override
			public boolean apply(@javax.annotation.Nullable Node node) {
				checkNotNull(node);
				return tree.isExpanded(node);
			}
		};
	}
}
