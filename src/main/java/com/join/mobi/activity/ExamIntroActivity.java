package com.join.mobi.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.ExamUtils;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
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

    @ViewById
    View examResult;



    @Pref
    PrefDef_ myPref;
    @Extra
    String examId;
    @RestService
    RPCService rpcService;

    ExamDto examDto;

    @AfterViews
    void afterViews(){
        showLoading();
        retrieveDataFromServer();
//        examDto = RPCTestData.getExamDetail();
    }

    @Background
    void retrieveDataFromServer(){
        //加载考试详情
        examDto = rpcService.getExamDetail(myPref.rpcUserId().get(), examId);
        updateData();
    }
    @UiThread
    public void updateData(){
        if(examDto==null)return;
        title.setText(examDto.getTitle());
        itemCount.setText("共 "+examDto.getItemCount()+" 道试题");
        timeLimit.setText("限时 "+ DateUtils.SecondToNormalTime(examDto.getDurationLimit()));


        examTime.setText(examTime.getText().toString().replace("$1", DateUtils.FormatForCourseLastLearningTime(examDto.getExamTime())).replace("$2", DateUtils.SecondToNormalTime(examDto.getDuration())));
        examTime.setVisibility(View.VISIBLE);
//        finishPercent.setText(finishPercent.getText().toString().replace("$1",examDto.getFinishPercent()+"%"));
        finishPercent.setText(finishPercent.getText().toString().replace("$1", ExamUtils.SpeculatePercent(examDto.getFinishPercent(), examDto.getItemCount() + "")+"%"));
        finishPercent.setVisibility(View.VISIBLE);

        String correctP = examDto.getCorrectPercent();

        if(correctP.equals(".0")){
            correctP = "0";
        }
        else if(correctP.endsWith(".0")){
            correctP = correctP.substring(0,correctP.indexOf("."));
        }

        correctPercent.setText(correctPercent.getText().toString().replace("$1",correctP+"%"));
        correctPercent.setVisibility(View.VISIBLE);

        if(examDto.getExamTime()==null||examDto.getExamTime().equals("")){
            examResult.setVisibility(View.INVISIBLE);
        }else
            examResult.setVisibility(View.VISIBLE);
        dismissLoading();
    }

    @Click
    void backClicked(){
        finish();
    }

    @Click
    void startExamClicked(){
        ExamActivity_.intent(this).examDto(examDto).start();
        finish();
    }

    @Click
    void introClicked(){
        IntroActivity_.intent(this).start();
    }

}
