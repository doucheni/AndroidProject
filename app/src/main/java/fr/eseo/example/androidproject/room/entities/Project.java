package fr.eseo.example.androidproject.room.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "Projects",
        foreignKeys = {@ForeignKey(entity = Supervisor.class, childColumns = "supervisor_forename", parentColumns = "forename"),
        @ForeignKey(entity= Supervisor.class, childColumns = "supervisor_surname", parentColumns = "surname"),
        @ForeignKey(entity = Students.class, childColumns = "student_id", parentColumns = "studentId"),
        @ForeignKey(entity = Students.class, childColumns = "student_forename", parentColumns = "studentForename"),
        @ForeignKey(entity = Students.class, childColumns = "student_surname",parentColumns = "studentSurname") },
        indices = {@Index(value= "supervisor_forename"), @Index(value = "supervisor_surname"),@Index(value="student_id"),
        @Index(value = "student_forename"), @Index(value="studentSurname")})
public class Project {
    @PrimaryKey(autoGenerate = true)
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
    @ColumnInfo (name="supervisor_forename")
    private String supervisorForename;
    @ColumnInfo(name="supervisor_surname")
    private String supervisorSurname;
    @ColumnInfo(name="student_id")
    private int studentId;
    @ColumnInfo(name="student_forename")
    private String studentForename;
    @ColumnInfo(name="student_surname")
    private String studentSurname;

    public Project(int projectId, String title, String descrip, String poster, int confid, String supervisorForename, String supervisorSurname,
                   int studentId, String studentForename, String studentSurname){
        this.projectId = projectId;
        this.title = title;
        this.descrip = descrip;
        this.poster = poster;
        this.confid = confid;
        this.supervisorForename = supervisorForename;
        this.supervisorSurname = supervisorSurname;
        this.studentId = studentId;
        this.studentForename = studentForename;
        this.studentSurname = studentSurname;
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

    public String getSupervisorForename() {
        return supervisorForename;
    }

    public void setSupervisorForename(String supervisorForename) {
        this.supervisorForename = supervisorForename;
    }

    public String getSupervisorSurname() {
        return supervisorSurname;
    }

    public void setSupervisorSurname(String supervisorSurname) {
        this.supervisorSurname = supervisorSurname;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentForename() {
        return studentForename;
    }

    public void setStudentForename(String studentForename) {
        this.studentForename = studentForename;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }
}
