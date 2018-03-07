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
import org.jboss.tools.rhamt.shared.event.RhamtCancelledEvent;

public class RhamtCancelledEventImpl extends RhamtEventImpl implements RhamtCancelledEvent {
	
	private final boolean isCancelled;
	
	public RhamtCancelledEventImpl(boolean isCancelled) {
		super(RhamtEvent.TYPE.CANCELLED);
		this.isCancelled = isCancelled;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtCancelledEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtCancelledEventImpl that = (RhamtCancelledEventImpl) o;

		return isCancelled == that.isCancelled;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (isCancelled ? 1 : 0);
		return result;
	}
}
