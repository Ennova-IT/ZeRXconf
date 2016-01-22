package it.ennova.zerxconf.discovery;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class DiscoveryOnSubscribeFactory {

    public static OnSubscribeEvent<NetworkServiceDiscoveryInfo> from (@NonNull Context context,
                                                                      @NonNull String protocol) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new JBDiscoveryOnSubscribeEvent(context, protocol);
        } else {
            return new CompatOnSubscribeEvent(context, protocol);
        }
    }
}
