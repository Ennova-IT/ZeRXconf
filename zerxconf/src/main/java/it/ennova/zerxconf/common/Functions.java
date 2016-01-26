package it.ennova.zerxconf.common;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import rx.functions.Func1;

/**
 * This class is the one that will allow the developers to apply function to the different
 * type of data
 */
public class Functions {

    private static final Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo> nsdServiceWrapperConversion =
            new Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo>() {
                @Override
                public NetworkServiceDiscoveryInfo call(NsdServiceInfoWrapper wrapper) {
                    return NetworkServiceDiscoveryInfo.from(wrapper);
                }
            };

    /**
     * This method is the one that will transform the given {@link NsdServiceInfoWrapper} into a new
     * instance of {@link NetworkServiceDiscoveryInfo}
     * @return the {@link Func1} needed in order to transform the different data types
     */
    public static Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo> toNetworkServiceDiscoveryInfo() {
        return nsdServiceWrapperConversion;
    }
}
