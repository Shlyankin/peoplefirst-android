package rokolabs.com.peoplefirst.model;

import java.util.ArrayList;

public class WitnessTestimony extends Testimony {

    public Boolean location_confirmation = false;
    public Boolean harassment_confirmation = false;
    public Boolean documentation_experienced = false;
    public String details;
    public User victim;
    public User aggressor;
    public boolean anonym;
    public int user_id;

    public Boolean happened_before;
    public ArrayList<HarassmentType> harassment_types = new ArrayList<>();
    public ArrayList<HarassmentReason> harassment_reasons = new ArrayList<>();


    public RetailWitnessTestimony convertToRetail() {
        RetailWitnessTestimony rwr = new RetailWitnessTestimony();
        rwr.location_confirmation = location_confirmation;
        rwr.harassment_confirmation = harassment_confirmation;
        rwr.documentation_experienced = documentation_experienced;
        rwr.details = details;
        rwr.victim = victim.convertToRetail();
        rwr.aggressor = aggressor.convertToRetail();
        rwr.anonym = anonym;
        rwr.user_id = user_id;

        rwr.happened_before = happened_before;
        rwr.harassment_types = harassment_types;
        rwr.harassment_reasons = harassment_reasons;


        return rwr;
    }

}
