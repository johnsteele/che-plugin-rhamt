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
import org.jboss.tools.rhamt.shared.event.RhamtSubTaskEvent;

public class RhamtSubTaskEventImpl extends RhamtEventImpl implements RhamtSubTaskEvent {
	
	private final String subTask;
	
	public RhamtSubTaskEventImpl(String subTask) {
		super(RhamtEvent.TYPE.SUB_TASK);
		this.subTask = subTask;
	}

	@Override
	public String getSubTask() {
		return subTask;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtSubTaskEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtSubTaskEventImpl that = (RhamtSubTaskEventImpl) o;

		return subTask.equals(that.subTask);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (subTask != null ? subTask.hashCode() : 0);
		return result;
	}
}
