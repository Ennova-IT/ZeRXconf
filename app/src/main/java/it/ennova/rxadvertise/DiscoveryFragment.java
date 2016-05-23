package it.ennova.rxadvertise;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscription;
import rx.functions.Action1;

public class DiscoveryFragment extends Fragment implements OnServiceSelectedListener {

    @Bind(R.id.commandViewFlipper)
    ViewFlipper viewFlipper;

    @Bind(R.id.resultList)
    RecyclerView servicesList;

    @Bind(R.id.txtServiceTypeDiscovery)
    EditText txtServiceLayer;

    @Bind(R.id.txtServiceClearContent)
    ImageButton imageClearContent;

    private Subscription subscription;
    private ServicesAdapter adapter;

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

        adapter = new ServicesAdapter(this);

        txtServiceLayer.addTextChangedListener(serviceContentWatcher);
        txtServiceLayer.setText(BuildConfig.DEFAULT_SERVICE_TYPE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        servicesList.setLayoutManager(layoutManager);
        servicesList.setAdapter(adapter);
    }

    private final TextWatcher serviceContentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                imageClearContent.setVisibility(View.INVISIBLE);
            } else {
                imageClearContent.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @OnClick(R.id.btnStartService)
    void onStartServiceClicked() {
        if (TextUtils.isEmpty(txtServiceLayer.getText())) {
            subscription = ZeRXconf.startDiscovery(getActivity()).subscribe(onNext, onError);
        } else {
            subscription = ZeRXconf.startDiscovery(getActivity(), txtServiceLayer.getText().toString(), true)
                    .subscribe(onNext, onError);
        }

        viewFlipper.showNext();
    }

    private Action1<NetworkServiceDiscoveryInfo> onNext = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo serviceInfo) {
            if (serviceInfo.isAdded()) {
                adapter.addService(serviceInfo);
            } else {
                adapter.removeService(serviceInfo);
            }
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

    @OnClick(R.id.txtServiceClearContent)
    void onClearContentRequested() {
        txtServiceLayer.setText("");
        imageClearContent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onServiceSelected(NetworkServiceDiscoveryInfo info) {
        onStopServiceClicked();
        getActivity().startActivity(DetailActivity.getDetailActivityIntent(getActivity(), info, info.getAddress() != null));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
