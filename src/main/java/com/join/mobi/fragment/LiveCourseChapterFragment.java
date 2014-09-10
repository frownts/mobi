package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.join.android.app.common.R;
import com.join.mobi.activity.LiveCourseDetailActivity_;
import com.join.mobi.adapter.LiveCourseChapterAdapter;
import com.join.mobi.dto.CourseDetailDto;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void afterViews() {
        CourseDetailDto courseDetailDto = ((LiveCourseDetailActivity_) getActivity()).getCourseDetail();
        liveCourseChapterAdapter = new LiveCourseChapterAdapter(getActivity(),courseDetailDto.getChapters());
        listView.setAdapter(liveCourseChapterAdapter);
        liveCourseChapterAdapter.notifyDataSetChanged();
    }

}
