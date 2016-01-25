package it.ennova.rxadvertise;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class ServiceViewHolder extends RecyclerView.ViewHolder {
    private TextView serviceName, serviceAddress;

    public ServiceViewHolder(View itemView) {
        super(itemView);
        serviceName = (TextView) itemView.findViewById(R.id.txtServiceName);
        serviceAddress = (TextView) itemView.findViewById(R.id.txtServiceType);
    }

    public void bindTo(@NonNull NetworkServiceDiscoveryInfo info) {
        serviceName.setText(info.getServiceName());
        serviceAddress.setText(String.format("%s:%s", info.getServiceLayer(), info.getServicePort()));
    }
}
