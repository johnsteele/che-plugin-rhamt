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

import java.io.File;
import java.rmi.RemoteException;

import javax.annotation.PreDestroy;

import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.commons.lang.execution.CommandLine;
import org.eclipse.che.commons.lang.execution.ProcessExecutor;
import org.eclipse.che.commons.lang.execution.ProcessHandler;
import org.eclipse.che.plugin.maven.server.rmi.RmiClient;
import org.eclipse.che.plugin.maven.server.rmi.RmiObjectWrapper;
import org.jboss.tools.rhamt.shared.RhamtEvent.TYPE;
import org.jboss.tools.rhamt.shared.impl.RhamtEventImpl;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtServerManager extends RmiObjectWrapper<ExecutionBuilder> {
	
	private static final Logger LOG = LoggerFactory.getLogger(RhamtServerManager.class);
	
	private final EventService eventService;
	private RmiClient<ExecutionBuilder> client;
	
	@Inject
	public RhamtServerManager(final EventService eventService) {
		this.eventService = eventService;
		this.client = new RmiClient<ExecutionBuilder>(ExecutionBuilder.class) {
			@Override
			protected ProcessExecutor getExecutor() {
				return createExecutor();
			}
		};
	}
	
	@Override
	protected ExecutionBuilder create() throws RemoteException {
		eventService.publish(new RhamtEventImpl(TYPE.STARTING));
		ExecutionBuilder executionBuilder = null;
		try {
			executionBuilder = client.acquire(this, "");
		} catch (Exception e) {
			eventService.publish(new RhamtEventImpl(TYPE.STARTING_ERROR));
		}
		if (executionBuilder == null) {
			eventService.publish(new RhamtEventImpl(TYPE.STARTING_ERROR));
		}
		else {
			eventService.publish(new RhamtEventImpl(TYPE.STARTED));
		}
		return executionBuilder;
	}
	
	private String computeRhamtExecutable() {
		String rhamtHome = System.getenv("RHAMT_HOME");
		LOG.info("Using RHAMT_HOME: " + rhamtHome);
		return new File(rhamtHome).toPath().resolve("bin").resolve("rhamt-cli").toString();
	}
	
	public boolean startServer() {
		ExecutionBuilder executionBuilder = null;
		LOG.info("Attempting to start the RHAMT server.");
		try {
			executionBuilder = super.getOrCreateWrappedObject();
			LOG.info("Got the ExecutionBuilder - " + executionBuilder);
		} catch (RemoteException e) {
			LOG.error("Unable to start the RHAMT server.", e);
		}
		return executionBuilder != null;
	}
	
	public void stopServer() {
		client.stopAll(true);
		cleanUp();
	}
	
	private ProcessExecutor createExecutor() {
		return () -> {
			CommandLine command = new CommandLine();
			command.setExecutablePath(computeRhamtExecutable());
			command.addParameter("--startServer");
			command.addParameter("-1");
			return new ProcessHandler(command.createProcess());
		};
	}
	
	public ExecutionBuilder getServer() {
		return super.getWrapped();
	}

	@PreDestroy
	public void shutdown() {
		client.stopAll(false);
		cleanUp();
	}
}
