package com.join.mobi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.FileUtils;
import com.join.mobi.customview.SpringProgressView;
import com.join.mobi.dto.ChapterDetailDto;
import com.join.mobi.dto.ChapterDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseChapterAdapter extends BaseAdapter {

    private List<ChapterDto> chapterDtos = new ArrayList<ChapterDto>(0);

    private Download download;
    private Context mContext;
    private LayoutInflater inflater;
    /**
     * 当前播放的是哪一个*
     */
    private int currentPosition;

    private ChapterDetailDto currentChapterDetailDto;

    private class ViewHolder {
        View main;
        TextView title;
        TextView fileCount;
        boolean expand = false;
        LinearLayout chapterContainer;
        View chapterView;
    }

    private class OViewHolder {
        View main;
        TextView title;
        TextView chapterDuration;
        TextView learnedTime;
        TextView filesize;
        ImageView download;
        SpringProgressView springProgressView;
    }

    public List<ChapterDto> getItems() {
        return chapterDtos;
    }

    public void updateItems(List<ChapterDto> _chapters) {
        chapterDtos.clear();
        chapterDtos.addAll(_chapters);
    }

    public LiveCourseChapterAdapter(Context c, List<ChapterDto> _chapters, Download _download) {
        mContext = c;
        chapterDtos.clear();
        chapterDtos.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.download = _download;
    }

    @Override
    public int getCount() {
        return chapterDtos.size();
    }

    @Override
    public ChapterDto getItem(int position) {
        if (chapterDtos == null || chapterDtos.size() == 0) return null;
        return chapterDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(chapterDtos.get(position).getChapter()!=null||chapterDtos.get(position).getPlayUrl()==""){
            return 0;
        }
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(chapterDtos.get(position).getChapter()!=null&&StringUtils.isEmpty(chapterDtos.get(position).getPlayUrl())){
            final ChapterDto chapter = chapterDtos.get(position);
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.livecourse_chapter_listview_layout, null);
                holder = new ViewHolder();
                holder.main = convertView.findViewById(R.id.main);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.fileCount = (TextView) convertView.findViewById(R.id.fileCount);
                holder.chapterContainer = (LinearLayout)convertView.findViewById(R.id.chapterContainer);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(chapter.getTitle());
            holder.fileCount.setText(chapter.getChapter().size()+"");
//        holder.filesize.setText(FileUtils.FormatFileSize(chapter.getFileSize()));
//        holder.chapterDuration.setText(DateUtils.SecondToNormalTime(chapter.getChapterDuration()));
//        holder.learnedTime.setText(DateUtils.SecondToNormalTime(chapter.getLearnedTime()));
//
//        if (chapter.getChapterDuration() == 0)
//            holder.springProgressView.setMaxCount(100);
//        else
//            holder.springProgressView.setMaxCount(chapter.getChapterDuration());
//        holder.springProgressView.setCurrentCount(chapter.getLearnedTime());


//        if (chapter.isPlaying())
//            holder.main.setBackgroundResource(R.drawable.red_border_frame);
//        else
//            holder.main.setBackgroundResource(R.drawable.border_bg);

            holder.chapterContainer.removeAllViews();
            //生成章节列表
            for(final ChapterDetailDto chapterDetailDto :chapter.getChapter()){
                View chapterView = inflater.inflate(R.layout.livecourse_chapter_listview_layout_bak, null);
                TextView cTitle = (TextView) chapterView.findViewById(R.id.title);
                cTitle.setText(chapterDetailDto.getTitle());
                ImageView cDownload = (ImageView) chapterView.findViewById(R.id.download);
                cDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chapterDetailDto.getFileSize() == 0) {
                            DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
                            return;
                        }
                        if (download != null) download.download(chapter,chapterDetailDto);
                    }
                });

                TextView cFileSize = (TextView) chapterView.findViewById(R.id.filesize);
                cFileSize.setText(FileUtils.FormatFileSize(chapterDetailDto.getFileSize()));

                SpringProgressView cSpringProgressView = (SpringProgressView) chapterView.findViewById(R.id.springProgressView);
                if (chapterDetailDto.getChapterDuration() == 0)
                    cSpringProgressView.setMaxCount(100);
                else
                    cSpringProgressView.setMaxCount(chapterDetailDto.getChapterDuration());
                cSpringProgressView.setCurrentCount(chapterDetailDto.getLearnedTime());

                TextView learnedTime = (TextView) chapterView.findViewById(R.id.learnedTime);
                learnedTime.setText(DateUtils.SecondToNormalTime(chapterDetailDto.getLearnedTime()));

                TextView chapterDuration = (TextView) chapterView.findViewById(R.id.chapterDuration);
                chapterDuration.setText(DateUtils.SecondToNormalTime(chapterDetailDto.getChapterDuration()));

                View main =  chapterView.findViewById(R.id.main);

                chapterView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtils.isEmpty(chapterDetailDto.getPlayUrl())) {
                            DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
                            return;
                        }
                        if(currentChapterDetailDto!=null){
                            currentChapterDetailDto.setPlaying(false);
                        }
                        currentChapterDetailDto =  chapterDetailDto;
                        chapterDetailDto.setPlaying(true);

