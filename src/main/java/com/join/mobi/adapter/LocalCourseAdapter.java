package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.ChapterManager;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.manager.ReferenceManager;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.activity.LocalCourseActivity;
import com.join.mobi.activity.LocalCourseDetailActivity_;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalCourseAdapter extends BaseAdapter {

    private List<LocalCourse> localCourses = new ArrayList<LocalCourse>(0);

    private Context mContext;
    private LayoutInflater inflater;
    private boolean trash;

    private class GirdHolder {
        ImageView url;
        ImageView trash;
        TextView title;
        TextView chapterNum;
        TextView referenceNum;
        TextView learningTimes;
    }

    public List<LocalCourse> getItems() {
        return localCourses;
    }

    public void updateItems(List<LocalCourse> _localCourses) {
        localCourses.clear();
        localCourses.addAll(_localCourses);
    }

    public LocalCourseAdapter(Context c, List<LocalCourse> _localCourses) {
        mContext = c;
        localCourses.clear();
        if (_localCourses != null)
            localCourses.addAll(_localCourses);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return localCourses.size();
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
        final LocalCourse course = localCourses.get(position);
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.localcourse_listview_layout, null);
            holder = new GirdHolder();
            holder.url = (ImageView) convertView.findViewById(R.id.url);
            holder.trash = (ImageView) convertView.findViewById(R.id.trash);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.chapterNum = (TextView) convertView.findViewById(R.id.chapterNum);
            holder.referenceNum = (TextView) convertView.findViewById(R.id.referenceNum);
            holder.learningTimes = (TextView) convertView.findViewById(R.id.learningTimes);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(course.getUrl(), holder.url);
        holder.title.setText(course.getTitle());
        holder.chapterNum.setText(course.getChapterNum() + " 个章节");
        holder.referenceNum.setText(course.getRefNum() + " 个资料");
        holder.learningTimes.setText(course.getLearningTimes() + "");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalCourseDetailActivity_.intent(mContext).courseId(course.getId() + "").name(course.getTitle()).start();
            }
        });

        if (trash) {
            holder.trash.setVisibility(View.VISIBLE);
        } else
            holder.trash.setVisibility(View.INVISIBLE);

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> ids = new ArrayList<Integer>(0);

                //先删除文件、再删除记录

                CloseableIterable<Chapter> closeableIterable = course.getChapters().getWrappedIterable();
                Iterator<Chapter> chapterIterator = closeableIterable.iterator();

                while (chapterIterator.hasNext()){
                    Chapter c = chapterIterator.next();
                    DownloadTool.deleteDownloadTask((DownloadApplication) mContext.getApplicationContext(), c.getDownloadUrl());
                    ids.add(c.getId());
                }
                closeableIterable.closeableIterator();
                if(ids.size()>0)
                ChapterManager.getInstance().deleteByIds(ids);
                ids.clear();

                CloseableIterable<Reference> _closeableIterable = course.getReferences().getWrappedIterable();
                Iterator<Reference> refIterator = _closeableIterable.iterator();

                while (refIterator.hasNext()){
                    Reference _c = refIterator.next();
                    DownloadTool.deleteDownloadTask((DownloadApplication) mContext.getApplicationContext(), _c.getUrl());
                    ids.add(_c.getId());
                }
                _closeableIterable.closeableIterator();
                if(ids.size()>0)
                ReferenceManager.getInstance().deleteByIds(ids);

                //删除主表记录
                LocalCourseManager.getInstance().deleteById(course.getId());

                ((LocalCourseActivity)(mContext)).retrieveDataFromDB();
            }
        });

        return convertView;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }
}

