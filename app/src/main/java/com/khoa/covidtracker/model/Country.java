package com.khoa.covidtracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Country {
    @ColumnInfo(name = "countryCode")
    public String countryCode;
    @ColumnInfo(name = "country")
    public String country;
    @ColumnInfo(name = "lat")
    public Float lat;
    @ColumnInfo(name = "lng")
    public Float lng;
    @ColumnInfo(name = "totalConfirmed")
    public Integer totalConfirmed;
    @ColumnInfo(name = "totalDeaths")
    public Integer totalDeaths;
    @ColumnInfo(name = "totalRecovered")
    public Integer totalRecovered;
    @ColumnInfo(name = "dailyConfirmed")
    public Integer dailyConfirmed;
    @ColumnInfo(name = "dailyDeaths")
    public Integer dailyDeaths;
    @ColumnInfo(name = "activeCases")
    public Integer activeCases;
    @ColumnInfo(name = "totalCritical")
    public Integer totalCritical;
    @ColumnInfo(name = "totalConfirmedPerMillionPopulation")
    public Integer totalConfirmedPerMillionPopulation;
    @ColumnInfo(name = "totalDeathsPerMillionPopulation")
    public Integer totalDeathsPerMillionPopulation;
    @ColumnInfo(name = "FR")
    public String fR;
    @ColumnInfo(name = "PR")
    public String pR;
    @ColumnInfo(name = "lastUpdated")
    public String lastUpdated;

}