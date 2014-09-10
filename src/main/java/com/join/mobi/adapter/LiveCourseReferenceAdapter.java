package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.dto.ReferenceDto;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseReferenceAdapter extends BaseAdapter {

    private List<ReferenceDto> referenceDtos = new ArrayList<ReferenceDto>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView title;
        TextView fileSize;
        TextView type;
        ImageView download;
    }

    public List<ReferenceDto> getItems() {
        return referenceDtos;
    }

    public void updateItems(List<ReferenceDto> referenceDtos) {
        referenceDtos.clear();
        referenceDtos.addAll(referenceDtos);
    }

    public LiveCourseReferenceAdapter(Context c, List<ReferenceDto> _chapters) {
        mContext = c;
        referenceDtos.clear();
        referenceDtos.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return referenceDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ReferenceDto reference = referenceDtos.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.livecourse_reference_listview_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            holder.type = (TextView) convertView.findViewById(R.id._type);
            holder.download = (ImageView) convertView.findViewById(R.id.download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(reference.getTitle());
        holder.fileSize.setText(reference.getFileSize()+"KB");
        //根据类型，判断生成文字。
        holder.type.setText(reference.getType()+"");


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().makeText(mContext,"start downloading",DialogManager.DIALOG_TYPE_OK);
            }
        });

        return convertView;
    }

}

