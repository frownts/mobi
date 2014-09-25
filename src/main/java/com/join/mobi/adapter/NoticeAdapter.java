package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Notice;
import com.join.android.app.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class NoticeAdapter extends BaseAdapter {

    private List<Notice> notices = new ArrayList<Notice>(0);

    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        TextView createTime;
        TextView content;
        TextView title;
    }

    public List<Notice> getItems() {
        return notices;
    }

    public void updateItems(List<Notice> _notices) {
        notices.clear();
        notices.addAll(_notices);
    }

    public NoticeAdapter(Context c, List<Notice> _liveCourses) {
        mContext = c;
        notices.clear();
        notices.addAll(_liveCourses);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notices.size();
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
        Notice notice = notices.get(position);
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notice_listview_layout, null);
            holder = new GirdHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }

        holder.title.setText(notice.getTitle());
        holder.content.setText(notice.getContent());
        Date date = DateUtils.Parse(notice.getCreateTime(),"yyyy-MM-dd");
        if(date!=null)
        holder.createTime.setText(DateUtils.FormatForDownloadTime(date.getTime()));

        return convertView;
    }

}

