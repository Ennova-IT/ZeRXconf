package it.ennova.rxadvertise;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class JBResolveOnSubscribeEvent implements OnSubscribeEvent<NsdServiceInfo>{

    private Subscriber<? super NsdServiceInfo> subscriber;
    private NsdManager nsdManager;
    private ConcurrentLinkedQueue<NsdServiceInfo> elements;

    public JBResolveOnSubscribeEvent(@NonNull Context context,
                                     @NonNull List<NsdServiceInfo> services) {

        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        elements = new ConcurrentLinkedQueue<>(services);
    }

    private final NsdManager.ResolveListener resolveListener = new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            onNext(serviceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            onNext(serviceInfo);
        }
    };

    private void onNext(NsdServiceInfo serviceInfo) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(serviceInfo);
        }
    }

    private void onNewServiceRequested() {
        NsdServiceInfo service = elements.poll();
        if (service != null) {
            nsdManager.resolveService(service, resolveListener);
        }
    }

    private final Action0 dismissAction = new Action0() {
        @Override
        public void call() {
            nsdManager = null;
            subscriber = null;
        }
    };

    @Override
    public Action0 onCompleted() {
        return dismissAction;
    }

    @Override
    public void call(Subscriber<? super NsdServiceInfo> subscriber) {
        this.subscriber = subscriber;
        subscriber.add(Subscriptions.create(dismissAction));
        onNewServiceRequested();
    }
}
