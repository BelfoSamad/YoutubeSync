package com.belfoapps.youtubesync.views.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.presenters.MainPresenter;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    /*************************************** Declarations *****************************************/
    private ArrayList<Device> mDevices;
    private MainPresenter mPresenter;
    private boolean isAdvertising;

    /*************************************** Constructor ******************************************/
    public DevicesAdapter(boolean isAdvertising, MainPresenter mPresenter, ArrayList<Device> mDevices) {
        this.mDevices = mDevices;
        this.mPresenter = mPresenter;
        this.isAdvertising = isAdvertising;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public DevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_recyclerview_item, parent, false);

        return new DevicesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.ViewHolder holder, int position) {
        holder.device_name.setText(mDevices.get(position).getDeviceName());

            holder.container.setOnClickListener(v -> {
                if (isAdvertising)
                    mPresenter.acceptConnection(position);
                else mPresenter.requestConnection(position);
            });
    }

    @Override
    public int getItemCount() {
        if (mDevices == null) return 0;
        else return mDevices.size();
    }

    public void clearAll() {
        if (mDevices != null) mDevices.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Device> devices) {
        mDevices = devices;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView device_name;

        ViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.device_container);
            device_name = v.findViewById(R.id.device_name);
        }
    }

}
