package com.example.diary.dbhelper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diary.entities.DiaryEntity;

import java.util.List;

@Dao
public interface DiaryDAO {
    @Query("SELECT Id, CreatedDate,Title,Content FROM DiaryEntity ORDER BY Id DESC")
    List<DiaryEntity> GetAll();

    @Query("SELECT Id, CreatedDate, Title, Content FROM DiaryEntity WHERE Id = :paramId")
    DiaryEntity GetSingleDiary(int paramId);

    @Insert
   public void InsertDiary(DiaryEntity diary);

    @Update
    void UpdateDiary(DiaryEntity diary);

    @Delete
    void DeleteDiary(DiaryEntity diary);
}
