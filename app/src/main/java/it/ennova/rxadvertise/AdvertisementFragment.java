package it.ennova.rxadvertise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AdvertisementFragment extends Fragment {

    private Map<String, String> attributes = new HashMap<>(1);
    private final Map<String, String> emptyAttributes = new HashMap<>(0);
    private boolean forceNative = false;
    private boolean forceEmptySet = false;

    private static final String SERVICE_NAME = "Skillo";
    private static final String SERVICE_LAYER = "_http._tcp.";
    private static final int SERVICE_PORT = 888;

    @Bind(R.id.imgAdvertiseStatus)
    ImageView imgAdvertiseStatus;
    @Bind(R.id.switchEmptyAttributeSet)
    Switch switchEmptyAttributes;
    private Subscription s;

    public AdvertisementFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_advertise, container, false);
        ButterKnife.bind(this, rootView);

        attributes.put("canConfigure", "NO");
        return rootView;
    }


    @OnCheckedChanged(R.id.switchNativeApi)
    void onNativeApiCheckedChanged(boolean isChecked) {
        forceNative = isChecked;
    }

    @OnCheckedChanged(R.id.switchEmptyAttributeSet)
    void onEmptyAttributesCheckedChanged(boolean isChecked) {
        forceEmptySet = isChecked;
    }

    @OnClick(R.id.btnAdvertiseService)
    void onAdvertisementButtonClicked() {
        if (s == null || s.isUnsubscribed()) {
            s = ZeRXconf.advertise(getActivity(), SERVICE_NAME, SERVICE_LAYER, SERVICE_PORT, getAttributes(), forceNative)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, onError);
        } else {
            s.unsubscribe();
            imgAdvertiseStatus.setImageResource(R.drawable.ic_remove_circle_red_500_18dp);
        }

    }

    private Map<String, String> getAttributes() {
        return forceEmptySet ? emptyAttributes : attributes;
    }

    private Action1<NetworkServiceDiscoveryInfo> onNext = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo networkServiceDiscoveryInfo) {
            imgAdvertiseStatus.setImageResource(R.drawable.ic_check_circle_green_500_18dp);
        }
    };

    private Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
