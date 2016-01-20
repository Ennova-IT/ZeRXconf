package it.ennova.zerxconf.model;


import android.os.Build;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

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

    public static NetworkServiceDiscoveryInfo from (android.net.nsd.NsdServiceInfo source) {
        return new NetworkServiceDiscoveryInfo(source.getServiceName(), source.getServiceType(),
                source.getPort(), getMapFrom(source));
    }

    private static Map<String, byte[]> getMapFrom(android.net.nsd.NsdServiceInfo source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return source.getAttributes();
        }
        return new HashMap<>(0);
    }

    public static NetworkServiceDiscoveryInfo from (ServiceInfo source, Map attributes) {
        return new NetworkServiceDiscoveryInfo(source.getName(), source.getType(), source.getPort(), attributes);
    }
}
