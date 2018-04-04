package com.uet.wifiposition.ui.main.home.motion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uet.wifiposition.R;

/**
 * Created by ducnd on 9/29/17.
 */

public class MotionAdapter extends BaseAdapter {
    private IMotionAdapter mInterf;

    public MotionAdapter(IMotionAdapter interf) {
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
        MotionViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
            viewHolder = new MotionViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MotionViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(mInterf.getName(position));
        return convertView;
    }

    private static final class MotionViewHolder {
        private TextView tvName;

        public MotionViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface IMotionAdapter {
        int getCount();

        String getName(int position);
    }
}
