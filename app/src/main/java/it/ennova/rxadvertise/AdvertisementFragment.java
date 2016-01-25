package it.ennova.rxadvertise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import it.ennova.zerxconf.*;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AdvertisementFragment extends Fragment {

    private Map<String, String> attributes = new HashMap<>(1);
    private final Map<String, String> emptyAttributes = new HashMap<>(0);
    private boolean forceNative = true;
    private boolean forceEmptySet = false;
    private Subscription subscription;

    @Bind(R.id.imgAdvertiseStatus)
    ImageView imgAdvertiseStatus;
    @Bind(R.id.switchEmptyAttributeSet)
    Switch switchEmptyAttributes;
    @Bind(R.id.txtServiceName)
    EditText txtServiceName;
    @Bind(R.id.txtServiceType)
    EditText txtServiceType;
    @Bind(R.id.txtServicePort)
    EditText txtServicePort;
    @Bind(R.id.commandViewFlipper)
    ViewFlipper viewFlipper;


    public AdvertisementFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_advertise, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildDefaultData();
        ViewFlipperUtils.initAnimationOn(viewFlipper, getActivity());
    }

    private void buildDefaultData() {
        attributes.put("canConfigure", "NO");
        txtServiceName.setText(BuildConfig.DEFAULT_SERVICE_NAME);
        txtServiceType.setText(BuildConfig.DEFAULT_SERVICE_TYPE);
        txtServicePort.setText(BuildConfig.DEFAULT_SERVICE_PORT);
    }

    @OnCheckedChanged(R.id.switchNativeApi)
    void onNativeApiCheckedChanged(boolean isChecked) {
        forceNative = isChecked;
        fixEmptyAttributeEnabledState(isChecked);
    }

    private void fixEmptyAttributeEnabledState(boolean isChecked) {
        if (!isChecked) {
            switchEmptyAttributes.setChecked(true);
            switchEmptyAttributes.setEnabled(false);
        } else {
            switchEmptyAttributes.setEnabled(true);
        }
    }

    @OnCheckedChanged(R.id.switchEmptyAttributeSet)
    void onEmptyAttributesCheckedChanged(boolean isChecked) {
        forceEmptySet = isChecked;
    }

    @OnClick(R.id.btnStartService)
    void onAdvertisementButtonClicked() {

        subscription = ZeRXconf.advertise(getActivity(), txtServiceName.getText().toString(), txtServiceType.getText().toString(),
                Integer.valueOf(txtServicePort.getText().toString()), getAttributes(), forceNative).subscribe(onNext, onError);

    }

    @OnClick(R.id.btnStopService)
    void onStopAdvertisementButtonClicked() {
        subscription.unsubscribe();
        imgAdvertiseStatus.setImageResource(R.drawable.ic_remove_circle_red_500_18dp);
        viewFlipper.showPrevious();
    }

    private Map<String, String> getAttributes() {
        return forceEmptySet ? emptyAttributes : attributes;
    }

    private Action1<NetworkServiceDiscoveryInfo> onNext = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo networkServiceDiscoveryInfo) {
            imgAdvertiseStatus.setImageResource(R.drawable.ic_check_circle_green_500_18dp);
            viewFlipper.showNext();
        }
    };

    private Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
