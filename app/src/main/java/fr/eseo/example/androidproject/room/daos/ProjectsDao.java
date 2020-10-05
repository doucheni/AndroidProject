package fr.eseo.example.androidproject.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.isMember;

public interface ProjectsDao {
    @Transaction
    @Query("SELECT * FROM Projects, Users WHERE Projects.members = Users.userId")
    public LiveData<List<isMember>> findAllMembersOfTheProject();

    @Update
    public void update(Project project);
}
