package com.mslji.mybluetooth.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FormClass.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    // DAO class का object बनाना होगा।
    public abstract DAO dao();
}
