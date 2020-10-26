package fr.eseo.example.androidproject.api;

import java.io.Serializable;
import java.util.List;

/**
 * Class Model of a Pseudo-Jury
 * A pseudo-jury contain a list of user (=visitor) who are not registered in the database
 * A pseudo-jury contain a list of project selected by a member of the communication service
 */
public class PseudoJuryModel implements Serializable {

    // Per default the name of the visitor is : "visitor_0", "visitor_1", ...
    private static final String DEFAULT_NAME = "visitor_";

    // The list of visitors in the pseudo-jury
    private List<String> visitors;
    // The list of projects selected by the communication service
    private List<ProjectModel> projects;

    /**
     * Constructor
     * @param projectsSelected list of project selected by communication service
     */
    public PseudoJuryModel(List<ProjectModel> projectsSelected){
        this.setProjects(projectsSelected);
    }

    /**
     * Add a new visitor to the list of visitor
     */
    public void addNewVisitor(){
        this.visitors.add(DEFAULT_NAME + this.visitors.size());
    }

    /**
     * Getter for the list of visitors
     * @return visitors the liste of visitors
     */
    public List<String> getVisitors() {
        return visitors;
    }

    /**
     * Setter for the list of visitors
     * @param visitors the new list of visitors
     */
    public void setVisitors(List<String> visitors) {
        this.visitors = visitors;
    }

    /**
     * Getter of the list of projects
     * @return the list of projects
     */
    public List<ProjectModel> getProjects() {
        return projects;
    }

    /**
     * Setter of the list of projects
     * @param projects the new list of projects
     */
    public void setProjects(List<ProjectModel> projects) {
        this.projects = projects;
    }
}
