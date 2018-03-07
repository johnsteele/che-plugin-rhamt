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
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.PROGRESS;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.SUCCESS;

import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.api.workspace.event.WsAgentServerStoppedEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class RhamtProgressSubscriber {
	
	private static enum Phase {
		INITIALIZE("InitializationPhase", "Initializing"),
		INIT_ANALYSIS("DiscoveryPhase", "Gathering rules"),
		DISCOVERY("InitialAnalysisPhase", "Rules loaded. Preparing to process"),
		MIGRATING("MigrationRulesPhase", "Analysis in progress. This will take a moment"),
		REPORT_GEN("ReportGenerationPhase", "Analysis complete. Preparing report"),
		REPORT_SAVE("ReportRenderingPhase", "Serializing report"),
		FINALIZE("PostFinalizePhasePhase", "Finalizing. One moment");
		
		public String id;
		public String desc;
		
		Phase(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}
		
		public boolean matches(String task) {
			return task.contains(id);
		}
	}

	private final NotificationManager notificationManager;
	private final RhamtProgressEventHandler progressSubscriable;
	
	private Resource resource;
	private String title;
	private StatusNotification notification;
	
	private int preWork;
	private int totalWork;
	
	@Inject
	public RhamtProgressSubscriber(
			NotificationManager notificationManager,
			RhamtProgressEventHandler progressSubscriable,
			EventBus eventBus) {
		this.notificationManager = notificationManager;
		this.progressSubscriable = progressSubscriable;
		eventBus.addHandler(WsAgentServerStoppedEvent.TYPE, e -> {
			unsubscribe(getPath());
			if (notification != null) {
				notification.setStatus(FAIL);
				notification.setContent("");
			}
		});
	}
	
	public void subscribe(Resource resource) {
		this.resource = resource;
		this.title = "Analyzing " + resource.getName();
		this.notification = notificationManager.notify(title, PROGRESS, FLOAT_MODE);
		progressSubscriable.addSubscriber(getPath(), this);
	}
	
	public void unsubscribe(String path) {
		progressSubscriable.removeSubscriber(path, this);
		this.totalWork = 0;
		this.preWork = 0;
	}
	
	public void beginTask(String task, int totalWork) {
		notification.setTitle(title);
		this.totalWork = totalWork;
	}

	public void done() {
		unsubscribe(getPath());
		if (notification != null) {
			notification.setTitle(title);
			notification.setStatus(SUCCESS);
			notification.setContent("");
		}
	}

	public void setCancelled(boolean value) {
		unsubscribe(getPath());
		notification.setStatus(SUCCESS);
		notification.setTitle("Cancelled");
		notification.setContent("");
	}

	public void setTaskName(String name) {
		notification.setContent(name);
	}

	public void subTask(String name) {
		Phase phase = findPhase(name);
		if (phase != null) {
			notification.setContent(phase.desc);	
		}
		else {
			notification.setContent("");
		}
	}
	
	private Phase findPhase(String task) {
		for (Phase phase : Phase.values()) {
			if (phase.matches(task)) {
				return phase;
			}
		}
		return null;
	}

	public void worked(int work) {
		preWork += work;
		int done = getPercentangeDone();
		String label = title;
		if (done > 0) {
			label+= " (" + done + "% done)";
		}
		notification.setTitle(label);
	}
	
	private int getPercentangeDone() {
		return Math.min((int) (preWork * 100 / totalWork), 100);
	}

	public void logMessage(String message) {
	}
	
	public String getPath() {
		return resource.getLocation().toString();
	}
	
	public void fail(String errorMessage) {
		unsubscribe(getPath());
		notification.setTitle(errorMessage);
		notification.setStatus(FAIL);
	}
}
