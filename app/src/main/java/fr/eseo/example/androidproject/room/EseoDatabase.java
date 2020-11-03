package fr.eseo.example.androidproject.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.eseo.example.androidproject.room.daos.CommentsVisitorDAO;
import fr.eseo.example.androidproject.room.daos.JuryDAO;
import fr.eseo.example.androidproject.room.daos.MarksJuryDAO;
import fr.eseo.example.androidproject.room.daos.MarksStudentDAO;
import fr.eseo.example.androidproject.room.daos.MarksVisitorDAO;
import fr.eseo.example.androidproject.room.daos.ProjectsDao;
import fr.eseo.example.androidproject.room.daos.UsersDao;
import fr.eseo.example.androidproject.room.entities.CommentsVisitor;
import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.MarksJury;
import fr.eseo.example.androidproject.room.entities.MarksStudents;
import fr.eseo.example.androidproject.room.entities.MarksVisitor;
import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.User;
import fr.eseo.example.androidproject.room.entities.isMember;

@Database(entities = {Jury.class, Project.class, User.class, MarksVisitor.class, CommentsVisitor.class, MarksJury.class, MarksStudents.class}, version = 5, exportSchema = true)
@TypeConverters(value = {DataConverters.Date.class})
public abstract class EseoDatabase extends RoomDatabase {
    
    private static EseoDatabase INSTANCE;

    public abstract ProjectsDao ProjectsDao();

    public abstract UsersDao UsersDao();

    public abstract JuryDAO JuryDao();

    public abstract MarksVisitorDAO marksVisitorDAO();


    public abstract CommentsVisitorDAO commentsVisitorDAO();

    public abstract MarksJuryDAO marksJuryDAO();

    public abstract MarksStudentDAO marksStudentDAO();

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
