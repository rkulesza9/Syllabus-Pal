package illeagle99.syllabuspal.appearance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import illeagle99.syllabuspal.fundamental.Assignment;
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;

/**
 * Created by kules on 9/7/2016.
 */
public class SDColorsUtility {

    public static void color(Context cont, View v, int status, boolean foreground){
        if(status == Assignment.NOT_STARTED)
            doesTheColoring(ThemeSettings.notStarted(),v,foreground);
        else if(status == Assignment.IN_PROGRESS)
            doesTheColoring(ThemeSettings.inProgress(),v,foreground);
        else if(status == Assignment.COMPLETE)
            doesTheColoring(ThemeSettings.complete(),v,foreground);
        else if(status == Assignment.RUN_OUT_OF_TIME)
            doesTheColoring(ThemeSettings.runningOutOfTime(),v,foreground);
        else if(status == Assignment.RAN_OUT_OF_TIME)
            doesTheColoring(ThemeSettings.ranOutOfTime(),v,foreground);
    }

    private static void doesTheColoring(int[][]codes,View v, boolean b){
        if(b) v.setBackgroundColor(Color.rgb(codes[1][0],codes[1][1],codes[1][2]));
        else v.setBackgroundColor(Color.rgb(codes[0][0],codes[0][1],codes[0][2]));
    }

    public static int[] getColors(int[][] codes){
        return new int[]{ Color.rgb(codes[0][0],codes[0][1],codes[0][2]),
                Color.rgb(codes[1][0],codes[1][1],codes[1][2]) };
    }

    /* special adapter */
    public static class Adapter extends ArrayAdapter<String> {
        private Boolean isCourse;
        private int count; /* so I only color each item once */

        public Adapter(Context context, ArrayList<String> options, boolean course){
            super(context,android.R.layout.simple_list_item_1,options);
            isCourse = course;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position,convertView,parent);
            if(isCourse){
                ifCourse(view,position);
            }
            else{
                ifAss(view,position);
            }
            return view;
        }

        private void ifCourse(View v,int x){
            Course c = CourseManager.getCourse(getItem(x));
            int status = c.status();
            color(getContext(),v,status,true);
            count++;
        }
        private void ifAss(View v,int x){
            Assignment a = CourseManager.getSelected().get(getItem(x));
            int status = a.realStatus();
            color(getContext(),v,status,true);
            count++;
        }

    }

    /* Bkgnd Handler function equivalents */
    public static void colorBackgroundComponents(Activity context, boolean newAss, int...compIDs){
        /* get Background color */
        //setup variables
        int backgroundColor = 0, status = -1;
        Course course = CourseManager.getSelected();
        Assignment assignment = null;
        if(course != null) assignment = course.selected();

        // get status
        if(assignment != null) status = newAss ? assignment.status() : assignment.realStatus();
        else if(course != null) status = course.status();
        else status = Assignment.NOT_STARTED;

        backgroundColor = getBackgroundColor(status);

        for(int id : compIDs){
            context.findViewById(id).setBackgroundColor(backgroundColor);
        }
    }
    public static void colorForegroundComponents(Activity context, boolean newAss, int...compIDs){
        int foreground = 0, status = -1;
        Course course = CourseManager.getSelected();
        Assignment assignment = null;
        if(course != null) assignment = course.selected();

        // get status
        if(assignment != null) status = newAss ? assignment.status() : assignment.realStatus();
        else if(course != null) status = course.status();
        else status = Assignment.NOT_STARTED;

        // find background color
        foreground = getForegroundColor(status);

        for(int id : compIDs){
            context.findViewById(id).setBackgroundColor(foreground);
        }
    }
    public static int getBackgroundColor(int status){
        int[][] colors = null;
        if(status == Assignment.NOT_STARTED) colors = ThemeSettings.notStarted();
        if(status == Assignment.IN_PROGRESS) colors = ThemeSettings.inProgress();
        if(status == Assignment.COMPLETE) colors = ThemeSettings.complete();
        if(status == Assignment.RUN_OUT_OF_TIME) colors = (ThemeSettings.runningOutOfTime());
        if(status == Assignment.RAN_OUT_OF_TIME) colors = (ThemeSettings.ranOutOfTime());
        return Color.rgb(colors[0][0], colors[0][1], colors[0][2]);
    }
    public static int getForegroundColor(int status){
        int[][] colors = null;
        if(status == Assignment.NOT_STARTED) colors = ThemeSettings.notStarted();
        if(status == Assignment.IN_PROGRESS) colors = ThemeSettings.inProgress();
        if(status == Assignment.COMPLETE) colors = ThemeSettings.complete();
        if(status == Assignment.RUN_OUT_OF_TIME) colors = (ThemeSettings.runningOutOfTime());
        if(status == Assignment.RAN_OUT_OF_TIME) colors = (ThemeSettings.ranOutOfTime());
        return Color.rgb(colors[1][0], colors[1][1], colors[1][2]);
    }
}
