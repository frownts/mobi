package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.ResourceShare;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class ShareAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ResourceShare> resourceShares = new ArrayList<ResourceShare>(0);

    private class GirdHolder {
        ImageView fileLegend;
        TextView fileName;
        TextView fileSize;
    }

    public ShareAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<ResourceShare> _resourceShares){
        resourceShares.clear();
        resourceShares.addAll(_resourceShares);
    }

    @Override
    public int getCount() {
        return resourceShares.size();
    }

    @Override
    public Object getItem(int position) {
        return resourceShares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ResourceShare resourceShare = resourceShares.get(position);

        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_share, null);
            holder = new GirdHolder();
            holder.fileLegend = (ImageView) convertView.findViewById(R.id.fileLegend);
            holder.fileName = (TextView) convertView.findViewById(R.id.fileName);
            holder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }
        holder.fileName.setText(resourceShare.getName());
        holder.fileSize.setText(resourceShare.getFileSize() + "");
        int type = resourceShare.getType();
        if (type == 1) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video));
        else if (type == 2) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.music));
        else if (type == 3) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdf));
        else if (type == 4) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img));


        return convertView;
    }


}

