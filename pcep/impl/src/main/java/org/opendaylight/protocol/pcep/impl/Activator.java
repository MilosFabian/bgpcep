/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.pcep.impl;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.protocol.pcep.impl.message.PCEPCloseMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPErrorMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPKeepAliveMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPNotificationMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPOpenMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPReplyMessageParser;
import org.opendaylight.protocol.pcep.impl.message.PCEPRequestMessageParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPBandwidthObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPClassTypeObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPCloseObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPEndPointsIpv4ObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPEndPointsIpv6ObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPErrorObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPExcludeRouteObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPExistingBandwidthObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPExplicitRouteObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPGlobalConstraintsObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPIncludeRouteObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPLoadBalancingObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPLspaObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPMetricObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPNoPathObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPNotificationObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPObjectiveFunctionObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPOpenObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPPathKeyObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPReportedRouteObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPRequestParameterObjectParser;
import org.opendaylight.protocol.pcep.impl.object.PCEPSvecObjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROAsNumberSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROIpv4PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROIpv6PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROLabelSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROPathKey128SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROPathKey32SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.EROUnnumberedInterfaceSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.GeneralizedLabelParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROIpv4PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROIpv6PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROLabelSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROPathKey128SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROPathKey32SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.RROUnnumberedInterfaceSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.Type1LabelParser;
import org.opendaylight.protocol.pcep.impl.subobject.WavebandSwitchingLabelParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROAsNumberSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROIpv4PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROIpv6PrefixSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROPathKey128SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROPathKey32SubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROSRLGSubobjectParser;
import org.opendaylight.protocol.pcep.impl.subobject.XROUnnumberedInterfaceSubobjectParser;
import org.opendaylight.protocol.pcep.impl.tlv.NoPathVectorTlvParser;
import org.opendaylight.protocol.pcep.impl.tlv.OFListTlvParser;
import org.opendaylight.protocol.pcep.impl.tlv.OrderTlvParser;
import org.opendaylight.protocol.pcep.impl.tlv.OverloadedDurationTlvParser;
import org.opendaylight.protocol.pcep.impl.tlv.ReqMissingTlvParser;
import org.opendaylight.protocol.pcep.spi.EROSubobjectRegistry;
import org.opendaylight.protocol.pcep.spi.LabelRegistry;
import org.opendaylight.protocol.pcep.spi.ObjectRegistry;
import org.opendaylight.protocol.pcep.spi.PCEPExtensionProviderContext;
import org.opendaylight.protocol.pcep.spi.TlvRegistry;
import org.opendaylight.protocol.pcep.spi.pojo.AbstractPCEPExtensionProviderActivator;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Close;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Keepalive;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Pcerr;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Pcntf;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Pcrep;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Pcreq;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.bandwidth.object.Bandwidth;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.classtype.object.ClassType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.close.object.CClose;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.endpoints.object.EndpointsObj;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.exclude.route.object.Xro;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.explicit.route.object.Ero;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.explicit.route.object.ero.subobject.subobject.type.PathKeyCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.gc.object.Gc;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.include.route.object.Iro;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.load.balancing.object.LoadBalancing;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.lspa.object.Lspa;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.metric.object.Metric;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.notification.object.CNotification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.of.list.tlv.OfList;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.of.object.Of;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.open.object.Open;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.order.tlv.Order;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.overload.duration.tlv.OverloadDuration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.pcep.error.object.ErrorObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.pcrep.message.pcrep.message.replies.result.failure._case.NoPath;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.pcrep.message.pcrep.message.replies.result.failure._case.no.path.tlvs.NoPathVector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.reported.route.object.Rro;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.req.missing.tlv.ReqMissing;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.rp.object.Rp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.svec.object.Svec;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.subobject.type.AsNumberCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.subobject.type.IpPrefixCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.subobject.type.LabelCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.subobject.type.SrlgCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.basic.explicit.route.subobjects.subobject.type.UnnumberedCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.label.subobject.label.type.GeneralizedLabelCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.label.subobject.label.type.Type1LabelCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.label.subobject.label.type.WavebandSwitchingLabelCase;

