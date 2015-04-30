package com.join.mobi.activity;

import android.content.Intent;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.adapter.ExamResultAdapter;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.rpc.ExamResult;
import org.androidannotations.annotations.*;
import org.apache.commons.lang3.StringUtils;

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

        testDuration.setText(DateUtils.SecondToNormalTime(Long.parseLong(examResult.getDuration())));
        correctNum.setText(examResult.getCorrectNum());
        incorrectNum.setText(examResult.getIncorrectNum());
        correctPercent.setText(examResult.getCorrectPercent() + "%");

        examResultAdapter = new ExamResultAdapter(this, examDto.getExamItems(), examDto.getExamItems());
        expandListView.setAdapter(examResultAdapter);

        Intent intent = new Intent("org.androidannotations.updateProgressOfExam");
        intent.putExtra("examId",examDto.getExamId());
        if(StringUtils.isEmpty(examDto.getFinishPercent()))examDto.setFinishPercent("0");

        examDto.setFinishPercent(((int)Float.parseFloat(examDto.getFinishPercent()))+"");
        try{
            if(examDto.getFinishPercent().equals(".0"))examDto.setFinishPercent("0");
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        intent.putExtra("finishPercent",examDto.getFinishPercent());
        sendBroadcast(intent);
    }

    @Click
    void backClicked() {

        finish();
    }
}
