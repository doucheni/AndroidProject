package fr.eseo.example.androidproject.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Jury")
public class Jury {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idJury")
    private int idJury;

    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

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
