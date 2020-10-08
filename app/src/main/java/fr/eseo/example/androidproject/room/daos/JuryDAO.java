package fr.eseo.example.androidproject.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;

@Dao
public interface JuryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Jury jury);



}
