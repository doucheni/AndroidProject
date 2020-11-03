package fr.eseo.example.androidproject.room.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MarksStudents {

    @PrimaryKey(autoGenerate = true)
    public int mark_id;


    @ColumnInfo(name = "student_id")
    public int student_id;

    @ColumnInfo(name = "mark")
    public int mark;

    public MarksStudents(){

    }

    public MarksStudents(int student_id, int mark){
        this.student_id = student_id;
        this.mark = mark;
    }


    public int getStudent_id(){ return student_id; }

    public void setStudent_id(int student_id){ this.student_id = student_id; }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
