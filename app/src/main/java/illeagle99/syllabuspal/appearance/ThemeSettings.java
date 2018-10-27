package illeagle99.syllabuspal.appearance;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kules on 10/24/2016.
 */

public class ThemeSettings {
    public static String notStarted = null,
                         inProgress = null,
                         complete = null,
                         runningOutOfTime = null,
                         ranOutOfTime = null;
    private static ArrayList<String> themes = new ArrayList<String>();
    private static File colorsJSON = null;

    /* extract from file -- it is assumed that this method was used before any others */
    /* all themes should be uploaded by file. No access to arraylist from outside other than retrieval */
    public static void extractFromFile(Context cont){
        if(themes.size() == 0){
            colorsJSON = new File(cont.getFilesDir() + File.separator + "colors.json");
            readJSON();
        }
    }

    public static String getTheme(int index){ return themes.get(index); }
    public static int numThemes(){ return themes.size(); }
    public static File getFile(){ return colorsJSON; }

    private static void readJSON(){
        try{
            /*reads the file */
            String fInt = "";
            Scanner reader = new Scanner(colorsJSON);
            while(reader.hasNext()) fInt += reader.next()+" ";
            fInt = fInt.substring(0,fInt.length()-1);
            reader.close();

            /* put file through JSON Parser*/
            JSONObject obj = new JSONObject(fInt);
            notStarted = (obj.getString("not started"));
            inProgress = (obj.getString("in progress"));
            complete = (obj.getString("complete"));
            runningOutOfTime = (obj.getString("running out of time"));
            ranOutOfTime = (obj.getString("ran out of time"));
            JSONArray jaChoices = obj.getJSONArray("colors");
            for(int x = 0; x < jaChoices.length(); x++){
                themes.add(jaChoices.getString(x));
            }
        }catch(Exception e){
            setToDefaultSettings();
            e.printStackTrace();
        }
    }

    private static String json(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("not started",notStarted);
            obj.put("in progress", inProgress);
            obj.put("complete", complete);
            obj.put("running out of time", runningOutOfTime);
            obj.put("ran out of time", ranOutOfTime);

            JSONArray achoices = new JSONArray();
            for(int x = 0; x < themes.size(); x++) achoices.put(themes.get(x));
            obj.put("colors", achoices);

            return obj.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void save(){
        try {
            FileWriter fwriter = new FileWriter(colorsJSON);
            fwriter.write(json());
            fwriter.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setToDefaultSettings(){
        try{
            if(!colorsJSON.exists()) colorsJSON.createNewFile();
            JSONObject json = new JSONObject();
            json.put("not started", "100 181 246-33 150 243"); //rgb background-foreground
            json.put("in progress", "255 241 118-255 235 59");
            json.put("complete", "129 199 132-76 175 80");
            json.put("running out of time","255 183 77-255 152 0");
            json.put("ran out of time","229 115 115-244 67 54");

            JSONArray colors = new JSONArray();
            colors.put("100 181 246-33 150 243");
            colors.put("255 241 118-255 235 59");
            colors.put("129 199 132-76 175 80");
            colors.put("255 183 77-255 152 0");
            colors.put("229 115 115-244 67 54");
            colors.put("240 98 146-233 30 99");
            colors.put("186 104 200-156 39 176");
            colors.put("149 117 205-103 58 183");
            colors.put("121 134 203-63 81 181");
            colors.put("79 195 247-3 169 244");
            colors.put("77 208 225-0 188 212");
            colors.put("77 182 172-0 150 136");
            colors.put("174 213 129-139 195 74");
            colors.put("220 231 117-205 220 57");
            colors.put("255 213 79-255 193 7");
            colors.put("255 138 101-255 87 234");
            colors.put("161 136 127-21 85 72");
            colors.put("224 224 224-158 158 158");
            colors.put("144 164 174-96 125 139");
            json.put("colors",colors);

            FileWriter writer = new FileWriter(colorsJSON);
            writer.write(json.toString());
            writer.close();

            readJSON();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int[][] notStarted(){ return parse(notStarted); }
    public static int[][] inProgress(){ return parse(inProgress); }
    public static int[][] complete(){ return parse(complete); }
    public static int[][] runningOutOfTime(){ return parse(runningOutOfTime); }
    public static int[][] ranOutOfTime(){ return parse(ranOutOfTime); }

    /* parses the string and returns the data as int[][]{r b g}{r b g} bg then fg */
    public static int[][] parseTheme(int i){ return parse(themes.get(i)); }
    private static int[][] parse(String colorSTR){
        int[][] rbgcodes = new int[2][3];
        Scanner parser = new Scanner(colorSTR);
        parser.useDelimiter("-");
        String background = parser.next(), foreground = parser.next();
        Scanner parserBG = new Scanner(background), parserFG = new Scanner(foreground);
        parserBG.useDelimiter(" ");
        parserFG.useDelimiter(" ");

        for(int x = 0; x < 3; x++){
            rbgcodes[0][x] = Integer.parseInt(parserBG.next());
            rbgcodes[1][x] = Integer.parseInt(parserFG.next());
        }

        parser.close();
        parserBG.close();
        parserFG.close();
        return rbgcodes;
    }

}
