package it.ennova.zerxconf.utils;


import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class NsdUtils {

    public static final String NSD_URL_PATTERN = "^\\_[\\w]+\\.\\_((udp)|(tcp))\\.$";

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
        return (info.getServiceName() + "." + info.getServiceLayer()).replaceAll("(\\.){2}", "").replace("local.", "");
    }

    public static boolean isValidProtocol(@NonNull String protocol) {
        return !TextUtils.isEmpty(protocol) && protocol.matches(NSD_URL_PATTERN);
    }
}
