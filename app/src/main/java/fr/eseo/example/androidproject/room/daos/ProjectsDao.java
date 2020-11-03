package fr.eseo.example.androidproject.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import fr.eseo.example.androidproject.room.entities.Project;

@Dao
public interface ProjectsDao {
    /*
    @Transaction
    @Query("SELECT * FROM Projects, Users WHERE Projects.members = Users.userId")
    public LiveData<List<isMember>> findAllMembersOfTheProject();

    @Query("SELECT * FROM Projects WHERE Projects.jury = :idjury")
    public LiveData<Project> getProjectsOfJury(int idjury);
    */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Project project);

    @Update
    void update(Project project);
}
