package fr.eseo.example.androidproject.room.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MarksJury {

    @PrimaryKey(autoGenerate = true)
    public int mark_id;

    @ColumnInfo(name = "project_id")
    public int project_id;

    @ColumnInfo(name = "mark")
    public int mark;

    public MarksJury(){

    }

    public MarksJury(int project_id, int mark){
        this.project_id = project_id;
        this.mark = mark;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
