package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
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
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView name;
        TextView itemCount;
        TextView finishPercent;
        TextView durationLimit;
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
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return examDtos.size();
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
        ExamDto exam = examDtos.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.livecourse_exam_listview_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.itemCount = (TextView) convertView.findViewById(R.id.itemCount);
            holder.finishPercent = (TextView) convertView.findViewById(R.id.finishPercent);
            holder.durationLimit = (TextView) convertView.findViewById(R.id.durationLimit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(exam.getName());
        holder.itemCount.setText("共"+exam.getItemCount()+"题");
        holder.finishPercent.setText(exam.getFinishPercent()+"%");
        holder.durationLimit.setText("限时:"+exam.getDurationLimit());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放
                DialogManager.getInstance().makeText(mContext,"start exam",DialogManager.DIALOG_TYPE_OK);
            }
        });

        return convertView;
    }

}

