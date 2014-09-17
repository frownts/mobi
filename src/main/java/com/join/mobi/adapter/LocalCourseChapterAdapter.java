package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.manager.DialogManager;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalCourseChapterAdapter extends BaseAdapter {

    private List<Chapter> Chapters = new ArrayList<Chapter>(0);

    private Context mContext;
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView title;
        TextView validUtil;
        TextView filesize;
        TextView learnedTime;
        TextView chapterDuration;
        View main;
    }

    public List<Chapter> getItems() {

        return Chapters;
    }

    public void updateItems(List<Chapter> _chapters) {
        Chapters.clear();
        Chapters.addAll(_chapters);
    }

    public LocalCourseChapterAdapter(Context c, List<Chapter> _chapters) {
        mContext = c;
        Chapters.clear();
        Chapters.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Chapters.size();
    }

    @Override
    public Object getItem(int position) {
        return Chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Chapter chapter = Chapters.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.localcourse_chapter_listview_layout, null);
            holder = new ViewHolder();
            holder.main = convertView.findViewById(R.id.main);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.chapterDuration = (TextView) convertView.findViewById(R.id._chapterDuration);
            holder.learnedTime = (TextView) convertView.findViewById(R.id.learnedTime);
            holder.filesize = (TextView) convertView.findViewById(R.id.filesize);
            holder.validUtil = (TextView) convertView.findViewById(R.id.validUtil);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(chapter.getTitle());
        holder.filesize.setText(chapter.getFilesize() + "byte");
        holder.chapterDuration.setText(chapter.getChapterDuration() + "");
        holder.learnedTime.setText(chapter.getLearnedTime() + "");
        holder.validUtil.setText(chapter.getValidUntil() + "");

        if (chapter.isPlaying()) {//红色背影框
            holder.main.setBackgroundResource(R.drawable.red_border_frame);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放
                DialogManager.getInstance().makeText(mContext, "play video" + chapter.getDownloadUrl(), DialogManager.DIALOG_TYPE_OK);
            }
        });

        return convertView;
    }

}

