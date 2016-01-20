package it.ennova.zerxconf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import it.ennova.zerxconf.discovery.DiscoveryOnSubscribeEvent;
import it.ennova.zerxconf.discovery.DiscoveryOnSubscribeFactory;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;

/**
 *
 */
public class ZeRXconf {

    private ZeRXconf() {
        throw new IllegalStateException();
    }

    public static Observable<NetworkServiceDiscoveryInfo> advertise(@NonNull Context context,
                                                                    @NonNull String serviceName,
                                                                    @NonNull String serviceLayer,
                                                                    int servicePort,
                                                                    @Nullable Map<String, String> attributes,
                                                                    boolean forceNative) {

        DiscoveryOnSubscribeEvent onSubscribe = DiscoveryOnSubscribeFactory.from(context, serviceName, serviceLayer,
                servicePort, attributes, forceNative);

        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted());
    }

 }
