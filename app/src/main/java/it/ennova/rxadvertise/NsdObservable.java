package it.ennova.rxadvertise;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import rx.Observable;

/**
 *
 */
public class NsdObservable {

    public static Observable<NsdServiceInfo> advertise(@NonNull Context context,
                                                       @NonNull String serviceName,
                                                       @NonNull String serviceLayer,
                                                       @NonNull int servicePort) {

        NsdOnSubscribeEvent onSubscribe = new NsdOnSubscribeEvent(context, serviceName, serviceLayer, servicePort);
        return Observable.create(onSubscribe).doOnUnsubscribe(onSubscribe.dismissAction);
    }
 }
