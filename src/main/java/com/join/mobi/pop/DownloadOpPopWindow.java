package com.join.mobi.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.join.android.app.common.R;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-14
 * Time: 下午8:01
 */
public class DownloadOpPopWindow extends PopupWindow {

    private View view;

    public DownloadOpPopWindow(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.download_op_popupwindow, null);

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadOpPopWindow.this.dismiss();
            }
        });

        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }
}
