package it.ennova.zerxconf.advertise;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.utils.MapUtils;

public class AdvertiseOnSubscribeFactory {

    /**
     * This method is the one that is called when the user needs to advertise a server.
     * Since the Android implementation can be used only from Android 4.1, for previous versions of
     * the system it will be automatically used the JmDNS implementation. Unfortunately, if you need
     * to pass custom attributes to the service, Android supports it only from version 5.1 (API 21)
     * so, in that case, the library will revert to the JmDNS implementation in order to give the
     * desired behaviour.<br/><br/>
     *
     * <b>Note</b>: the JmDNS implementation is considerably slower than Android's during the service
     * boot phase (as a matter of fact, JmDNS needs to build up a service, while Android already has
     * the service up and running - i.e. for discovering Cast-enabled devices - and it just needs a
     * reference to it)
     * @param context      needed in order to retrieve the service for native API
     * @param serviceName  the name of the service that will be advertised on the network
     * @param serviceLayer the type of service that will be served
     * @param servicePort  the port on which the service will be available
     * @param attributes   the additional attributes that will be passed from the server
     * @return an instance of {@link OnSubscribeEvent} that is the one needed in order to start
     * the discovery
     */
    public static OnSubscribeEvent<NetworkServiceDiscoveryInfo> from(@NonNull Context context,
                                                                     @NonNull String serviceName,
                                                                     @NonNull String serviceLayer,
                                                                     int servicePort,
                                                                     @Nullable Map<String, String> attributes) {

        if (canUseNativeJB(attributes) || canUseNativeLP(attributes)) {
            return buildNativeOnSubscribeEventFrom(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            return new CompatAdvertiseOnSubscribeEvent(serviceName, serviceLayer, servicePort, attributes);
        }
    }

    private static OnSubscribeEvent<NetworkServiceDiscoveryInfo> buildNativeOnSubscribeEventFrom(@NonNull Context context,
                                                                                                 @NonNull String serviceName,
                                                                                                 @NonNull String serviceLayer,
                                                                                                 int servicePort,
                                                                                                 @Nullable Map<String, String> attributes) {

        if (canUseNativeLP(attributes)) {
            return new LPAdvertiseOnSubscribeEvent(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            return new JBAdvertiseOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        }
    }

    private static  boolean canUseNativeJB(@Nullable Map<String, String> attributes) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && MapUtils.isEmpty(attributes);
    }

    private static boolean canUseNativeLP(@Nullable Map<String, String> attributes) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !MapUtils.isEmpty(attributes);
    }
}
