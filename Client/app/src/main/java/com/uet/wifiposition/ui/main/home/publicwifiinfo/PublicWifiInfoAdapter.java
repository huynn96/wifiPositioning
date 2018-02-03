package com.uet.wifiposition.ui.main.home.publicwifiinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uet.wifiposition.R;

/**
 * Created by ducnd on 9/29/17.
 */

public class PublicWifiInfoAdapter extends BaseAdapter {
    private IPublicWifiInfoAdapter mInterf;

    public PublicWifiInfoAdapter(IPublicWifiInfoAdapter interf) {
        mInterf = interf;
    }

    @Override
    public int getCount() {
        return mInterf.getCount();
    }

    @Override
    public String getItem(int position) {
        return mInterf.getName(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PublicWifiInfoViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
            viewHolder = new PublicWifiInfoViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PublicWifiInfoViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(mInterf.getName(position));
        return convertView;
    }

    private static final class PublicWifiInfoViewHolder {
        private TextView tvName;

        public PublicWifiInfoViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface IPublicWifiInfoAdapter {
        int getCount();

        String getName(int position);
    }
}
