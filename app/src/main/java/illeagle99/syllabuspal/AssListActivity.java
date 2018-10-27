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
import illeagle99.syllabuspal.fundamental.Course;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.DateUtility;
import illeagle99.syllabuspal.fundamental.secondary.RealtimeTextUpdater;

public class AssListActivity extends AppCompatActivity {
    private Course selCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ass_list);
        selCourse = CourseManager.getSelected();
        setupTextViews();
        //setupListView();
        //setColors();
    }

    public void setColors(){
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.aalrel);
        SDColorsUtility.colorForegroundComponents(this,false,R.id.newAssButton);
        /* still need to color individual assignments */
    }

    public void setupTextViews() {
        RealtimeTextUpdater.setTextViewText(this, selCourse.name(), R.id.aal_name);
        RealtimeTextUpdater.setTextViewText(this, DateUtility.format(selCourse.from()), R.id.aal_from);
        RealtimeTextUpdater.setTextViewText(this, DateUtility.format(selCourse.to()), R.id.aal_to);
    }

    public void setupListView() {
        final Context context = this;
        ListView assignments = (ListView) findViewById(R.id.aal_listview);
        ArrayList<String> options = new ArrayList<String>();
        for (int x = 0; x < selCourse.length(); x++) options.add(selCourse.get(x).name());
        SDColorsUtility.Adapter adapter = new SDColorsUtility.Adapter(this,options,false);
        assignments.setAdapter(adapter);
        assignments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, AssEditActivity.class);
                CourseManager.getSelected().select(selCourse.get(position));
                startActivity(intent);
            }
        });
        //long press
        assignments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selCourse.select(selCourse.get(i));
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Assignment Removal");
                adb.setMessage("Do you want to remove "+selCourse.selected().name());
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = selCourse.selected().name();
                        selCourse.remove(name);
                        selCourse.select(null);
                        Snackbar.make(findViewById(R.id.aalrel),name+" has been removed!", Snackbar.LENGTH_SHORT).show();
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

    //button press
    public void onNewAssignment(View v) {
        Intent intent = new Intent(this, AssEditActivity.class);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        /* update syllabus and views */

        setupTextViews();
        setupListView();
        setColors();
    }
}
