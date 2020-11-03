package fr.eseo.example.androidproject.room.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.MarksStudents;

@Dao
public interface MarksStudentDAO {

    @Query("SELECT * FROM marksstudents")
    List<MarksStudents> getAllMarks();

    @Query("SELECT * FROM marksstudents WHERE student_id =:student_id")
    List<MarksStudents> getMarks(int student_id);

    @Query("UPDATE marksstudents SET mark = :mark WHERE student_id = :student_id")
    int updateMark(int mark, int student_id);
    @Insert
    void insertAll(MarksStudents...marksStudents);
}
