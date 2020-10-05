package fr.eseo.example.androidproject.room.entities;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="userId")
    private int userId;
    private String username;
    private String forename;
    private String surname;


    public User(int userId,String username, String forename, String surname){
        this.userId = userId;
        this.username = username;
        this.forename = forename;
        this.surname = surname;

    }

    public String getUsername(){ return username; }

    public void  setUsername(){  this.username = username; }


}
