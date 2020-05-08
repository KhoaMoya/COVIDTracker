package com.khoa.covidtracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class News {
    @ColumnInfo(name = "nid")
    public Integer nid;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "content")
    public String content;
    @ColumnInfo(name = "author")
    public String author;
    @ColumnInfo(name = "url")
    public String url;
    @ColumnInfo(name = "urlToImage")
    public String urlToImage;
    @ColumnInfo(name = "publishedAt")
    public String publishedAt;
    @ColumnInfo(name = "addedOn")
    public String addedOn;
    @ColumnInfo(name = "siteName")
    public String siteName;
    @ColumnInfo(name = "language")
    public String language;
    @ColumnInfo(name = "countryCode")
    public String countryCode;
    @ColumnInfo(name = "status")
    public Integer status;

}
