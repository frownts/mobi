package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.ChapterManager;
import com.join.android.app.common.db.manager.CourseManager;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.Course;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseChapterAdapter;
import com.join.mobi.dto.ChapterDetailDto;
import com.join.mobi.dto.ChapterDto;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.enums.Dtype;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.livecourse_chapter_fragment_layout)
public class LiveCourseChapterFragment extends Fragment {

    @ViewById
    ListView listView;
    LiveCourseChapterAdapter liveCourseChapterAdapter;
    CourseDetailDto courseDetailDto;
    String url;
    //学习总时长
    String totalDuration;

    @AfterViews
    void afterViews() {

        courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        url = ((LiveCourseDetailActivity_) getActivity()).getUrl();
        Course course = CourseManager.getInstance().getByCourseId(courseDetailDto.getCourseId());
        if (course != null)
            totalDuration = course.getTotalDuration();


        List<ChapterDto> chapterDtos = courseDetailDto.getChapter();
        liveCourseChapterAdapter = new LiveCourseChapterAdapter(getActivity(), chapterDtos, new LiveCourseChapterAdapter.Download() {
            @Override
            public void download(ChapterDto chapterDto,ChapterDetailDto chapterDetailDto) {
                doDownload(chapterDto,chapterDetailDto);
            }
        });

        if (chapterDtos != null && chapterDtos.size() > 0) {
            ChapterDto  chapter = chapterDtos.get(0);
            if(chapter.getChapter()!=null&&chapter.getChapter().size()>0&& StringUtils.isEmpty(chapter.getDownloadUrl())){
                chapter.getChapter().get(0).setPlaying(true);
                liveCourseChapterAdapter.setCurrentChapterDetailDto(chapter.getChapter().get(0));
            }

            else
                chapter.setPlaying(true);
        }


        listView.setAdapter(liveCourseChapterAdapter);
        liveCourseChapterAdapter.notifyDataSetChanged();
    }

