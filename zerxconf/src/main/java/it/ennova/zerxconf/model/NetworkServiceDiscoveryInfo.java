package it.ennova.zerxconf.model;


import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

import it.ennova.zerxconf.utils.InetUtils;
import it.ennova.zerxconf.utils.MapUtils;

import static it.ennova.zerxconf.utils.InetUtils.isValid;

/**
 * This class is the one that represents the model for the service that is used inside this library.
 *
 * @see #from(NsdServiceInfo) Creating a new instance from the native Android API
 * @see #from(ServiceInfo, Map) Creating a new instance from the JmDNS API
 */
public class NetworkServiceDiscoveryInfo implements NsdStatus, Parcelable {

    @NonNull
    private final String serviceName;
    @NonNull
    private final String serviceLayer;
    private final int servicePort;
    @NonNull
    private final Map<String, byte[]> attributes;
    @STATUS
    private final int status;
    private final InetAddress address;

    final String toStringMessage;

    private NetworkServiceDiscoveryInfo(@NonNull String serviceName,
                                        @NonNull String serviceLayer,
                                        int servicePort,
                                        @NonNull Map<String, byte[]> attributes,
                                        @STATUS int status, @Nullable InetAddress address) {

        this.serviceName = serviceName;
        this.serviceLayer = serviceLayer;
        this.servicePort = servicePort;
        this.attributes = attributes;
        this.status = status;
        this.address = address;

        toStringMessage = buildToStringMessage();
    }

    private String buildToStringMessage() {
        StringBuffer buffer = new StringBuffer(serviceName)
                .append("(")
                .append(serviceLayer)
                .append(") - ")
                .append(address.getHostAddress())
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

    @Nullable
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public boolean isAdded() {
        return status == ADDED;
    }

    @Override
    public String toString() {
        return toStringMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NetworkServiceDiscoveryInfo)) {
            return false;
        }

        final NetworkServiceDiscoveryInfo current = (NetworkServiceDiscoveryInfo) o;
        return serviceName.equals(current.getServiceName())
                && serviceLayer.equals(current.getServiceLayer())
                && servicePort == current.getServicePort()
                && InetUtils.isSame(address, current.getAddress());
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode() + serviceLayer.hashCode() + servicePort + address.hashCode();
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
                source.getPort(), MapUtils.getMapFrom(source), status, source.getHost());
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source,  @STATUS int status) {
        return from (source, MapUtils.getMapFrom(source), status);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source, @NonNull Map attributes) {
        return from (source, attributes, ADDED);
    }

    @NonNull
    public static NetworkServiceDiscoveryInfo from (@NonNull ServiceInfo source, @NonNull Map attributes, @STATUS int status) {
        return new NetworkServiceDiscoveryInfo(source.getName(), source.getType(), source.getPort(), attributes, status, InetUtils.getHostAddressFrom(source));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeString(serviceLayer);
        dest.writeInt(servicePort);
        dest.writeMap(attributes);
        dest.writeInt(status);
        dest.writeSerializable(address);
    }

    public static final Parcelable.Creator<NetworkServiceDiscoveryInfo> CREATOR
            = new Parcelable.Creator<NetworkServiceDiscoveryInfo>() {

        public NetworkServiceDiscoveryInfo createFromParcel(Parcel in) {
            return new NetworkServiceDiscoveryInfo(in);
        }

        public NetworkServiceDiscoveryInfo[] newArray(int size) {
            return new NetworkServiceDiscoveryInfo[size];
        }
    };

    private NetworkServiceDiscoveryInfo(Parcel in) {
        serviceName = in.readString();
        serviceLayer = in.readString();
        servicePort = in.readInt();
        attributes = in.readHashMap(HashMap.class.getClassLoader());
        status = getStatusFrom(in.readInt());
        address = (InetAddress) in.readSerializable();

        toStringMessage = buildToStringMessage();
    }

    @STATUS
    private int getStatusFrom(int data) {
        return (data == ADDED) ? ADDED : REMOVED;
    }
}
