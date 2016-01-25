package it.ennova.zerxconf.utils;

import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.ServiceInfo;

public class MapUtils {

    public static boolean isEmpty(@Nullable final Map<?,?> source) {
        return source == null || source.isEmpty();
    }

    public static Map<String, byte[]> getMapFrom(@NonNull NsdServiceInfo source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return source.getAttributes();
        }
        return new HashMap<>(0);
    }

    public static Map<String, byte[]> getMapFrom(@NonNull ServiceInfo source) {
        Enumeration<String> propertyNames = source.getPropertyNames();
        Map<String, byte[]> attributes = new HashMap<>();

        while (propertyNames.hasMoreElements()) {
            String key = propertyNames.nextElement();
            attributes.put(key, source.getPropertyBytes(key));
        }

        return attributes;
    }
}
