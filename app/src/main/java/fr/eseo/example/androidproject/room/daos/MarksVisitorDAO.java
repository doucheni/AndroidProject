package fr.eseo.example.androidproject.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.MarksVisitor;

@Dao
public interface MarksVisitorDAO {

    @Query("SELECT * FROM marksvisitor")
    List<MarksVisitor> getAllMarks();

    @Query("SELECT * FROM marksvisitor WHERE project_id = :project_id")
    List<MarksVisitor> getMarks(int project_id);

    @Insert
    void insertAll(MarksVisitor...marksVisitors);

}
