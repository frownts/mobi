package com.join.android.app.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.join.android.app.common.R;
import com.join.android.app.common.db.tables.Account;
import com.join.android.app.common.db.tables.Order;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static String DATABASE_NAME = "helloAndroid.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
//	private Dao<SimpleData, Integer> simpleDao = null;
//	private Dao<SimpleData1, Integer> simpleDao1 = null;
    private RuntimeExceptionDao<Order, Integer> orderRuntimeDao = null;
    private RuntimeExceptionDao<Account, Integer> accountRuntimeDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    public static void setDatabaseName(String name) {
        DATABASE_NAME = name;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, Account.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

//		// here we try inserting data in the on-create as a test
//		RuntimeExceptionDao<SimpleData, Integer> dao = getSimpleDataDao();
//		long millis = System.currentTimeMillis();
//		// create some entries in the onCreate
//		SimpleData simple = new SimpleData(millis);
//		dao.create(simple);
//		simple = new SimpleData(millis + 1);
//		dao.create(simple);
//		Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Order.class, true);
            TableUtils.dropTable(connectionSource, Account.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
//	public Dao<SimpleData, Integer> getDao() throws SQLException {
//		if (simpleDao == null) {
//			simpleDao = getDao(SimpleData.class);
//		}
//		return simpleDao;
//	}
//
//    public Dao<SimpleData1, Integer> getDao1() throws SQLException {
//		if (simpleDao1 == null) {
//            simpleDao1 = getDao(SimpleData1.class);
//		}
//		return simpleDao1;
//	}

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */


    public RuntimeExceptionDao<Order, Integer> getOrderDao() {
        if (orderRuntimeDao == null) {
            orderRuntimeDao = getRuntimeExceptionDao(Order.class);
        }
        return orderRuntimeDao;
    }

    public RuntimeExceptionDao<Account, Integer> getAccountDao() {
        if (accountRuntimeDao == null) {
            accountRuntimeDao = getRuntimeExceptionDao(Account.class);
        }
        return accountRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        orderRuntimeDao = null;
        accountRuntimeDao = null;
    }
}
