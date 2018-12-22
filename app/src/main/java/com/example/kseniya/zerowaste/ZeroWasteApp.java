package com.example.kseniya.zerowaste;

import android.app.Application;
import android.content.Context;

import com.example.kseniya.zerowaste.data.db.SQLiteHelper;

public class ZeroWasteApp extends Application {

private static SQLiteHelper sqLiteHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
    }

    public static ZeroWasteApp get(Context context){
        return (ZeroWasteApp) context.getApplicationContext();
    }
    public SQLiteHelper getSqLiteHelper() {
        return sqLiteHelper;
    }
}
