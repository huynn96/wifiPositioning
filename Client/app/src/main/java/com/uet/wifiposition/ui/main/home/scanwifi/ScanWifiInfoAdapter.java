package com.uet.wifiposition.ui.main.home.scanwifi;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.WifiInfoModel;
import com.uet.wifiposition.ui.customview.viewholder.NonViewHolder;
import com.uet.wifiposition.ui.main.base.intetf.IGetPosition;

import java.text.DecimalFormat;

/**
 * Created by ducnd on 9/22/17.
 */

public class ScanWifiInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private IScanWifiInfoAdapter mInterf;
    private DecimalFormat format = new DecimalFormat("#.##");

    public ScanWifiInfoAdapter(IScanWifiInfoAdapter interf) {
        mInterf = interf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_info, parent, false);
            return new ScanWifiInfoViewHolder(view, this);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_none, parent, false);
        return new NonViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == getItemCount() - 1) {
            return;
        }
        ScanWifiInfoViewHolder holder = (ScanWifiInfoViewHolder) viewHolder;
        WifiInfoModel data = mInterf.getData(position);
        holder.tvName.setText(data.getName());
        holder.tvFrequency.setText("Frequency: " + format.format(data.getFrequency()));
        holder.tvMacAddress.setText("Mac address: " + data.getMacAddress());
        holder.tvLevel.setText("Leve: " + format.format(data.getLevel()));
        holder.tvNumberScan.setText(data.getCount() + "");
        holder.check.setChecked(data.isCheck());
    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                IGetPosition getPosition = (IGetPosition) v.getTag();
                mInterf.getData(getPosition.getPosition()).setCheck(((CheckBox) v).isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return 1;
        }
        return 0;
    }

    private static final class ScanWifiInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvFrequency, tvMacAddress, tvLevel, tvNumberScan;
        private CheckBox check;

        private ScanWifiInfoViewHolder(View itemView, View.OnClickListener onClick) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvFrequency = (TextView) itemView.findViewById(R.id.tv_frequency);
            tvMacAddress = (TextView) itemView.findViewById(R.id.tv_mac_address);
            tvLevel = (TextView) itemView.findViewById(R.id.tv_level);
            tvNumberScan = (TextView) itemView.findViewById(R.id.tv_number_scan);
            check = (CheckBox) itemView.findViewById(R.id.check);
            IGetPosition position = this::getAdapterPosition;
            check.setTag(position);
            check.setOnClickListener(onClick);
        }
    }

    interface IScanWifiInfoAdapter {
        int getCount();

        WifiInfoModel getData(int position);

    }
}
