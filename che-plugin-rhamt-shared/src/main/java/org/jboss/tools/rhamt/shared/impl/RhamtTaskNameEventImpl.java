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
import org.jboss.tools.rhamt.shared.event.RhamtTaskNameEvent;

public class RhamtTaskNameEventImpl extends RhamtEventImpl implements RhamtTaskNameEvent {
	
	private String taskName;
	
	public RhamtTaskNameEventImpl(String taskName) {
		super(RhamtEvent.TYPE.TASK_NAME);
		this.taskName = taskName;
	}
	
	@Override
	public String getTaskName() {
		return taskName;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtTaskNameEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtTaskNameEventImpl that = (RhamtTaskNameEventImpl) o;

		return taskName.equals(that.taskName);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (taskName != null ? taskName.hashCode() : 0);
		return result;
	}
}
