package it.ennova.zerxconf.discovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.exceptions.NsdException;
import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import it.ennova.zerxconf.utils.NsdUtils;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static it.ennova.zerxconf.exceptions.NsdException.*;
import static it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo.*;

public class JBDiscoveryOnSubscribeEvent implements OnSubscribeEvent<NsdServiceInfoWrapper> {

    private NsdManager nsdManager;
    private final String protocol;
    private Subscriber<? super NsdServiceInfoWrapper> subscriber;


    public JBDiscoveryOnSubscribeEvent(@NonNull final Context context,
                                       @NonNull String protocol) {

        this.protocol = protocol;
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    private final Action0 dismissAction = new Action0() {
        @Override
        public void call() {
            if (nsdManager != null) {
                nsdManager.stopServiceDiscovery(discoveryListener);
                nsdManager = null;
            }
        }
    };

    private final NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            nsdManager.stopServiceDiscovery(this);
            subscriber.onError(new NsdException(START_DISCOVERY, serviceType, errorCode));
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            nsdManager.stopServiceDiscovery(this);
            subscriber.onError(new NsdException(STOP_DISCOVERY, serviceType, errorCode));
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {}

        @Override
        public void onDiscoveryStopped(String serviceType) {}

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            onNextRequested(serviceInfo, ADDED);
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            onNextRequested(serviceInfo, REMOVED);
        }
    };

    private void onNextRequested(NsdServiceInfo serviceInfo, @STATUS int status) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(new NsdServiceInfoWrapper(serviceInfo, status));
        }
    }
    @Override
    public void call(Subscriber<? super NsdServiceInfoWrapper> subscriber) {
        this.subscriber = subscriber;
        if (!NsdUtils.isValidProtocol(protocol)) {
            subscriber.onError(new NsdException());
        } else {
            nsdManager.discoverServices(protocol, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
            subscriber.add(Subscriptions.create(dismissAction));
        }
    }

    @Override
    public Action0 onCompleted() {
        return dismissAction;
    }
}
