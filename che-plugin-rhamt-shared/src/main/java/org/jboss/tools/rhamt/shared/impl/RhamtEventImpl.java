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

public class RhamtEventImpl implements RhamtEvent {

	private final TYPE type;

	public RhamtEventImpl(TYPE type) {
		this.type = type;
	}

	@Override
	public TYPE getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RhamtEventImpl))
			return false;

		RhamtEventImpl that = (RhamtEventImpl) o;

		return type == that.type;
	}

	@Override
	public int hashCode() {
		return type != null ? type.hashCode() : 0;
	}
}
