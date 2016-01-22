package it.ennova.zerxconf.model;


import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

/**
 * This class is the one that represents the model for the service that is used inside this library.
 *
 * @see #from(NsdServiceInfo) Creating a new instance from the native Android API
 * @see #from(ServiceInfo, Map) Creating a new instance from the JmDNS API
 */
public class NetworkServiceDiscoveryInfo implements NsdStatus {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String serviceLayer;
    private final int servicePort;
    @NonNull
    private final Map<String, byte[]> attributes;
    @STATUS
    private final int status;

    final String toStringMessage;

    private NetworkServiceDiscoveryInfo(@NonNull String serviceName,
                                        @NonNull String serviceLayer,
                                        int servicePort,
                                        @NonNull Map<String, byte[]> attributes,
                                        @STATUS int status) {

        this.serviceName = serviceName;
        this.serviceLayer = serviceLayer;
        this.servicePort = servicePort;
        this.attributes = attributes;
        this.status = status;

        toStringMessage = buildToStringMessage();
    }

    private String buildToStringMessage() {
        StringBuffer buffer = new StringBuffer(serviceName)
                .append(".")
                .append(serviceLayer)
                .append(":")
                .append(servicePort)
                .append(" - Status: ")
                .append((isAdded()) ? "ADDED" : "REMOVED");

        return buffer.toString();
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

    @Override
    public boolean isAdded() {
        return status == ADDED;
    }

    @Override
    public String toString() {
        return toStringMessage;
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull NsdServiceInfo source) {
        return from (source, ADDED);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull NsdServiceInfoWrapper wrapper) {
        return from(wrapper.getNsdServiceInfo(), wrapper.getStatus());
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull NsdServiceInfo source, @STATUS int status) {
        return new NetworkServiceDiscoveryInfo(source.getServiceName(), source.getServiceType(),
                source.getPort(), getMapFrom(source), status);
    }

    private static Map<String, byte[]> getMapFrom(@NonNull NsdServiceInfo source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return source.getAttributes();
        }
        return new HashMap<>(0);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source,  @STATUS int status) {

        Enumeration<String> propertyNames = source.getPropertyNames();
        Map<String, byte[]> attributes = new HashMap<>();

        while (propertyNames.hasMoreElements()) {
            String key = propertyNames.nextElement();
            attributes.put(key, source.getPropertyBytes(key));
        }

        return from (source, attributes, status);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source, @NonNull Map attributes) {
        return from (source, attributes, ADDED);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source, @NonNull Map attributes, @STATUS int status) {
        return new NetworkServiceDiscoveryInfo(source.getName(), source.getType(), source.getPort(), attributes, status);
    }


}
