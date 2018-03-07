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
package org.jboss.tools.rhamt.shared.impl;

import org.jboss.tools.rhamt.shared.RhamtEvent;
import org.jboss.tools.rhamt.shared.event.RhamtBeginTaskEvent;

public class RhamtBeginTaskEventImpl extends RhamtEventImpl implements RhamtBeginTaskEvent {
	
	private final String task;
	private final int totalWork;

	public RhamtBeginTaskEventImpl(String task, int totalWork) {
		super(RhamtEvent.TYPE.BEGIN_TASK);
		this.task = task;
		this.totalWork = totalWork;
	}

	@Override
	public String getTask() {
		return task;
	}
	
	@Override
	public int getTotalWork() {
		return totalWork;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtBeginTaskEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtBeginTaskEventImpl that = (RhamtBeginTaskEventImpl) o;

		return task.equals(that.task);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (task != null ? task.hashCode() : 0);
		return result;
	}
}
