/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.evpn.impl.esi.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.opendaylight.protocol.bgp.evpn.impl.EvpnTestUtil.MAC;
import static org.opendaylight.protocol.bgp.evpn.impl.EvpnTestUtil.MAC_MODEL;
import static org.opendaylight.protocol.bgp.evpn.impl.EvpnTestUtil.VALUE_SIZE;
import static org.opendaylight.protocol.bgp.evpn.impl.EvpnTestUtil.createContBuilder;
import static org.opendaylight.protocol.bgp.evpn.impl.EvpnTestUtil.createValueBuilder;
import static org.opendaylight.protocol.bgp.evpn.impl.esi.types.EsiModelUtil.LD_NID;
import static org.opendaylight.protocol.bgp.evpn.impl.esi.types.EsiModelUtil.SYSTEM_MAC_NID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.protocol.util.ByteArray;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.Uint24;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.Esi;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.esi.ArbitraryCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.esi.MacAutoGeneratedCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.esi.MacAutoGeneratedCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.esi.mac.auto.generated._case.MacAutoGenerated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.evpn.rev160321.esi.esi.mac.auto.generated._case.MacAutoGeneratedBuilder;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.ContainerNode;

public final class MacParserTest {
    private static final byte[] VALUE = {(byte) 0xf2, (byte) 0x0c, (byte) 0xdd, (byte) 0x80, (byte) 0x9f, (byte) 0xf7, (byte) 0x02,
        (byte) 0x02, (byte) 0x00};
    private static final byte[] RESULT = {(byte) 0x03, (byte) 0xf2, (byte) 0x0c, (byte) 0xdd, (byte) 0x80, (byte) 0x9f, (byte) 0xf7, (byte) 0x02,
        (byte) 0x02, (byte) 0x00};
    private static final long UINT24_LD_MODEL = 131584L;
    private static final Uint24 UINT24_LD = new Uint24(UINT24_LD_MODEL);
    private MacParser parser;

    @Before
    public void setUp() {
        this.parser = new MacParser();
    }

    @Test
    public void parserTest() {
        final ByteBuf buff = Unpooled.buffer(VALUE_SIZE);

        final MacAutoGeneratedCase macAuto = new MacAutoGeneratedCaseBuilder().setMacAutoGenerated(new MacAutoGeneratedBuilder()
            .setLocalDiscriminator(UINT24_LD).setSystemMacAddress(MAC).build()).build();
        this.parser.serializeEsi(macAuto, buff);
        assertArrayEquals(RESULT, ByteArray.getAllBytes(buff));

        final Esi acResult = this.parser.parseEsi(Unpooled.wrappedBuffer(VALUE));
        assertEquals(macAuto, acResult);

        final ContainerNode cont = createContBuilder(new NodeIdentifier(MacAutoGenerated.QNAME)).addChild(createValueBuilder(MAC_MODEL, SYSTEM_MAC_NID).build())
            .addChild(createValueBuilder(UINT24_LD_MODEL, LD_NID).build()).build();
        final Esi acmResult = this.parser.serializeEsi(cont);
        assertEquals(macAuto, acmResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongCaseTest() {
        this.parser.serializeEsi(new ArbitraryCaseBuilder().build(), null);
    }
}