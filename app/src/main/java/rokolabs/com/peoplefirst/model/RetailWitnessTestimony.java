package rokolabs.com.peoplefirst.model;

import java.util.ArrayList;

public class RetailWitnessTestimony extends Testimony {

    public Boolean location_confirmation = false;
    public Boolean harassment_confirmation = false;
    public Boolean documentation_experienced = false;
    public String details;
    public RetailUser victim;
    public RetailUser aggressor;
    public boolean anonym;
    public int user_id;

    public Boolean happened_before;
    public ArrayList<HarassmentType> harassment_types = new ArrayList<>();
    public ArrayList<HarassmentReason> harassment_reasons = new ArrayList<>();



}
