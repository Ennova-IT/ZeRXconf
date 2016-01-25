package it.ennova.zerxconf.utils;


import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Set;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class NsdUtils {

    public static NsdServiceInfo from(@NonNull NetworkServiceDiscoveryInfo source) {
        NsdServiceInfo info = new NsdServiceInfo();
        info.setServiceName(source.getServiceName());
        info.setServiceType(source.getServiceLayer());
        info.setPort(source.getServicePort());
        info.setHost(source.getAddress());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Set<String> keySet = source.getAttributes().keySet();
            for (String key : keySet) {
                info.setAttribute(key, new String(source.getAttributes().get(key)));
            }
        }

        return info;
    }

    public static String cleanProtocolOf(@NonNull NetworkServiceDiscoveryInfo info) {
        return (info.getServiceName() + "." + info.getServiceLayer()).replaceAll("\\.", "").replace("local.", "");
    }
}
