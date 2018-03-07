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
package org.jboss.tools.rhamt.ui;

import static org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode.FLOAT_MODE;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.PROGRESS;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.SUCCESS;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.jboss.tools.rhamt.shared.Constants.STARTED;
import static org.jboss.tools.rhamt.shared.Constants.STARTING;
import static org.jboss.tools.rhamt.shared.Constants.STARTING_ERROR;

import org.eclipse.che.api.core.jsonrpc.commons.RequestHandlerConfigurator;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtEventHandler {
	
	private StatusNotification notification;
	private final NotificationManager notificationManager;

	@Inject
	public RhamtEventHandler(NotificationManager notificationManager,
			RequestHandlerConfigurator configurator) {
		this.notificationManager = notificationManager;
		configureHandlers(configurator);
	}
	
	private void configureHandlers(RequestHandlerConfigurator configurator) {
		configurator.newConfiguration().methodName(STARTING).noParams().noResult()
				.withConsumer(this::starting);
		configurator.newConfiguration().methodName(STARTING_ERROR).noParams().noResult()
				.withConsumer(this::startingError);
		configurator.newConfiguration().methodName(STARTED).noParams().noResult()
				.withConsumer(this::started);
	}
	
	private void starting(String m) {
		this.notification = notificationManager.notify("Starting RHAMT server", PROGRESS, FLOAT_MODE);
		notification.setContent("");
	}
	
	private void startingError(String m) {
		notification.setTitle("RHAMT startup error");
		notification.setStatus(FAIL);
		notification.setContent("");
	}
	
	private void started(String m) {
		notification.setTitle("RHAMT server started");
		notification.setStatus(SUCCESS);
		notification.setContent("");
	}
}
