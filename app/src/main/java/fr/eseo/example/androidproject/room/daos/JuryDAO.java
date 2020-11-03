package fr.eseo.example.androidproject.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import fr.eseo.example.androidproject.room.entities.Jury;

@Dao
public interface JuryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Jury jury);



}
