package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Live;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseAdapter extends BaseAdapter {

    private List<Live> liveCourses = new ArrayList<Live>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        ImageView url;
        TextView title;
        TextView lastLearn;
        TextView courseHour;
        TextView totalDuration;
    }

    public void updateItems(List<Live> _liveCourses){
        liveCourses.clear();
        liveCourses.addAll(_liveCourses);
    }

    public LiveCourseAdapter(Context c, List<Live> _liveCourses) {
        mContext = c;
        liveCourses.clear();
        liveCourses.addAll(_liveCourses);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return liveCourses.size();
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
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.livecourse_listview_layout, null);
            holder = new GirdHolder();
            holder.url = (ImageView) convertView.findViewById(R.id.url);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.lastLearn = (TextView) convertView.findViewById(R.id.lastLearn);
            holder.courseHour = (TextView) convertView.findViewById(R.id.courseHour);
            holder.totalDuration = (TextView) convertView.findViewById(R.id.totalDuration);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }


        return convertView;
    }

}

