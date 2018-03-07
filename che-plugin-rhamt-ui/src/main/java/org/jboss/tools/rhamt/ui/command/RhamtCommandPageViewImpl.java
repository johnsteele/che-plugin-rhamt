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

import org.jboss.tools.rhamt.ui.RhamtLocalizationConstant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class RhamtCommandPageViewImpl implements RhamtCommandPageView {

	private static final RhamtPageViewImplUiBinder UI_BINDER = GWT.create(RhamtPageViewImplUiBinder.class);

	private final FlowPanel rootElement;

	@UiField
	TextBox workingDirectory;
	@UiField
	TextBox arguments;

	@UiField(provided = true)
	RhamtLocalizationConstant locale;

	private ActionDelegate delegate;

	@Inject
	public RhamtCommandPageViewImpl(RhamtLocalizationConstant locale) {
		this.locale = locale;
		rootElement = UI_BINDER.createAndBindUi(this);
	}

	@Override
	public void setDelegate(ActionDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public Widget asWidget() {
		return rootElement;
	}

	@Override
	public String getWorkingDirectory() {
		return workingDirectory.getValue();
	}

	@Override
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory.setValue(workingDirectory);
	}

	@Override
	public String getArguments() {
		return arguments.getValue();
	}

	@Override
	public void setArguments(String args) {
		this.arguments.setValue(args);
	}

	@UiHandler({ "workingDirectory" })
	void onWorkingDirectoryChanged(KeyUpEvent event) {
		new Timer() {
			@Override
			public void run() {
				delegate.onWorkingDirectoryChanged();
			}
		}.schedule(0);
	}

	@UiHandler({ "arguments" })
	void onArgumentsChanged(KeyUpEvent event) {
		new Timer() {
			@Override
			public void run() {
				delegate.onArgumentsChanged();
			}
		}.schedule(0);
	}

	interface RhamtPageViewImplUiBinder extends UiBinder<FlowPanel, RhamtCommandPageViewImpl> {
	}
}
