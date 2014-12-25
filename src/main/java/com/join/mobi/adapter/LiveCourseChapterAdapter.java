package com.join.mobi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.LruCache;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class ViewHolder2{
    public View chapterView;
    public TextView cTitle;
    public ImageView cDownload;
    public TextView cFileSize;
    public SpringProgressView cSpringProgressView;
    public TextView learnedTime;
    public TextView chapterDuration;
    public View main;
}
    private LruCache chapterCacheMap = new LruCache(20);
    View mainbg;
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


            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //判断是否已展开
                    if(holder.chapterContainer.getVisibility()==View.VISIBLE){
                        holder.chapterContainer.setVisibility(View.GONE);
                    }else{
                        holder.chapterContainer.setVisibility(View.VISIBLE);
                        holder.chapterContainer.removeAllViews();
                        //生成章节列表
                        for(final ChapterDetailDto chapterDetailDto :chapter.getChapter()){
                            ViewHolder2 viewHolder2 = (ViewHolder2) chapterCacheMap.get(position);
                            if(viewHolder2==null){
                                viewHolder2 = new ViewHolder2();
                                viewHolder2.chapterView = inflater.inflate(R.layout.livecourse_chapter_listview_layout_bak, null);
                                viewHolder2.cTitle = (TextView) viewHolder2.chapterView.findViewById(R.id.title);
                                viewHolder2.cDownload=(ImageView) viewHolder2.chapterView.findViewById(R.id.download);
                                viewHolder2.cFileSize = (TextView) viewHolder2.chapterView.findViewById(R.id.filesize);
                                viewHolder2.cSpringProgressView = (SpringProgressView) viewHolder2.chapterView.findViewById(R.id.springProgressView);
                                viewHolder2.learnedTime = (TextView) viewHolder2.chapterView.findViewById(R.id.learnedTime);
                                viewHolder2.chapterDuration =  (TextView) viewHolder2.chapterView.findViewById(R.id.chapterDuration);
                                viewHolder2.main = viewHolder2.chapterView.findViewById(R.id.main);
                            }
                            viewHolder2.cTitle.setText(chapterDetailDto.getTitle());
                            viewHolder2.cDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (chapterDetailDto.getFileSize() == 0) {
                                        DialogManager.getInstance().makeText(mContext, mContext.getString(R.string.file_invalid), DialogManager.DIALOG_TYPE_ERROR);
                                        return;
                                    }
                                    if (download != null) download.download(chapter,chapterDetailDto);
                                }
                            });

                            viewHolder2.cFileSize.setText(FileUtils.FormatFileSize(chapterDetailDto.getFileSize()));

                            if (chapterDetailDto.getChapterDuration() == 0)
                                viewHolder2.cSpringProgressView.setMaxCount(100);
                            else
                                viewHolder2.cSpringProgressView.setMaxCount(chapterDetailDto.getChapterDuration());
                            viewHolder2.cSpringProgressView.setCurrentCount(chapterDetailDto.getLearnedTime());

                            viewHolder2.learnedTime.setText(DateUtils.SecondToNormalTime(chapterDetailDto.getLearnedTime()));

                            viewHolder2.chapterDuration.setText(DateUtils.SecondToNormalTime(chapterDetailDto.getChapterDuration()));

                            final ViewHolder2 finalViewHolder = viewHolder2;
                            viewHolder2.chapterView.setOnClickListener(new View.OnClickListener() {
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

                                    for(ChapterDetailDto c :chapter.getChapter()){
                                        c.setPlaying(false);
                                    }
                                    chapterDetailDto.setPlaying(true);

                                    if(mainbg!=null)mainbg.setBackgroundResource(R.drawable.border_bg);
                                    finalViewHolder.main.setBackgroundResource(R.drawable.red_border_frame);
                                    mainbg = finalViewHolder.main;

                                    Intent intent = new Intent("org.androidannotations.play");
                                    intent.putExtra("playUrl", chapterDetailDto.getPlayUrl());
                                    mContext.sendBroadcast(intent);
                                    LiveCourseChapterAdapter.this.notifyDataSetChanged();
                                }
                            });

                            if (chapterDetailDto.isPlaying()){
                                viewHolder2.main.setBackgroundResource(R.drawable.red_border_frame);
                                mainbg = viewHolder2.main;
                            }

                            else
                                viewHolder2.main.setBackgroundResource(R.drawable.border_bg);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            params.setMargins(0,0,0,20);

                            holder.chapterContainer.addView(viewHolder2.chapterView,params);

                        }

                    }

                }
            });

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


    public ChapterDetailDto getCurrentChapterDetailDto() {
        return currentChapterDetailDto;
    }

    public void setCurrentChapterDetailDto(ChapterDetailDto currentChapterDetailDto) {
        this.currentChapterDetailDto = currentChapterDetailDto;
    }

    public interface Download {
        public void download(ChapterDto chapterDto,ChapterDetailDto chapterDetailDto);
    }


}

