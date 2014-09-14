package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.mobi.dto.ExamItem;
import com.join.mobi.dto.ItemOption;

import java.util.List;

public class ExamResultAdapter extends BaseExpandableListAdapter {
    public List<ExamItem> childDatas;   //二级目录
    public List<ExamItem> groupDatas;              //一级目录

    private Context context;
    private View tempView;
    private LayoutInflater mInflater = null;
    private OnLongClickListener onLongClick;
    private OnClickListener onclickListener;
    private ChildViewHolder childHolder;
    private GroupViewHolder groupHolder;

    ExamItem examItem;

    //初始化一级,二级目录
    public ExamResultAdapter(Context ct, List<ExamItem> gdatas, List<ExamItem> cDatas) {
        context = ct;
        mInflater = LayoutInflater.from(ct);
        groupDatas = gdatas;
        childDatas = cDatas;
    }

    //添加一级目录
    public void addGroup(ExamItem gdata) {
    }

    //删除一级目录
    public void delGroup(int groupIndex) {
    }

    //添加二级目录
    public void addChild(int groupIndex, ExamItem cdata) {

    }

    //删除二级目录
    public void delChild(int groupIndex, int childIndex) {

    }

    @Override
    public int getGroupCount() {
        return (groupDatas != null) ? groupDatas.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return (groupDatas != null) ? groupDatas.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)    //自定义绘制一级目录的每一行		                    View convertView, ViewGroup parent)
    {
        if (convertView != null) {
            tempView = convertView;
        } else {
            tempView = mInflater.inflate(R.layout.exam_result_explist_group_item, parent, false);
        }
        groupHolder = (GroupViewHolder) tempView.getTag();
        if (groupHolder == null) {
            groupHolder = new GroupViewHolder();
            groupHolder.no = (TextView) tempView.findViewById(R.id.no);
            groupHolder.title = (TextView) tempView.findViewById(R.id.title);
            groupHolder.isCorrect = (ImageView) tempView.findViewById(R.id.isCorrect);
            tempView.setTag(groupHolder);
        }
        examItem = groupDatas.get(groupPosition);
        groupHolder.no.setText((groupPosition + 1) + ".");      //设置一级目录序号
        groupHolder.title.setText(examItem.getTitle());      //设置一级目录标题


        if (examItem.isCorrect())
            groupHolder.isCorrect.setImageDrawable(context.getResources().getDrawable(R.drawable.right_icon));
        else
            groupHolder.isCorrect.setImageDrawable(context.getResources().getDrawable(R.drawable.wrong_icon));
        return tempView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,      //自定义绘制二级级目录的每一行
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView != null) {
            tempView = convertView;
        } else {
            tempView = mInflater.inflate(R.layout.exam_result_detail_explist_group_item, parent, false);
        }
        childHolder = (ChildViewHolder) tempView.getTag();

        if (childHolder == null) {
            childHolder = new ChildViewHolder();
            childHolder.optionsContainer = (LinearLayout) tempView.findViewById(R.id.optionsContainer);
            childHolder.correctAnswer = (TextView) tempView.findViewById(R.id.correctAnswer);
            childHolder.myAnswer = (TextView) tempView.findViewById(R.id.myAnswer);
        }

        childHolder.optionsContainer.removeAllViews();

        ExamItem examItem1 = childDatas.get(groupPosition);
        List<ItemOption> options = examItem1.getItemOptions();
        String correctAnswer = "";
        String myAnswer = "";

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOptionCode().equals("t")) {
                if (i == 0) correctAnswer += " A";
                else if (i == 1) correctAnswer += " B";
                else if (i == 2) correctAnswer += " C";
                else if (i == 3) correctAnswer += " D";
                else if (i == 4) correctAnswer += " E";
                else if (i == 5) correctAnswer += " F";
                else if (i == 6) correctAnswer += " G";
            }
        }

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).isSelected()) {
                if (i == 0) myAnswer += " A";
                else if (i == 1) myAnswer += " B";
                else if (i == 2) myAnswer += " C";
                else if (i == 3) myAnswer += " D";
                else if (i == 4) myAnswer += " E";
                else if (i == 5) myAnswer += " F";
                else if (i == 6) myAnswer += " G";
            }
        }

        childHolder.correctAnswer.setText(correctAnswer);
        childHolder.myAnswer.setText(myAnswer);
        String letter="";
        for (int i = 0; i < options.size(); i++) {
            TextView optinTxt = new TextView(context);

            if (i == 0) letter = " A";
            else if (i == 1) letter = " B";
            else if (i == 2) letter = " C";
            else if (i == 3) letter = " D";
            else if (i == 4) letter = " E";
            else if (i == 5) letter = " F";
            else if (i == 6) letter = " G";
            optinTxt.setText("   "+letter + "   " + options.get(i).getTitle());


            childHolder.optionsContainer.addView(optinTxt);
        }


        tempView.setTag(childHolder);
        return tempView;
    }

    public class GroupViewHolder {
        private TextView no;
        private TextView title;
        private ImageView isCorrect;
    }

    public class ChildViewHolder {
        private LinearLayout optionsContainer;
        private TextView correctAnswer;
        private TextView myAnswer;
    }

}
