package fr.eseo.example.androidproject.api;

import java.io.Serializable;

public class ProjectModel implements Serializable {

    private int projectId;
    private String projectTitle;
    private String projectDescription;
    private Boolean projectPoster;
    private String projectPosterString;
    private int confidentiality;
    private String supervisor;

    public ProjectModel(int projectId, String projectTitle, String projectDescription, Boolean projectPoster, int confidentiality, String supervisor) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.projectPoster = projectPoster;
        this.confidentiality = confidentiality;
        this.supervisor = supervisor;
    }

    public ProjectModel(){

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

    public Boolean getProjectPoster() {
        return projectPoster;
    }

    public void setProjectPoster(Boolean projectPoster) {
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

    /**
     * Getter of project's poster (String format)
     * @return projectPosterString the poster in String format
     */
    public String getProjectPosterString() {
        return projectPosterString;
    }

    /**
     * Setter of project's poster (String format)
     * @param projectPosterString the new poster in String format
     */
    public void setProjectPosterString(String projectPosterString) {
        this.projectPosterString = projectPosterString;
    }
}
