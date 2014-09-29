package com.join.android.app.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-13
 * Time: 下午4:04
 * 带文字的加载中
 */
public class CommonDialogWithLetterLoading extends Dialog {
    Context context;

    ImageView vLoading; // 圆型进度条
    Animation anim;// 动画
    String txt;

    public CommonDialogWithLetterLoading(Context context,String txt) {
        super(context, R.style.HKDialogLoading);
        this.context = context;
        this.txt = txt;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);

        anim = AnimationUtils.loadAnimation(this.getContext(),
                R.anim.rotate_repeat);
        anim.setInterpolator(new LinearInterpolator());

        vLoading = new ImageView(context);
        vLoading.setImageResource(R.drawable.load);// 加载中的图片,建议圆形的

        // 布局
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        View view = LayoutInflater.from(context).inflate(R.layout.progress_width_letter_layout,null);
        ((TextView)view.findViewById(R.id.txt)).setText(txt);
//        addContentView(vLoading, lp);
        addContentView(view, lp);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按下了键盘上返回按钮
            this.hide();
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        super.show();
        vLoading.startAnimation(anim);
    }
}




