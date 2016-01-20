package it.ennova.rxadvertise;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import rx.Observable;

/**
 *
 */
public class JBNsdObservable {

    public static Observable<NsdServiceInfo> advertise(@NonNull Context context,
                                                       @NonNull String serviceName,
                                                       @NonNull String serviceLayer,
                                                       int servicePort) {

        NsdOnSubscribeEvent onSubscribe = new JBNsdOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted());
    }
 }
