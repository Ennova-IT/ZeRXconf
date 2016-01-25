package it.ennova.zerxconf.common;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class Transformers {

    private static final Observable.Transformer<NetworkServiceDiscoveryInfo, NetworkServiceDiscoveryInfo> schedulerTransformer =
            new Observable.Transformer<NetworkServiceDiscoveryInfo, NetworkServiceDiscoveryInfo>() {
                @Override
                public Observable<NetworkServiceDiscoveryInfo> call(Observable<NetworkServiceDiscoveryInfo> observable) {
                    return observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            };


    public static Observable.Transformer<NetworkServiceDiscoveryInfo, NetworkServiceDiscoveryInfo> networking() {
        return schedulerTransformer;
    }
}