//                    currentPosition = position;
//                    for (ChapterDto c : chapterDtos) c.setPlaying(false);
//                    LiveCourseChapterAdapter.this.notifyDataSetChanged();
//                    chapter.setPlaying(true);
                        LiveCourseChapterAdapter.this.notifyDataSetChanged();
                        Intent intent = new Intent("org.androidannotations.play");
                        intent.putExtra("playUrl", chapterDetailDto.getPlayUrl());
                        mContext.sendBroadcast(intent);
                    }
                });

                if (chapterDetailDto.isPlaying())
                    main.setBackgroundResource(R.drawable.red_border_frame);
                else
                    main.setBackgroundResource(R.drawable.border_bg);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0,0,0,20);
                holder.chapterContainer.addView(chapterView,params);

            }

//        holder.download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (chapter.getFileSize() == 0) {
//                    DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
//                    return;
//                }
//                if (download != null) download.download(chapter);
//            }
//        });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //判断是否已展开
                    if(holder.chapterContainer.getVisibility()==View.VISIBLE){
                        holder.chapterContainer.setVisibility(View.GONE);
                    }else{
                        holder.chapterContainer.setVisibility(View.VISIBLE);
                    }

                }
            });

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (StringUtils.isEmpty(chapter.getPlayUrl())) {
//                    DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
//                    return;
//                }
//
//                currentPosition = position;
//                for (ChapterDto c : chapterDtos) c.setPlaying(false);
//                LiveCourseChapterAdapter.this.notifyDataSetChanged();
//                chapter.setPlaying(true);
//
//                Intent intent = new Intent("org.androidannotations.play");
//                intent.putExtra("playUrl", chapter.getPlayUrl());
//                mContext.sendBroadcast(intent);
//            }
//        });
        }else{

            final ChapterDto chapter = chapterDtos.get(position);
            final OViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.livecourse_chapter_listview_layout1, null);
                holder = new OViewHolder();
                holder.main = convertView.findViewById(R.id.main);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.chapterDuration = (TextView) convertView.findViewById(R.id.chapterDuration);
                holder.learnedTime = (TextView) convertView.findViewById(R.id.learnedTime);
                holder.filesize = (TextView) convertView.findViewById(R.id.filesize);
                holder.download = (ImageView) convertView.findViewById(R.id.download);
                holder.springProgressView = (SpringProgressView) convertView.findViewById(R.id.springProgressView);
                convertView.setTag(holder);
            } else {
                holder = (OViewHolder) convertView.getTag();
            }

            holder.title.setText(chapter.getTitle());
            holder.filesize.setText(FileUtils.FormatFileSize(chapter.getFileSize()));
            holder.chapterDuration.setText(DateUtils.SecondToNormalTime(chapter.getChapterDuration()));
            holder.learnedTime.setText(DateUtils.SecondToNormalTime(chapter.getLearnedTime()));


            if (chapter.getChapterDuration() == 0)
                holder.springProgressView.setMaxCount(100);
            else
                holder.springProgressView.setMaxCount(chapter.getChapterDuration());
            holder.springProgressView.setCurrentCount(chapter.getLearnedTime());


            if (chapter.isPlaying())
                holder.main.setBackgroundResource(R.drawable.red_border_frame);
            else
                holder.main.setBackgroundResource(R.drawable.border_bg);


            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chapter.getFileSize() == 0) {
                        DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
                        return;
                    }
                    if (download != null) download.download(chapter,null);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (StringUtils.isEmpty(chapter.getPlayUrl())) {
                        DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
                        return;
                    }

                    currentPosition = position;
                    for (ChapterDto c : chapterDtos) c.setPlaying(false);
                    LiveCourseChapterAdapter.this.notifyDataSetChanged();
                    chapter.setPlaying(true);

                    Intent intent = new Intent("org.androidannotations.play");
                    intent.putExtra("playUrl", chapter.getPlayUrl());
                    mContext.sendBroadcast(intent);
                }
            });

            return convertView;
        }


        return convertView;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public interface Download {
        public void download(ChapterDto chapterDto,ChapterDetailDto chapterDetailDto);
    }
}

