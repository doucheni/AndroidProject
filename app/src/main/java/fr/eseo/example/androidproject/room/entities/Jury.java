package fr.eseo.example.androidproject.room.entities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "Jury")
public class Jury implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idJury")
    private int idJury;

    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

    public Jury(){

    }

    public Jury(int idJury, String dateString){
        this.idJury = idJury;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = formatter.parse(dateString);
        }catch (ParseException e){
            e.printStackTrace();
        }
        this.date = date;
        // Get String from date : formatter.format(date)

    }

    public int getIdJury() {
        return idJury;
    }

    public void setIdJury(int idJury) {
        this.idJury = idJury;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }
}
