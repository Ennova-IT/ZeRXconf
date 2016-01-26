package it.ennova.zerxconf.common;


import rx.Observable;
import rx.functions.Action0;

public interface OnSubscribeEvent<T> extends Observable.OnSubscribe<T> {
    Action0 onCompleted();
}
