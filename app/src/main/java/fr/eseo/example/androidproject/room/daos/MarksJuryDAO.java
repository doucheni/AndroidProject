package fr.eseo.example.androidproject.room.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.MarksJury;

@Dao
public interface MarksJuryDAO {
    @Query("SELECT * FROM marksjury")
    List<MarksJury> getAllMarks();

    @Query("SELECT * FROM marksjury WHERE project_id = :project_id")
    List<MarksJury> getMarks(int project_id);

    @Insert
    void insertAll(MarksJury...marksJuries);
}
