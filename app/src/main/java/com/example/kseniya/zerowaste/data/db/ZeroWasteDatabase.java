package com.example.kseniya.zerowaste.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.kseniya.zerowaste.ReceptionPoints;


@Database(entities = {ReceptionPoints.class}, version = 1)
public abstract class ZeroWasteDatabase extends RoomDatabase {
    public abstract ZeroWasteDAO mZeroWasteDAO();
}
