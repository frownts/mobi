package com.join.mobi.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Chapter;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.adapter.LocalCourseReferenceAdapter;
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
@EFragment(R.layout.localcourse_reference_fragment_layout)
public class LocalCourseReferenceFragment extends Fragment {
    @ViewById
    ListView listView;
    LocalCourseReferenceAdapter localCourseReferenceAdapter;
    LocalCourse localCourse;

    @AfterViews
    void afterViews() {
        localCourse = ((LocalCourseDetailActivity) getActivity()).getLocalCourse();
        List<Reference> references = new ArrayList<Reference>(0);
        CloseableIterable<Reference> closeableIterable = localCourse.getReferences().getWrappedIterable();
        Iterator<Reference> referenceIterator = closeableIterable.iterator();

        while (referenceIterator.hasNext()) {
            Reference r = referenceIterator.next();
            if (DownloadTool.isFinished((DownloadApplication) getActivity().getApplicationContext(), r.getUrl())) {
                references.add(r);
            }
        }
        closeableIterable.closeableIterator();

        localCourseReferenceAdapter = new LocalCourseReferenceAdapter(getActivity(), references);
        listView.setAdapter(localCourseReferenceAdapter);
        localCourseReferenceAdapter.notifyDataSetChanged();
    }

    @ItemClick
    void listViewItemClicked(Chapter chapter) {
        DialogManager.getInstance().makeText(getActivity(), "open file", DialogManager.DIALOG_TYPE_OK);
    }

}
