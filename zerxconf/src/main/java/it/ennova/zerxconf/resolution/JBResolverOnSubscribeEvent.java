package it.ennova.zerxconf.resolution;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import rx.Observable;
import rx.Subscriber;

import static android.net.nsd.NsdManager.*;


public class JBResolverOnSubscribeEvent implements Observable.OnSubscribe<NsdServiceInfoWrapper> {

    private NsdServiceInfoWrapper nsdServiceInfo;
    private NsdManager nsdManager;
    private Subscriber<? super NsdServiceInfoWrapper> subscriber;

    public static JBResolverOnSubscribeEvent from (@NonNull final Context context,
                                                   @NonNull final NsdServiceInfoWrapper nsdServiceInfo) {

        return new JBResolverOnSubscribeEvent(context, nsdServiceInfo);
    }

    private JBResolverOnSubscribeEvent(@NonNull Context context,
                                       @NonNull NsdServiceInfoWrapper nsdServiceInfo) {

        this.nsdServiceInfo = nsdServiceInfo;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    private final ResolveListener resolveListener = new ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(new NsdServiceInfoWrapper(serviceInfo, nsdServiceInfo.getStatus()));
                subscriber.onCompleted();
            }
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(new NsdServiceInfoWrapper(serviceInfo, nsdServiceInfo.getStatus()));
                subscriber.onCompleted();
            }
        }
    };

    @Override
    public void call(Subscriber<? super NsdServiceInfoWrapper> subscriber) {
        this.subscriber = subscriber;
        nsdManager.resolveService(nsdServiceInfo.getNsdServiceInfo(), resolveListener);
    }
}
