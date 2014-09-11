package com.join.mobi.activity;

import android.widget.ImageView;
import android.widget.TextView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.join.mobi.rpc.RPCTestData;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 上午12:05
 * <p/>
 * 点击某个测试题，进行测试时，首先跳转到该页面，对即将进行的试题进行说明
 */
@EActivity(R.layout.exam_intro_activity_layout)
public class ExamIntroActivity extends BaseActivity {

    @ViewById
    ImageView startExam;
    @ViewById
    ImageView back;
    @ViewById
    TextView title;
    @ViewById
    TextView itemCount;
    @ViewById
    TextView timeLimit;
    @ViewById
    TextView examTime;
    @ViewById
    TextView finishPercent;
    @ViewById
    TextView correctPercent;

    @Pref
    PrefDef_ myPref;
    @Extra
    String examId;
    @RestService
    RPCService rpcService;

    ExamDto examDto;

    @AfterViews
    void afterViews(){
        //加载考试详情
//        examDto = rpcService.getExamDetail(myPref.userId().get(), examId);
        examDto = RPCTestData.getExamDetail();
        updateData();

    }

    public void updateData(){
        title.setText(examDto.getTitle());
        itemCount.setText("共 "+examDto.getItemCount()+" 道试题");
        timeLimit.setText("限时 "+examDto.getDurationLimit());

        examTime.setText(examTime.getText().toString().replace("$1",examDto.getExamTime()).replace("$2",examDto.getCost()));
        finishPercent.setText(finishPercent.getText().toString().replace("$1",examDto.getFinishPercent()+"%"));
        correctPercent.setText(correctPercent.getText().toString().replace("$1",examDto.getCorrectPercent()+"%"));

    }

    @Click
    void backClicked(){
        finish();
    }

    @Click
    void startExamClicked(){
        ExamActivity_.intent(this).examDto(examDto).start();
//        DialogManager.getInstance().makeText(this,"start exam ..go go go ",DialogManager.DIALOG_TYPE_OK);
    }

    @Click
    void introClicked(){
        IntroActivity_.intent(this).start();
    }

}
