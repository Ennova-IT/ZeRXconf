package it.ennova.zerxconf.common;

import android.net.nsd.NsdServiceInfo;

import java.util.Set;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import it.ennova.zerxconf.model.NsdStatus;
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

    private static final Func1<NetworkServiceDiscoveryInfo, NsdServiceInfoWrapper> nsdServiceWrapperBackConversion =
            new Func1<NetworkServiceDiscoveryInfo, NsdServiceInfoWrapper>() {
                @Override
                public NsdServiceInfoWrapper call(NetworkServiceDiscoveryInfo source) {
                    return NsdServiceInfoWrapper.from(source);
                }
            };

    public static Func1<NetworkServiceDiscoveryInfo, NsdServiceInfoWrapper> toNsdServiceInfoWrapper () {
        return nsdServiceWrapperBackConversion;
    }
}
