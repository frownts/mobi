package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseReferenceAdapter;
import com.join.mobi.dto.CourseDetailDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void afterViews() {
        CourseDetailDto courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        liveCourseReferenceAdapter = new LiveCourseReferenceAdapter(getActivity(), courseDetailDto.getReferences());
        listView.setAdapter(liveCourseReferenceAdapter);
        liveCourseReferenceAdapter.notifyDataSetChanged();
    }

}
