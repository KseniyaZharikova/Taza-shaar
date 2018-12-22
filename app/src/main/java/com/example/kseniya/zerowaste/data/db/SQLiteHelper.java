package com.example.kseniya.zerowaste.data.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kseniya.zerowaste.data.ReceptionPoint;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "DB_VACANCIES";
    private final static int DB_VERSION = 2;

    private final static String TABLE_POINTS = "TABLE_POINTS";
    private final static String ID = "_id";
    private final static String ID_POINTS = "ID_POINTS";
    private final static String NAME = "NAME";
    private final static String ADDRESS = "ADDRESS";
    private final static String TYPE = "TYPE";
    private final static String PRICE = "PRICE";
    private final static String WORK_TME = "WORK_TME";
    private final static String LATITUDE = "LATITUDE";
    private final static String LONGTITUDE = "LONGTITUDE";
    private final static String PHONE = "PHONE";

    private final static String TABLE_CREATED_POINTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_POINTS + "(" +
            ID + " INTEGER_PRIMARY_KEY, " +
            ID_POINTS + " TEXT ," +
            NAME + " TEXT ," +
            ADDRESS + " TEXT ," +
            TYPE + " TEXT ," +
            PRICE + " TEXT ," +
            WORK_TME + " TEXT ," +
            LATITUDE + " TEXT ," +
            LONGTITUDE + " TEXT ," +
            PHONE + " TEXT " +
            ");";


    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATED_POINTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        onCreate(sqLiteDatabase);
    }


    public void saveReceptionPoints(List<ReceptionPoint> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < list.size(); i++) {
            ReceptionPoint model = list.get(i);
            cv.put(ID_POINTS, model.getId());
            cv.put(NAME, model.getName());
            cv.put(ADDRESS, model.getAddress());
            cv.put(TYPE, model.getType());
            cv.put(PRICE, model.getPrice());
            cv.put(WORK_TME, model.getWork_time());
            cv.put(LATITUDE, model.getLatitude());
            cv.put(LONGTITUDE, model.getLongitude());
            cv.put(PHONE, model.getPhone());
            long rowsId = db.insert(TABLE_POINTS, null, cv);
            Log.d("saved vacancies", "rows" + rowsId + model.getId());
        }
        db.close();
    }


    public List<ReceptionPoint> getReceptionPoints() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<ReceptionPoint> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_POINTS,
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            int indexPid = cursor.getColumnIndex(ID_POINTS);
            int indexName = cursor.getColumnIndex(NAME);
            int indexAddress = cursor.getColumnIndex(ADDRESS);
            int indexType = cursor.getColumnIndex(TYPE);
            int indexPrice = cursor.getColumnIndex(PRICE);
            int indexworkTime = cursor.getColumnIndex(WORK_TME);
            int indexLatitude = cursor.getColumnIndex(LATITUDE);
            int indexLongtitude = cursor.getColumnIndex(LONGTITUDE);
            int indexPhone = cursor.getColumnIndex(PHONE);

            do {
                ReceptionPoint model = new ReceptionPoint();
                model.setId(Integer.parseInt(cursor.getString(indexPid)));
                model.setName(cursor.getString(indexName));
                model.setAddress(cursor.getString(indexAddress));
                model.setType(Long.parseLong(cursor.getString(indexType)));
                model.setPrice(cursor.getString(indexPrice));
                model.setWork_time(cursor.getString(indexworkTime));
                model.setLatitude(cursor.getString(indexLatitude));
                model.setLongitude(cursor.getString(indexLongtitude));
                model.setPhone(cursor.getString(indexPhone));

                list.add(model);
            } while (cursor.moveToNext());
            Log.d("getReceptionPoints", "is getting");

        } else {
            Log.d("getReceptionPoints", "failed");
        }

        cursor.close();
        db.close();
        return list;

    }

    public void deleteReceptionPoints() {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.delete(TABLE_POINTS, null, null);
        Log.d("database ","clear successful" + rowId);
        db.close();

    }
}