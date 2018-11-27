package com.example.kseniya.zerowaste.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import java.io.Serializable;


@Entity
public class ReceptionPoint implements Serializable {
    //data class ReceptionPoint () : Serializable {
//    @PrimaryKey(autoGenerate = true)
//    var id_prymary: Long? = null
//
//    val id: String? = null
//    val name: String? = null
//    val address: String? = null
//    val type: Long = 0
//    val price: String? = null
//    val open_time: String? = null
//    val close_time: String? = null
//    val latitude: String? = null
//    val longitude: String? = null
    @PrimaryKey(autoGenerate = true)
    private int id_key;
    private int id;
    private String name;
    private String address;
    private long type;
    private String price;
    private String open_time;
    private String close_time;
    private String latitude;
    private String longitude;

    public int getId_key() {
        return id_key;
    }

    public void setId_key(int id_key) {
        this.id_key = id_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
