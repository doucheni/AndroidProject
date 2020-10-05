package fr.eseo.example.androidproject.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.eseo.example.androidproject.room.daos.ProjectsDao;
import fr.eseo.example.androidproject.room.daos.UsersDao;
import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.User;
import fr.eseo.example.androidproject.room.entities.isMember;

@Database(entities = {isMember.class, Jury.class, Project.class, User.class}, version = 1, exportSchema = true)
@TypeConverters(value = {DataConverters.Date.class})
public abstract class EseoDatabase extends RoomDatabase {
    
    private static EseoDatabase INSTANCE;

    public abstract ProjectsDao ProjectsDao();

    public abstract UsersDao UsersDao();

    public static EseoDatabase getDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, EseoDatabase.class, "eseo.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
