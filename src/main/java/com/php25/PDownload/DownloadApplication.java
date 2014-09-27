package com.php25.PDownload;

import android.app.Application;
import android.util.Log;
import com.join.android.app.common.db.manager.ChapterManager;
import com.join.android.app.common.db.tables.Chapter;
import com.php25.tools.DigestTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with penghuiping
 * User: penghuiping
 * Date: 14-9-10
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 */
public class DownloadApplication extends Application {

    private Map<String,Future> futureMap = new HashMap<String, Future>(0);
    private Map<String, com.php25.PDownload.DownloadManager> map = new ConcurrentHashMap<String, DownloadManager>();

    private ExecutorService pools = Executors.newCachedThreadPool();

    public void addDownloadManager(String key, DownloadManager downloadManager) {
        map.put(key, downloadManager);
    }

    public DownloadManager getDownloadManager(String key) {
        return map.get(key);
    }

    public void removeDownloadManager(String key) {
        DownloadManager downloadManager = getDownloadManager(key);
        map.remove(key);
        downloadManager.setStopped(true);
        downloadManager = null;
    }

    public boolean containsDownloadManager(String key) {
        return map.containsKey(key);
    }

    public Future execute(Runnable runnable) {
        return pools.submit(runnable);
    }

    private DownloadFileDao downloadFileDao;

    public DownloadFileDao getDownloadFileDao() {
        return downloadFileDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadFileDao = new DownloadFileDao(DownloadApplication.this);
    }

    public void startAllUnCompleteDownload() {
        execute(new Runnable() {
            @Override
            public void run() {

                List<DownloadFile> downloadFiles = DownloadTool.getAllDownloadingTask(DownloadApplication.this);
                for (DownloadFile file : downloadFiles) {
                    addDownloadManager(DigestTool.md5(file.getUrl()), new DownloadManager(DownloadApplication.this));
                }
            }
        });
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.v(DownloadTool.LOG_TAG, "资源释放");
        if (null != downloadFileDao) {
            downloadFileDao.close();
            downloadFileDao = null;
        }
        pools.shutdown();
    }

    public Map<String, Future> getFutureMap() {
        return futureMap;
    }

    public void setFutureMap(Map<String, Future> futureMap) {
        this.futureMap = futureMap;
    }

    public  void checkLocalFileExpired() {
        //检查chapter有效性
        List<Chapter> chapters = ChapterManager.getInstance().findAll();
        if (chapters != null) {
            for (Chapter chapter : chapters) {
                if (chapter.getValidUntil() != null) {
                    DownloadFile file = DownloadTool.getDownLoadFile((DownloadApplication) getApplicationContext(), chapter.getDownloadUrl());
                    if (file != null) {
                        if(file.getFinishTime()==null){
                            continue;
                        }
                        long finishTime = Long.parseLong(file.getFinishTime());
                        int day = (int) ((System.currentTimeMillis() - finishTime) / 1000 / 60 / 60 / 24);
                        long left = Long.parseLong(chapter.getValidUntil()) - day;
                        if (left <= 0) {//删除该章节
                            DownloadTool.deleteDownloadTask((DownloadApplication) getApplicationContext(), chapter.getDownloadUrl());
                            ChapterManager.getInstance().delete(chapter);
                        } else {
                            chapter.setLeftDays(left + "");
                            ChapterManager.getInstance().saveOrUpdate(chapter);
                        }
                    }
                }
            }
        }

    }
}
