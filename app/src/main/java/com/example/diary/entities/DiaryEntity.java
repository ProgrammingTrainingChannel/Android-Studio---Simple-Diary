package com.example.diary.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.diary.dbhelper.DateConverter;

import java.util.Date;

@Entity
public class DiaryEntity {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "CreatedDate")
    @TypeConverters(DateConverter.class)
    public Date CreatedDate;

    @ColumnInfo(name = "Title")
    public String Title;

    @ColumnInfo(name = "Content")
    public String Content;
}
