package com.join.mobi.activity;

import android.widget.ExpandableListView;
import android.widget.TextView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.mobi.adapter.ExamResultAdapter;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.rpc.ExamResult;
import org.androidannotations.annotations.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-13
 * Time: 下午12:25
 */
@EActivity(R.layout.exam_result_activity_layout)
public class ExamResultActivity extends BaseActivity {

    @Extra
    ExamResult examResult;
    @Extra
    ExamDto examDto;
    @ViewById
    TextView title;
    @ViewById
    TextView testDuration;
    @ViewById
    TextView correctNum;
    @ViewById
    TextView incorrectNum;
    @ViewById
    TextView correctPercent;
    @ViewById
    ExpandableListView expandListView;

    ExamResultAdapter examResultAdapter;

    @AfterViews
    void afterViews() {
        title.setText(examDto.getTitle());
        testDuration.setText(examResult.getDuration());
        correctNum.setText(examResult.getCorrectNum());
        incorrectNum.setText(examResult.getIncorrectNum());
        correctPercent.setText(examResult.getCorrectPercent());

        examResultAdapter = new ExamResultAdapter(this, examDto.getExamItems(), examDto.getExamItems());
        expandListView.setAdapter(examResultAdapter);

    }

    @Click
    void backClicked() {
        finish();
    }
}
