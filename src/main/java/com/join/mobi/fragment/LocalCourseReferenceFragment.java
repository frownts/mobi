package com.join.mobi.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import com.j256.ormlite.dao.CloseableIterable;
import com.join.android.app.common.R;
import com.join.android.app.common.db.manager.ReferenceManager;
import com.join.android.app.common.db.tables.LocalCourse;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.view.SwipeListView;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.adapter.LocalCourseReferenceAdapter;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.*;

import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-10
 * Time: 上午11:41
 */
@EFragment(R.layout.localcourse_reference_fragment_layout)
public class LocalCourseReferenceFragment extends Fragment {
    @ViewById
    SwipeListView listView;
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

        localCourseReferenceAdapter = new LocalCourseReferenceAdapter(getActivity(), references,listView.getRightViewWidth(),new LocalCourseReferenceAdapter.OnRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                Reference reference =  localCourseReferenceAdapter.getItems().get(position);
                //删除本地文件
                DownloadTool.deleteDownloadTask((DownloadApplication)getActivity().getApplicationContext(),reference.getUrl());
                ReferenceManager.getInstance().delete(reference);
                retrieveDataFromDB();

            }
        });
        listView.setAdapter(localCourseReferenceAdapter);
        localCourseReferenceAdapter.notifyDataSetChanged();
    }

    void retrieveDataFromDB(){
        Map params = new HashMap(0);
        params.put("localcourse_id",localCourse.getId());
        localCourseReferenceAdapter.setItems(ReferenceManager.getInstance().findForParams(params));
        localCourseReferenceAdapter.notifyDataSetChanged();
    }

    @ItemClick
    void listViewItemClicked(Reference reference) {
        DialogManager.getInstance().makeText(getActivity(), "open file", DialogManager.DIALOG_TYPE_OK);
    }

    /**
     * 当点击删除图标时，刷新list
     * @param intent
     */
    @Receiver(actions = "org.androidannotations.ACTION_1", registerAt = Receiver.RegisterAt.OnStartOnStop)
    protected void onAction1RegisteredOnAttachOnDetach(Intent intent) {
        localCourseReferenceAdapter.notifyDataSetChanged();
    }


    /**
     * 当点击某列进行播放时
     * @param intent
     */
    @Receiver(actions = "org.androidannotations.ACTION_2", registerAt = Receiver.RegisterAt.OnStartOnStop)
    protected void onAction2RegisteredOnAttachOnDetach(Intent intent) {
        //open file
        localCourseReferenceAdapter.getItem(intent.getExtras().getInt("position"));
    }

}
