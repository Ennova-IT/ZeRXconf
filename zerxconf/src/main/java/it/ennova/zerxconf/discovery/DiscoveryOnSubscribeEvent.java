package it.ennova.zerxconf.discovery;


import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;
import rx.functions.Action0;

public interface DiscoveryOnSubscribeEvent extends Observable.OnSubscribe<NetworkServiceDiscoveryInfo> {
    Action0 onCompleted();
}
