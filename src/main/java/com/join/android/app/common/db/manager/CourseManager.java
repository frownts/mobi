package com.join.android.app.common.db.manager;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.join.android.app.common.db.tables.Course;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-21
 * Time: 上午11:01
 */

public class CourseManager extends BaseManager<Course> {

    private static CourseManager courseManager;
    private static RuntimeExceptionDao<Course, Integer> dao;

    private CourseManager() {
        super(dao);
    }

    public static CourseManager getInstance() {
        if (courseManager == null) {
            dao = DBManager.getInstance(null).getHelper().getCourseDao();
            courseManager = new CourseManager();
        }
        return courseManager;
    }

}
