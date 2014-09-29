package com.join.mobi.activity;

import android.content.Intent;
import android.widget.ListView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.mobi.adapter.ExamListAdapter;
import com.join.mobi.dto.ExamItem;
import org.androidannotations.annotations.*;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 下午3:39
 */
@EActivity(R.layout.exam_list_activity_layout)
public class ExamListActivity extends BaseActivity {

    @Extra
    int currentPageIndex;

    @Extra
    List<ExamItem> examItems;

    @ViewById
    ListView listView;

    ExamListAdapter examListAdapter;

    @AfterViews
    void afterViews() {
        examListAdapter = new ExamListAdapter(this, examItems);
        listView.setAdapter(examListAdapter);
        examListAdapter.notifyDataSetChanged();
    }

    @Click
    void backClicked() {
        ExamActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT).examIndex(currentPageIndex).start();
        finish();
    }


}
