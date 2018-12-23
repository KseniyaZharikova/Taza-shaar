package com.example.kseniya.zerowaste.utils;

import com.example.kseniya.zerowaste.R;

public class Constants {

    public static final String FIREBASE_RECEPTION_POINTS = "reception_points";

    //PERMISSION
    public static final int LOCATION_REQUEST_CODE = 100;

    //BISHKEK_LOCATION
    public static final double LAT = 42.8748635;
    public static final double LNG = 74.6048324;

    //OFFLINE_AREA
    public static final double LAT_SW = 42.79755;
    public static final double LNG_SW = 74.50739;

    public static final double LAT_NE = 42.93717;
    public static final double LNG_NE = 74.70884;

    public static  int HIGHT_OF_ACTIVITY = 0;

    public static int PointsType(long type) {
        switch ((int)type){
            case 1: return R.mipmap.pl_location;
            case 2: return R.mipmap.glass_location;
            case 3: return R.mipmap.paper_location;
            case 4: return R.mipmap.clothing_location;
            case 5: return R.mipmap.polietilen_location;
            case 6: return R.mipmap.organic_location;
            case 7: return R.mipmap.tech_location;
            case 8: return R.mipmap.skot_location;

        }
        return R.drawable.arrow_up;

    }
}
