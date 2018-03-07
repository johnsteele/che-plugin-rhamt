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

import org.eclipse.che.inject.DynaModule;
import org.jboss.tools.rhamt.core.model.ModelService;
import org.jboss.windup.tooling.WindupToolingProgressMonitor;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

@DynaModule
public class RhamtModule extends AbstractModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("che.rhamt.home")).to("rhamt");
		bind(ModelService.class);
		bind(RhamtServerManager.class);
		bind(RhamtService.class);
		bind(ModelService.class);
	    bind(WindupToolingProgressMonitor.class).to(RhamtProgressMonitorAdapter.class).in(Singleton.class);
		bind(RhamtRunner.class);
		bind(RhamtEventHandler.class);
	}
}
