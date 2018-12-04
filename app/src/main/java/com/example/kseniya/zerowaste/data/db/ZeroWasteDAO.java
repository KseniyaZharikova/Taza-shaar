package com.example.kseniya.zerowaste.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;


import com.example.kseniya.zerowaste.data.ReceptionPoint;

import java.util.List;

@Dao
public interface ZeroWasteDAO {

    @Insert()
    void insertReceptionPoints(List<ReceptionPoint> mList);

    @Delete
    void deleteReceptionPoints(List<ReceptionPoint> mList);

    @Query("SELECT * FROM ReceptionPoint")
    List<ReceptionPoint> getReceptionPoints();


}
