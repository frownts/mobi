package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.CourseManager;
import com.join.android.app.common.db.manager.LocalCourseManager;
import com.join.android.app.common.db.manager.ReferenceManager;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseReferenceAdapter;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.dto.ReferenceDto;
import com.join.mobi.enums.Dtype;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.livecourse_reference_fragment_layout)
public class LiveCourseReferenceFragment extends Fragment {
    @ViewById
    ListView listView;
    LiveCourseReferenceAdapter liveCourseReferenceAdapter;
    CourseDetailDto courseDetailDto;
    String url;
    //学习总时长
    String totalDuration;

    @AfterViews
    void afterViews() {
        courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        url = ((LiveCourseDetailActivity_) getActivity()).getUrl();
        totalDuration = CourseManager.getInstance().findForId(courseDetailDto.getCourseId()).getTotalDuration();
        liveCourseReferenceAdapter = new LiveCourseReferenceAdapter(getActivity(), courseDetailDto.getReferences(), new LiveCourseReferenceAdapter.Download() {
            @Override
            public void download(ReferenceDto reference) {//下载工作
                doDownload(reference);
            }
        });
        listView.setAdapter(liveCourseReferenceAdapter);
        liveCourseReferenceAdapter.notifyDataSetChanged();
    }

    void doDownload(ReferenceDto reference) {
        //首先判断本地课程主表记录是否存在
        Map<String,Object> params = new HashMap<String, Object>(0);
        params.put("courseId",courseDetailDto.getCourseId());
        LocalCourse course;
        List<LocalCourse> courseList  = LocalCourseManager.getInstance().findForParams(params);
        if(courseList==null||courseList.size()==0){
            LocalCourse entity = new LocalCourse();
            entity.setCourseId(courseDetailDto.getCourseId());
            entity.setCourseHour(courseDetailDto.getCourseHour());
            entity.setBranch(courseDetailDto.getBranch());
            entity.setCreateTime(courseDetailDto.getCreateTime());
            entity.setDescription(courseDetailDto.getDescription());
            entity.setTitle(courseDetailDto.getName());
            entity.setValidUntil(courseDetailDto.getValidUntil());
            entity.setLearningTimes(Integer.parseInt(totalDuration));
            entity.setUrl(url);
            course = LocalCourseManager.getInstance().saveIfNotExists(entity);
        }else{
            course = courseList.get(0);
        }

        //判断该资料是否已经存在
        Map<String,Object> referenceParams = new HashMap<String, Object>(0);
        referenceParams.put("referenceId",reference.getReferenceId());
        referenceParams.put("localcourse_id",course.getCourseId());

        List<Reference> references = ReferenceManager.getInstance().findForParams(referenceParams);
        if(references==null||references.size()==0){
            Reference ref = new Reference();
            ref.setUrl(reference.getUrl());
            ref.setTitle(reference.getTitle());
            ref.setFileSize(reference.getFileSize());
            ref.setReferenceId(reference.getReferenceId());
            ref.setType(reference.getType());
            ref.setLocalCourse(course);
            ReferenceManager.getInstance().save(ref);
            DialogManager.getInstance().makeText(getActivity(),"开始下载",DialogManager.DIALOG_TYPE_OK);
        }else{
            DialogManager.getInstance().makeText(getActivity(),"正在下载,或已下载.",DialogManager.DIALOG_TYPE_OK);
        }

        //下载
        DownloadTool.startDownload((DownloadApplication)getActivity().getApplicationContext(),reference.getUrl(),reference.getTitle(), Dtype.Rference,reference.getType()+"");

    }

}
