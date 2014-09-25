package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.activity.LocalCourseDetailActivity;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
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

    LocalCourse course;
    long originTime;

    @AfterViews
    void afterViews() {
        course = ((LocalCourseDetailActivity) getActivity()).getLocalCourse();
        if (course == null) return;
        createTime.setText(course.getCreateTime());
        branch.setText(course.getBranch());
        courseHour.setText(DateUtils.SecondToNormalTime(course.getCourseHour()));
        totalHour.setText(DateUtils.SecondToNormalTime(course.getLearningTimes()));
        description.setText(course.getDescription());

        originTime = course.getLearningTimes();
    }


    /**
     * 当播放开始时，更新总时间
     */
    @Receiver(actions = "org.androidannotations.updateLearningTime", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void onUpdateLearningTimeRegisteredOnAttachOnDetach() {
        if (course == null) return;
        course.setLearningTimes(course.getLearningTimes() + 1);
        totalHour.setText(DateUtils.SecondToNormalTime(course.getLearningTimes()));
    }

    public long getDuration() {
        return course.getLearningTimes() - originTime;
    }


}
