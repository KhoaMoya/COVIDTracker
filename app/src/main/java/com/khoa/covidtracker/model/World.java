package com.khoa.covidtracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class World {
    @ColumnInfo(name = "totalConfirmed")
    public Integer totalConfirmed;
    @ColumnInfo(name = "totalDeaths")
    public Integer totalDeaths;
    @ColumnInfo(name = "totalRecovered")
    public Integer totalRecovered;
    @ColumnInfo(name = "totalNewCases")
    public Integer totalNewCases;
    @ColumnInfo(name = "totalNewDeaths")
    public Integer totalNewDeaths;
    @ColumnInfo(name = "totalActiveCases")
    public Integer totalActiveCases;
    @ColumnInfo(name = "totalCasesPerMillionPop")
    public Integer totalCasesPerMillionPop;
    @ColumnInfo(name = "created")
    public String created;
}
