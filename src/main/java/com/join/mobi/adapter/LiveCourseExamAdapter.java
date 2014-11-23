package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.ExamUtils;
import com.join.mobi.activity.ExamIntroActivity_;
import com.join.mobi.customview.SpringProgressView;
import com.join.mobi.dto.ExamDto;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LiveCourseExamAdapter extends BaseAdapter {

    private List<ExamDto> examDtos = new ArrayList<ExamDto>(0);


    private Context mContext;

    private class ViewHolder {
        TextView name;
        TextView itemCount;
        TextView finishPercent;
        TextView durationLimit;
        SpringProgressView springProgressView;
    }

    public List<ExamDto> getItems() {
        return examDtos;
    }

    public void updateItems(List<ExamDto> examDtos) {
        examDtos.clear();
        examDtos.addAll(examDtos);
    }

    public LiveCourseExamAdapter(Context c, List<ExamDto> _examDtos) {
        mContext = c;
        examDtos.clear();
        examDtos.addAll(_examDtos);

    }

    @Override
    public int getCount() {
        if(examDtos!=null)
        return examDtos.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ExamDto exam = examDtos.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.livecourse_exam_listview_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.itemCount = (TextView) convertView.findViewById(R.id.itemCount);
            holder.finishPercent = (TextView) convertView.findViewById(R.id.finishPercent);
            holder.durationLimit = (TextView) convertView.findViewById(R.id.durationLimit);
            holder.springProgressView = (SpringProgressView) convertView.findViewById(R.id.springProgressView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int itemCount = exam.getItemCount();
        if(itemCount==0)itemCount=10;
        holder.springProgressView.setMaxCount(itemCount);
        holder.springProgressView.setCurrentCount(Long.parseLong(exam.getFinishPercent()));
        holder.name.setText(exam.getName());
        holder.itemCount.setText("共"+exam.getItemCount()+"题");

        holder.finishPercent.setText(ExamUtils.SpeculatePercent(exam.getFinishPercent(), exam.getItemCount() + "")+"%");
        if(exam.getItemCount()==0)holder.finishPercent.setText("0%");

        holder.durationLimit.setText("限时:"+ DateUtils.SecondToNormalTime(exam.getDurationLimit()));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exam.getItemCount()==0){
                    Toast.makeText(mContext,"无试题",1000).show();
                }else
                    ExamIntroActivity_.intent(mContext).examId(exam.getExamId()+"").start();
            }
        });

        return convertView;
    }

}

