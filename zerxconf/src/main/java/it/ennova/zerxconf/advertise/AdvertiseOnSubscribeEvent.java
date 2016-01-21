package it.ennova.zerxconf.advertise;


import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;
import rx.functions.Action0;

public interface AdvertiseOnSubscribeEvent extends Observable.OnSubscribe<NetworkServiceDiscoveryInfo> {
    Action0 onCompleted();
}
