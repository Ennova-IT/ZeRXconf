package it.ennova.zerxconf.common;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import rx.functions.Func1;

public class Functions {

    private static final Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo> nsdServiceWrapperConversion =
            new Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo>() {
        @Override
        public NetworkServiceDiscoveryInfo call(NsdServiceInfoWrapper wrapper) {
            return NetworkServiceDiscoveryInfo.from(wrapper);
        }
    };

    public static Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo> toNetworkServiceDiscoveryInfo() {
        return nsdServiceWrapperConversion;
    }
}
