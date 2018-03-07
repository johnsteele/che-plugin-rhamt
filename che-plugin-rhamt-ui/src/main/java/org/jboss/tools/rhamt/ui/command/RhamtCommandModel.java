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
package org.jboss.tools.rhamt.ui.command;

import org.eclipse.che.ide.CommandLine;

class RhamtCommandModel {

	private String workingDirectory;
	private String arguments;

	RhamtCommandModel(String workingDirectory, String args) {
		this.workingDirectory = workingDirectory;
		this.arguments = args;
	}

	static RhamtCommandModel fromCommandLine(String commandLine) {
		final CommandLine cmd = new CommandLine(commandLine);

		String workingDirectory = null;

		cmd.removeArgument("rhamt-cli");
		String arguments = cmd.toString();

		return new RhamtCommandModel(workingDirectory, arguments);
	}

	String getWorkingDirectory() {
		return workingDirectory;
	}

	void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	String getArguments() {
		return arguments;
	}

	void setArguments(String args) {
		this.arguments = args;
	}

	String toCommandLine() {
		final StringBuilder cmd = new StringBuilder("rhamt-cli");

		if (!arguments.trim().isEmpty()) {
			cmd.append(' ').append(arguments.trim());
		}

		return cmd.toString();
	}
}
