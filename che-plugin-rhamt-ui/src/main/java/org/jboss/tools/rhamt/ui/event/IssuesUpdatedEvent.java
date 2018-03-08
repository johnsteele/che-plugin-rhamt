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
package org.jboss.tools.rhamt.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class IssuesUpdatedEvent extends GwtEvent<IssuesUpdatedEvent.IssuesUpdatedHandler> {

    public interface IssuesUpdatedHandler extends EventHandler {
        void onIssuesUpdated(IssuesUpdatedEvent event);
    }

    private static Type<IssuesUpdatedHandler> TYPE;

    public static Type<IssuesUpdatedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<IssuesUpdatedHandler>();
        }
        return TYPE;
    }

    @Override
    public Type<IssuesUpdatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(IssuesUpdatedHandler handler) {
        handler.onIssuesUpdated(this);
    }
}
