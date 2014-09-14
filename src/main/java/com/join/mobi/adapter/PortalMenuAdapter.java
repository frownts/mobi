package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.mobi.activity.*;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class PortalMenuAdapter extends BaseAdapter {

    private NeedLogin needLogin;
    private boolean isLogin;
    //存放各功能图片
    private Integer[] mFunctionPics = {R.drawable.menu_live, R.drawable.menu_live_course,
            R.drawable.menu_share, R.drawable.menu_annoce, R.drawable.local_course
            , R.drawable.local_source, R.drawable.menu_download, R.drawable.menu_setting};

    private Integer[] mFunctionPicsDisabled = {R.drawable.live_selected, R.drawable.live_course_selected,
            R.drawable.share_selected, R.drawable.announce_selected};

    private Context mContext;
    private LayoutInflater inflater;

    private class GirdHolder {
        ImageView phone_function_pic;
        TextView phone_function_name;
    }

    public PortalMenuAdapter(Context c, boolean _isLogin, NeedLogin _needLogin) {
        isLogin = _isLogin;
        mContext = c;
        needLogin = _needLogin;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFunctionPics.length;
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
        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, null);
            holder = new GirdHolder();
            holder.phone_function_pic = (ImageView) convertView.findViewById(R.id.function_view);
            holder.phone_function_name = (TextView) convertView.findViewById(R.id.function_name);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }

        if (!isLogin && (position == 0 || position == 1 || position == 2 || position == 3)) {

            holder.phone_function_pic.setImageResource(mFunctionPicsDisabled[position]);
            holder.phone_function_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (needLogin != null) needLogin.login();
                }
            });

        } else {
            holder.phone_function_pic.setImageResource(mFunctionPics[position]);
            holder.phone_function_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==1){
                        LiveCourseActivity_.intent(mContext).start();
                    }else if(position==2){
                        ShareActivity_.intent(mContext).start();
                    }else if(position==5){
                        LocalActivity_.intent(mContext).start();
                    }else if(position==6){
                        DownloadingActivity_.intent(mContext).start();
                    }else if(position==7)
                        ((PortalActivity_)mContext).showSetting();
                }
            });
        }

        return convertView;
    }

    public interface NeedLogin {
        public void login();
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
}

