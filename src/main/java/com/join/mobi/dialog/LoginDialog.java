package com.join.mobi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.join.android.app.common.R;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:58
 */
public class LoginDialog extends AlertDialog {
    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_login,null);
        setContentView(view);
    }
}
