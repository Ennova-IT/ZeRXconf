package it.ennova.zerxconf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import it.ennova.zerxconf.advertise.AdvertiseOnSubscribeEvent;
import it.ennova.zerxconf.advertise.AdvertiseOnSubscribeFactory;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Observable;

/**
 * This class is the one entry point for the library.
 *
 * @see #advertise(Context, String, String, int, Map, boolean) Advertising a new service
 */
public class ZeRXconf {

    private ZeRXconf() {
        throw new IllegalStateException();
    }

    /**
     * This method is the one used in order to advertise the service on the network. In case you
     * choose not to use the default Android API and go with the JmDNS implementation, remember to
     * subscribe this {@link Observable} on a new thread, as the library implementation itself is
     * single threaded.
     *
     * @param context needed in order to retrieve the service for native API
     * @param serviceName the name of the service that will be advertised on the network
     * @param serviceLayer the type of service that will be served
     * @param servicePort the port on which the service will be available
     * @param attributes the additional attributes that will be passed from the server
     * @param forceNative {@code true} if you want to use the native API on Android 4.1 JellyBean
     *                                and newer, {@code false} to use instead the JmDNS implementation
     * @return an {@link Observable} that will emit the {@link NetworkServiceDiscoveryInfo} as soon
     * as the service is correctly started.
     */
    public static Observable<NetworkServiceDiscoveryInfo> advertise(@NonNull Context context,
                                                                    @NonNull String serviceName,
                                                                    @NonNull String serviceLayer,
                                                                    int servicePort,
                                                                    @Nullable Map<String, String> attributes,
                                                                    boolean forceNative) {

        AdvertiseOnSubscribeEvent onSubscribe = AdvertiseOnSubscribeFactory.from(context, serviceName, serviceLayer,
                servicePort, attributes, forceNative);

        return Observable.create(onSubscribe).doOnCompleted(onSubscribe.onCompleted());
    }

 }
