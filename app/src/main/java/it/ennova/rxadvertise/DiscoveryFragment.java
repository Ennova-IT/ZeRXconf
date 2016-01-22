package it.ennova.rxadvertise;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscription;
import rx.functions.Action1;

public class DiscoveryFragment extends Fragment {

    @Bind(R.id.commandViewFlipper)
    ViewFlipper viewFlipper;
    private Subscription subscription;
    private final String TAG = "ZeRXconf";

    public DiscoveryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_discovery, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewFlipperUtils.initAnimationOn(viewFlipper, getActivity());
    }

    @OnClick(R.id.btnStartService)
    void onStartServiceClicked() {
        subscription = ZeRXconf.startDiscovery(getActivity())
                .subscribe(onNext, onError);


        viewFlipper.showNext();
    }

    private Action1<NetworkServiceDiscoveryInfo> onNext = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo serviceInfo) {
            Log.d("ZERXCONF-Discovery", serviceInfo.toString());
        }
    };

    private Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.btnStopService)
    void onStopServiceClicked() {
        subscription.unsubscribe();
        viewFlipper.showPrevious();
    }

}