    void doDownload(ChapterDto chapter,ChapterDetailDto chapterDetailDto) {

        //首先判断本地课程主表记录是否存在
        Map<String, Object> params = new HashMap<String, Object>(0);
        params.put("courseId", courseDetailDto.getCourseId());
        LocalCourse course;
        List<LocalCourse> courseList = LocalCourseManager.getInstance().findForParams(params);
        if (courseList == null || courseList.size() == 0) {
            LocalCourse entity = new LocalCourse();
            entity.setCourseId(courseDetailDto.getCourseId());
            entity.setCourseHour(courseDetailDto.getCourseHour());
            entity.setBranch(courseDetailDto.getBranch());
            entity.setCreateTime(courseDetailDto.getCreateTime());
            entity.setDescription(courseDetailDto.getDescription());
            entity.setTitle(courseDetailDto.getName());
            entity.setValidUntil(courseDetailDto.getValidUntil());
            entity.setUrl(url);
            entity.setLearningTimes(Integer.parseInt(totalDuration));
            course = LocalCourseManager.getInstance().saveIfNotExists(entity);
        } else {
            course = courseList.get(0);
            //更新总学习时间
            course.setLearningTimes(Integer.parseInt(totalDuration));
            LocalCourseManager.getInstance().saveOrUpdate(course);
        }

        //判断该章节是否已经存在
        Map<String, Object> chapterParams = new HashMap<String, Object>(0);
        chapterParams.put("chapterId", chapter.getChapterId());
        chapterParams.put("localcourse_id", course.getId());

        List<Chapter> chapters = ChapterManager.getInstance().findForParams(chapterParams);
        if (chapters == null || chapters.size() == 0) {
            Chapter entity = new Chapter();
            entity.setLocalCourse(course);
            entity.setTitle(chapter.getTitle());
            entity.setChapterId(chapter.getChapterId());
            entity.setFilesize(chapter.getFileSize());
            entity.setLearnedTime(chapter.getLearnedTime());
            entity.setChapterDuration(chapter.getChapterDuration());
            entity.setDownloadUrl(chapter.getDownloadUrl());
            entity.setValidUntil(course.getValidUntil() + "");
            Chapter _chapter = ChapterManager.getInstance().saveIfNotExists(entity);

            if(chapterDetailDto!=null){//保存要下载的子章节
                entity = new Chapter();
                entity.setLocalCourse(course);
                entity.setTitle(chapterDetailDto.getTitle());
                entity.setChapterId(chapterDetailDto.getChapterId());
                entity.setFilesize(chapterDetailDto.getFileSize());
                entity.setLearnedTime(chapterDetailDto.getLearnedTime());
                entity.setChapterDuration(chapterDetailDto.getChapterDuration());
                entity.setDownloadUrl(chapterDetailDto.getDownloadUrl());
                entity.setValidUntil(course.getValidUntil() + "");
                entity.setParent(_chapter);
                ChapterManager.getInstance().saveIfNotExists(entity);
                //下载
                DownloadTool.startDownload((DownloadApplication) getActivity().getApplicationContext(), chapterDetailDto.getDownloadUrl(), chapterDetailDto.getTitle(), Dtype.Chapter, "0");
                DialogManager.getInstance().makeText(getActivity(), "开始下载", DialogManager.DIALOG_TYPE_OK);
            }else{
                DownloadTool.startDownload((DownloadApplication) getActivity().getApplicationContext(), chapter.getDownloadUrl(), chapter.getTitle(), Dtype.Chapter, "0");
                DialogManager.getInstance().makeText(getActivity(), "开始下载", DialogManager.DIALOG_TYPE_OK);
            }

        } else {

            if(chapterDetailDto!=null){
                chapterParams.clear();
                chapterParams.put("chapterId", chapterDetailDto.getChapterId());
                List<Chapter> _chapters = ChapterManager.getInstance().findForParams(chapterParams);
                if(_chapters!=null&&_chapters.size()>0){
                    DialogManager.getInstance().makeText(getActivity(), "正在下载,或已下载.", DialogManager.DIALOG_TYPE_OK);
                }else{
                    Chapter entity = new Chapter();
                    entity.setLocalCourse(course);
                    entity.setTitle(chapterDetailDto.getTitle());
                    entity.setChapterId(chapterDetailDto.getChapterId());
                    entity.setFilesize(chapterDetailDto.getFileSize());
                    entity.setLearnedTime(chapterDetailDto.getLearnedTime());
                    entity.setChapterDuration(chapterDetailDto.getChapterDuration());
                    entity.setDownloadUrl(chapterDetailDto.getDownloadUrl());
                    entity.setParent(chapters.get(0));
                    ChapterManager.getInstance().saveIfNotExists(entity);
                    //下载
                    DownloadTool.startDownload((DownloadApplication) getActivity().getApplicationContext(), chapterDetailDto.getDownloadUrl(), chapterDetailDto.getTitle(), Dtype.Chapter, "0");
                    DialogManager.getInstance().makeText(getActivity(), "开始下载", DialogManager.DIALOG_TYPE_OK);
                }

            }else{
                DialogManager.getInstance().makeText(getActivity(), "正在下载,或已下载.", DialogManager.DIALOG_TYPE_OK);

            }

        }



    }


    /**
     * 当播放进行时，更新进度和学习时间
     */
    @Receiver(actions = "org.androidannotations.updateLearningTime", registerAt = Receiver.RegisterAt.OnResumeOnPause)
    public void updateProgressAndLearningTime() {
        ChapterDto chapterDto = liveCourseChapterAdapter.getItem(liveCourseChapterAdapter.getCurrentPosition());
        if (chapterDto == null) return;
        chapterDto.setLearnedTime(chapterDto.getLearnedTime() + 1);

        ChapterDetailDto chapterDetailDto = liveCourseChapterAdapter.getCurrentChapterDetailDto();
        if(chapterDetailDto!=null)
            chapterDetailDto.setLearnedTime(chapterDetailDto.getLearnedTime()+1);
        liveCourseChapterAdapter.notifyDataSetChanged();
    }

    public CourseDetailDto getCourseDetailDto() {
        return courseDetailDto;
    }

    public void setCourseDetailDto(CourseDetailDto courseDetailDto) {
        this.courseDetailDto = courseDetailDto;
    }

    public long getLastChapterId() {
        if(liveCourseChapterAdapter.getCurrentChapterDetailDto()!=null){
            return liveCourseChapterAdapter.getCurrentChapterDetailDto().getChapterId();
        }
        return liveCourseChapterAdapter.getItems().get(liveCourseChapterAdapter.getCurrentPosition()).getChapterId();
    }
}
