package it.ennova.rxadvertise;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView serviceName, serviceAddress;
    private OnServiceSelectedListener serviceSelectedListener;
    private NetworkServiceDiscoveryInfo info;

    public ServiceViewHolder(View itemView, OnServiceSelectedListener serviceSelectedListener) {
        super(itemView);
        this.serviceSelectedListener = serviceSelectedListener;
        serviceName = (TextView) itemView.findViewById(R.id.txtServiceName);
        serviceAddress = (TextView) itemView.findViewById(R.id.txtServiceType);

        itemView.setOnClickListener(this);
    }

    public void bindTo(@NonNull NetworkServiceDiscoveryInfo info) {
        this.info = info;
        serviceName.setText(String.format("%s (%s)", info.getServiceName(), info.getServiceLayer()));
        serviceAddress.setText(String.format("%s:%s", getAddressFrom(info), info.getServicePort()));
    }

    private String getAddressFrom(@NonNull NetworkServiceDiscoveryInfo info) {
        String address;
        if (info.getAddress() != null) {
            address = info.getAddress().getHostAddress();
        } else {
            address = "N/A";
        }
        return address;
    }

    @Override
    public void onClick(View v) {
        serviceSelectedListener.onServiceSelected(info);
    }
}
