package fr.eseo.example.androidproject.room.entities;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Users")
public class User{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="userId")
    private int userId;
    //private String username;
    private String forename;
    private String surname;

    /*
    public User(int userId,String username, String forename, String surname){
        this.userId = userId;
        this.username = username;
        this.forename = forename;
        this.surname = surname;
    }
    */
    public User(int userId, String forename, String surname){
        this.userId = userId;
        this.forename = forename;
        this.surname = surname;
    }

    /*
    public String getUsername(){ return username; }

    public void  setUsername(){  this.username = username; }
    */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


}
