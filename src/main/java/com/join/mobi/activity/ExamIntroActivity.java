package com.join.mobi.activity;

import android.widget.ImageView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void afterViews(){

    }

    @Click
    void backClicked(){
        finish();
    }

    @Click
    void startExamClicked(){
        DialogManager.getInstance().makeText(this,"start exam ..go go go ",DialogManager.DIALOG_TYPE_OK);
    }

    @Click
    void introClicked(){
        IntroActivity_.intent(this).start();
    }

}
