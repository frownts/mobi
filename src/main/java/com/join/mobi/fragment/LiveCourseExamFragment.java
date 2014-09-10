package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseExamAdapter;
import com.join.mobi.dto.CourseDetailDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
        liveCourseExamAdapter = new LiveCourseExamAdapter(getActivity(),courseDetailDto.getExams());
        listView.setAdapter(liveCourseExamAdapter);
        liveCourseExamAdapter.notifyDataSetChanged();
    }
}
