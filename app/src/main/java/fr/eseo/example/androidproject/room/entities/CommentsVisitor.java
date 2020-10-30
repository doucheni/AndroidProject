package fr.eseo.example.androidproject.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CommentsVisitor {

    @PrimaryKey(autoGenerate = true)
    public int comment_id;

    @ColumnInfo(name= "project_id")
    public int project_id;

    @ColumnInfo(name = "comment")
    public String comment;

    public CommentsVisitor(){

    }

    public CommentsVisitor(int project_id, String comment){
        this.project_id = project_id;
        this.comment = comment;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
