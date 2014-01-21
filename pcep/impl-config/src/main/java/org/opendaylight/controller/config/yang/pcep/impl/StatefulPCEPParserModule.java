/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Generated file

 * Generated from: yang module name: odl-pcep-impl-cfg  yang module local name: pcep-parser-stateful
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Tue Jan 14 11:25:06 CET 2014
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.pcep.impl;

import org.opendaylight.protocol.pcep.ietf.stateful07.StatefulActivator;

/**
 *
 */
public final class StatefulPCEPParserModule extends org.opendaylight.controller.config.yang.pcep.impl.AbstractStatefulPCEPParserModule {

	public StatefulPCEPParserModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
			final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
		super(identifier, dependencyResolver);
	}

	public StatefulPCEPParserModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
			final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, final StatefulPCEPParserModule oldModule,
			final java.lang.AutoCloseable oldInstance) {

		super(identifier, dependencyResolver, oldModule, oldInstance);
	}

	@Override
	public java.lang.AutoCloseable createInstance() {
		return new StatefulActivator();
	}
}
