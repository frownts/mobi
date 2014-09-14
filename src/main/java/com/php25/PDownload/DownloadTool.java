package com.php25.PDownload;

import com.join.mobi.enums.Dtype;
import com.php25.tools.DigestTool;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created with penghuiping
 * User: penghuiping
 * Date: 14-9-10
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 */
public class DownloadTool {
    public static final String LOG_TAG = "com.php25.PDownload";

    /**
     * 开启一个下载线程
     *
     * @param context
     * @param url
     */
    public static void startDownload(DownloadApplication context, String url, String showName, Dtype dtype,String fileType) {
        if (context.containsDownloadManager(DigestTool.md5(url))) {
            DownloadManager downloadManager = context.getDownloadManager(DigestTool.md5(url));
            downloadManager.download(url, showName, dtype,fileType);
        } else {
            DownloadManager downloadManager = new DownloadManager(context);
            context.addDownloadManager(DigestTool.md5(url), downloadManager);
            downloadManager.download(url, showName, dtype,fileType);
        }
    }

    /**
     * 正在下载和已下载完成，这两种情况都会返回true.如果你想判断是否在下载状态请用方法:isDownloadingNow
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean isDownloading(DownloadApplication context, String url) {
        if (context.containsDownloadManager(DigestTool.md5(url)) || isFinished(context, url)) return true;
        return false;
    }

    public static boolean isDownloadingNow(DownloadApplication context, String url) {
        if (context.containsDownloadManager(DigestTool.md5(url))&&!isStopped(context,url)) return true;
        return false;
    }


    /**
     * 开启个下载进度更新线程
     *
     * @param context
     * @param handler
     * @param downloadFile
     * @return
     */
    public static Future updateDownloadProgress(DownloadApplication context, DownloadHandler handler, DownloadFile downloadFile) {
        final DownloadManager downloadManager = context.getDownloadManager(DigestTool.md5(downloadFile.getUrl()));
        downloadManager.setDownloadHandler(handler);
        return downloadManager.updateDownloadProgress(downloadFile);
    }

    /**
     * 停止下载线程
     *
     * @param context
     * @param url
     */
    public static void stopDownload(DownloadApplication context, String url) {
        DownloadManager downloadManager = context.getDownloadManager(DigestTool.md5(url));
        if (downloadManager != null)
            downloadManager.setStopped(true);
    }

    /**
     * 判断下载线程是否停止
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean isStopped(DownloadApplication context, String url) {
        DownloadManager downloadManager = context.getDownloadManager(DigestTool.md5(url));
        return downloadManager.isStopped();
    }

    /**
     * 获取所有需要下载的任务列表
     *
     * @param context
     * @return
     */
    public static List<DownloadFile> getAllDownloadingTask(DownloadApplication context) {
        return context.getDownloadFileDao().queryAllDownloadingTask();
    }

    /**
     * 获取所有下载的任务列表
     *
     * @param context
     * @return
     */
    public static List<DownloadFile> getAllDownload(DownloadApplication context) {
        return context.getDownloadFileDao().queryAll();
    }

    /**
     * 获取所有已下载的文件
     *
     * @param context
     * @return
     */
    public static List<DownloadFile> getAllDownloaded(DownloadApplication context,Dtype dtype) {
        return context.getDownloadFileDao().queryAllDownloaded(dtype);
    }

    /**
     * 获取当前url是否已经下载完了。
     *
     * @param context
     * @return
     */
    public static boolean isFinished(DownloadApplication context, String url) {
        DownloadFile temp = context.getDownloadFileDao().queryByTag(DigestTool.md5(url));
        if (temp == null) return false;
        return temp.getDownloading();
    }

    /**
     * 删除下载任务,包括数据库和文件(若有)
     */
    public static void delDownloadTask(DownloadApplication context, DownloadFile downloadFile) {
        //停止下载任务
        stopDownload(context, downloadFile.getUrl());

        //删除文件
        File file = new File(downloadFile.getAbsolutePath());
        if (file.exists()) file.delete();

        //删除数据库中的记录
        context.getDownloadFileDao().delete(downloadFile);

    }
}
