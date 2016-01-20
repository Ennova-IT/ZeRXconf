package it.ennova.zerxconf;

import android.net.nsd.NsdServiceInfo;

import rx.Observable;
import rx.functions.Action0;

/**
 *
 */
public interface DiscoveryOnSubscribeEvent extends Observable.OnSubscribe<NsdServiceInfo> {
    Action0 onCompleted();
}
