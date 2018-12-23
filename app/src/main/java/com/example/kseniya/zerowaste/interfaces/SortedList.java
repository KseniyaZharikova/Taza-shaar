package com.example.kseniya.zerowaste.interfaces;

import com.example.kseniya.zerowaste.data.ReceptionPoint;

import java.util.ArrayList;
import java.util.List;

public interface SortedList {
    ArrayList<ReceptionPoint> list = new ArrayList<>();

    void onClickItem(int position);
    void setNoResultVisible(boolean isEmpty);
}
