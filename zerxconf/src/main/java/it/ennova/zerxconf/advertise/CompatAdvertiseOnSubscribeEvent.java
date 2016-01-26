package it.ennova.zerxconf.advertise;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import it.ennova.zerxconf.common.OnSubscribeEvent;
import it.ennova.zerxconf.exceptions.NsdException;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.utils.NsdUtils;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


public class CompatAdvertiseOnSubscribeEvent implements OnSubscribeEvent<NetworkServiceDiscoveryInfo> {

    private ServiceInfo serviceInfo;
    private final NetworkServiceDiscoveryInfo nsdServiceInfo;
    private JmDNS jmDNS;

    public CompatAdvertiseOnSubscribeEvent(@NonNull String serviceName,
                                           @NonNull String serviceLayer,
                                           int servicePort,
                                           @Nullable Map<String, String> attributes) {

        serviceInfo = ServiceInfo.create(serviceLayer, serviceName, servicePort, 0, 0, attributes);
        nsdServiceInfo = NetworkServiceDiscoveryInfo.from(serviceInfo, attributes);
    }

    @Override
    public Action0 onCompleted() {
        return dismissAction;
    }

    private final Action0 dismissAction = new Action0() {
        @Override
        public void call() {
            jmDNS.unregisterService(serviceInfo);
            jmDNS.unregisterAllServices();
            serviceInfo = null;
        }
    };

    @Override
    public void call(Subscriber<? super NetworkServiceDiscoveryInfo> subscriber) {

        if (subscriber.isUnsubscribed()) {
            return;
        }
        if (!NsdUtils.isValidProtocol(serviceInfo.getProtocol())) {
            subscriber.onError(new NsdException());
        } else {
            startSubscription(subscriber);
        }
    }

    private void startSubscription(Subscriber<? super NetworkServiceDiscoveryInfo> subscriber) {
        try {
            jmDNS = JmDNS.create();
            jmDNS.registerService(serviceInfo);
            subscriber.onNext(nsdServiceInfo);
            subscriber.add(Subscriptions.create(dismissAction));
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }
}
