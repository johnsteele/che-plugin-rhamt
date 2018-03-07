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

import static com.google.common.collect.Sets.newConcurrentHashSet;
import static org.jboss.tools.rhamt.shared.Constants.RHAMT_OUTPUT_SUBSCRIBE;
import static org.jboss.tools.rhamt.shared.Constants.RHAMT_OUTPUT_UNSUBSCRIBE;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.BEGIN_TASK;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.CANCELLED;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.DONE;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.LOG_MESSAGE;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.SUB_TASK;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.TASK_NAME;
import static org.jboss.tools.rhamt.shared.RhamtEvent.TYPE.WORKED;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.che.api.core.jsonrpc.commons.RequestHandlerConfigurator;
import org.eclipse.che.api.core.jsonrpc.commons.RequestTransmitter;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.core.notification.EventSubscriber;
import org.eclipse.che.dto.server.DtoFactory;
import org.jboss.tools.rhamt.shared.Constants;
import org.jboss.tools.rhamt.shared.RhamtEvent;
import org.jboss.tools.rhamt.shared.dto.BeginTaskDto;
import org.jboss.tools.rhamt.shared.dto.CancelledDto;
import org.jboss.tools.rhamt.shared.dto.DoneDto;
import org.jboss.tools.rhamt.shared.dto.LogMessageDto;
import org.jboss.tools.rhamt.shared.dto.SubTaskDto;
import org.jboss.tools.rhamt.shared.dto.TaskNameDto;
import org.jboss.tools.rhamt.shared.dto.WorkedDto;
import org.jboss.tools.rhamt.shared.event.RhamtBeginTaskEvent;
import org.jboss.tools.rhamt.shared.event.RhamtCancelledEvent;
import org.jboss.tools.rhamt.shared.event.RhamtDoneEvent;
import org.jboss.tools.rhamt.shared.event.RhamtLogMessageEvent;
import org.jboss.tools.rhamt.shared.event.RhamtSubTaskEvent;
import org.jboss.tools.rhamt.shared.event.RhamtTaskNameEvent;
import org.jboss.tools.rhamt.shared.event.RhamtWorkedEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtEventHandler implements EventSubscriber<RhamtEvent> {

	private final Set<String> endpointIds = newConcurrentHashSet();
	
	private EventService eventService;
	private RequestTransmitter transmitter;

	@Inject
	public RhamtEventHandler(EventService eventService, RequestTransmitter transmitter) {
		this.eventService = eventService;
		this.transmitter = transmitter;
	}

	@PostConstruct
	private void subscribe() {
		eventService.subscribe(this);
	}

	@PreDestroy
	private void unsubscribe() {
		eventService.unsubscribe(this);
	}

	@Inject
	private void configureHandlers(RequestHandlerConfigurator configurator) {
		configurator.newConfiguration().methodName(RHAMT_OUTPUT_SUBSCRIBE).noParams().noResult()
				.withConsumer(endpointIds::add);
		configurator.newConfiguration().methodName(RHAMT_OUTPUT_UNSUBSCRIBE).noParams().noResult()
				.withConsumer(endpointIds::remove);
	}
	
	@Override
	public void onEvent(RhamtEvent event) {
		switch (event.getType()) {
			case BEGIN_TASK:
				beginTask((RhamtBeginTaskEvent) event);
				break;
			case DONE:
				done((RhamtDoneEvent) event);
				break;
			case CANCELLED:
				cancelled((RhamtCancelledEvent) event);
				break;
			case TASK_NAME:
				taskName((RhamtTaskNameEvent) event);
				break;
			case SUB_TASK:
				subTask((RhamtSubTaskEvent) event);
				break;
			case WORKED:
				worked((RhamtWorkedEvent) event);
				break;
			case LOG_MESSAGE:
				logMessage((RhamtLogMessageEvent) event);
				break;
			case STARTING:
				starting();
				break;
			case STARTING_ERROR:
				startingError();
				break;
			case STARTED:
				started();
				break;
		}
	}
	
	private void beginTask(RhamtBeginTaskEvent event) {
		BeginTaskDto dto = DtoFactory.newDto(BeginTaskDto.class);
		dto.setTask(event.getTask());
		dto.setTotalWork(event.getTotalWork());
		dto.setType(BEGIN_TASK);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.BEGIN_TASK)
				.paramsAsDto(dto).sendAndSkipResult());
	}
	
	private void done(RhamtDoneEvent event) {
		DoneDto dto = DtoFactory.newDto(DoneDto.class);
		dto.setDone(event.isDone());
		dto.setType(DONE);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.DONE).paramsAsDto(dto)
				.sendAndSkipResult());
	}

	private void cancelled(RhamtCancelledEvent event) {
		CancelledDto dto = DtoFactory.newDto(CancelledDto.class);
		dto.setCancelled(event.isCancelled());
		dto.setType(CANCELLED);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.CANCELLED)
				.paramsAsDto(dto).sendAndSkipResult());
	}

	private void taskName(RhamtTaskNameEvent event) {
		TaskNameDto dto = DtoFactory.newDto(TaskNameDto.class);
		dto.setTaskName(event.getTaskName());
		dto.setType(TASK_NAME);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.TASK_NAME)
				.paramsAsDto(dto).sendAndSkipResult());
	}
	
	private void subTask(RhamtSubTaskEvent event) {
		SubTaskDto dto = DtoFactory.newDto(SubTaskDto.class);
		dto.setSubTask(event.getSubTask());
		dto.setType(SUB_TASK);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.SUB_TASK)
				.paramsAsDto(dto).sendAndSkipResult());
	}
	
	private void worked(RhamtWorkedEvent event) {
		WorkedDto dto = DtoFactory.newDto(WorkedDto.class);
		dto.setWork(event.getWork());
		dto.setType(WORKED);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.WORKED).paramsAsDto(dto)
				.sendAndSkipResult());
	}

	private void logMessage(RhamtLogMessageEvent event) {
		LogMessageDto dto = DtoFactory.newDto(LogMessageDto.class);
		dto.setLogMessage(event.getMessage());
		dto.setType(LOG_MESSAGE);
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.LOG_MSG).paramsAsDto(dto)
				.sendAndSkipResult());
	}
	
	private void starting() {
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.STARTING)
				.noParams().sendAndSkipResult());
	}
	
	private void started() {
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.STARTED)
				.noParams().sendAndSkipResult());
	}
	
	private void startingError() {
		endpointIds.forEach(it -> transmitter.newRequest().endpointId(it).methodName(Constants.STARTING_ERROR)
				.noParams().sendAndSkipResult());
	}
}
