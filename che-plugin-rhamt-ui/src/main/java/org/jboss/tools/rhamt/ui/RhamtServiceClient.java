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
package org.jboss.tools.rhamt.ui;

import java.util.List;

import org.eclipse.che.api.promises.client.Promise;

public interface RhamtServiceClient {
	Promise<String> start();
	Promise<String> stop();
	Promise<String> analyze(List<String> paths);
}
