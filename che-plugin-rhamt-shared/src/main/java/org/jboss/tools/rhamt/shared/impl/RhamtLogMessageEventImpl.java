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
import org.jboss.tools.rhamt.shared.event.RhamtLogMessageEvent;

public class RhamtLogMessageEventImpl extends RhamtEventImpl implements RhamtLogMessageEvent {

	private final String message;
	
	public RhamtLogMessageEventImpl(String message) {
		super(RhamtEvent.TYPE.LOG_MESSAGE);
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtLogMessageEventImpl))
			return false;
		if (!super.equals(o))
			return false;

		RhamtLogMessageEventImpl that = (RhamtLogMessageEventImpl) o;

		return message.equals(that.message);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (message != null ? message.hashCode() : 0);
		return result;
	}
}
