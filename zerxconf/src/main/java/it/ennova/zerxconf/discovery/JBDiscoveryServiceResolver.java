package it.ennova.zerxconf.discovery;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import it.ennova.zerxconf.model.NsdServiceInfoWrapper;
import rx.Observable;
import rx.functions.Func1;


public class JBDiscoveryServiceResolver implements Func1<NsdServiceInfoWrapper, Observable<NsdServiceInfoWrapper>> {

    private WeakReference<Context> weakContext;

    public static JBDiscoveryServiceResolver with(@NonNull final Context context) {
        return new JBDiscoveryServiceResolver(new WeakReference<>(context));
    }

    private JBDiscoveryServiceResolver(@NonNull WeakReference<Context> weakContext) {
        this.weakContext = weakContext;
    }

    @Override
    public Observable<NsdServiceInfoWrapper> call(NsdServiceInfoWrapper nsdServiceInfo) {
        return Observable.create(JBResolverOnSubscribeEvent.from(weakContext.get(), nsdServiceInfo));
    }
}
