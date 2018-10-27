package illeagle99.syllabuspal.fundamental;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import illeagle99.syllabuspal.fundamental.secondary.NotificationSettings;

/**
 * Created by kules on 10/24/2016.
 */

public class CourseManager {
    private static ArrayList<Course> courses = new ArrayList<Course>();
    private static File syllabusJSON = null;
    private static Course selected = null;

    /* load from context --> assumed that this has been run before anything else */
    public static void loadFromContext(Context context){
        courses = new ArrayList<Course>();
        syllabusJSON = new File(context.getFilesDir() + File.separator + "syllabus.json");
        selected = null;
        readJSON();
    }
    public static boolean isLoaded(){ return syllabusJSON != null; }
    private static void readJSON(){
        try {
            /*reads the file*/
            Scanner reader = new Scanner(syllabusJSON);
            String fInt = "";
            while(reader.hasNext()) fInt += reader.next()+" ";
            reader.close();

            /*put file through JSON parser*/
            JSONArray json = new JSONArray(fInt);
            for(int x = 0; x < json.length(); x++){
                Course course = new Course(json.getJSONObject(x));
                courses.add(course);
            }
        }catch(Exception e){
            System.out.print("Syllabus--readJSON");
            try {
                syllabusJSON.createNewFile();
            }catch(Exception ex){
                System.out.print("Syllabus--readJSON (2)");
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    private static String json(){
        try {
            JSONArray json = new JSONArray();
            for(int x = 0; x < courses.size(); x++) json.put(new JSONObject(courses.get(x).json()));
            return json.toString();
        }catch(Exception e){
            System.out.println("syllabus--json");
            e.printStackTrace();
            return null;
        }
    }
    public static void save(){
        try {
            FileWriter fwriter = new FileWriter(syllabusJSON);
            fwriter.write(json());
            fwriter.close();
        } catch(Exception e){
            System.out.println("syllabus--save");
            e.printStackTrace();
        }
    }
    public static void reset(){
        try {
            syllabusJSON.delete();
            syllabusJSON.createNewFile();
        }catch(Exception e){
            System.out.println("syllabus--reset");
            e.printStackTrace();
        }
    }

    /* for resetting a syllabus object, without wiping the file */
    public static void reset(Context context){
        selected = null;
        syllabusJSON = null;
        for(int x = courses.size()-1; x >= 0; x--) courses.remove(x);
        loadFromContext(context);
    }

    /* for notifications */
    public static void getAssesToNotify(ArrayList<Assignment> everything){
        for(int x = 0; x < courses.size(); x++)
            for(int y = 0; y < courses.get(x).length(); y++){
                if(courses.get(x).get(y).nSettings().getFreqBase() == NotificationSettings.NONE) continue;
                everything.add(courses.get(x).get(y));
            }
    }

    /* array modifiers / getters */
    public static Course getCourse(int index){ return courses.get(index); }
    public static int numCourses(){ return courses.size(); }
    public static void removeCourse(int index){ courses.remove(index); }
    public static boolean addCourse(Course c){
        if(nameCheck(c.name())){
            courses.add(c);
            return true;
        }
        return false;
    }
    public static Course getCourse(String nm){
        for(Course c : courses){
            if(c.name().equals(nm)) return c;
        }
        return null;
    }
    public static void removeCourse(String nm){
        for(int x = courses.size()-1; x >= 0; x--)
            if(courses.get(x).name().equals(nm)) removeCourse(x);
    }
    public static boolean set(int x, Course course){
        boolean doesNotExist = nameCheck(course.name());
        if(doesNotExist) courses.add(x, course);
        return doesNotExist;
    }
    public static int positionOf(String name){
        for(int x = 0; x < numCourses(); x++){
            if(getCourse(x).name().equals(name)) return x;
        }
        return -1;
    }
    public static int positionOf(Course c){
        for(int x = 0; x < numCourses(); x++){
            Course course = getCourse(x);
            if(course.name().equals(c.name())) return x;
        }
        return -1;
    }

    /* helpful methods */
    public static boolean nameCheck(String name){
        for(int x = 0; x < courses.size(); x++)
            if(name.equals(getCourse(x).name()))
                return false;
        return true;
    }

    /* file access */
    public static void setFile(File f){ syllabusJSON = f; }
    public static File getFile(){ return syllabusJSON; }

    /* selected access */
    public static Course getSelected(){ return selected; }
    public static void setSelected(Course c){ selected = c; }
}
