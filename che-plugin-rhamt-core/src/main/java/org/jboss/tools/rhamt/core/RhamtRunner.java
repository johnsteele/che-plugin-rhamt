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

import org.jboss.tools.rhamt.core.model.ModelService;
import org.jboss.tools.rhamt.options.IOptionKeys;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtRunner {
	
	private static final Logger LOG = LoggerFactory.getLogger(RhamtRunner.class);
	
	private final RhamtServerManager rhamtManager;
	private final ModelService modelService;
	private final RhamtProgressMonitorAdapter progressMonitor;
	
	@Inject
	public RhamtRunner(RhamtServerManager rhamtManager, ModelService modelService, RhamtProgressMonitorAdapter progressMonitor) {
		this.rhamtManager = rhamtManager;
		this.modelService = modelService;
		this.progressMonitor = progressMonitor;
	}
	
	public void analyze(String input) {
		ConfigurationElement configuration = modelService.createConfiguration();
		modelService.createInput(configuration, input);
		analyze(configuration);
	}
	
	public void analyze(ConfigurationElement configuration) {
		 ExecutionBuilder execBuilder = rhamtManager.getServer();
		 String input = configuration.getInputs().get(0).getUri();
		 LOG.info("analyzing: " + input);
		 try {
			 LOG.info("Setting up RHAMT for analysis");
	         execBuilder.clear();
	         execBuilder.setInput(input);
	         LOG.info("Using input: " + input);
	         execBuilder.setOutput(configuration.getGeneratedReportsLocation());
	         LOG.info("Using output: " + configuration.getGeneratedReportsLocation());
	         execBuilder.setWindupHome(System.getenv("RHAMT_HOME"));
	         LOG.info("Using RHAMT_HOME: " + System.getenv("RHAMT_HOME"));
	         execBuilder.setProgressMonitor(progressMonitor);
	         execBuilder.setOption(IOptionKeys.sourceModeOption, true);
	         execBuilder.setOption(IOptionKeys.skipReportsRenderingOption, false);
	     	 execBuilder.ignore("\\.class$");
	         execBuilder.setOption(IOptionKeys.targetOption, Lists.newArrayList("eap:[7]"));
	         execBuilder.setOption("sourceMode", true);
	         LOG.info("Attempting to run the analysis");
	         ExecutionResults results = execBuilder.execute();
	         LOG.info("ExecutionBuilder has returned the RHAMT results");
	         modelService.populateConfiguration(configuration, results);
		 }
         catch (Exception e) {
        	 	LOG.error(e.getMessage(), e);
         }
	}
}
