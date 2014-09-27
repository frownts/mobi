package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.dto.CourseDetailDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 * 直播详情页面
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

    CourseDetailDto courseDetailDto;

    long originTime;

    @AfterViews
    void afterViews() {
        courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        createTime.setText(DateUtils.FormatForCourseLastLearningTime(courseDetailDto.getCreateTime()));
        branch.setText(courseDetailDto.getBranch());
        courseHour.setText(DateUtils.SecondToNormalTime(courseDetailDto.getCourseHour()));
        totalHour.setText(DateUtils.SecondToNormalTime(courseDetailDto.getTotalHour()));
        if(StringUtils.isNotEmpty(courseDetailDto.getDescription()))
        description.setText(courseDetailDto.getDescription());

        originTime = courseDetailDto.getTotalHour();
    }

    /**
     * 当播放开始时，更新总时间
     */
    @Receiver(actions = "org.androidannotations.updateLearningTime", registerAt = Receiver.RegisterAt.OnCreateOnDestroy)
    public void onUpdateLearningTimeRegisteredOnAttachOnDetach() {
        if (courseDetailDto == null) return;
        courseDetailDto.setTotalHour(courseDetailDto.getTotalHour() + 1);
        totalHour.setText(DateUtils.SecondToNormalTime(courseDetailDto.getTotalHour()));
    }

    public long getDuration(){
        return courseDetailDto.getTotalHour()- originTime;
    }

}
