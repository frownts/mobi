package com.php25.PDownload;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.join.mobi.enums.Dtype;
import com.php25.tools.DigestTool;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;

/**
 * Created with penghuiping
 * User: penghuiping
 * Date: 14-9-1
 * Time: 下午2:34
 * To change this template use File | Settings | File Templates.
 */
public class DownloadManager {
    private String basePath;

    private boolean finished;

    private boolean stopped;

    private boolean stoppedProgress;

    private DownloadHandler downloadHandler;

    private Context context;

    private DownloadApplication app;

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isStoppedProgress() {
        return stoppedProgress;
    }

    public void setStoppedProgress(boolean stoppedProgress) {
        this.stoppedProgress = stoppedProgress;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public DownloadHandler getDownloadHandler() {
        return downloadHandler;
    }

    public void setDownloadHandler(DownloadHandler downloadHandler) {
        this.downloadHandler = downloadHandler;
    }

    public DownloadManager(DownloadHandler downloadHandler, Context context) {
        this.downloadHandler = downloadHandler;
        this.context = context;
        this.basePath = context.getExternalFilesDir("").getAbsolutePath();
        this.setStopped(true);
        app = (DownloadApplication) context.getApplicationContext();
    }

    public DownloadManager(Context context) {
        this.context = context;
        this.basePath = context.getExternalFilesDir("").getAbsolutePath();
        this.setStopped(true);
        app = (DownloadApplication) context.getApplicationContext();
    }


    public void download(final String url,final String showName, final Dtype dtype, final String fileType) {
        if (!isStopped()) {
            return;
        }
        this.setStopped(false);
        app.execute(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                OutputStream out = null;

                ObjectInputStream inputStream = null;
                ObjectOutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    URLConnection conn = u.openConnection();
                    conn.setDoOutput(false);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(30000);

                    String filePath = null;
                    String fileName = null;

//                    if (contentType != null) {
//                        int start = contentType.lastIndexOf("/");
//                        int end = contentType.length();
//                        String postfix = contentType.substring(start + 1, end);
//                        filePath = basePath + "/" + DigestTool.md5(url) + "." + postfix;
//                        fileName = DigestTool.md5(url) + "." + postfix;
//                    } else {
                        filePath = basePath + "/" + DigestTool.md5(url);
                        fileName = DigestTool.md5(url);
//                    }

                    final File downloadFile = new File(filePath);
                    //创建meta文件
                    DownloadFile temp  = app.getDownloadFileDao().queryByTag(DigestTool.md5(url));
                    if(temp == null) {
                        temp = new DownloadFile();
                        temp.setAbsolutePath(filePath);
                        temp.setBasePath(basePath);
                        temp.setTag(DigestTool.md5(url));
                        temp.setUrl(url);
                        temp.setName(fileName);
                        temp.setShowName(showName);
                        temp.setDtype(dtype.name());

                        temp.setDownloading(true);
                        temp.setFinished(false);
                        temp.setFileType(fileType);
                        temp.setCreateTime(System.currentTimeMillis()+"");

                        long id = app.getDownloadFileDao().insert(temp);
                        temp.setId(id);
                        out = new FileOutputStream(downloadFile);
                    } else {
                        File ff = new File(temp.getAbsolutePath());
                        conn.setRequestProperty("RANGE", "bytes="+ff.length()+"-");
                        out = new FileOutputStream(downloadFile,true);
                    }

                    String contentType = conn.getContentType();
                    final int contentLength = conn.getContentLength();
                    if(temp.getTotalSize()==null||temp.getTotalSize()==0){
                        temp.setTotalSize(contentLength);
                    }


                    if (contentType == null) {
                        contentType = conn.guessContentTypeFromName(url);
                    }

                    if (contentType != null && contentType.startsWith("text/") || contentLength == -1) {
                        throw new RuntimeException("This is not a binary file.");
                    }

                    app.getDownloadFileDao().update(temp);

                    //开始下载
                    in = conn.getInputStream();

                    byte buff[] = new byte[1024];
                    int pos = 0;
                    while (!isStopped() && ((pos = in.read(buff)) != -1)) {
                        out.write(buff, 0, pos);
                        out.flush();
                    }
                    if (!isStopped()) {
                        setFinished(true);
                    } else {
                        temp.setDownloading(false);
                        app.getDownloadFileDao().update(temp);
                    }

                    if (isFinished()) {
                        temp.setFinished(true);
                        temp.setFinishTime(System.currentTimeMillis()+"");
                        app.getDownloadFileDao().update(temp);
                        app.removeDownloadManager(DigestTool.md5(url));
                        app.checkLocalFileExpired();
                        Intent intent = new Intent("org.androidannotations.downloadCompelte");
                        intent.putExtra("name",showName);
                        app.sendBroadcast(intent);
                    }
                    in.close();
                    out.close();
                    Log.v(DownloadTool.LOG_TAG, "end download");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                finally {
                    try {
                        if (null != in) {
                            in.close();
                            in = null;
                        }

                        if (null != out) {
                            out.close();
                            out = null;
                        }

                        if (null != inputStream) {
                            inputStream.close();
                            inputStream = null;
                        }

                        if (null != outputStream) {
                            outputStream.close();
                            outputStream = null;
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        });


    }

    public Future updateDownloadProgress(final DownloadFile file) {
        final File downloadFile = new File(file.getAbsolutePath());
        DownloadApplication app = (DownloadApplication) context.getApplicationContext();
        //下载进度提示
        Future future = app.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isFinished()&&!stopped) {
                        if (null != downloadHandler){
                            float filelength = downloadFile.length();
                            float totalSize = new Float(file.getTotalSize());

                            float p = filelength/totalSize;
                            downloadHandler.updateProcess(p);
                        }

                        Thread.sleep(2000);
                    }
                    if (isFinished()) {
                        if (null != downloadHandler)
                            downloadHandler.finished();
                        Log.v(DownloadTool.LOG_TAG, "download has finished!!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }



}
