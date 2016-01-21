package it.ennova.rxadvertise;


import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiscoveryFragment extends Fragment implements NsdManager.DiscoveryListener{

    public DiscoveryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onDiscoveryStarted(String serviceType) {

    }

    @Override
    public void onDiscoveryStopped(String serviceType) {

    }

    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {

    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {

    }
}
