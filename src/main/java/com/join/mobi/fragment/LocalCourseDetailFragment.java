package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.mobi.activity.LocalCourseDetailActivity;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.localcourse_detail_fragment_layout)
public class LocalCourseDetailFragment extends Fragment {

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
        LocalCourse course = ((LocalCourseDetailActivity) getActivity()).getLocalCourse();
        createTime.setText(course.getTitle());
        branch.setText(course.getBranch());
        courseHour.setText(course.getCourseHour() + "");
        totalHour.setText(course.getLearningTimes() + "");
        description.setText(course.getDescription());
    }
}
