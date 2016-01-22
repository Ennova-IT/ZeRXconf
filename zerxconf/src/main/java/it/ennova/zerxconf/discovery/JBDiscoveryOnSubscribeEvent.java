package it.ennova.zerxconf.discovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.exceptions.NsdException;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static it.ennova.zerxconf.exceptions.NsdException.*;
import static it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo.*;

public class JBDiscoveryOnSubscribeEvent implements OnSubscribeEvent<NetworkServiceDiscoveryInfo> {

    private NsdManager nsdManager;
    private final String protocol;
    private Subscriber<? super NetworkServiceDiscoveryInfo> subscriber;


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
            subscriber.onNext(from(serviceInfo, ADDED));
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            subscriber.onNext(from(serviceInfo, REMOVED));
        }
    };

    @Override
    public void call(Subscriber<? super NetworkServiceDiscoveryInfo> subscriber) {
        this.subscriber = subscriber;
        nsdManager.discoverServices(protocol, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        subscriber.add(Subscriptions.create(dismissAction));
    }

    @Override
    public Action0 onCompleted() {
        return dismissAction;
    }
}
