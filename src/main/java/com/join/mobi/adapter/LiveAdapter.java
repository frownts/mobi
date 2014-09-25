package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Live;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.activity.MyVideoViewBufferFullScreen_;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveAdapter extends BaseAdapter {

    private List<Live> lives = new ArrayList<Live>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        TextView title;
        TextView createTime;
        TextView author;
    }

    public List<Live> getItems() {
        return lives;
    }

    public void updateItems(List<Live> _lives) {
        lives.clear();
        lives.addAll(_lives);
    }

    public LiveAdapter(Context c, List<Live> _liveCourses) {
        mContext = c;
        lives.clear();
        lives.addAll(_liveCourses);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lives.size();
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
        final Live live = lives.get(position);
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.live_listview_layout, null);
            holder = new GirdHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            holder.author = (TextView) convertView.findViewById(R.id.author);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }
        holder.title.setText(live.getTitle());
        holder.author.setText(live.getAuthor());
        Date date = DateUtils.Parse(live.getCreateTime(),"yyyy-MM-dd");
        if(date!=null)
            holder.createTime.setText(DateUtils.FormatForDownloadTime(date.getTime()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放直播
                MyVideoViewBufferFullScreen_.intent(mContext).path(live.getUrl()).start();
            }
        });

        return convertView;
    }

}

