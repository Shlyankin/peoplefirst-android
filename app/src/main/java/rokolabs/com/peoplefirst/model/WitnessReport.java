package rokolabs.com.peoplefirst.model;

import java.util.ArrayList;

public class WitnessReport {

    public int id;
    public Boolean location_confirmation = false;
    public Boolean victim_confirmation = false;
    public Boolean aggressor_confirmation = false;
    public String details;
    public User victim;
    public User aggressor;
    public boolean anonym;
    public int user_id;

    public Boolean happened_before;
    public ArrayList<HarassmentType> harassment_types;
    public ArrayList<HarassmentReason> harassment_reasons;

    public RetailWitnessReport convertToRetail() {
        RetailWitnessReport rwr = new RetailWitnessReport();
        rwr.id = id;
        rwr.location_confirmation = location_confirmation;
        rwr.victim_confirmation = victim_confirmation;
        rwr.aggressor_confirmation = aggressor_confirmation;
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
