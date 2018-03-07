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

import java.util.List;

import javax.inject.Inject;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.StringUnmarshaller;
import org.jboss.tools.rhamt.shared.dto.AnalyzeRequest;

import com.google.inject.Singleton;

@Singleton
public class RhamtServiceClientImpl implements RhamtServiceClient {
	
	private final AppContext appContext;
	private final AsyncRequestFactory asyncRequestFactory;
	private final DtoFactory dtoFactory;

	@Inject
	public RhamtServiceClientImpl(final AppContext appContext, final AsyncRequestFactory asyncRequestFactory,
			final DtoFactory dtoFactory) {
		this.appContext = appContext;
		this.asyncRequestFactory = asyncRequestFactory;
		this.dtoFactory = dtoFactory;
	}

	@Override
	public Promise<String> start() {		
		return asyncRequestFactory.createPostRequest(appContext.getWsAgentServerApiEndpoint() + "/rhamt/start", null)
				.send(new StringUnmarshaller());
	}
	
	@Override
	public Promise<String> stop() {
		return asyncRequestFactory.createPostRequest(appContext.getWsAgentServerApiEndpoint() + "/rhamt/stop", null)
				.send(new StringUnmarshaller());
	}
	
	@Override
	public Promise<String> analyze(List<String> paths) {
		AnalyzeRequest request = dtoFactory.createDto(AnalyzeRequest.class).withFiles(paths);
		String url = appContext.getWsAgentServerApiEndpoint() + "/rhamt/analyze";
		return asyncRequestFactory.createPostRequest(url, request)
				.send(new StringUnmarshaller());
	}
}
