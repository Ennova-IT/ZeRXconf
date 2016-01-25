package it.ennova.zerxconf.resolution;

import android.content.Context;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.common.Functions;
import it.ennova.zerxconf.common.Transformers;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;

public class ResolutionObservableFactory {

    public static Observable<NetworkServiceDiscoveryInfo> from(@NonNull Context context,
                                                               @NonNull NetworkServiceDiscoveryInfo source) {

        return Observable.just(source)
                .map(Functions.toNsdServiceInfoWrapper())
                .concatMap(JBDiscoveryServiceResolver.with(context))
                .map(Functions.toNetworkServiceDiscoveryInfo())
                .compose(Transformers.networking());
    }
}
