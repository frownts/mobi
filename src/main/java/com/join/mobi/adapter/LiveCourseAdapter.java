package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Course;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseAdapter extends BaseAdapter {

    private List<Course> liveCourses = new ArrayList<Course>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        ImageView url;
        TextView title;
        TextView lastLearn;
        TextView courseHour;
        TextView totalDuration;
    }

    public List<Course> getItems(){
        return liveCourses;
    }

    public void updateItems(List<Course> _liveCourses){
        liveCourses.clear();
        liveCourses.addAll(_liveCourses);
    }

    public LiveCourseAdapter(Context c, List<Course> _liveCourses) {
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
        final Course course = liveCourses.get(position);
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
        ImageLoader.getInstance().displayImage(course.getUrl(),holder.url);
        holder.title.setText(course.getTitle());
        holder.lastLearn.setText("上次学习:  "+ DateUtils.FormatForCourseLastLearningTime(course.getLastLearn()));
        holder.courseHour.setText("课程总时长:  "+DateUtils.SecondToNormalTime(course.getCourseHour()));
        holder.totalDuration.setText("累计学习:  "+DateUtils.SecondToNormalTime(Long.parseLong(course.getTotalDuration())));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveCourseDetailActivity_.intent(mContext).url(course.getUrl()).courseId(course.getCourseId()+"").name(course.getTitle()).start();
            }
        });

        return convertView;
    }

}

