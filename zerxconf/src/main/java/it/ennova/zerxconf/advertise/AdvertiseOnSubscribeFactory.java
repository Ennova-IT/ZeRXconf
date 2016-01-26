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

    public static OnSubscribeEvent<NetworkServiceDiscoveryInfo> from(@NonNull Context context,
                                                                     @NonNull String serviceName,
                                                                     @NonNull String serviceLayer,
                                                                     int servicePort,
                                                                     @Nullable Map<String, String> attributes) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !MapUtils.isEmpty(attributes)) {

            return new LPAdvertiseOnSubscribeEvent(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            return new JBAdvertiseOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        }
    }
}
