package com.join.android.app.common.utils;

import java.text.DecimalFormat;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-17
 * Time: 上午11:15
 */
public class ExamUtils {

    public static String SpeculatePercent(String count,String total){

        DecimalFormat decimalFormat = new DecimalFormat(".00");
        float finishPercent = (Float.parseFloat(count) / Float.parseFloat(total)) * 100;

        String tempFinishPercent = decimalFormat.format(finishPercent);
        if(tempFinishPercent.equals(".00")){
            tempFinishPercent = "0";
        }else if(tempFinishPercent.endsWith(".0")||tempFinishPercent.equals(".00")){
            tempFinishPercent = tempFinishPercent.substring(0,tempFinishPercent.indexOf("."));
        }
        return tempFinishPercent;

    }

}
