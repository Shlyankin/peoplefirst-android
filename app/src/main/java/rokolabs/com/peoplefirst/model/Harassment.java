package rokolabs.com.peoplefirst.model;

import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;

public class Harassment {

    public ArrayList<Integer> types = new ArrayList<>();
    public String whatHappened;
    public ArrayList<Person> beingHarassed = new ArrayList<>();
    public ArrayList<Person> agressors = new ArrayList<>();
    public ArrayList<Person> witnesses = new ArrayList<>();
    public String location;

    public Spanned toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append("Who was harassed?").append("</b>").append("<br/>");
        for (Person person : beingHarassed) {
            sb.append(person.first_name).append(" ").append(person.last_name).append("<br/>");
        }
        sb.append("<b>").append("Who was the agressor?").append("</b>").append("<br/>");
        for (Person person : agressors) {
            sb.append(person.first_name).append(" ").append(person.last_name).append("<br/>");
        }
        sb.append("<b>").append("Where did this take place?").append("</b>").append("<br/>");
        sb.append(location).append("<br/>");
        return Html.fromHtml(sb.toString());
    }
}
