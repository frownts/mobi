package com.join.mobi.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseExamAdapter;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.dto.ExamDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.livecourse_exam_fragment_layout)
public class LiveCourseExamFragment extends Fragment {
    @ViewById
    ListView listView;
    LiveCourseExamAdapter liveCourseExamAdapter;

    @AfterViews
    void afterViews() {
        CourseDetailDto courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        liveCourseExamAdapter = new LiveCourseExamAdapter(getActivity(),courseDetailDto.getExam());
        listView.setAdapter(liveCourseExamAdapter);
        liveCourseExamAdapter.notifyDataSetChanged();
    }


    @Receiver(actions = "org.androidannotations.updateProgressOfExam", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void updateProgressOfExam(Intent intent){
        long examId = intent.getLongExtra("examId",0);
        String finishPercent = intent.getStringExtra("finishPercent");
        List<ExamDto> examDtos = liveCourseExamAdapter.getItems();
        for(ExamDto examDto : examDtos){
            if(examId==examDto.getExamId()){
                examDto.setFinishPercent(finishPercent);
                break;
            }
        }

        liveCourseExamAdapter.notifyDataSetChanged();

    }

}
