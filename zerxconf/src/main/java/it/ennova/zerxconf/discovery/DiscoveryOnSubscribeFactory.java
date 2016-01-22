package it.ennova.zerxconf.discovery;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.common.Transformers;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import rx.Observable;
import rx.functions.Func1;

public class DiscoveryOnSubscribeFactory {

    public static Observable<NetworkServiceDiscoveryInfo> from(@NonNull Context context,
                                                               @NonNull String protocol) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return buildJBObservableFrom(context, protocol);
        } else {
            return buildCompatObservableFrom(context, protocol);
        }
    }

    private static Observable<NetworkServiceDiscoveryInfo> buildJBObservableFrom(Context context, String protocol) {
        OnSubscribeEvent<NsdServiceInfoWrapper> onSubscribe = new JBDiscoveryOnSubscribeEvent(context, protocol);
        return Observable.create(onSubscribe)
                .concatMap(JBDiscoveryServiceResolver.with(context))
                .map(new Func1<NsdServiceInfoWrapper, NetworkServiceDiscoveryInfo>() {
                    @Override
                    public NetworkServiceDiscoveryInfo call(NsdServiceInfoWrapper wrapper) {
                        return NetworkServiceDiscoveryInfo.from(wrapper);
                    }
                }).compose(Transformers.networking());
    }

    private static Observable<NetworkServiceDiscoveryInfo> buildCompatObservableFrom(Context context, String protocol) {
        OnSubscribeEvent<NetworkServiceDiscoveryInfo> onSubscribe = new CompatOnSubscribeEvent(context, protocol);
        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted()).compose(Transformers.networking());
    }
}
