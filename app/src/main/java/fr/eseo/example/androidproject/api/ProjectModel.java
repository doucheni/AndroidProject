package fr.eseo.example.androidproject.api;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ProjectModel implements Serializable {

    private int projectId;
    private String projectTitle;
    private String projectDescription;
    private String projectPoster;
    private int confidentiality;
    private String supervisor;

    public ProjectModel(int projectId, String projectTitle, String projectDescription, String projectPoster, int confidentiality, String supervisor) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.projectPoster = projectPoster;
        this.confidentiality = confidentiality;
        this.supervisor = supervisor;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectPoster() {
        return projectPoster;
    }

    public void setProjectPoster(String projectPoster) {
        this.projectPoster = projectPoster;
    }

    public int getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(int confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
