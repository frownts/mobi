package com.join.android.app.common.db.manager;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.join.android.app.common.db.DatabaseHelper;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-21
 * Time: 上午11:03
 */
public class DBManager {

    private static DBManager dbManager;
    private Context context;

    private DBManager(Context context){
        this.context = context;
    }

    public static DBManager getInstance(Context context){
        if(dbManager == null)dbManager = new DBManager(context);
        return dbManager;
    }

    private DatabaseHelper databaseHelper = null;

    public void onDestroy() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void createDB(String dbName) {
        if (databaseHelper != null && databaseHelper.getDatabaseName().equals(dbName)) return;
        DatabaseHelper.setDatabaseName(dbName);
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

}
