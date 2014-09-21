package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.FileUtils;
import com.join.mobi.dto.ReferenceDto;
import com.join.mobi.utils.ContentTypeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseReferenceAdapter extends BaseAdapter {

    private List<ReferenceDto> referenceDtos = new ArrayList<ReferenceDto>(0);

    private Download download;
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

    public LiveCourseReferenceAdapter(Context c, List<ReferenceDto> _chapters,Download _download) {
        mContext = c;
        referenceDtos.clear();
        referenceDtos.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.download = _download;
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
        final ReferenceDto reference = referenceDtos.get(position);
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
        holder.fileSize.setText(FileUtils.FormatFileSize(reference.getFileSize()));
        //根据类型，判断生成文字。
        holder.type.setText(ContentTypeUtils.convertReferenceType(reference.getType()));


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(download!=null)download.download(reference);
            }
        });

        return convertView;
    }

    public interface Download{
        public void download(ReferenceDto referenceId);
    }

}

