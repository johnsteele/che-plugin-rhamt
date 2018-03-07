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

import com.google.inject.Inject;

import static org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode.FLOAT_MODE;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.PROGRESS;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.SUCCESS;

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.jboss.tools.rhamt.ui.RhamtServiceClientImpl;

public class StopRhamtAction extends BaseAction {

	private final NotificationManager notificationManager;
	private final RhamtServiceClientImpl rhamtClient;
	
	@Inject
	public StopRhamtAction(final NotificationManager notificationManager, final RhamtServiceClientImpl serviceClient) {
		super("Stop RHAMT server", "Stop the RHAMT server");
		this.notificationManager = notificationManager;
		this.rhamtClient = serviceClient;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		StatusNotification notification = notificationManager.notify("Stopping RHAMT", PROGRESS, FLOAT_MODE);
		rhamtClient.stop().then(new Operation<String>() {
			@Override
			public void apply(String response) throws OperationException {
				notification.setTitle(response);
				notification.setStatus(SUCCESS);
			}
		}).catchError(new Operation<PromiseError>() {
			@Override
			public void apply(PromiseError error) throws OperationException {
				notification.setTitle(error.getMessage());
				notification.setStatus(FAIL);
			}
		});
	}
}