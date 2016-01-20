package it.ennova.zerxconf;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import rx.Observable;

/**
 *
 */
public class JBNsdObservable {

    public static Observable<NsdServiceInfo> advertise(@NonNull Context context,
                                                       @NonNull String serviceName,
                                                       @NonNull String serviceLayer,
                                                       int servicePort,
                                                       @Nullable Map<String, String> attributes) {

        DiscoveryOnSubscribeEvent onSubscribe;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onSubscribe = new LPDiscoveryOnSubscribeEvent(context, serviceName, serviceLayer, servicePort, attributes);
        } else {
            onSubscribe = new JBDiscoveryOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        }
        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted());
    }
 }
