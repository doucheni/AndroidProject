package fr.eseo.example.androidproject.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;

public interface JuryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Jury jury);

}
