package rokolabs.com.peoplefirst.model;

import java.util.ArrayList;

public class VictimTestimony extends Testimony {

    public Boolean location_confirmation;
    public Boolean harassment_confirmation;
    public Boolean documentation_experienced;
    public User aggressor;
    public ArrayList<User> witnesses = new ArrayList<>();
    public ArrayList<HarassmentType> harassment_types = new ArrayList<>();
    public ArrayList<HarassmentReason> harassment_reasons = new ArrayList<>();
    public Boolean happened_before;
    public String victim_proposed_solution;
    public String details;

}
