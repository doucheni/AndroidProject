package fr.eseo.example.androidproject.room.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "Projects",
        foreignKeys = {@ForeignKey(entity = User.class, childColumns = "members", parentColumns = "userId"),
        @ForeignKey(entity = Jury.class, childColumns = "jury", parentColumns = "idJury")})
public class Project {
    @PrimaryKey
    @ColumnInfo(name = "projectId")
    private int projectId;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="description")
    private String descrip;
    @ColumnInfo(name="poster")
    private String poster;
    @ColumnInfo(name="confid")
    private int confid;
    @ColumnInfo(name="supervisor")
    private String supervisor;
    @ColumnInfo(name="jury")
    private int jury;


    @ColumnInfo(name="members")
    private int membersId;


    public Project(int projectId, String title, String descrip, String poster, int confid, String supervisor, int jury, int membersId){
        this.projectId = projectId;
        this.title = title;
        this.descrip = descrip;
        this.poster = poster;
        this.confid = confid;
        this.supervisor = supervisor;
        this.jury = jury;
        this.membersId = membersId;

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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
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

    public int getJury() {
        return jury;
    }

    public void setJury(int jury) {
        this.jury = jury;
    }
    public int getMembersId() {
        return membersId;
    }

    public void setMembersId(int membersId) {
        this.membersId = membersId;
    }
}
