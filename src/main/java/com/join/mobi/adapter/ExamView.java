package com.join.mobi.adapter;

import android.view.View;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 下午9:14
 */
public class ExamView{
    View view;
    int examIndex;

    public ExamView(View _view,int _examIndex){
        this.view = _view;
        this.examIndex = _examIndex;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getExamIndex() {
        return examIndex;
    }

    public void setExamIndex(int examIndex) {
        this.examIndex = examIndex;
    }
}
