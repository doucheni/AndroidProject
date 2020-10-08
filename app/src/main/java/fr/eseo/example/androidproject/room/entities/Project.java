package fr.eseo.example.androidproject.room.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "Projects")
public class Project implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "projectId")
    private int projectId;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="description")
    private String descrip;
    @ColumnInfo(name="poster")
    private Boolean poster;
    @ColumnInfo(name="confid")
    private int confid;
    @ColumnInfo(name="supervisor")
    private String supervisor;


    public Project(int projectId, String title, String descrip, Boolean poster, int confid, String supervisor){
        this.projectId = projectId;
        this.title = title;
        this.descrip = descrip;
        this.poster = poster;
        this.confid = confid;
        this.supervisor = supervisor;
    }

    public int getProjectId(){ return projectId;}
    public void setProjectId(int projectId){ this.projectId = projectId;}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public Boolean getPoster() {
        return poster;
    }

    public void setPoster(Boolean poster) {
        this.poster = poster;
    }

    public int getConfid() {
        return confid;
    }

    public void setConfid(int confid) {
        this.confid = confid;
    }
    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

}
