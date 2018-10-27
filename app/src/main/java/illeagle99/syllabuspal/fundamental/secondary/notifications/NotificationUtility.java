package illeagle99.syllabuspal.fundamental.secondary.notifications;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import illeagle99.syllabuspal.fundamental.CourseManager;

/**
 * Created by kules on 10/20/2016.
 */

public class NotificationUtility {
    private static Context context = null;
    private static File file = null;

    /* helps keeep code as similar as possible to tested code.
       all content renewal methods are here as well
     */

    //all functions assume this has already been set */
    public static void setContext(Context c){
        context = c;
    }
    public static void updateSyllabusFromFile(Context c){
        CourseManager.reset(c);
    }

    /* all functions assume this has already been set */
    public static void setupFile(){
        try{
            file = new File(context.getFilesDir()+File.separator+"watched");
            if(file.createNewFile()){
                updateFile("running",true);
                updateFile("update",false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void hesitate(long ms){
        long until = System.currentTimeMillis() + ms;
        while(System.currentTimeMillis() < until){};
    }
    public static Context getContext(){ return context; }

    /* is thread running */
    public static boolean isThreadRunning(){
        return getFromFile("running");
    }

    /* is it time to update */
    public static boolean isTimeToUpdate(){
        return getFromFile("update");
    }

    /* set thread running */
    public static void setThreadRunning(boolean b){
        updateFile("running",b);
    }

    /* set time to update */
    public static void setTimeToUpdate(boolean b){
        updateFile("update",b);
    }

    private static void updateFile(String tag, boolean value){
        try{
            String content = getContent();
            FileWriter writer = new FileWriter(file);
            JSONObject json = content.equals("") ? new JSONObject() : new JSONObject(content);
            json.put(tag,value);
            writer.write(json.toString());
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static boolean getFromFile(String tag){
        try{
            String content = getContent();
            JSONObject json = content.equals("") ? new JSONObject() : new JSONObject(content);
            return json.getBoolean(tag);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static String getContent(){
        try{
            Scanner reader = new Scanner(file);
            String content = "";
            while(reader.hasNext()){
                content += reader.next() + " ";
            }
            reader.close();
            return content;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
