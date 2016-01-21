package it.ennova.rxadvertise;


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoveryFragment extends Fragment{

    private NsdManager nsdManager;
    private static final String TAG = "ZERXCONF-Discovery";

    @Bind(R.id.commandViewFlipper)
    ViewFlipper viewFlipper;

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
        nsdManager = (NsdManager) getActivity().getSystemService(Context.NSD_SERVICE);
        ViewFlipperUtils.initAnimationOn(viewFlipper, getActivity());
    }

    @OnClick(R.id.btnStartService)
    void onStartServiceClicked() {
        nsdManager.discoverServices("_services._dns-sd._udp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        viewFlipper.showNext();
    }

    @OnClick(R.id.btnStopService)
    void onStopServiceClicked() {
        nsdManager.stopServiceDiscovery(discoveryListener);
        viewFlipper.showPrevious();
    }

    private final NsdManager.ResolveListener resolveListener = new NsdManager.ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d(TAG, "onResolveFailed(" + serviceInfo.getServiceType() + "," + errorCode +")");
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceResolved(" + serviceInfo.getServiceName() +")");
        }
    };

    private final NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.d(TAG, "onStartDiscoveryFailed(" + serviceType + "," + errorCode +")");
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.d(TAG, "onStopDiscoveryFailed(" + serviceType + "," + errorCode +")");
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
            Log.d(TAG, "onDiscoveryStarted(" + serviceType +")");
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.d(TAG, "onDiscoveryStopped(" + serviceType +")");
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceFound(" + serviceInfo.getServiceName() +")");
            nsdManager.resolveService(serviceInfo, resolveListener);
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceLost(" + serviceInfo.getServiceName() +")");
        }
    };
}
