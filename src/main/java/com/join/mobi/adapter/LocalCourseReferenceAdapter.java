package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Reference;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalCourseReferenceAdapter extends BaseAdapter {

    private List<Reference> references = new ArrayList<Reference>(0);

    private Context mContext;
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView title;
        TextView fileSize;
        TextView type;
    }

    public List<Reference> getItems() {
        return references;
    }

    public void updateItems(List<Reference> References) {
        References.clear();
        References.addAll(References);
    }

    public LocalCourseReferenceAdapter(Context c, List<Reference> _reference) {
        mContext = c;
        references.clear();
        references.addAll(_reference);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return references.size();
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
        final Reference reference = references.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.localcourse_reference_listview_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            holder.type = (TextView) convertView.findViewById(R.id._type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(reference.getTitle());
        holder.fileSize.setText(reference.getFileSize()+"KB");
        //根据类型，判断生成文字。
        holder.type.setText(reference.getType()+"");

        return convertView;
    }

}

