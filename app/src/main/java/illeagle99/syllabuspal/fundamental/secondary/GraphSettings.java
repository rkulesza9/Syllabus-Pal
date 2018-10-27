package illeagle99.syllabuspal.fundamental.secondary;

import illeagle99.syllabuspal.fundamental.Assignment;
import illeagle99.syllabuspal.fundamental.Course;

/**
 * Created by kules on 8/22/2016.
 */
public class GraphSettings {
    private Course course;

    public GraphSettings(Course c){
        course = c;
    }

    /*count assignments of status ? */
    public int count(int status){
        int count = 0;
        for(int x = 0; x < course.length(); x++){
            if(course.get(x).realStatus() == status) count++;
        }
        return count;
    }

    /*total assignments*/
    public int total(){
        return course.length();
    }

    /*percent of total for a status */
    public double perOfTotal(int status){
        double dtotes = total();
        return (count(status)/dtotes) * 100;
    }

    public int majority(){
        /*data for comparison*/
        int max = Integer.MIN_VALUE, r = 0;
        int ns = count(Assignment.NOT_STARTED);
        int ip = count(Assignment.IN_PROGRESS);
        int c = count(Assignment.COMPLETE);
        int ru = count(Assignment.RUN_OUT_OF_TIME);
        int ra = count(Assignment.RAN_OUT_OF_TIME);
        int[] data = {ns,ip,c,ru,ra};
        int[] types = {Assignment.NOT_STARTED,Assignment.IN_PROGRESS,Assignment.COMPLETE,
                        Assignment.RUN_OUT_OF_TIME,Assignment.RAN_OUT_OF_TIME};
        boolean theresATie = false;

        /* min max comparison */
        for(int x = 0; x < data.length; x++){
            if(data[x] > max){
                max = data[x];
                r = x;
                theresATie = false;
            } else if(data[x] == max){
                theresATie = true;
            }
        }

        if(theresATie) r = 1;
        return types[r];
    }

}
