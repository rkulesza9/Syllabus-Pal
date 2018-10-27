package illeagle99.syllabuspal.fundamental;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.NotificationSettings;

/**
 * Created by kules on 8/19/2016.
 */
public class Assignment {
    private String name;
    private int status;
    private long from, to;
    private Course parent;
    private boolean isSelected;
    private NotificationSettings settings;

    public Assignment(){
        parent(null);
        name("new assignment");
        status(0);
        from(System.currentTimeMillis());
        to(System.currentTimeMillis()+1000*60*60*60*24*7);
        select(false);
        nSettings(null);

    }

    public static final int NOT_STARTED = 0, IN_PROGRESS = 1, COMPLETE = 2, RUN_OUT_OF_TIME = 3, RAN_OUT_OF_TIME = 4;

    public Course parent(){
        return parent;
    }
    public String name(){
        return name;
    }
    public int status(){
        return status;
    }
    public boolean isSelected(){
        return isSelected;
    }
    public long from(){
        return from;
    }
    public long to(){
        return to;
    }
    public NotificationSettings nSettings(){
        return settings;
    }
    public int realStatus(){
        if(status == COMPLETE) return COMPLETE;
        long timeLost = System.currentTimeMillis() - from, totalTime = to - from;
        if(timeLost >= totalTime){
            return RAN_OUT_OF_TIME;
        }
        if(timeLost >= totalTime/2){
            return RUN_OUT_OF_TIME;
        }
        return status;
    }

    /* json -> code */
    public Assignment(JSONObject obj){
        parent(null); /* set externally */
        try {
            name(obj.getString("name"));
            status(obj.getInt("status"));
            from(obj.getLong("from"));
            to(obj.getLong("to"));
            nSettings(new NotificationSettings(obj.getJSONArray("nsettings")));
        }catch(Exception e){
            System.out.println("ass--constructor");
            e.printStackTrace();
        }
    }
    public void select(boolean b){
        isSelected = b;
    }
    public void parent(Course c){
        parent = c;
    }
    public void nSettings(NotificationSettings ns){
        settings = ns;
    }
    public void to(long t){
        to = t;
    }
    public void from(long fro){
        from = fro;
    }
    public void status(int st){
        status = st;
    }
    public void status(String st){ /* may be useful for editing assignment from user-interface*/
        if(st.equalsIgnoreCase("not started")) status = NOT_STARTED;
        if(st.equalsIgnoreCase("in progress")) status = IN_PROGRESS;
        if(st.equalsIgnoreCase("complete")) status = COMPLETE;
    }
    public void name(String str){
        name = str;
    }

    public String json(){
        try{
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("status", status);
            json.put("from", from);
            json.put("to", to);
            json.put("nsettings", new JSONArray(settings.json()));

            return json.toString();
        }catch(Exception e){
            System.out.print("ass--json");
            e.printStackTrace();
            return null;
        }
    }

    /* user input -> assignment */
    public void to(Date t){
        to = DateUtility.dateToLong(t);
    }
    public void from(Date fro){
        from = DateUtility.dateToLong(fro);
    }
}
