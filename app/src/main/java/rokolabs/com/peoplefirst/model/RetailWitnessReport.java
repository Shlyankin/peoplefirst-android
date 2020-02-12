package rokolabs.com.peoplefirst.model;

import java.util.ArrayList;

public class RetailWitnessReport {

    public int id;
    public Boolean location_confirmation = false;
    public Boolean victim_confirmation = false;
    public Boolean aggressor_confirmation = false;
    public String details;
    public RetailUser victim;
    public RetailUser aggressor;
    public boolean anonym;
    public int user_id;

    public Boolean happened_before;
    public ArrayList<HarassmentType> harassment_types;
    public ArrayList<HarassmentReason> harassment_reasons;

}
