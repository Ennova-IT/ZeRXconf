package it.ennova.zerxconf.utils;


import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

/**
 * This class is the one used in order to deal with the different issues with {@link NetworkServiceDiscoveryInfo}
 */
public class NsdUtils {

    /**
     * This constant is the one that matches the specification of the ZeroConf protocol
     */
    public static final String NSD_URL_PATTERN = "^_[\\w]+\\._((udp)|(tcp))\\.[(local)\\.]*$";

    /**
     * This method is the one that will build the {@link NsdServiceInfo} from the
     * {@link NetworkServiceDiscoveryInfo}
     * @param source the source data
     * @return an instance of {@link NsdServiceInfo} with all the needed data
     */
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

    /**
     * This method is needed in order to clean the protocol in case a discovery if requested after
     * invoking the {@link it.ennova.zerxconf.ZeRXconf#startDiscovery(Context)} method
     * @param info the target {@link NetworkServiceDiscoveryInfo} data structure
     * @return the cleaned {@link String} ready to be used for a new scanning
     */
    public static String cleanProtocolOf(@NonNull NetworkServiceDiscoveryInfo info) {
        return (info.getServiceName() + "." + info.getServiceLayer()).replaceAll("(\\.){2}", "").replace("local.", "");
    }

    /**
     * This method is the one that will apply the {@link #NSD_URL_PATTERN} regular expression
     * in order to understand if the given protocol is valid or not.
     * @param protocol the target protocol
     * @return {@code true} if the given protocol is valid, {@code false} otherwise
     */
    public static boolean isValidProtocol(@NonNull String protocol) {
        return !TextUtils.isEmpty(protocol) && protocol.matches(NSD_URL_PATTERN);
    }
}
