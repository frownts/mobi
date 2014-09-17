package com.join.mobi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.join.android.app.common.utils.DateUtils;
import com.join.android.app.common.utils.FileUtils;
import com.join.mobi.activity.DownloadingActivity;
import com.join.mobi.enums.Dtype;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadFile;
import com.php25.PDownload.DownloadHandler;
import com.php25.PDownload.DownloadTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:21
 */
public class DownloadAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<DownloadFile> downloadFiles = new ArrayList<DownloadFile>(0);

    private class GirdHolder {
        TextView fileName;
        TextView downloadTime;
        TextView dtype;
        TextView totalSize;
        TextView downloadStatus;
        TextView downloadPercent;
    }

    public DownloadAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<DownloadFile> getItems(){
            return downloadFiles;
    }

    public void setItems(List<DownloadFile> _downloadFiles) {
        downloadFiles.clear();
        downloadFiles.addAll(_downloadFiles);
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
        GirdHolder holder;
        DownloadFile downloadFile = downloadFiles.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_download, null);
            holder = new GirdHolder();
            holder.fileName = (TextView) convertView.findViewById(R.id.fileName);
            holder.downloadTime = (TextView) convertView.findViewById(R.id.downloadTime);
            holder.dtype = (TextView) convertView.findViewById(R.id.dtype);
            holder.totalSize = (TextView) convertView.findViewById(R.id.totalSize);
            holder.downloadStatus = (TextView) convertView.findViewById(R.id.downloadStatus);
            holder.downloadPercent = (TextView) convertView.findViewById(R.id.downloadPercent);
            convertView.setTag(holder);
        } else {
            holder = (GirdHolder) convertView.getTag();
        }
        holder.fileName.setText(downloadFile.getShowName());
        holder.downloadTime.setText(DateUtils.FormatForDownloadTime(Long.parseLong(downloadFile.getCreateTime())));

        holder.dtype.setText(downloadFile.getDtype());
        if(downloadFile.getDtype().equals(Dtype.Share.name()))holder.dtype.setText("共享资源");
        String status = getDownloadingStatus(downloadFile);
        holder.downloadStatus.setText(status);
        if (status.equals("正在下载")) holder.downloadStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        else
            holder.downloadStatus.setTextColor(mContext.getResources().getColor(R.color.goldYellow));

        holder.totalSize.setText(FileUtils.FormatFileSize(downloadFile.getTotalSize()));
        holder.downloadPercent.setText(downloadFile.getPercent());

        return convertView;
    }

    private String getDownloadingStatus(DownloadFile downloadFile) {
        if (DownloadTool.isDownloadingNow((DownloadApplication) mContext.getApplicationContext(), downloadFile.getUrl())) {
            downloadFile.setDownloadingNow(true);
            updateDownloadProgress(downloadFile);
            return "正在下载";
        }else{
            downloadFile.setDownloadingNow(false);
            if(downloadFile.getPercent()==null){
                final File file = new File(downloadFile.getAbsolutePath());
                float filelength = file.length();
                float totalSize = new Float(downloadFile.getTotalSize());
                float f = filelength/totalSize;
                String p = ((f*100)+"");
                if(p.length()>4)
                    p = p.substring(0,4);
                downloadFile.setPercent(p+"%");
            }
        }

        return "暂停";

    }


    public void updateDownloadProgress(final DownloadFile file) {

        Map<String,Future> futureMap = ((DownloadApplication) mContext.getApplicationContext()).getFutureMap();

        if(futureMap.get(file.getTag())==null){
            Future future = DownloadTool.updateDownloadProgress((DownloadApplication) mContext.getApplicationContext(), new DownloadHandler() {

                @Override
                public void updateProcess(float process) {
                    String p = (((process)*100)+"");
                    if(p.length()>4)
                        p = p.substring(0,4);
                    file.setPercent(p+"%");
                    ((DownloadingActivity)(mContext)).refreshAdapter();
                }

                @Override
                public void finished() {
                    downloadFiles.remove(file);
                    ((DownloadingActivity)(mContext)).refreshAdapter();
                }
            }, file);

            futureMap.put(file.getTag(),future);
        }
    }



}

