/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.rib.impl;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import org.opendaylight.protocol.bgp.rib.impl.spi.BGPPeerRegistry;
import org.opendaylight.protocol.bgp.rib.spi.BGPSessionNegotiatorFactory;
import org.opendaylight.protocol.bgp.rib.spi.SessionNegotiator;

public final class BGPServerSessionNegotiatorFactory implements BGPSessionNegotiatorFactory<BGPSessionImpl> {
    private final BGPPeerRegistry registry;

    public BGPServerSessionNegotiatorFactory(final BGPPeerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public SessionNegotiator getSessionNegotiator(final Channel channel, final Promise<BGPSessionImpl> promise) {
        return new BGPServerSessionNegotiator(promise, channel, this.registry);
    }
}
