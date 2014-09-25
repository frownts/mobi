package com.join.mobi.adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.FileUtils;
import com.join.android.app.common.utils.MThumbnailUtils;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.customview.SpringProgressView;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalCourseChapterAdapter extends BaseAdapter {

    private List<Chapter> chapters = new ArrayList<Chapter>(0);

    private Context mContext;
    private LayoutInflater inflater;
    private int mRightWidth = 0;
    /** 当前播放的是哪一个**/
    private int currentPosition;

    private class ViewHolder {
        TextView title;
        TextView validUtil;
        TextView filesize;
        TextView learnedTime;
        TextView chapterDuration;
        View main;
        RelativeLayout item_left;
        RelativeLayout item_right;
        TextView item_right_txt;
        ImageView roundDel;
        ImageView thumbnail;
        SpringProgressView springProgressView;
    }

    public List<Chapter> getItems() {

        return chapters;
    }

    public void updateItems(List<Chapter> _chapters) {
        chapters.clear();
        chapters.addAll(_chapters);
    }

    public LocalCourseChapterAdapter(Context c, List<Chapter> _chapters, int rightWidth, OnRightItemClickListener onRightItemClickListener) {
        mContext = c;
        chapters.clear();
        chapters.addAll(_chapters);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRightWidth = rightWidth;
        this.mListener = onRightItemClickListener;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public Chapter getItem(int position) {
        if (chapters == null || chapters.size() == 0) return null;
        return chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Chapter chapter = chapters.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.localcourse_chapter_listview_layout, null);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
            holder.main = convertView.findViewById(R.id.main);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.chapterDuration = (TextView) convertView.findViewById(R.id._chapterDuration);
            holder.learnedTime = (TextView) convertView.findViewById(R.id.learnedTime);
            holder.filesize = (TextView) convertView.findViewById(R.id.filesize);
            holder.validUtil = (TextView) convertView.findViewById(R.id.validUtil);
            holder.roundDel = (ImageView) convertView.findViewById(R.id.roundDel);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

            holder.springProgressView = (SpringProgressView) convertView.findViewById(R.id.springProgressView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        holder.title.setText(chapter.getTitle());
        holder.filesize.setText(FileUtils.FormatFileSize(chapter.getFilesize()));
        holder.chapterDuration.setText(DateUtils.SecondToNormalTime(chapter.getChapterDuration()));
        holder.learnedTime.setText(DateUtils.SecondToNormalTime(chapter.getLearnedTime()));

        holder.thumbnail.setImageBitmap(MThumbnailUtils.getVideoThumbnail(DownloadTool.getFileByUrl((DownloadApplication)mContext.getApplicationContext(),chapter.getDownloadUrl()),80,80, MediaStore.Images.Thumbnails.MICRO_KIND));

        if(chapter.getValidUntil()==null)holder.validUtil.setVisibility(View.GONE);
        else{
            holder.validUtil.setVisibility(View.VISIBLE);
            holder.validUtil.setText(chapter.getLeftDays() + "天后过期");
        }

        if (((LocalCourseDetailActivity) mContext).isTrashShowing()) {
            holder.roundDel.setVisibility(View.VISIBLE);
        } else
            holder.roundDel.setVisibility(View.GONE);

        if (chapter.isPlaying()) {//红色背影框
            holder.main.setBackgroundResource(R.drawable.red_border_frame);
        }

        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });

        holder.springProgressView.setMaxCount(chapter.getChapterDuration());
        holder.springProgressView.setCurrentCount(chapter.getLearnedTime());

        return convertView;
    }

    /**
     * 单击事件监听器
     */
    private OnRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(OnRightItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

}

