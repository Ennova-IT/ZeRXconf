package it.ennova.rxadvertise;

import android.net.nsd.NsdServiceInfo;

import rx.Observable;
import rx.functions.Action0;

/**
 *
 */
public interface NsdOnSubscribeEvent extends Observable.OnSubscribe<NsdServiceInfo> {
    Action0 getDismissAction();
}
