package rokolabs.com.peoplefirst.model;

import java.io.File;
import java.util.ArrayList;

public class Report {

    public final static Report EMPTY = new Report();


    public String datetime;
    public String created_at;
    public String updated_at;
    public String submitted_at;
    public Integer id=null;
    public int author_id;
    public User victim;
    public ArrayList<User> aggressors = new ArrayList<>();
    public ArrayList<User> witnesses = new ArrayList<>();
    public ArrayList<HarassmentType> harassment_types = new ArrayList<>();
    public ArrayList<HarassmentReason> harassment_reasons = new ArrayList<>();
    public int counselling_service_id;
    public String location_city="";
    public String location_details="";
    public String details;
    public String file;
    public String status;
    public String victim_proposed_solution;
    public int resolutions_offered;
    public String status_changed_at;
    public Boolean happened_before = null;
    public boolean anonym;
    public ArrayList<File> attachments = new ArrayList<>();
    public int days_left;


    public RetailReport convertToRetail() {
        RetailReport rr = new RetailReport();
        rr.datetime = datetime;
        rr.created_at = created_at;
        rr.updated_at = updated_at;
        rr.submitted_at = submitted_at;
        rr.id = id;
        rr.author_id = author_id;
        if (victim != null)
            rr.victim = victim.convertToRetail();
        for (User aggressor : aggressors)
            rr.aggressors.add(aggressor.convertToRetail());
        for (User witness : witnesses)
            rr.witnesses.add(witness.convertToRetail());
        rr.harassment_types = harassment_types;
        rr.harassment_reasons = harassment_reasons;
        rr.counselling_service_id = counselling_service_id;
        rr.location_city = location_city;
        rr.location_details = location_details;
        rr.details = details;
        rr.file = file;
        rr.status = status;
        rr.victim_proposed_solution = victim_proposed_solution;
        rr.resolutions_offered = resolutions_offered;
        rr.status_changed_at = status_changed_at;
        rr.happened_before = happened_before;
        rr.anonym = anonym;
        rr.attachments = attachments;
        rr.days_left = days_left;
        return rr;
    }

}
