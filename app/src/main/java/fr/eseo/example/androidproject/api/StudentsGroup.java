package fr.eseo.example.androidproject.api;

import java.io.Serializable;
import java.util.List;

public class StudentsGroup implements Serializable {

    private int idProject;
    private List<UserModel> members;

    public StudentsGroup(int idProject, List<UserModel> members) {
        this.idProject = idProject;
        this.members = members;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }
}
