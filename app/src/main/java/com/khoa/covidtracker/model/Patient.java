package com.khoa.covidtracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.khoa.covidtracker.utils.TimeUtils;

@Entity
public class Patient  implements ClusterItem {
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "lat")
    public Float lat;
    @ColumnInfo(name = "lng")
    public Float lng;
    @ColumnInfo(name = "patientGroup")
    public String patientGroup;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "verifyDate")
    public String verifyDate;

    @Ignore
    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @Ignore
    public String toDescription(){
        return "Địa chỉ: " + address
                + "\nNhóm: " + patientGroup
                + "\nThời gian: " + TimeUtils.convertDate(verifyDate)
                + "\nGhi chú: " + note;

    }

}
