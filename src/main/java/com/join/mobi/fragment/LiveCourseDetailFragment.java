package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.dto.CourseDetailDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.livecourse_detail_fragment_layout)
public class LiveCourseDetailFragment extends Fragment {

    @ViewById
    TextView createTime;
    @ViewById
    TextView branch;
    @ViewById
    TextView courseHour;
    @ViewById
    TextView totalHour;
    @ViewById
    TextView description;

    @AfterViews
    void afterViews() {
        CourseDetailDto courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        createTime.setText(courseDetailDto.getCreateTime());
        branch.setText(courseDetailDto.getBranch());
        courseHour.setText(courseDetailDto.getCourseHour()+"");
        totalHour.setText(courseDetailDto.getTotalHour()+"");
        description.setText(courseDetailDto.getDescription());
    }
}
