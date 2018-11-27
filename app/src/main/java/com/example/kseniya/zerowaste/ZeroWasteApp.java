package com.example.kseniya.zerowaste;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.kseniya.zerowaste.data.db.ZeroWasteDatabase;

public class ZeroWasteApp extends Application {

private static ZeroWasteDatabase mDB;
    @Override
    public void onCreate() {
        super.onCreate();
        mDB = Room.databaseBuilder(this, ZeroWasteDatabase.class, "base").build();
    }
}
