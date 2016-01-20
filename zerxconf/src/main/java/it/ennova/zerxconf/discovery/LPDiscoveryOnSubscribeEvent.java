package it.ennova.zerxconf.discovery;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LPDiscoveryOnSubscribeEvent extends JBDiscoveryOnSubscribeEvent {

    public LPDiscoveryOnSubscribeEvent(@NonNull Context context,
                                       @NonNull String serviceName,
                                       @NonNull String serviceLayer,
                                       int servicePort,
                                       @Nullable Map<String, String> attributes) {

        super(context, serviceName, serviceLayer, servicePort);
        if (attributes != null) {
            addAttributesFrom(attributes);
        }
    }

    private void addAttributesFrom(@NonNull Map<String, String> attributes) {
        for (String key : attributes.keySet()) {
            nsdServiceInfo.setAttribute(key, attributes.get(key));
        }
    }
}
