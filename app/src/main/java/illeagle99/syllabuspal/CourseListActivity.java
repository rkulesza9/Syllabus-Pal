package illeagle99.syllabuspal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.fundamental.Assignment;
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;

public class CourseListActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        setupListView();
        setColors();
        context = this;
    }

    public void setColors(){
        SDColorsUtility.color(this,findViewById(R.id.relly),majority(),false);
        SDColorsUtility.color(this,findViewById(R.id.newCourse),majority(),true);
    }
    private int majority(){
        /* data collection */
        int ns = 0, ip = 0, c = 0, ru = 0, ra = 0;
        for(int x = 0; x < CourseManager.numCourses(); x++){
            int cMajor =  CourseManager.getCourse(x).status();
            if(cMajor == Assignment.NOT_STARTED) ns++;
            if(cMajor == Assignment.IN_PROGRESS) ip++;
            if(cMajor == Assignment.COMPLETE) c++;
            if(cMajor == Assignment.RUN_OUT_OF_TIME) ru++;
            if(cMajor == Assignment.RAN_OUT_OF_TIME) ra++;
        }

        /* calc majority */
        int max = findMax(ns,ip,c,ru,ra);
        if(max == 0) return Assignment.NOT_STARTED;
        if(max == 1) return Assignment.IN_PROGRESS;
        if(max == 2) return Assignment.COMPLETE;
        if(max == 3) return Assignment.RUN_OUT_OF_TIME;
        return Assignment.RAN_OUT_OF_TIME;
    }
    private int findMax(int...data){
        boolean isATie = false;
        int max = Integer.MIN_VALUE;
        int index = -1;
        for(int x = 0; x < data.length; x++){
            if(data[x] > max){
                max = data[x];
                index = x;
                isATie = false;
            } else if(data[x] == max){
                isATie = true;
            }

            if(isATie) index = 1;
        }
        return index;
    }

    public void setupListView(){
        ArrayList<String> courseNames = new ArrayList<String>();
        for(int x = 0; x < CourseManager.numCourses(); x++) courseNames.add(CourseManager.getCourse(x).name());
        ListView listView = (ListView) findViewById(R.id.courseView);
        SDColorsUtility.Adapter adapter = new SDColorsUtility.Adapter(this,courseNames,true);
        listView.setAdapter(adapter);
        //normal click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course selCourse = CourseManager.getCourse(position);
                CourseManager.setSelected(selCourse);
                Intent intent = new Intent(context,CourseOptionsActivity.class);
                startActivity(intent);
            }
        });
        //long press
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseManager.setSelected(CourseManager.getCourse(i));
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Course Removal");
                adb.setMessage("Do you want to remove "+CourseManager.getSelected().name());
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = CourseManager.getSelected().name();
                        CourseManager.removeCourse(name);
                        CourseManager.setSelected(null);
                        Snackbar.make(findViewById(R.id.relly),name+" has been removed!", Snackbar.LENGTH_SHORT).show();
                        CourseManager.save();
                        setupListView();
                        setColors();
                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                adb.show();
                return true;
            }
        });
    }

    /* new course button */
    public void onNewCourse(View v){
        Intent intent = new Intent(this,CourseEditActivity.class);
        startActivity(intent);
    }

    public void onResume(){
        super.onResume();

        setupListView();
        setColors();

    }
}
