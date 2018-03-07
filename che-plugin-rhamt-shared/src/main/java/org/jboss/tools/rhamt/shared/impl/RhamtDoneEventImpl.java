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
import org.jboss.tools.rhamt.shared.event.RhamtDoneEvent;

public class RhamtDoneEventImpl extends RhamtEventImpl implements RhamtDoneEvent {
	
	private final boolean isDone;
	
	public RhamtDoneEventImpl(boolean isDone) {
		super(RhamtEvent.TYPE.DONE);
		this.isDone = isDone;
	}
	
	@Override
	public boolean isDone() {
		return isDone;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtDoneEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtDoneEventImpl that = (RhamtDoneEventImpl) o;

		return isDone == that.isDone;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (isDone ? 1 : 0);
		return result;
	}
}
