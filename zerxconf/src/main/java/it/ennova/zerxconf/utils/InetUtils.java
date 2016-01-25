package it.ennova.zerxconf.utils;

import java.net.InetAddress;

import javax.jmdns.ServiceInfo;

public class InetUtils {

    public static InetAddress getHostAddressFrom(ServiceInfo source) {
        if (isValid(source.getInetAddresses())) {
            return source.getInetAddresses()[0];
        } else if (isValid(source.getInet4Addresses())) {
            return source.getInet4Addresses()[0];
        } else if (isValid(source.getInet6Addresses())) {
            return source.getInet6Addresses()[0];
        }

        return null;
    }

    private static <T extends InetAddress> boolean isValid(T[] vector) {
        return vector != null && vector.length > 0;
    }

    public static boolean isSame(InetAddress current, InetAddress newOne) {
        return (current == null && newOne == null) || current != null && newOne != null && current.equals(newOne);
    }
}
