package illeagle99.syllabuspal.fundamental.secondary;

import org.json.JSONArray;

import java.util.Date;
import java.util.GregorianCalendar;

import illeagle99.syllabuspal.fundamental.Assignment;

/**
 * Created by kules on 8/19/2016.
 */
public class NotificationSettings {
    private long from, to, frequency, dt, fbase;
    public static final long NONE = 0, MINUTES = 1000*60, HOURS = MINUTES*60, DAYS = HOURS*24, WEEKS = DAYS*7;

    /* will be stored as longs in json, initializer/methods here
    are appropriate for that extraction.
     */
    public NotificationSettings(){
        from = 0;
        to = 0;
        frequency = 0;
        dt = 0;
        fbase = 0;
    }

    public long getDt(){
        return dt;
    }

    public NotificationSettings(JSONArray nsARRAY){
        try{
            from(nsARRAY.getLong(0));
            to(nsARRAY.getLong(1));
            frequency(nsARRAY.getLong(2));
            fbase = nsARRAY.getLong(3);
        }catch(Exception e){
            System.out.println("nsettings--constructor");
            e.printStackTrace();
        }
    }

    public void frequency(long freq){
        frequency = freq;
    }
    public long frequency(){
        return frequency;
    }
    public void from(long fro){
        from = fro;
        dt = to - from;
    }
    public long from(){
        return from;
    }
    public void to(long t){
        to = t;
        dt = to - from;
    }
    public long to(){
        return to;
    }
    public void setFreqBase(long l){ fbase = l; }
    public long getFreqBase(){ return fbase; }

    /* this might make it more useful for user-input
    * user input alternatives to above methods*/
    public NotificationSettings(Date from, Date to,long frequency, long fbase){
        from(DateUtility.dateToLong(from));
        to(DateUtility.dateToLong(to));
        frequency(frequency);
        this.fbase = fbase;
    }
    public void from(Date fro){
        from = DateUtility.dateToLong(fro);
        dt = to - from;
    }
    public void to(Date t){
        to = DateUtility.dateToLong(t);
        dt = to - from;
    }


    /* For notificationHandler -- whatever thread handles the notfications
        none -> doesn't work
        program won't save fbase/frequency for some reason*/
    public boolean timeToNotify(){
        long now = System.currentTimeMillis();
        if(from <= now){
            if(to >= now){
                if(frequency < DAYS){
                    from = now + frequency;
                } else {
                    from = now + frequency;
                    to += frequency;
                }
                return true;
            } else {
                from += DAYS - dt;
                to += DAYS;
                return true;
            }
        }
        return false;
    }

    /*this will make writing to syllabus.JSON a breeze */
    public String json(){
        JSONArray jsonARR = new JSONArray();
        jsonARR.put(from);
        jsonARR.put(to);
        jsonARR.put(frequency);
        jsonARR.put(fbase);
        return jsonARR.toString();
    }

    public static String frequencyAsStr(long freq){
        if(freq == MINUTES) return "MINUTES";
        if(freq == NONE) return "NONE";
        if(freq == WEEKS) return "WEEKLY";
        if(freq == HOURS) return "HOURLY";
        if(freq == DAYS) return "DAYS";
        else return null;
    }
}
