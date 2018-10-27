package illeagle99.syllabuspal.fundamental;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.GraphSettings;

/**
 * Created by kules on 8/19/2016.
 */
public class Course {
    private long from, to;
    private ArrayList<Assignment> assignments;
    private boolean isSelected;
    private GraphSettings gSettings;
    private Assignment selected;
    private String name;

    /* for user input */
    public Course(){
        from = 0;
        to = 0;
        assignments = new ArrayList<Assignment>();
        isSelected = false;
        gSettings = new GraphSettings(this);
        selected = null;
        name = "New Course";
    }
    public void from(long fro){
        from = fro;
    }
    public void from(Date fro){
        from = DateUtility.dateToLong(fro);
    }
    public void to(long t){
        to = t;
    }
    public void to(Date t){
        to = DateUtility.dateToLong(t);
    }
    public void isSelected(boolean b){
        isSelected = b;
    }
    public void graphSettings(GraphSettings gs){
        gSettings = gs;
    }
    public void select(Assignment sel){ selected = sel; }
    public GraphSettings graphSettings(){ return gSettings; }
    public boolean isSelected(){ return isSelected; }
    public long to(){ return to; }
    public long from(){ return from; }
    public Assignment selected(){ return selected; }
    public String name(){ return name; }
    public void name(String nm){ name = nm; }
    public boolean set(int index,Assignment ass){
        boolean doesNotExist = nameCheck(ass.name());
        if(doesNotExist) assignments.add(index, ass);
        return doesNotExist;
    } /* stops list from changing order, damn annoying */
    public int positionOf(String name){
        for(int x = 0; x < length(); x++){
            if(get(x).name().equals(name)) return x;
        }
        return -1;
    }
    public int positionOf(Assignment ass){
        for(int x = 0; x < length(); x++){
            if(get(x).name().equals(ass.name())) return x;
        }
        return -1;
    }

    public int length(){ return assignments.size(); }
    public Assignment get(int x){ return assignments.get(x); }
    public Assignment get(String name){
        for(Assignment ass : assignments) if(ass.name().equals(name)) return ass;
        return null;
    }
    public void remove(int x){ assignments.remove(x); }
    public void remove(String name){
        for(int x = assignments.size() - 1; x >= 0; x--)
            if(assignments.get(x).name().equals(name)) remove (x);
    }
    public boolean add(Assignment ass) {
        if (nameCheck(ass.name())) {
            assignments.add(ass);
            return true;
        }
        return false;
    }

    /*checks new ass's name against other ass' names */
    public boolean nameCheck(String name){
        for(int x = 0; x < assignments.size(); x++)
            if(name.equals(get(x).name()))
                return false;
        return true;
    }

    /* for json initialization */
    public Course(JSONObject courseJSON){
        try {
            name(courseJSON.getString("name"));
            to(courseJSON.getLong("to"));
            from(courseJSON.getLong("from"));
            isSelected(false);
            graphSettings(new GraphSettings(this));
            assignments = new ArrayList<Assignment>();

            JSONArray assARRAY = courseJSON.getJSONArray("assignments");
            for(int x = 0; x < assARRAY.length(); x++) {
                Assignment assignment = new Assignment(assARRAY.getJSONObject(x));
                assignments.add(assignment);
                assignment.parent(this);
            }

            selected = null;
        }catch(Exception e){
            System.out.print("course--constructor");
            e.printStackTrace();
        }
    }
    public String json(){
        try{
            JSONObject json = new JSONObject();
            json.put("name",name);
            json.put("to",to);
            json.put("from",from);

            JSONArray assJSON = new JSONArray();
            for(int x = 0; x < assignments.size(); x++) assJSON.put(new JSONObject(assignments.get(x).json()));
            json.put("assignments",assJSON);
            return json.toString();
        }catch(Exception e){
            System.out.println("course--json");
            e.printStackTrace();
            return null;
        }
    }

    public int status(){
        int status = -1;
        int ns = gSettings.count(Assignment.NOT_STARTED);
        int ip = gSettings.count(Assignment.IN_PROGRESS);
        int c = gSettings.count(Assignment.COMPLETE);
        int ruoot = gSettings.count(Assignment.RUN_OUT_OF_TIME);
        int raoot = gSettings.count(Assignment.RAN_OUT_OF_TIME);
        int total = gSettings.total();

        if(ip == 0 && c == 0 && ruoot == 0) status =  Assignment.NOT_STARTED;
        if(ip >= 1 || (c >= 1 && c < total)) status = Assignment.IN_PROGRESS;
        if(c == total) status = Assignment.COMPLETE;

        status = reviewStatus(status);

        return status;
    }

    private int reviewStatus(int status){
        if(status == Assignment.COMPLETE) return Assignment.COMPLETE;
        long timeLost = System.currentTimeMillis() - from, totalTime = to - from;
        if(timeLost >= totalTime){
            return Assignment.RAN_OUT_OF_TIME;
        }
        if(timeLost >= totalTime/2){
            return Assignment.RUN_OUT_OF_TIME;
        }
        return status;
    }
}
