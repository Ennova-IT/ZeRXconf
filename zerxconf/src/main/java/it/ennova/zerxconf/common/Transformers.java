package it.ennova.zerxconf.common;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class is the one that is used in order to provide {@link rx.Observable.Transformer}s to
 * the library
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

    /**
     * This method is the one that, used with the {@link Observable#compose(Observable.Transformer)}
     * operator, will allow the target {@link Observable} to be executed on a proper Scheduler
     * and return its result onto the main thread.
     * @return the {@link rx.Observable.Transformer} needed for threading purposes
     */
    public static Observable.Transformer<NetworkServiceDiscoveryInfo, NetworkServiceDiscoveryInfo> networking() {
        return schedulerTransformer;
    }
}
