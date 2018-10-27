package illeagle99.syllabuspal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import illeagle99.syllabuspal.appearance.SDColorsUtility;
import illeagle99.syllabuspal.appearance.ThemeSettings;
import illeagle99.syllabuspal.fundamental.CourseManager;
import illeagle99.syllabuspal.fundamental.secondary.notifications.NotificationUtility;

public class MainActivity extends AppCompatActivity {

    /* blue theme, two buttons: "courses" "color settings" */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* talk to update thread (this should go with save functionality) */
        NotificationUtility.setContext(this);
        NotificationUtility.setupFile();
        //NotificationUtility.setTimeToUpdate(true); on saves
        CourseManager.loadFromContext(this);
        ThemeSettings.extractFromFile(this);
        setupStylisticCrap();

    }
    public void setupStylisticCrap(){
        SDColorsUtility.colorBackgroundComponents(this,false,R.id.main);
        SDColorsUtility.colorForegroundComponents(this,false,R.id.courses,R.id.colorSettings);
    }

    /* onClick methods */
    public void toCourseList(View v) {
        Intent intent = new Intent(this,CourseListActivity.class);
        startActivity(intent);
    }
    public void toColorsSettings(View v){
        Intent intent = new Intent(this,ColorsSettingsActivity.class);
        startActivity(intent);
    }
}
