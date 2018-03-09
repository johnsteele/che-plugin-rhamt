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

import java.util.List;

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;
import org.eclipse.che.ide.ui.smartTree.Tree;
import org.eclipse.che.ide.ui.smartTree.data.Node;

public interface IssueExplorerView extends View<IssueExplorerView.ActionDelegate> {
	boolean setGoIntoModeOn(Node node);
	void setGoIntoModeOff();
	boolean isGoIntoActivated();
	void reloadChildren(Node parent);
	void reloadChildren(Node parent, boolean deep);
	void reloadChildrenByType(Class<?> type);
	void collapseAll();
	void showHiddenFilesForAllExpandedNodes(boolean show);
	void select(Node item, boolean keepExisting);
	void select(List<Node> items, boolean keepExisting);
	void setVisible(boolean visible);
	Tree getTree();
	void showPlaceholder(boolean placeholder);
	interface ActionDelegate extends BaseActionDelegate {	}
}
