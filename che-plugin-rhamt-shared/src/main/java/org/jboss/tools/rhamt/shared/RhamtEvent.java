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
package org.jboss.tools.rhamt.shared;

public interface RhamtEvent {
	TYPE getType();

	enum TYPE {
		BEGIN_TASK, DONE, CANCELLED, TASK_NAME, SUB_TASK, WORKED, LOG_MESSAGE, STARTING, STARTING_ERROR, STARTED
	}
}
