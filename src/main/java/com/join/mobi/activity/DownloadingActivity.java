package com.join.mobi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.mobi.adapter.DownloadAdapter;
import com.join.mobi.dialog.DownloadOpDialog;
import com.join.mobi.enums.Dtype;
import com.php25.PDownload.DownloadApplication;
import com.php25.PDownload.DownloadFile;
import com.php25.PDownload.DownloadTool;
import org.androidannotations.annotations.*;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-14
 * Time: 下午5:37
 */

@EActivity(R.layout.downloading_activity_layout)
public class DownloadingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewById
    RelativeLayout main;

    @ViewById
    GridView listView;

    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    DownloadAdapter downloadAdapter;
    DownloadOpDialog downloadOpDialog;

    @AfterViews
    void afterViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        downloadAdapter = new DownloadAdapter(this);
        listView.setAdapter(downloadAdapter);
        retrieveDataFromDB();
    }

    private void retrieveDataFromDB() {
        List<DownloadFile> downloadFiles = DownloadTool.getAllDownloadingTask((DownloadApplication) getApplicationContext());
//        List<DownloadFile> downloadFiles = DownloadTool.getAllDownload((DownloadApplication) getApplicationContext());
        downloadAdapter.setItems(downloadFiles);
        downloadAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }


    @ItemClick
    void listViewItemClicked(final DownloadFile downloadFile) {

        downloadOpDialog = new DownloadOpDialog(this, downloadFile, new DownloadOpDialog.Op() {
            @Override
            public void onDel() {
                DownloadTool.delDownloadTask((DownloadApplication) getApplicationContext(), downloadFile);
                downloadOpDialog.dismiss();
                retrieveDataFromDB();
            }

            @Override
            public void onOpAction() {
                if (downloadFile.isDownloadingNow()) {//执行暂停操作
                    DownloadTool.stopDownload((DownloadApplication) (getApplicationContext()), downloadFile.getUrl());
                } else {//执行启动操作
                    Dtype dtype = Dtype.Share;
                    if (downloadFile.getDtype().equals(Dtype.Share)) {
                        dtype = Dtype.Share;
                    } else if (downloadFile.getDtype().equals(Dtype.Chapter)) {
                        dtype = Dtype.Chapter;
                    } else if (downloadFile.getDtype().equals(Dtype.Rference)) {
                        dtype = Dtype.Rference;
                    }
                    DownloadTool.startDownload((DownloadApplication) (getApplicationContext()), downloadFile.getUrl(), downloadFile.getShowName(), dtype,downloadFile.getFileType());
                }
                downloadOpDialog.dismiss();
                retrieveDataFromDB();
            }
        });
        downloadOpDialog.show();
    }


    @Click
    void backClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        retrieveDataFromDB();
    }
}
