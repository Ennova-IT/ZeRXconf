package it.ennova.zerxconf.discovery;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import it.ennova.zerxconf.utils.MapUtils;

public class DiscoveryOnSubscribeFactory {

    public static DiscoveryOnSubscribeEvent from(@NonNull Context context,
                                                 @NonNull String serviceName,
                                                 @NonNull String serviceLayer,
                                                 int servicePort,
                                                 @Nullable Map<String, String> attributes,
                                                 boolean forceNative) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && forceNative) {
            return buildNativeOnSubscribeEventFrom(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            return new CompatDiscoveryOnSubscribeEvent(serviceName, serviceLayer, servicePort, attributes);
        }
    }

    private static DiscoveryOnSubscribeEvent buildNativeOnSubscribeEventFrom(@NonNull Context context,
                                                                             @NonNull String serviceName,
                                                                             @NonNull String serviceLayer,
                                                                             int servicePort,
                                                                             @Nullable Map<String, String> attributes) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !MapUtils.isEmpty(attributes)) {

            return new LPDiscoveryOnSubscribeEvent(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            return new JBDiscoveryOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        }
    }
}
