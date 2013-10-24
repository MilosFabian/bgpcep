/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.pcep.impl.tlv;

import org.opendaylight.protocol.pcep.PCEPDeserializerException;
import org.opendaylight.protocol.pcep.spi.TlvParser;
import org.opendaylight.protocol.pcep.spi.TlvSerializer;
import org.opendaylight.protocol.util.ByteArray;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.OverloadDurationTlv;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.Tlv;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.notification.object.tlvs.OverloadDurationBuilder;

/**
 * Parser for {@link OverloadDurationTlv}
 */
public class OverloadedDurationTlvParser implements TlvParser, TlvSerializer {

	public static final int TYPE = 2;

	private static final int OVERLOADED_DURATION_LENGTH = 4;

	@Override
	public OverloadDurationTlv parseTlv(final byte[] buffer) throws PCEPDeserializerException {
		final long l = ByteArray.bytesToInt(ByteArray.subByte(buffer, 0, OVERLOADED_DURATION_LENGTH));
		return new OverloadDurationBuilder().setDuration(l).build();
	}

	@Override
	public byte[] serializeTlv(final Tlv tlv) {
		if (tlv == null) {
			throw new IllegalArgumentException("OverloadedTlv is mandatory.");
		}
		final OverloadDurationTlv odt = (OverloadDurationTlv) tlv;
		return ByteArray.subByte(ByteArray.longToBytes(odt.getDuration()), OVERLOADED_DURATION_LENGTH, OVERLOADED_DURATION_LENGTH);
	}

	@Override
	public int getType() {
		return TYPE;
	}
}
