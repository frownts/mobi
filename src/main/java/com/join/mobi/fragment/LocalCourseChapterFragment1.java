package com.join.mobi.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.ChapterManager;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.view.SwipeListView;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.adapter.LocalCourseChapterAdapter;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.localcourse_chapter_fragment_layout)
public class LocalCourseChapterFragment1 extends Fragment {

    @ViewById
    SwipeListView listView;
    LocalCourseChapterAdapter localCourseChapterAdapter;
    LocalCourse localCourse;

    @AfterViews
    void afterViews() {
        localCourse = ((LocalCourseDetailActivity) getActivity()).getLocalCourse();
        List<Chapter> chapters = new ArrayList<Chapter>(0);
        CloseableIterable<Chapter> closeableIterable = localCourse.getChapters().getWrappedIterable();
        Iterator<Chapter> chapterIterator = closeableIterable.iterator();

        while (chapterIterator.hasNext()) {
            Chapter c = chapterIterator.next();
            if (DownloadTool.isFinished((DownloadApplication) getActivity().getApplicationContext(), c.getDownloadUrl())) {
                chapters.add(c);
//                if(c.getParent()!=null){
//                    Chapter parent = ChapterManager.getInstance().findForId(c.getParent().getId());
//                    boolean exists = false;
//                    for(Chapter _c : chapters){
//                        if(_c.getChapterId()==parent.getId()){
//                            _c.getChildren().add(c);
//                            exists = true;
//                            break;
//                        }
//                    }
//
//                    if(!exists){
//                        if(parent.getChildren()==null)parent.setChildren(new ArrayList<Chapter>(0));
//                        parent.getChildren().add(c);
//                        chapters.add(parent);
//                    }
//
//                }else{
//                    chapters.add(c);
//                }

            }
        }
        chapters.get(0).setPlaying(true);
//        if(chapters.size()>0){
//            if(chapters.get(0).getChildren()!=null&& StringUtils.isEmpty(chapters.get(0).getPlayUrl()))
//                if(chapters.get(0).getChildren().size()>0)
//                chapters.get(0).getChildren().get(0).setPlaying(true);
//            else
//                chapters.get(0).setPlaying(true);
//        }

        closeableIterable.closeableIterator();
        localCourseChapterAdapter = new LocalCourseChapterAdapter(getActivity(), chapters,listView.getRightViewWidth(),new LocalCourseChapterAdapter.OnRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                Chapter chapter =  localCourseChapterAdapter.getItems().get(position);
                //删除本地文件
                DownloadTool.deleteDownloadTask((DownloadApplication)getActivity().getApplicationContext(),chapter.getDownloadUrl());
                ChapterManager.getInstance().delete(chapter);
                retrieveDataFromDB();
            }
        });

        listView.setAdapter(localCourseChapterAdapter);
    }

    void retrieveDataFromDB(){
        Map params = new HashMap(0);
        params.put("localcourse_id",localCourse.getId());
        localCourseChapterAdapter.updateItems(ChapterManager.getInstance().findForParams(params));
        localCourseChapterAdapter.notifyDataSetChanged();
    }

    /**
     * 当点击删除图标时，刷新list
     * @param intent
     */
    @Receiver(actions = "org.androidannotations.ACTION_1", registerAt = Receiver.RegisterAt.OnStartOnStop)
    protected void onAction1RegisteredOnAttachOnDetach(Intent intent) {
        localCourseChapterAdapter.notifyDataSetChanged();
    }

    /**
     * 当点击某列进行播放时
     * @param intent
     */
    @Receiver(actions = "org.androidannotations.ACTION_2", registerAt = Receiver.RegisterAt.OnStartOnStop)
    protected void onAction2RegisteredOnAttachOnDetach(Intent intent) {
        try{
            //将原来的播放的章节进度进行保存
            Chapter _chapter = localCourseChapterAdapter.getItem(currentPosition);
            if(_chapter!=null)
                ChapterManager.getInstance().saveOrUpdate(_chapter);

            for (Chapter c : localCourseChapterAdapter.getItems()) c.setPlaying(false);
            currentPosition = intent.getExtras().getInt("position");
            Chapter chapter = localCourseChapterAdapter.getItem(intent.getExtras().getInt("position"));
            chapter.setPlaying(true);

            localCourseChapterAdapter.notifyDataSetChanged();

            Intent _intent = new Intent("org.androidannotations.play");
            String playUrl = DownloadTool.getFileByUrl((DownloadApplication)getActivity().getApplicationContext(),chapter.getDownloadUrl());
            _intent.putExtra("playUrl",playUrl);
            getActivity().sendBroadcast(_intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 当播放进行时，更新进度和学习时间
     */
    @Receiver(actions = "org.androidannotations.updateLearningTime", registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void updateProgressAndLearningTime(){
        Chapter chapter = localCourseChapterAdapter.getItem(currentPosition);
        if(chapter==null)return;
        chapter.setLearnedTime(chapter.getLearnedTime()+1);
        localCourseChapterAdapter.notifyDataSetChanged();
    }


    public long getLastChapterId(){
        return localCourseChapterAdapter.getItems().get(currentPosition).getChapterId();
    }

    private int currentPosition;

}
