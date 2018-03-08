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

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;
import org.eclipse.che.ide.ui.smartTree.data.Node;

import com.google.inject.ImplementedBy;

@ImplementedBy(IssuesViewImpl.class)
public interface IssuesView extends View<IssuesView.ActionDelegate> {

    void setIssues(List<Node> nodes);
    void setVisible(boolean visible);

    interface ActionDelegate extends BaseActionDelegate {
        void onSelectionChanged(List<Node> nodes);
    }
}
