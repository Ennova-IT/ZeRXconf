package it.ennova.rxadvertise;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 *
 */
class NsdOnSubscribeEvent implements Observable.OnSubscribe<NsdServiceInfo>,
        NsdManager.RegistrationListener{

    private NsdServiceInfo nsdServiceInfo;
    private Context context;
    private Subscriber<? super NsdServiceInfo> subscriber;
    private NsdManager nsdManager;

    public NsdOnSubscribeEvent(@NonNull Context context,
                               @NonNull String serviceName,
                               @NonNull String serviceLayer,
                               int servicePort) {

        nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(serviceName);
        nsdServiceInfo.setServiceType(serviceLayer);
        nsdServiceInfo.setPort(servicePort);

        this.context = context;
    }
    @Override
    public void call(Subscriber<? super NsdServiceInfo> subscriber) {
        this.subscriber = subscriber;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
        onError();
    }

    private void onError() {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onError(new IllegalStateException());
        }
    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
        onError();
    }

    @Override
    public void onServiceRegistered(NsdServiceInfo serviceInfo) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(serviceInfo);
        }
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
        context = null;
        nsdManager = null;
    }

    private void dismiss() {
        if (nsdManager != null) {
            nsdManager.unregisterService(this);
        }
    }

    final Action0 dismissAction = new Action0() {
        @Override
        public void call() {
            dismiss();
        }
    };
}
