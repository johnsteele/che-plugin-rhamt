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
import org.jboss.tools.rhamt.shared.event.RhamtWorkedEvent;

public class RhamtWorkedEventImpl extends RhamtEventImpl implements RhamtWorkedEvent {

	private int work;
	
	public RhamtWorkedEventImpl(int work) {
		super(RhamtEvent.TYPE.WORKED);
		this.work = work;
	}
	
	@Override
	public int getWork() {
		return work;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtWorkedEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtWorkedEventImpl that = (RhamtWorkedEventImpl) o;

		return work == that.work;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Integer.hashCode(work);
		return result;
	}
}
