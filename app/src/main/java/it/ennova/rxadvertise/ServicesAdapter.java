package it.ennova.rxadvertise;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;

public class ServicesAdapter extends RecyclerView.Adapter<ServiceViewHolder> {
    private List<NetworkServiceDiscoveryInfo> services = new ArrayList<>();

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ServiceViewHolder(inflater.inflate(R.layout.service_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        holder.bindTo(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void addService(@NonNull NetworkServiceDiscoveryInfo service) {
        services.add(service);
        notifyItemInserted(services.size() - 1);
        notifyDataSetChanged();
    }

    public void removeService(@NonNull NetworkServiceDiscoveryInfo service) {
        int foundIndex = -1;
        int listSize = services.size();

        for (int i = 0; i < listSize && (foundIndex == -1); i++) {
            if (services.get(i).equals(service)) {
                foundIndex = i;
            }
        }

        services.remove(foundIndex);
        notifyItemRemoved(foundIndex);
        notifyDataSetChanged();
    }
}
