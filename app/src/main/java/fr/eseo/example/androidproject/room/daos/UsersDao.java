package fr.eseo.example.androidproject.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.eseo.example.androidproject.room.entities.User;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM Users")
    public LiveData<List<User>> findAllUsers();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(User user);
}
