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
import static org.eclipse.che.ide.api.jsonrpc.Constants.WS_AGENT_JSON_RPC_ENDPOINT_ID;
import static org.jboss.tools.rhamt.shared.Constants.BEGIN_TASK;
import static org.jboss.tools.rhamt.shared.Constants.CANCELLED;
import static org.jboss.tools.rhamt.shared.Constants.DONE;
import static org.jboss.tools.rhamt.shared.Constants.LOG_MSG;
import static org.jboss.tools.rhamt.shared.Constants.RHAMT_OUTPUT_SUBSCRIBE;
import static org.jboss.tools.rhamt.shared.Constants.SUB_TASK;
import static org.jboss.tools.rhamt.shared.Constants.TASK_NAME;
import static org.jboss.tools.rhamt.shared.Constants.WORKED;

import org.eclipse.che.api.core.jsonrpc.commons.RequestHandlerConfigurator;
import org.eclipse.che.api.core.jsonrpc.commons.RequestTransmitter;
import org.jboss.tools.rhamt.shared.dto.BeginTaskDto;
import org.jboss.tools.rhamt.shared.dto.CancelledDto;
import org.jboss.tools.rhamt.shared.dto.DoneDto;
import org.jboss.tools.rhamt.shared.dto.LogMessageDto;
import org.jboss.tools.rhamt.shared.dto.SubTaskDto;
import org.jboss.tools.rhamt.shared.dto.TaskNameDto;
import org.jboss.tools.rhamt.shared.dto.WorkedDto;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtProgressEventHandler {
	
	private RhamtProgressSubscriber subscriber;
	private boolean isSubscribed = false;
	
	@Inject
	public RhamtProgressEventHandler(RequestHandlerConfigurator configurator) {
		configureHandlers(configurator);
	}

	private void configureHandlers(RequestHandlerConfigurator configurator) {
		configurator.newConfiguration().methodName(BEGIN_TASK).paramsAsDto(BeginTaskDto.class).noResult()
				.withConsumer(e -> notifyBeginTask(e));
		configurator.newConfiguration().methodName(DONE).paramsAsDto(DoneDto.class).noResult()
				.withConsumer(e -> notifyDone(e));
		configurator.newConfiguration().methodName(CANCELLED).paramsAsDto(CancelledDto.class).noResult()
				.withConsumer(e -> notifyCancelled(e));
		configurator.newConfiguration().methodName(TASK_NAME).paramsAsDto(TaskNameDto.class).noResult()
				.withConsumer(e -> notifyTaskName(e));
		configurator.newConfiguration().methodName(SUB_TASK).paramsAsDto(SubTaskDto.class).noResult()
				.withConsumer(e -> notifySubTask(e));
		configurator.newConfiguration().methodName(WORKED).paramsAsDto(WorkedDto.class).noResult()
				.withConsumer(e -> notifyWorked(e));
		configurator.newConfiguration().methodName(LOG_MSG).paramsAsDto(LogMessageDto.class).noResult()
				.withConsumer(e -> notifyLogMessage(e));
	}

	public void addSubscriber(String path, RhamtProgressSubscriber subscriber) {
		this.subscriber = subscriber;
	}

	public void removeSubscriber(String path, RhamtProgressSubscriber subscriber) {
		this.subscriber = null;
	}
	
	@Inject
	private void subscribe(RequestTransmitter requestTransmitter) {
		if (isSubscribed) {
			return;
		}
		requestTransmitter.newRequest().endpointId(WS_AGENT_JSON_RPC_ENDPOINT_ID).methodName(RHAMT_OUTPUT_SUBSCRIBE)
				.noParams().sendAndSkipResult();
		isSubscribed = true;
	}
	
	private void notifyBeginTask(BeginTaskDto dto) {
		subscriber.beginTask(dto.getTask(), dto.getTotalWork());
	}

	private void notifyDone(DoneDto dto) {
		subscriber.done();
	}

	private void notifyCancelled(CancelledDto dto) {
		subscriber.setCancelled(dto.isCancelled());
	}

	private void notifyTaskName(TaskNameDto dto) {
		subscriber.setTaskName(dto.getTaskName());
	}

	private void notifySubTask(SubTaskDto dto) {
		subscriber.subTask(dto.getSubTask());
	}

	private void notifyWorked(WorkedDto dto) {
		subscriber.worked(dto.getWork());
	}

	private void notifyLogMessage(LogMessageDto dto) {
		subscriber.logMessage(dto.getLogMessage());
	}
}
