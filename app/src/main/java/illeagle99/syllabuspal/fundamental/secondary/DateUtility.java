package illeagle99.syllabuspal.fundamental.secondary;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by kules on 10/25/2016.
 */

public class DateUtility {
    public static final long dateToLong(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }
    public static final Date longToDate(long lng){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(lng);
        return cal.getTime();
    }
    public static final String format(Long lng){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(longToDate(lng));
    }
    public static final long parse(String str){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return dateToLong(sdf.parse(str));
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /* parsing "times" instead of "dates"
       must have same date as curr day
     */
    private static Date makeItToday(Date date){
        GregorianCalendar gc = new GregorianCalendar();
        /*extract time from date */
        gc.setTime(date);
        int hourOfDay = gc.get(Calendar.HOUR_OF_DAY);
        int minutes = gc.get(Calendar.MINUTE);

        /* put extracted time into today*/
        gc.setTimeInMillis(System.currentTimeMillis());
        gc.set(Calendar.HOUR_OF_DAY, hourOfDay);
        gc.set(Calendar.MINUTE, minutes);

        return gc.getTime();

    }
    public static final String formatTime(Date date){
        Date date2 = makeItToday(date);
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        return sdf.format(date2);
    }
    public static final long parseTime(String str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
            Date date = sdf.parse(str);
            Date date2 = makeItToday(date);
            return dateToLong(date2);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /* date checking */
    public static boolean checkDate(Long before, Long after){
        if(after <= before) return false;
        return true;
    }
}
