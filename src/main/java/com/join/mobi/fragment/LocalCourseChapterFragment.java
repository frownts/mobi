package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.adapter.LocalCourseChapterAdapter;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.localcourse_chapter_fragment_layout)
public class LocalCourseChapterFragment extends Fragment {

    @ViewById
    ListView listView;
    LocalCourseChapterAdapter localCourseChapterAdapter;

    @AfterViews
    void afterViews() {
        LocalCourse course = ((LocalCourseDetailActivity) getActivity()).getLocalCourse();
        List<Chapter> chapters = new ArrayList<Chapter>(0);
        CloseableIterable<Chapter> closeableIterable = course.getChapters().getWrappedIterable();
        Iterator<Chapter> chapterIterator = closeableIterable.iterator();

        while (chapterIterator.hasNext()) {
            Chapter c = chapterIterator.next();
            if (DownloadTool.isFinished((DownloadApplication) getActivity().getApplicationContext(), c.getDownloadUrl())) {
                chapters.add(c);
            }
        }
        closeableIterable.closeableIterator();
        localCourseChapterAdapter = new LocalCourseChapterAdapter(getActivity(), chapters);
        listView.setAdapter(localCourseChapterAdapter);
    }

    @ItemClick
    void listViewItemClicked(Chapter chapter) {
        for (Chapter c : localCourseChapterAdapter.getItems()) c.setPlaying(false);
        chapter.setPlaying(true);

        DialogManager.getInstance().makeText(getActivity(), "playing", DialogManager.DIALOG_TYPE_OK);
        localCourseChapterAdapter.notifyDataSetChanged();
    }

}
