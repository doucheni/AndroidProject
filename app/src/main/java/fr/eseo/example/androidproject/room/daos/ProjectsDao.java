package fr.eseo.example.androidproject.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.isMember;

@Dao
public interface ProjectsDao {

    @Transaction
    @Query("SELECT * FROM Projects, Users WHERE Projects.members = Users.userId")
    public LiveData<List<isMember>> findAllMembersOfTheProject();

    @Query("SELECT * FROM Projects WHERE Projects.jury = :idjury")
    public LiveData<Project> getProjectsOfJury(int idjury);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Project project);

    @Update
    public void update(Project project);
}
