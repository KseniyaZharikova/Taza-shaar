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
    public static  boolean SCROLL = false;

    public static int PointsType(long type) {
        switch ((int)type){
            case 1: return R.drawable.pl_location;
            case 2: return R.drawable.glass_location;
            case 3: return R.drawable.paper_location;
            case 4: return R.drawable.clothing_location;
            case 5: return R.drawable.polietilen_location;
            case 6: return R.drawable.organic_location;
            case 7: return R.drawable.tech_location;
            case 8: return R.drawable.skot_location;

        }
        return R.drawable.arrow_up;

    }
}
