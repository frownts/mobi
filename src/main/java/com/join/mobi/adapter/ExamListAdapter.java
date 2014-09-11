package com.join.mobi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.mobi.activity.ExamActivity_;
import com.join.mobi.dto.ExamItem;
import com.join.mobi.utils.MarkerMap;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class ExamListAdapter extends BaseAdapter {

    private List<ExamItem> examItems = new ArrayList<ExamItem>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        TextView title;
        TextView seq;
        ImageView marker;
    }

    public List<ExamItem> getItems(){
        return examItems;
    }

    public void updateItems(List<ExamItem> _examItems){
        examItems.clear();
        examItems.addAll(_examItems);
    }

    public ExamListAdapter(Context c, List<ExamItem> _examItems) {
        mContext = c;
        examItems.clear();
        examItems.addAll(_examItems);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return examItems.size();
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
        ExamItem examItem = examItems.get(position);
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.examlist_listview_layout, null);
            holder = new GirdHolder();
            holder.seq = (TextView) convertView.findViewById(R.id.seq);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.marker = (ImageView) convertView.findViewById(R.id.marker);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }

        holder.title.setText(examItem.getTitle());
        holder.seq.setText((position+1)+".");
        if(MarkerMap.isMarked(examItem.getItemId()+""))holder.marker.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_flag));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExamActivity_.intent(mContext).examIndex(position).start();
                ((Activity)mContext).finish();
            }
        });

        return convertView;
    }

}

