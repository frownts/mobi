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
import com.join.mobi.dto.ChapterDto;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseChapterAdapter extends BaseAdapter {

    private List<ChapterDto> chapterDtos = new ArrayList<ChapterDto>(0);


    private Context mContext;
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView title;
        TextView chapterDuration;
        TextView learnedTime;
        TextView filesize;
        ImageView download;
    }

    public List<ChapterDto> getItems() {
        return chapterDtos;
    }

    public void updateItems(List<ChapterDto> _chapters) {
        chapterDtos.clear();
        chapterDtos.addAll(_chapters);
    }

    public LiveCourseChapterAdapter(Context c, List<ChapterDto> _chapters) {
        mContext = c;
        chapterDtos.clear();
        chapterDtos.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chapterDtos.size();
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
        ChapterDto chapter = chapterDtos.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.livecourse_chapter_listview_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.chapterDuration = (TextView) convertView.findViewById(R.id.chapterDuration);
            holder.learnedTime = (TextView) convertView.findViewById(R.id.learnedTime);
            holder.filesize = (TextView) convertView.findViewById(R.id.filesize);
            holder.download = (ImageView) convertView.findViewById(R.id.download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(chapter.getTitle());
        holder.filesize.setText(chapter.getFilesize() + "byte");
        holder.chapterDuration.setText(chapter.getChapterDuration() + "");
        holder.learnedTime.setText(chapter.getLearnedTime() + "");


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().makeText(mContext,"start downloading",DialogManager.DIALOG_TYPE_OK);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放
                DialogManager.getInstance().makeText(mContext,"play video",DialogManager.DIALOG_TYPE_OK);
            }
        });

        return convertView;
    }

}

