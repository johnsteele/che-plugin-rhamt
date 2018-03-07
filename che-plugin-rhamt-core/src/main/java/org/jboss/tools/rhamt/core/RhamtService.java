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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.che.api.fs.server.PathTransformer;
import org.jboss.tools.rhamt.shared.dto.AnalyzeRequest;

import com.google.inject.Inject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/rhamt", description = "RHAMT REST API")
@Path("rhamt")
public class RhamtService {
	
	private final RhamtServerManager rhamtManager;
	private final RhamtRunner rhamtRunner;
	private final PathTransformer pathTransformer;
	
	@Inject
	public RhamtService(RhamtServerManager rhamtManager, PathTransformer pathTransformer, RhamtRunner rhamtRunner) {
		this.rhamtManager = rhamtManager;
		this.rhamtRunner = rhamtRunner;
		this.pathTransformer = pathTransformer;
	}
	
	@POST
	@ApiResponses({
		@ApiResponse(code = 200, message = "RHAMT started"),
		@ApiResponse(code = 500, message = "Cound't start RHAMT due to internal server error occurred")
	})
	@Path("/start")
	public Response start() {
		boolean started = rhamtManager.startServer();
		if (started) {
			return Response.status(200).entity("RHAMT started").build();
		}
		return Response.status(500).entity("Internal server error occurred").build();
	}

	@POST
	@ApiResponse(code = 200, message = "RHAMT stopped")
	@Path("/stop")
	public Response stop() {
		rhamtManager.stopServer();
		return Response.status(200).entity("RHAMT stopped").build();
	}
	
	@POST
	@ApiResponses({
		@ApiResponse(code = 200, message = "Analysis completed"),
		@ApiResponse(code = 500, message = "Cound't complete analysis due to internal server error occurred")
	})
	@Path("/analyze")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public Response analyze(AnalyzeRequest request) {
		String path = request.getFiles().get(0);
		boolean started = rhamtManager.startServer();
		if (started) {
			rhamtRunner.analyze(getAbsoluteResourcePath(path));
		}
		else {
			return Response.status(500).entity("The RHAMT server could not be started.").build();
		}
		return Response.status(200).entity("Analysis complete").build();
	}
	
	private String getAbsoluteResourcePath(String wsRelatedPath) {
		return pathTransformer.transform(wsRelatedPath).toString();
	}
}
