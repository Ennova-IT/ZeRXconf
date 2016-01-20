package it.ennova.zerxconf.model;


import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

/**
 * This class is the one that represents the model for the service that is used inside this library.
 *
 * @see #from(NsdServiceInfo) Creating a new instance from the native Android API
 * @see #from(ServiceInfo, Map) Creating a new instance from the JmDNS API
 */
public class NetworkServiceDiscoveryInfo {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String serviceLayer;
    private final int servicePort;
    @NonNull
    private final Map<String, byte[]> attributes;

    private NetworkServiceDiscoveryInfo(@NonNull String serviceName,
                                        @NonNull String serviceLayer,
                                        int servicePort,
                                        @NonNull Map<String, byte[]> attributes) {

        this.serviceName = serviceName;
        this.serviceLayer = serviceLayer;
        this.servicePort = servicePort;
        this.attributes = attributes;
    }

    @NonNull
    public String getServiceName() {
        return serviceName;
    }

    @NonNull
    public String getServiceLayer() {
        return serviceLayer;
    }

    public int getServicePort() {
        return servicePort;
    }

    @NonNull
    public Map<String, byte[]> getAttributes() {
        return attributes;
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull NsdServiceInfo source) {
        return new NetworkServiceDiscoveryInfo(source.getServiceName(), source.getServiceType(),
                source.getPort(), getMapFrom(source));
    }

    private static Map<String, byte[]> getMapFrom(@NonNull NsdServiceInfo source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return source.getAttributes();
        }
        return new HashMap<>(0);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source, @NonNull Map attributes) {
        return new NetworkServiceDiscoveryInfo(source.getName(), source.getType(), source.getPort(), attributes);
    }
}
