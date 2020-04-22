package com.liyiwei.basethirdlib.roomwcdb.entity;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by johnwhe on 2017/7/12.
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