public final class Activator extends AbstractPCEPExtensionProviderActivator {
    @Override
    protected List<AutoCloseable> startImpl(final PCEPExtensionProviderContext context) {
        final List<AutoCloseable> regs = new ArrayList<>();

        registerLabelParsers(regs, context);

        final LabelRegistry labelReg = context.getLabelHandlerRegistry();
        registerEROParsers(regs, context, labelReg);
        registerRROParsers(regs, context, labelReg);
        registerXROParsers(regs, context);
        registerTlvParsers(regs, context);
        registerObjectParsers(regs, context);

        final ObjectRegistry objReg = context.getObjectHandlerRegistry();
        final PCEPOpenMessageParser openParser = new PCEPOpenMessageParser(objReg);
        context.registerMessageParser(PCEPOpenMessageParser.TYPE, openParser);
        context.registerMessageSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.message.rev131007.Open.class,
            openParser);

        final PCEPKeepAliveMessageParser kaParser = new PCEPKeepAliveMessageParser(objReg);
        context.registerMessageParser(PCEPKeepAliveMessageParser.TYPE, kaParser);
        context.registerMessageSerializer(Keepalive.class, kaParser);

        final PCEPRequestMessageParser reqParser = new PCEPRequestMessageParser(objReg);
        context.registerMessageParser(PCEPRequestMessageParser.TYPE, reqParser);
        context.registerMessageSerializer(Pcreq.class, reqParser);

        final PCEPReplyMessageParser repParser = new PCEPReplyMessageParser(objReg);
        context.registerMessageParser(PCEPReplyMessageParser.TYPE, repParser);
        context.registerMessageSerializer(Pcrep.class, repParser);

        final PCEPNotificationMessageParser notParser = new PCEPNotificationMessageParser(objReg);
        context.registerMessageParser(PCEPNotificationMessageParser.TYPE, notParser);
        context.registerMessageSerializer(Pcntf.class, notParser);

        final PCEPErrorMessageParser errParser = new PCEPErrorMessageParser(objReg);
        context.registerMessageParser(PCEPErrorMessageParser.TYPE, errParser);
        context.registerMessageSerializer(Pcerr.class, errParser);

