package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.FileUtils;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadFile;
import com.php25.PDownload.DownloadTool;

import java.util.ArrayList;
import java.util.List;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class LocalAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<DownloadFile> downloadFiles = new ArrayList<DownloadFile>(0);
    private boolean trashIsShowing;

    private class GirdHolder {
        ImageView fileLegend;
        ImageView trash;
        TextView fileName;
        TextView fileSize;
        TextView finishTime;
    }

    public LocalAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<DownloadFile> _DownloadFiles){
        downloadFiles.clear();
        downloadFiles.addAll(_DownloadFiles);
    }

    @Override
    public int getCount() {
        return downloadFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return downloadFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DownloadFile downloadFile = downloadFiles.get(position);

        GirdHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_local, null);
            holder = new GirdHolder();
            holder.fileLegend = (ImageView) convertView.findViewById(R.id.fileLegend);
            holder.trash = (ImageView) convertView.findViewById(R.id.trash);
            holder.fileName = (TextView) convertView.findViewById(R.id.fileName);
            holder.fileSize = (TextView) convertView.findViewById(R.id.fileSize);
            holder.finishTime = (TextView) convertView.findViewById(R.id.finishTime);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }
        holder.fileName.setText(downloadFile.getShowName());
        holder.fileSize.setText(FileUtils.FormatFileSize(downloadFile.getTotalSize()));
        holder.finishTime.setText("下载: "+ DateUtils.FormatForDownloadTime(Long.parseLong(downloadFile.getFinishTime())));
        int type = Integer.parseInt(downloadFile.getFileType());
        if (type == 1) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video));
        else if (type == 2) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.music));
        else if (type == 3) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdf));
        else if (type == 4) holder.fileLegend.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img));

        if(trashIsShowing){
            holder.trash.setVisibility(View.VISIBLE);
            holder.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete local file
                    DownloadTool.delDownloadTask((DownloadApplication)mContext.getApplicationContext(),downloadFile);
                    downloadFiles.remove(downloadFile);
                    LocalAdapter.this.notifyDataSetChanged();
                }
            });
        }else
            holder.trash.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public boolean isTrashIsShowing() {
        return trashIsShowing;
    }

    public void setTrashIsShowing(boolean trashIsShowing) {
        this.trashIsShowing = trashIsShowing;
    }
}

