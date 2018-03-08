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

import java.util.List;

import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.ui.smartTree.NodeLoader;
import org.eclipse.che.ide.ui.smartTree.NodeStorage;
import org.eclipse.che.ide.ui.smartTree.Tree;
import org.eclipse.che.ide.ui.smartTree.data.Node;
import org.eclipse.che.ide.ui.smartTree.event.SelectionChangedEvent;
import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IssuesViewImpl extends BaseView<IssuesView.ActionDelegate> implements IssuesView {

	private Tree tree;

	@Inject
	public IssuesViewImpl(RhamtLocalizationConstant locale) {

		setTitle(locale.issuesViewTitle());

		tree = new Tree(new NodeStorage(), new NodeLoader());
		tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler() {
			public void onSelectionChanged(SelectionChangedEvent event) {
				delegate.onSelectionChanged(event.getSelection());
			}
		});
		tree.setAutoSelect(true);

		setContentWidget(tree);
	}

	@Override
	public void setIssues(List<Node> nodes) {
		tree.getNodeStorage().clear();
		tree.getNodeStorage().add(nodes);
		tree.expandAll();
	}
}

