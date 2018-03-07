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
package org.jboss.tools.rhamt.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.LogRecord;

import org.eclipse.che.api.core.notification.EventService;
import org.jboss.tools.rhamt.shared.impl.RhamtBeginTaskEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtCancelledEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtDoneEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtLogMessageEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtSubTaskEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtTaskNameEventImpl;
import org.jboss.tools.rhamt.shared.impl.RhamtWorkedEventImpl;
import org.jboss.windup.tooling.WindupToolingProgressMonitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtProgressMonitorAdapter extends UnicastRemoteObject implements WindupToolingProgressMonitor, Remote {
	
	private static final long serialVersionUID = 1L;
	
	private EventService eventService;
	
	@Inject
    public RhamtProgressMonitorAdapter(EventService eventService) throws RemoteException {
		this.eventService = eventService;
    }

	@Override
	public void beginTask(String task, int totalWork) throws RemoteException {
		eventService.publish(new RhamtBeginTaskEventImpl(task, totalWork));
	}

	@Override
	public void done() throws RemoteException {
		eventService.publish(new RhamtDoneEventImpl(true));
	}

	@Override
	public boolean isCancelled() throws RemoteException {
		return false;
	}

	@Override
	public void setCancelled(boolean value) throws RemoteException {
		eventService.publish(new RhamtCancelledEventImpl(value));
	}

	@Override
	public void setTaskName(String name) throws RemoteException {
		eventService.publish(new RhamtTaskNameEventImpl(name));
	}

	@Override
	public void subTask(String name) throws RemoteException {
		eventService.publish(new RhamtSubTaskEventImpl(name));
	}

	@Override
	public void worked(int work) throws RemoteException {
		eventService.publish(new RhamtWorkedEventImpl(work));
	}

	@Override
	public void logMessage(LogRecord logRecord) {
		eventService.publish(new RhamtLogMessageEventImpl(logRecord.getMessage()));
	}
}