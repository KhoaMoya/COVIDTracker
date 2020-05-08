package com.khoa.covidtracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Patient {
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
}