        final PCEPCloseMessageParser closeParser = new PCEPCloseMessageParser(objReg);
        context.registerMessageParser(PCEPCloseMessageParser.TYPE, closeParser);
        context.registerMessageSerializer(Close.class, closeParser);
        return regs;
    }

    private void registerObjectParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context) {
        final TlvRegistry tlvReg = context.getTlvHandlerRegistry();
        final PCEPOpenObjectParser openParser = new PCEPOpenObjectParser(tlvReg);
        context.registerObjectParser(PCEPOpenObjectParser.CLASS, PCEPOpenObjectParser.TYPE, openParser);
        context.registerObjectSerializer(Open.class, openParser);

        final PCEPRequestParameterObjectParser rpParser = new PCEPRequestParameterObjectParser(tlvReg);
        context.registerObjectParser(PCEPRequestParameterObjectParser.CLASS, PCEPRequestParameterObjectParser.TYPE, rpParser);
        context.registerObjectSerializer(Rp.class, rpParser);

        final PCEPNoPathObjectParser noPathParser = new PCEPNoPathObjectParser(tlvReg);
        context.registerObjectParser(PCEPNoPathObjectParser.CLASS, PCEPNoPathObjectParser.TYPE, noPathParser);
        context.registerObjectSerializer(NoPath.class, noPathParser);

        final PCEPEndPointsIpv4ObjectParser endpoints4Parser = new PCEPEndPointsIpv4ObjectParser(tlvReg);
        context.registerObjectParser(PCEPEndPointsIpv4ObjectParser.CLASS, PCEPEndPointsIpv4ObjectParser.TYPE, endpoints4Parser);
        context.registerObjectParser(PCEPEndPointsIpv6ObjectParser.CLASS, PCEPEndPointsIpv6ObjectParser.TYPE,
            new PCEPEndPointsIpv4ObjectParser(tlvReg));
        context.registerObjectSerializer(EndpointsObj.class, endpoints4Parser);

        final PCEPBandwidthObjectParser bwParser = new PCEPBandwidthObjectParser(tlvReg);
        context.registerObjectParser(PCEPBandwidthObjectParser.CLASS, PCEPBandwidthObjectParser.TYPE, bwParser);
        context.registerObjectParser(PCEPExistingBandwidthObjectParser.CLASS, PCEPExistingBandwidthObjectParser.TYPE,
            new PCEPExistingBandwidthObjectParser(tlvReg));
        context.registerObjectSerializer(Bandwidth.class, bwParser);

        final PCEPMetricObjectParser metricParser = new PCEPMetricObjectParser(tlvReg);
        context.registerObjectParser(PCEPMetricObjectParser.CLASS, PCEPMetricObjectParser.TYPE, metricParser);
        context.registerObjectSerializer(Metric.class, metricParser);

        final EROSubobjectRegistry eroSubReg = context.getEROSubobjectHandlerRegistry();
        final PCEPExplicitRouteObjectParser eroParser = new PCEPExplicitRouteObjectParser(eroSubReg);
        context.registerObjectParser(PCEPExplicitRouteObjectParser.CLASS, PCEPExplicitRouteObjectParser.TYPE, eroParser);
        context.registerObjectSerializer(Ero.class, eroParser);

        final PCEPReportedRouteObjectParser rroParser = new PCEPReportedRouteObjectParser(context.getRROSubobjectHandlerRegistry());
        context.registerObjectParser(PCEPReportedRouteObjectParser.CLASS, PCEPReportedRouteObjectParser.TYPE, rroParser);
        context.registerObjectSerializer(Rro.class, rroParser);

        final PCEPLspaObjectParser lspaParser = new PCEPLspaObjectParser(tlvReg);
        context.registerObjectParser(PCEPLspaObjectParser.CLASS, PCEPLspaObjectParser.TYPE, lspaParser);
        context.registerObjectSerializer(Lspa.class, lspaParser);

        final PCEPIncludeRouteObjectParser iroParser = new PCEPIncludeRouteObjectParser(eroSubReg);
        context.registerObjectParser(PCEPIncludeRouteObjectParser.CLASS, PCEPIncludeRouteObjectParser.TYPE, iroParser);
        context.registerObjectSerializer(Iro.class, iroParser);

        final PCEPSvecObjectParser svecParser = new PCEPSvecObjectParser(tlvReg);
        context.registerObjectParser(PCEPSvecObjectParser.CLASS, PCEPSvecObjectParser.TYPE, svecParser);
        context.registerObjectSerializer(Svec.class, svecParser);

        final PCEPNotificationObjectParser notParser = new PCEPNotificationObjectParser(tlvReg);
        context.registerObjectParser(PCEPNotificationObjectParser.CLASS, PCEPNotificationObjectParser.TYPE, notParser);
        context.registerObjectSerializer(CNotification.class, notParser);

        final PCEPErrorObjectParser errParser = new PCEPErrorObjectParser(tlvReg);
        context.registerObjectParser(PCEPErrorObjectParser.CLASS, PCEPErrorObjectParser.TYPE, errParser);
        context.registerObjectSerializer(ErrorObject.class, errParser);

        final PCEPLoadBalancingObjectParser lbParser = new PCEPLoadBalancingObjectParser(tlvReg);
        context.registerObjectParser(PCEPLoadBalancingObjectParser.CLASS, PCEPLoadBalancingObjectParser.TYPE, lbParser);
        context.registerObjectSerializer(LoadBalancing.class, lbParser);

        final PCEPCloseObjectParser closeParser = new PCEPCloseObjectParser(tlvReg);
        context.registerObjectParser(PCEPCloseObjectParser.CLASS, PCEPCloseObjectParser.TYPE, closeParser);
        context.registerObjectSerializer(CClose.class, closeParser);

        final PCEPPathKeyObjectParser pathKeyParser = new PCEPPathKeyObjectParser(eroSubReg);
        context.registerObjectParser(PCEPPathKeyObjectParser.CLASS, PCEPPathKeyObjectParser.TYPE, pathKeyParser);
        context.registerObjectSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.path.key.object.PathKey.class,
            pathKeyParser);

        final PCEPExcludeRouteObjectParser xroParser = new PCEPExcludeRouteObjectParser(context.getXROSubobjectHandlerRegistry());
        context.registerObjectParser(PCEPExcludeRouteObjectParser.CLASS, PCEPExcludeRouteObjectParser.TYPE, xroParser);
        context.registerObjectSerializer(Xro.class, xroParser);

        final PCEPObjectiveFunctionObjectParser objectiveParser = new PCEPObjectiveFunctionObjectParser(tlvReg);
        context.registerObjectParser(PCEPObjectiveFunctionObjectParser.CLASS, PCEPObjectiveFunctionObjectParser.TYPE, objectiveParser);
        context.registerObjectSerializer(Of.class, objectiveParser);

        final PCEPClassTypeObjectParser ctParser = new PCEPClassTypeObjectParser(tlvReg);
        context.registerObjectParser(PCEPClassTypeObjectParser.CLASS, PCEPClassTypeObjectParser.TYPE, ctParser);
        context.registerObjectSerializer(ClassType.class, ctParser);

        final PCEPGlobalConstraintsObjectParser gcParser = new PCEPGlobalConstraintsObjectParser(tlvReg);
        context.registerObjectParser(PCEPGlobalConstraintsObjectParser.CLASS, PCEPGlobalConstraintsObjectParser.TYPE, gcParser);
        context.registerObjectSerializer(Gc.class, gcParser);
    }

    private void registerEROParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context, final LabelRegistry labelReg) {
        final EROIpv4PrefixSubobjectParser ipv4prefixParser = new EROIpv4PrefixSubobjectParser();
        regs.add(context.registerEROSubobjectParser(EROIpv4PrefixSubobjectParser.TYPE, ipv4prefixParser));
        regs.add(context.registerEROSubobjectSerializer(IpPrefixCase.class, ipv4prefixParser));
        regs.add(context.registerEROSubobjectParser(EROIpv6PrefixSubobjectParser.TYPE, new EROIpv6PrefixSubobjectParser()));

        final EROAsNumberSubobjectParser asNumberParser = new EROAsNumberSubobjectParser();
        regs.add(context.registerEROSubobjectParser(EROAsNumberSubobjectParser.TYPE, asNumberParser));
        regs.add(context.registerEROSubobjectSerializer(AsNumberCase.class, asNumberParser));

        final EROUnnumberedInterfaceSubobjectParser unnumberedParser = new EROUnnumberedInterfaceSubobjectParser();
        regs.add(context.registerEROSubobjectParser(EROUnnumberedInterfaceSubobjectParser.TYPE, unnumberedParser));
        regs.add(context.registerEROSubobjectSerializer(UnnumberedCase.class, unnumberedParser));

        final EROPathKey32SubobjectParser pathKeyParser =  new EROPathKey32SubobjectParser();
        regs.add(context.registerEROSubobjectParser(EROPathKey32SubobjectParser.TYPE, pathKeyParser));
        regs.add(context.registerEROSubobjectParser(EROPathKey128SubobjectParser.TYPE, new EROPathKey128SubobjectParser()));
        regs.add(context.registerEROSubobjectSerializer(PathKeyCase.class, pathKeyParser));

        final EROLabelSubobjectParser labelParser = new EROLabelSubobjectParser(labelReg);
        regs.add(context.registerEROSubobjectParser(EROLabelSubobjectParser.TYPE, labelParser));
        regs.add(context.registerEROSubobjectSerializer(LabelCase.class, labelParser));
    }

    private void registerRROParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context, final LabelRegistry labelReg) {
        final RROIpv4PrefixSubobjectParser ipv4prefixParser = new RROIpv4PrefixSubobjectParser();
        regs.add(context.registerRROSubobjectParser(RROIpv4PrefixSubobjectParser.TYPE, ipv4prefixParser));
        regs.add(context.registerRROSubobjectSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.record.route.subobjects.subobject.type.IpPrefixCase.class,
            ipv4prefixParser));
        regs.add(context.registerRROSubobjectParser(RROIpv6PrefixSubobjectParser.TYPE, new RROIpv6PrefixSubobjectParser()));

        final RROUnnumberedInterfaceSubobjectParser unnumberedParser = new RROUnnumberedInterfaceSubobjectParser();
        regs.add(context.registerRROSubobjectParser(RROUnnumberedInterfaceSubobjectParser.TYPE, unnumberedParser));
        regs.add(context.registerRROSubobjectSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.record.route.subobjects.subobject.type.UnnumberedCase.class,
            unnumberedParser));

        final RROPathKey32SubobjectParser pathKeyParser =  new RROPathKey32SubobjectParser();
        regs.add(context.registerRROSubobjectParser(RROPathKey32SubobjectParser.TYPE, pathKeyParser));
        regs.add(context.registerRROSubobjectParser(RROPathKey128SubobjectParser.TYPE, new RROPathKey128SubobjectParser()));
        regs.add(context.registerRROSubobjectSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pcep.types.rev131005.reported.route.object.rro.subobject.subobject.type.PathKeyCase.class,
            pathKeyParser));

        final RROLabelSubobjectParser labelParser = new RROLabelSubobjectParser(labelReg);
        regs.add(context.registerRROSubobjectParser(RROLabelSubobjectParser.TYPE, labelParser));
        regs.add(context.registerRROSubobjectSerializer(
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.rsvp.rev130820.record.route.subobjects.subobject.type.LabelCase.class,
            labelParser));
    }

    private void registerXROParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context) {
        final XROIpv4PrefixSubobjectParser ipv4prefixParser = new XROIpv4PrefixSubobjectParser();
        regs.add(context.registerXROSubobjectParser(XROIpv4PrefixSubobjectParser.TYPE, ipv4prefixParser));
        regs.add(context.registerXROSubobjectSerializer(IpPrefixCase.class, ipv4prefixParser));
        regs.add(context.registerXROSubobjectParser(XROIpv6PrefixSubobjectParser.TYPE, new XROIpv6PrefixSubobjectParser()));

        final XROAsNumberSubobjectParser asNumberParser = new XROAsNumberSubobjectParser();
        regs.add(context.registerXROSubobjectParser(XROAsNumberSubobjectParser.TYPE, asNumberParser));
        regs.add(context.registerXROSubobjectSerializer(AsNumberCase.class, asNumberParser));

        final XROSRLGSubobjectParser srlgParser = new XROSRLGSubobjectParser();
        regs.add(context.registerXROSubobjectParser(XROSRLGSubobjectParser.TYPE, srlgParser));
        regs.add(context.registerXROSubobjectSerializer(SrlgCase.class, srlgParser));

        final XROUnnumberedInterfaceSubobjectParser unnumberedParser = new XROUnnumberedInterfaceSubobjectParser();
        regs.add(context.registerXROSubobjectParser(XROUnnumberedInterfaceSubobjectParser.TYPE, unnumberedParser));
        regs.add(context.registerXROSubobjectSerializer(UnnumberedCase.class, unnumberedParser));

        final XROPathKey32SubobjectParser pathKeyParser =  new XROPathKey32SubobjectParser();
        regs.add(context.registerXROSubobjectParser(XROPathKey32SubobjectParser.TYPE, pathKeyParser));
        regs.add(context.registerXROSubobjectParser(XROPathKey128SubobjectParser.TYPE, new XROPathKey128SubobjectParser()));
        regs.add(context.registerXROSubobjectSerializer(PathKeyCase.class, pathKeyParser));
    }

    private void registerLabelParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context) {
        final Type1LabelParser type1Parser = new Type1LabelParser();
        regs.add(context.registerLabelParser(Type1LabelParser.CTYPE, type1Parser));
        regs.add(context.registerLabelSerializer(Type1LabelCase.class, type1Parser));

        final GeneralizedLabelParser generalizedParser = new GeneralizedLabelParser();
        regs.add(context.registerLabelParser(GeneralizedLabelParser.CTYPE, generalizedParser));
        regs.add(context.registerLabelSerializer(GeneralizedLabelCase.class, generalizedParser));

        final WavebandSwitchingLabelParser wavebandParser = new WavebandSwitchingLabelParser();
        regs.add(context.registerLabelParser(WavebandSwitchingLabelParser.CTYPE, wavebandParser));
        regs.add(context.registerLabelSerializer(WavebandSwitchingLabelCase.class, wavebandParser));
    }

    private void registerTlvParsers(final List<AutoCloseable> regs, final PCEPExtensionProviderContext context) {
        final NoPathVectorTlvParser noPathParser = new NoPathVectorTlvParser();
        regs.add(context.registerTlvParser(NoPathVectorTlvParser.TYPE, noPathParser));
        regs.add(context.registerTlvSerializer(NoPathVector.class, noPathParser));

        final OverloadedDurationTlvParser overloadedDurationParser = new OverloadedDurationTlvParser();
        regs.add(context.registerTlvParser(OverloadedDurationTlvParser.TYPE, overloadedDurationParser));
        regs.add(context.registerTlvSerializer(OverloadDuration.class, overloadedDurationParser));

        final ReqMissingTlvParser reqMissingParser = new ReqMissingTlvParser();
        regs.add(context.registerTlvParser(ReqMissingTlvParser.TYPE, reqMissingParser));
        regs.add(context.registerTlvSerializer(ReqMissing.class, reqMissingParser));

        final OFListTlvParser ofListParser = new OFListTlvParser();
        regs.add(context.registerTlvParser(OFListTlvParser.TYPE, ofListParser));
        regs.add(context.registerTlvSerializer(OfList.class, ofListParser));

        final OrderTlvParser orderParser = new OrderTlvParser();
        regs.add(context.registerTlvParser(OrderTlvParser.TYPE, orderParser));
        regs.add(context.registerTlvSerializer(Order.class, orderParser));
    }
}
