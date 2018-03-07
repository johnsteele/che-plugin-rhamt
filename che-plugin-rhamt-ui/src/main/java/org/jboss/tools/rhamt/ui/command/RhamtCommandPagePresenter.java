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

import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.command.CommandPage;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RhamtCommandPagePresenter implements RhamtCommandPageView.ActionDelegate, CommandPage {

	private final RhamtCommandPageView view;

	private CommandImpl editedCommand;
	private RhamtCommandModel editedCommandModel;

	private String workingDirectoryInitial;
	private String argumentsInitial;

	private DirtyStateListener listener;

	@Inject
	public RhamtCommandPagePresenter(RhamtCommandPageView view) {
		this.view = view;
		view.setDelegate(this);
	}

	@Override
	public void resetFrom(CommandImpl command) {
		editedCommand = command;
		editedCommandModel = RhamtCommandModel.fromCommandLine(command.getCommandLine());
		workingDirectoryInitial = editedCommandModel.getWorkingDirectory();
		argumentsInitial = editedCommandModel.getArguments();
	}

	@Override
	public void go(AcceptsOneWidget container) {
		container.setWidget(view);
		view.setWorkingDirectory(editedCommandModel.getWorkingDirectory());
		view.setArguments(editedCommandModel.getArguments());
	}

	@Override
	public void onSave() {
		workingDirectoryInitial = editedCommandModel.getWorkingDirectory();
		argumentsInitial = editedCommandModel.getArguments();
	}

	@Override
	public boolean isDirty() {
		return !(workingDirectoryInitial.equals(editedCommandModel.getWorkingDirectory())
				&& argumentsInitial.equals(editedCommandModel.getArguments()));
	}

	@Override
	public void setDirtyStateListener(DirtyStateListener listener) {
		this.listener = listener;
	}

	@Override
	public void setFieldStateActionDelegate(FieldStateActionDelegate delegate) {
	}

	@Override
	public void onWorkingDirectoryChanged() {
		editedCommandModel.setWorkingDirectory(view.getWorkingDirectory());
		editedCommand.setCommandLine(editedCommandModel.toCommandLine());
		listener.onDirtyStateChanged();
	}

	@Override
	public void onArgumentsChanged() {
		editedCommandModel.setArguments(view.getArguments());
		editedCommand.setCommandLine(editedCommandModel.toCommandLine());
		listener.onDirtyStateChanged();
	}
}
