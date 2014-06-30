package com;

import android.app.Activity;
import com.join.android.app.common.db.DatabaseHelper;
import com.join.android.app.common.db.manager.DBManager;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-10
 * Time: 下午3:20
 */
public class BaseActivity extends Activity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManager.getInstance(this).onDestroy();
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null)
            databaseHelper = DBManager.getInstance(this).getHelper();

        return databaseHelper;
    }

    /**
     * db stuff
     * when needs multiple databases we need create database manually.
     * for example ,after login
     *
     * @param dbName
     */
    public void createDB(String dbName) {
        DBManager.getInstance(this).createDB(dbName);
//        if (databaseHelper != null && databaseHelper.getDatabaseName().equals(dbName)) return;
//        DatabaseHelper.setDatabaseName(dbName);
//        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
    }
}
