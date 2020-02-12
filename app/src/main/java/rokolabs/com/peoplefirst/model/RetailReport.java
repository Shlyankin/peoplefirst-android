package rokolabs.com.peoplefirst.model;

import java.io.File;
import java.util.ArrayList;

public class RetailReport {

    public final static RetailReport EMPTY = new RetailReport();


    public String datetime;
    public String created_at;
    public String updated_at;
    public String submitted_at;
    public Integer id;
    public int author_id;
    public RetailUser victim;
    public ArrayList<RetailUser> aggressors = new ArrayList<>();
    public ArrayList<RetailUser> witnesses = new ArrayList<>();
    public ArrayList<HarassmentType> harassment_types = new ArrayList<>();
    public ArrayList<HarassmentReason> harassment_reasons = new ArrayList<>();
    public int counselling_service_id;
    public String location_city;
    public String location_details;
    public String details;
    public String file;
    public String status;
    public String victim_proposed_solution;
    public int resolutions_offered;
    public String status_changed_at;
    public Boolean happened_before = null;
    public boolean anonym;
    public int days_left;


    public ArrayList<File> attachments = new ArrayList<>();
}
