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
    @PrimaryKey
    @NonNull
    private String username;
    private String forename;
    private String surname;
    private int idRole;
    private String descr;

    public User(String username, String forename, String surname, int idRole, String descr){
        this.username = username;
        this.forename = forename;
        this.surname = surname;
        this.idRole = idRole;
        this.descr = descr;
    }

    public String getUsername(){ return username; }

    public void  setUsername(){  this.username = username; }
}
