package com.join.android.app.common.db.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-6-27
 * Time: 下午10:16
 */
public class Test {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true)
    private String name;
    @ForeignCollectionField(eager = false)
    ForeignCollection<Order> orders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
