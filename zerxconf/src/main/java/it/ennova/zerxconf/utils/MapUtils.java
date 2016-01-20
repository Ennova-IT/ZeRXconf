package it.ennova.zerxconf.utils;

import android.support.annotation.Nullable;

import java.util.Map;

public class MapUtils {

    public static boolean isEmpty(@Nullable final Map<?,?> source) {
        return source == null || source.isEmpty();
    }
}
