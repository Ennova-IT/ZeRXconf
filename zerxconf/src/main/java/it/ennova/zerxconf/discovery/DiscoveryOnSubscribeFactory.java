package it.ennova.zerxconf.discovery;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.common.Functions;
import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.common.Transformers;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import it.ennova.zerxconf.resolution.JBDiscoveryServiceResolver;
import rx.Observable;

public class DiscoveryOnSubscribeFactory {

    public static Observable<NetworkServiceDiscoveryInfo> from(@NonNull Context context) {
        return buildNewJBObservableFrom(context, ZeRXconf.ALL_AVAILABLE_SERVICES)
                .map(Functions.toNetworkServiceDiscoveryInfo())
                .compose(Transformers.networking());
    }

    public static Observable<NetworkServiceDiscoveryInfo> from(@NonNull Context context,
                                                               @NonNull String protocol) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return buildJBObservableFrom(context, protocol);
        } else {
            return buildCompatObservableFrom(context, protocol);
        }
    }

    private static Observable<NetworkServiceDiscoveryInfo> buildJBObservableFrom(Context context, String protocol) {
        return buildNewJBObservableFrom(context, protocol)
                .concatMap(JBDiscoveryServiceResolver.with(context))
                .map(Functions.toNetworkServiceDiscoveryInfo())
                .compose(Transformers.networking());
    }

    private static Observable<NsdServiceInfoWrapper> buildNewJBObservableFrom(Context context, String protocol) {
        OnSubscribeEvent<NsdServiceInfoWrapper> onSubscribe = new JBDiscoveryOnSubscribeEvent(context, protocol);
        return Observable.create(onSubscribe);

    }
    private static Observable<NetworkServiceDiscoveryInfo> buildCompatObservableFrom(Context context, String protocol) {
        OnSubscribeEvent<NetworkServiceDiscoveryInfo> onSubscribe = new CompatOnSubscribeEvent(context, protocol);
        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted()).compose(Transformers.networking());
    }
}
