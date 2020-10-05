package fr.eseo.example.androidproject.room.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class isMember {
    @Embedded
    private Project project;

    @Relation(parentColumn = "members", entityColumn = "userId", entity = User.class)
    private List<User> users;

    public isMember(Project project, List<User> users) {
        this.project = project;
        this.users = users;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
