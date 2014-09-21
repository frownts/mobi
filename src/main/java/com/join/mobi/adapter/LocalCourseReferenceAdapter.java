package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Reference;
import com.join.android.app.common.utils.FileUtils;
import com.join.mobi.activity.LocalCourseDetailActivity;
import com.join.mobi.utils.ContentTypeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalCourseReferenceAdapter extends BaseAdapter {

    private List<Reference> references = new ArrayList<Reference>(0);

    private Context mContext;
    private LayoutInflater inflater;
    private int mRightWidth = 0;

    private class ViewHolder {
        TextView title;
        TextView fileSize;
        TextView type;
        RelativeLayout item_left;
        RelativeLayout item_right;
        TextView item_right_txt;
        ImageView roundDel;
    }

    public List<Reference> getItems() {
        return references;
    }

    public void updateItems(List<Reference> References) {
        References.clear();
        References.addAll(References);
    }

    public LocalCourseReferenceAdapter(Context c, List<Reference> _reference,int rightWidth,OnRightItemClickListener onRightItemClickListener) {
        mContext = c;
        references.clear();
        references.addAll(_reference);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListener = onRightItemClickListener;
        mRightWidth = rightWidth;
    }

    public void setItems(List<Reference> _reference){
        references.clear();
        references.addAll(_reference);
    }

    @Override
    public int getCount() {
        return references.size();
    }

    @Override
    public Reference getItem(int position) {
        if(references==null||references.size()==0)return null;
        return references.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Reference reference = references.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.localcourse_reference_listview_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            holder.type = (TextView) convertView.findViewById(R.id._type);
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            holder.roundDel = (ImageView) convertView.findViewById(R.id.roundDel);;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.title.setText(reference.getTitle());
        holder.fileSize.setText(FileUtils.FormatFileSize(reference.getFileSize()));
        //根据类型，判断生成文字。
        holder.type.setText(ContentTypeUtils.convertReferenceType(reference.getType()));

        if(((LocalCourseDetailActivity)mContext).isTrashShowing()){
            holder.roundDel.setVisibility(View.VISIBLE);
        }else
            holder.roundDel.setVisibility(View.GONE);


        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });

        return convertView;
    }

    /**
     * 单击事件监听器
     */
    private OnRightItemClickListener mListener = null;
    public void setOnRightItemClickListener(OnRightItemClickListener listener){
        mListener = listener;
    }

    public interface OnRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

}

