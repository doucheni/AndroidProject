package fr.eseo.example.androidproject.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.CommentsVisitor;

@Dao
public interface CommentsVisitorDAO {

    @Query("SELECT * FROM commentsvisitor")
    List<CommentsVisitor> getAll();

    @Query("SELECT * FROM commentsvisitor WHERE project_id = :project_id")
    List<CommentsVisitor> getComments(int project_id);

    @Insert
    void insertAll(CommentsVisitor... commentsVisitors);

}
