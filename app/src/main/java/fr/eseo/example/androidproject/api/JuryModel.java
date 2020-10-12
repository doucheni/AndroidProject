package fr.eseo.example.androidproject.api;

import java.io.Serializable;
import java.util.List;
import fr.eseo.example.androidproject.room.entities.User;

public class JuryModel implements Serializable {

    private int juryId;
    private String date;
    private List<UserModel> members;

    public JuryModel(int juryId, String date, List<UserModel> members) {
        this.juryId = juryId;
        this.date = date;
        this.members = members;
    }

    public int getJuryId() {
        return juryId;
    }

    public void setJuryId(int juryId) {
        this.juryId = juryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }
}
